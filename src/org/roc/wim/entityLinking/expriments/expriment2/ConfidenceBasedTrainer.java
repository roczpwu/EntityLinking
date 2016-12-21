package org.roc.wim.entityLinking.expriments.expriment2;

import org.roc.wim.entityLinking.expriments.Trainer;

import javax.annotation.PostConstruct;

/**
 * User: rocwu
 * Date: 2016/12/20
 * Time: 16:58
 * Desc: 基于置信度的训练器
 */
public class ConfidenceBasedTrainer extends Trainer {
    public ConfidenceBasedTrainer(String model, String[] featureNames) {
        super(model, featureNames);
    }

    @PostConstruct
    public void init() {
        this.features.init(this.featureNames);
    }
}
