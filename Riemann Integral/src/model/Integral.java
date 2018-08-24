package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class Integral {

    private Function<Double, Double> function;

    private Partition partition;

    private int precision;

    private List<List<Double>> population;

    private double recombinationProbability;

    private double mutationProbability;

    public Integral(Function<Double, Double> function, Partition partition, int precision, double recombinationProbability, double mutationProbability) {
        this.function = function;
        this.partition = partition;
        this.precision = precision;
        this.recombinationProbability = recombinationProbability;
        this.mutationProbability = mutationProbability;
        partition.setPartitionSize(1000);
        partition.generatePartition();
        generatePopulation();
    }

    public double computeRiemannSum(List<Double> intermediatePoints) {
        List<Double> partitionValues = partition.getPartition();
        int index = 1;
        double sum = 0;
        while (index < partition.getPartitionSize()) {
            sum += function.apply(intermediatePoints.get(index - 1))
                    * (partitionValues.get(index) - partitionValues.get(index - 1));
            index++;
        }
        return sum;
    }

    public double computeIntegral() {
        double value = 0;
        for (int index = 0; index < population.size(); index++) {
            value += computeRiemannSum(population.get(index));
        }
        return value / population.size();
    }

    public void generatePopulation() {
        population = new ArrayList<>();
        for (int index = 0; index < precision; index++) {
            partition.generateIntermediatePoints();
            population.add(partition.getIntermediatePoints());
        }
    }

    public List<Double> binaryMutation(List<Double> list) {
        int index = ThreadLocalRandom.current().nextInt(0, list.size() - 1);
        if (index == 0) return list;
        double value = ThreadLocalRandom.current().nextDouble(list.get(index - 1), list.get(index));
        list.set(index, value);
        return list;
    }

    public List<Double> geneticAlgorithm(int size) {
        List<Integer> bestIndices = evaluateFitness(population, size);
        List<List<Double>> newGeneration = new ArrayList<>();
        List<Double> firstParent;
        List<Double> secondParent;
        int counter = 0;
        while (counter < 20) {
            for (int index = 1; index < bestIndices.size() / 2; index++) {
                double randomNumber = ThreadLocalRandom.current().nextDouble(0, 1);
                if (randomNumber <= recombinationProbability) {
                    List<List<Double>> next = onePointCrossover(population.get(bestIndices.get(2 * index) - 1),
                            population.get(bestIndices.get(2 * index)));
                    firstParent = next.get(0);
                    secondParent = next.get(1);
                } else {
                    firstParent = population.get(bestIndices.get(2 * index));
                    secondParent = population.get(bestIndices.get(2 * index + 1));
                }
                double mutation = ThreadLocalRandom.current().nextDouble(0, 1);
                if (mutation < mutationProbability) {
                    firstParent = binaryMutation(firstParent);
                    secondParent = binaryMutation(secondParent);
                }
                newGeneration.add(firstParent);
                newGeneration.add(secondParent);
            }

            bestIndices = evaluateFitness(newGeneration, size);
            population = newGeneration;
            counter++;
        }

        return population.get(bestIndices.get(0));
    }

    public List<List<Double>> onePointCrossover(List<Double> first, List<Double> second) {
        int randomCut = ThreadLocalRandom.current().nextInt(0, partition.getPartitionSize() - 1);
        List<Double> firstParent = new ArrayList<>();
        List<Double> secondParent = new ArrayList<>();
        for (int index = 0; index <= randomCut; index++) {
            firstParent.add(first.get(index));
            secondParent.add(second.get(index));

        }

        for (int index = randomCut + 1; index < partition.getPartitionSize(); index++) {
            firstParent.add(second.get(index));
            secondParent.add(first.get(index));
        }

        List<List<Double>> result = new ArrayList<>();
        result.add(firstParent);
        result.add(secondParent);

        return result;
    }

    public List<Integer> evaluateFitness(List<List<Double>> population, int size) {
        List<Double> sum = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();
        sum.add(0.0);
        double error;
        for (int index = 1; index < population.size(); index++) {
            error = Math.abs(computeRiemannSum(population.get(index - 1)) - computeIntegral());
            sum.add(sum.get(index - 1) + error);
        }
        for (int counter = 1; counter < size; counter++) {
            Random random = new Random();
            double randomNumber = random.nextDouble() * sum.get(sum.size() - 1);
            int index = 1;
            while (index < sum.size() && sum.get(index) < randomNumber) {
                index++;
            }
            indexes.add(index);
        }
        return indexes;
    }


    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public Function<Double, Double> getFunction() {
        return function;
    }

    public void setFunction(Function<Double, Double> function) {
        this.function = function;
    }

    public Partition getPartition() {
        return partition;
    }

    public void setPartition(Partition partition) {
        this.partition = partition;
    }

    public List<List<Double>> getPopulation() {
        return population;
    }

    public void setPopulation(List<List<Double>> population) {
        this.population = population;
    }

    public double getRecombinationProbability() {
        return recombinationProbability;
    }

    public void setRecombinationProbability(double recombinationProbability) {
        this.recombinationProbability = recombinationProbability;
    }
}
