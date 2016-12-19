package org.roc.wim.entityLinking.ml;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.*;
import weka.classifiers.meta.AdditiveRegression;
import weka.classifiers.trees.FT;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.LMT;

/**
 * User: Lu Tingming
 * Date: 15-5-17
 * Time: 下午10:01
 * Desc:
 */
public class ClassifierFactory {

    public static final String NaiveBayes = "NaiveBayes";
    public static final String SMO = "SMO";
    public static final String J48 = "J48";
    public static final String LibSVM = "LibSVM";
    public static final String LibLINEAR = "LibLINEAR";
    public static final String AdaBoostM1 = "AdaBoostM1";
    public static final String Bagging = "Bagging";
    public static final String DecisionTable = "DecisionTable";
    public static final String FunctionalTrees = "FunctionalTrees";
    public static final String LogisticModelTrees = "LogisticModelTrees";
    public static final String LogisticRegression = "LogisticRegression";
    public static final String AdditiveLogisticRegression = "AdditiveLogisticRegression";
    public static final String MultilayerPerceptron = "MultilayerPerceptron";
    public static final String RandomForest = "RandomForest";
    public static final String WeightedVoting = "WeightedVoting";
    public static final String FixWeightedVoters = "FixWeightedVoting";

    public static Classifier create(String model) {
        if(NaiveBayes.equals(model)) {
            return new NaiveBayes();
        } else if(SMO.equals(model)) {
            return new SMO();
        } else if(J48.equals(model)) {
            return new J48();
        } else if(LibSVM.equals(model)) {
            return new LibSVM();
        } else if(LibLINEAR.equals(model)) {
            return new LibLINEAR();
        } else if(AdaBoostM1.equals(model)) {
            return new weka.classifiers.meta.AdaBoostM1();
        } else if(Bagging.equals(model)) {
            return new weka.classifiers.meta.Bagging();
        } else if(DecisionTable.equals(model)) {
            return new weka.classifiers.rules.DecisionTable();
        } else if(FunctionalTrees.equals(model)) {
            return new FT();
        } else if(LogisticModelTrees.equals(model)) {
            return new LMT();
        } else if(LogisticRegression.equals(model)) {
            return new Logistic();
        } else if(AdditiveLogisticRegression.equals(model)) {
            return new AdditiveRegression();
        } else if(MultilayerPerceptron.equals(model)) {
            return new weka.classifiers.functions.MultilayerPerceptron();
        } else if(RandomForest.equals(model)) {
            return new weka.classifiers.trees.RandomForest();
        } /*else if(WeightedVoting.equals(model)) {
            return new WeightedVoting();
        } else if(FixWeightedVoters.equals(model)) {
            return new FixWeightedVoting();
        }*/ else {
            return null;
        }
    }
}
