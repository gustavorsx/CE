package models;

import java.util.ArrayList;
import java.util.List;

public class Subject implements Comparable<Subject> {
    private double[] genes;
    private double[] evaluation;
    private double crowdingDistance;
    public BLXAlpha crossBLX;
    public IMutation mutation;

    public Subject() {
        this.crossBLX = new BLXAlpha(0.1);
    }

    public Subject(double[] genes) {
        this.genes = genes;
        this.crossBLX = new BLXAlpha(0.1);
        this.mutation = new None();
    }

    public Subject(double[] genes, IMutation mutation) {
        this.genes = genes;
        this.crossBLX = new BLXAlpha(0.1);
        this.mutation = mutation;
    }

    public double[] getValues() {
        return genes;
    }

    public void setValues(double[] genes) {
        this.genes = genes;
    }

    public double getCrowdingDistance() {
        return crowdingDistance;
    }

    public void setCrowdingDistance(double crowdingDistance) {
        this.crowdingDistance = crowdingDistance;
    }

    public double[] getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(double[] evaluation) {
        this.evaluation = evaluation;
    }

    public List<Subject> recombine(Subject p2) {
        List<Subject> children = new ArrayList<>(2);

        double[][] childrenMat = crossBLX.getOffSpring(this.genes, p2.genes, new double[] { -10, -10, -10 },
                new double[] { 10, 10, 10 });
        Subject f1 = new Subject(childrenMat[0]);
        Subject f2 = new Subject(childrenMat[1]);

        if (f1.genes.length == 0 || f2.genes.length == 0) {
            int i = 0;
        }

        children.add(f1);
        children.add(f2);

        return children;
    }

    public void mutate() {
        this.genes = mutation.getMutation(this.genes, new double[] { -10, -10 }, new double[] { 10, 10 });
    }

    @Override
    public int compareTo(Subject s2) {
        return Double.compare(s2.crowdingDistance, this.crowdingDistance);
    }
}
