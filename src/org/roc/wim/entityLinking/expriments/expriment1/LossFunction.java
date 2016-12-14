package org.roc.wim.entityLinking.expriments.expriment1;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;

import java.util.Random;

/**
 * User: rocwu
 * Date: 2016/12/13
 * Time: 14:25
 * Desc: 损失函数模型
 */
public class LossFunction {
    private static float alpha1 = 0.2f;
    private static float alpha2 = 0.2f;
    private static float alpha3 = 0.2f;
    private static float alpha4 = 0.2f;

    private float[] local_weights;
    private float[] global_weights;

    public LossFunction() {
        local_weights = new float[5];
        global_weights = new float[2];
        Random r = new Random(0);
        for (int i = 0; i < 5; i++)
            local_weights[i] = r.nextFloat();
        for (int i = 0; i < 2; i++)
            global_weights[i] = r.nextFloat();
    }

    public void iteration(String[] mentions, String mentionDoc, int[] candidateIds, int index) {
        int n = mentions.length;
        // 计算 local feature
        // local feature损失
        DerivativeStructure localDerivativeStructure;
        for (int i = 0; i < 5; i++) {
            //DerivativeStructure tmp = new DerivativeStructure()
        }
    }
}
