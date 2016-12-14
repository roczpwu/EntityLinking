package org.roc.wim.entityLinking.caculateModel.samples;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;

/**
 * example
 */
public class Sample {
    public static void test() {
        // Function of 1 variable, keep track of 3 derivatives with respect to that variable,
        // use 2.5 as the current value.  Basically, the identity function.
//        DerivativeStructure x = new DerivativeStructure(1, 3, 0, 2.5);
//        // Basically, x --> x^2.
//        DerivativeStructure x2 = x.pow(2);
//        //Linear combination: y = 4x^2 + 2x
//        DerivativeStructure y = new DerivativeStructure(4.0, x2, 2.0, x);
//        System.out.println("y    = " + y.getValue());
//        System.out.println("y'   = " + y.getPartialDerivative(1));
//        System.out.println("y''  = " + y.getPartialDerivative(2));
//        System.out.println("y''' = " + y.getPartialDerivative(3));

        DerivativeStructure x = new DerivativeStructure(2, 3, 0, 2.5);
        x = x.pow(2);
        DerivativeStructure y = new DerivativeStructure(2, 3, 1, 2.5);
        y = y.pow(2);
        DerivativeStructure z = new DerivativeStructure(1.0, x, 1.0, y);
        System.out.println("z    = " + z.getValue());
        System.out.println("z'   = " + z.getPartialDerivative(1,0));
    }

    public static void main(String[] args) {
        test();
    }
}
