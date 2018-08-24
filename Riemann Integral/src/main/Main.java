package main;

import model.Integral;
import model.Partition;

public class Main {

    public static void main(String[] args) {
        Partition partition = new Partition(0, 1);
        Integral integral = new Integral(Math::exp, partition, 100, 0.5, 0.05);
        System.out.println(integral.computeRiemannSum(integral.geneticAlgorithm(20)));
    }
}
