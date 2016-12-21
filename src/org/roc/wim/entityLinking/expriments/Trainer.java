package org.roc.wim.entityLinking.expriments;

import com.roc.core.Utils.Stopwatch;
import org.apache.logging.log4j.LogManager;
import org.roc.wim.entityLinking.el.doc.Doc;
import org.roc.wim.entityLinking.el.doc.DocBL;
import org.roc.wim.entityLinking.el.mention.Mention;
import org.roc.wim.entityLinking.el.mention.MentionBL;
import org.roc.wim.entityLinking.ml.ClassifierFactory;
import org.roc.wim.entityLinking.wiki.doctionary.*;
import org.roc.wim.entityLinking.wiki.doctionary.Dictionary;
import org.roc.wim.entityLinking.wiki.page.Title2IdCache;
import org.roc.wim.entityLinking.wiki.pageAbst.PageAbstBL;
import org.roc.wim.entityLinking.wiki.pageAbst.PageAbstCache;
import org.springframework.beans.factory.annotation.Autowired;
import weka.classifiers.Classifier;
import weka.classifiers.functions.LibSVM;
import weka.core.*;
import java.util.*;

/**
 * User: rocwu
 * Date: 2016/12/19
 * Time: 16:56
 * Desc: 训练器
 */
public abstract class Trainer {
    private org.apache.logging.log4j.Logger logger = LogManager.getLogger(this.getClass().getName());
    protected String model;
    protected String[] featureNames;
    private Instances instances;
    private Set<Integer> labeledSet;
    private Set<Integer> unLabeledSet;
    private static final int WEIGH = 1;

    @Autowired
    protected Features features;
    @Autowired
    private DataSet dataSet;
    @Autowired
    private CandidateCache candidateCache;
    @Autowired
    private DocBL docBL;
    @Autowired
    private PageAbstCache pageAbstCache;
    @Autowired
    private Title2IdCache title2IdCache;
    @Autowired
    private MentionBL mentionBL;

    public Trainer(String model, String[] featureNames) {
        this.model = model;
        this.featureNames = featureNames;
        this.labeledSet = new HashSet<>();
        this.unLabeledSet = new HashSet<>();
        this.features = new Features();
    }

    public void initTrainSet(int size) {
        Stopwatch st = new Stopwatch();
        // 洗牌算法选出size个训练样例
        int totalCount = dataSet.getTrainMentions().size();
        int[] sequences = new int[totalCount];
        for (int i = 0; i < totalCount; i++){
            unLabeledSet.add(dataSet.getTrainMentions().get(i).getId());
            sequences[i] = i;
        }
//        Random random = new Random(0);
//        for (int i = 0; i < totalCount; i++) {
//            int tmp = sequences[i];
//            int index = random.nextInt(totalCount);
//            sequences[i] = sequences[index];
//            sequences[index] = tmp;
//        }
        List<Mention> mentionList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            mentionList.add(dataSet.getTrainMentions().get(sequences[i]));
        }
        List<Instance> instanceList = generateInstances(mentionList);
        this.instances = createDataSet(instanceList.size());
        for (Instance instance : instanceList) {
            instances.add(instance);
        }
        logger.info("finished to initTrainSet, time cost=" + st.stopAndGetFormattedTime());
        logger.info(instances.numInstances() + " instances loaded.");
    }

    private List<Instance> generateInstances(List<Mention> mentionList) {
        int size = mentionList.size();
        List<Instance> instanceList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            unLabeledSet.remove(mentionList.get(i).getId());
            labeledSet.add(mentionList.get(i).getId());
            String mention = dataSet.getTrainMentions().get(i).getName();
            String title = dataSet.getTrainMentions().get(i).getWikiTitle();
            List<Candidate> candidateList = candidateCache.get(mention);
            Doc doc = (Doc) docBL.get(dataSet.getTrainMentions().get(i).getDocId());
            String mentionContext = doc.getContent();
            //上下文中的其他mention，取5个
            int contextMentionCount = 5;
            List contextMentionList = mentionBL.getListByCondition(Mention.DocId+" = '"+doc.getId()+"' and "+Mention.EntityId+" >'0' and "+Mention.NeType+" != 'MISC'");
            Collections.sort(contextMentionList, (o1, o2) -> ((Mention)o1).getSeqInDoc()-((Mention)o2).getSeqInDoc());
            int index = 0;
            for (int j = 0; j < contextMentionList.size(); j++) {
                Mention _mention = (Mention) contextMentionList.get(j);
                if (_mention.getId() == dataSet.getTrainMentions().get(i).getId()) {
                    index = j;
                    break;
                }
            }
            if (contextMentionList.size()<contextMentionCount+1)
                contextMentionCount = contextMentionList.size()-1;
            String[] contextMentions = new String[contextMentionCount];
            int l_index = index-1, r_index = index+1, current_index = 0;
            while (true) {
                if (current_index>=contextMentionCount) break;
                if (l_index < 0) contextMentions[current_index++] = ((Mention)contextMentionList.get(r_index++)).getName();
                else if (r_index >= contextMentionList.size()) contextMentions[current_index++] = ((Mention)contextMentionList.get(l_index--)).getName();
                else {
                    int left_dis = ((Mention)contextMentionList.get(index)).getSeqInDoc()-((Mention)contextMentionList.get(l_index)).getSeqInDoc();
                    int right_dis = ((Mention)contextMentionList.get(r_index)).getSeqInDoc()-((Mention)contextMentionList.get(index)).getSeqInDoc();
                    if (left_dis < right_dis) contextMentions[current_index++] = ((Mention)contextMentionList.get(l_index--)).getName();
                    else contextMentions[current_index++] = ((Mention)contextMentionList.get(r_index++)).getName();
                }
            }
            for (Candidate candidate : candidateList) {
                String candidateTitle = candidate.getWikiTitle();
                int candidateId = candidate.getEntityId();
                String candidateAbst = pageAbstCache.get(candidateId);
                float[] feature_values = features.generateFeatureVector(mentionContext, candidateAbst, mention, candidateTitle, contextMentions);
                double[] values = new double[feature_values.length+1];
                if (title.equals(candidate.getWikiTitle())) values[0] = 1;  // 正例
                else values[0] = 0;                                         // 反例
                for (int k = 1; k < values.length; k++) {
                    values[k] = feature_values[k-1];
                }
                Instance instance = new Instance(WEIGH, values);
                instanceList.add(instance);
            }
            logger.info((i+1)+" mentions completed, total "+ size);
        }
        return instanceList;
    }

    public Instances createDataSet(int capacity) {
        // 创建分类属性
        FastVector classValues = new FastVector(2);
        classValues.addElement("0");
        classValues.addElement("1");
        Attribute classAttribute = new Attribute("class", classValues);

        // 创建属性列表
        FastVector attributes = new FastVector();
        attributes.addElement(classAttribute);

        for (String featureName : featureNames) {
            attributes.addElement(new Attribute(featureName));
        }

        // 创建weka对象
        Instances instances = new Instances("entityLinking", attributes, capacity);
        instances.setClassIndex(0);

        return instances;
    }

    public Classifier train() {
        Classifier classifier = ClassifierFactory.create(model);
        if(classifier instanceof LibSVM) {
            logger.info("LibSVM.ProbabilityEstimates = "+((LibSVM) classifier).getProbabilityEstimates());
            ((LibSVM) classifier).setProbabilityEstimates(true);
            ((LibSVM) classifier).setKernelType(new SelectedTag(LibSVM.KERNELTYPE_LINEAR, LibSVM.TAGS_KERNELTYPE));
        }
        logger.info("start to buildClassifier");
        Stopwatch st = new Stopwatch();
        try {
            classifier.buildClassifier(instances);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        logger.info("finished to buildClassifier, time cost=" + st.stopAndGetFormattedTime());
        return classifier;
    }
}
