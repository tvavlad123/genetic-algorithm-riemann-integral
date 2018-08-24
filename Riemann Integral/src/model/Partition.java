package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Partition {

    private final double LEFT_ENDPOINT;

    private final double RIGHT_ENDPOINT;

    private int partitionSize;

    private List<Double> partition;

    private List<Double> intermediatePoints;

    public Partition(double leftEndpoint, double rightEndpoint) {
        this.LEFT_ENDPOINT = leftEndpoint;
        this.RIGHT_ENDPOINT = rightEndpoint;
        this.partition = new ArrayList<>();
        this.intermediatePoints = new ArrayList<>();
    }

    public void generatePartition() {
        int index = 1;
        double divisionPoint = 0;
        partition.add(divisionPoint);
        while (index <= partitionSize) {
            divisionPoint += (RIGHT_ENDPOINT - LEFT_ENDPOINT) / partitionSize;
            partition.add(divisionPoint);
            index++;
        }
    }

    public void generateIntermediatePoints() {
        int index = 0;
        while (index < partitionSize) {
            double randomNumber = ThreadLocalRandom.current().nextDouble(partition.get(index), partition.get(index + 1));
            intermediatePoints.add(randomNumber);
            index++;
        }
    }

    public double getLeftEndpoint() {
        return LEFT_ENDPOINT;
    }

    public double getRightEndpoint() {
        return RIGHT_ENDPOINT;
    }

    public int getPartitionSize() {
        return partitionSize;
    }

    public void setPartitionSize(int partitionSize) {
        this.partitionSize = partitionSize;
    }

    public List<Double> getPartition() {
        return partition;
    }

    public void setPartition(List<Double> partition) {
        this.partition = partition;
    }

    public List<Double> getIntermediatePoints() {
        return intermediatePoints;
    }

    public void setIntermediatePoints(List<Double> intermediatePoints) {
        this.intermediatePoints = intermediatePoints;
    }
}
