package org.roc.wim.entityLinking.expriments;

import com.roc.core.Utils.Stopwatch;
import org.apache.logging.log4j.LogManager;
import org.roc.wim.entityLinking.ml.ClassifierFactory;
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

    @Autowired
    private DataSet dataSet;

    public Trainer(String model, String[] featureNames) {
        this.model = model;
        this.featureNames = featureNames;
        this.labeledSet = new HashSet<>();
        this.unLabeledSet = new HashSet<>();
    }

    public void initTrainSet(int size) {
        // 洗牌算法选出size个训练样例
        int totalCount = dataSet.getTrainMentions().size();
        int[] sequences = new int[totalCount];
        for (int i = 0; i < totalCount; i++){
            unLabeledSet.add(dataSet.getTrainMentions().get(i).getId());
            sequences[i] = i;
        }
        Random random = new Random(0);
        for (int i = 0; i < totalCount; i++) {
            int tmp = sequences[i];
            int index = random.nextInt()%totalCount;
            sequences[i] = sequences[index];
            sequences[index] = tmp;
        }
        List<Instance> instanceList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            unLabeledSet.remove(dataSet.getTrainMentions().get(sequences[i]).getId());
            labeledSet.add(dataSet.getTrainMentions().get(sequences[i]).getId());
            //TODO: 抽取训练样例
        }
        this.instances = createDataSet(instanceList.size());
        for (Instance instance : instanceList) {
            instances.add(instance);
        }
        logger.info(instances.numInstances() + " instances loaded.");
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
