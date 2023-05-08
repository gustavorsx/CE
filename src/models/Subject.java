package models;

public class Subject implements Comparable<Subject>{
    private Double[] values;
    private Double[] evaluation;
    private Double crowdingDistance;

    public  Subject(){

    }
    public Subject(Double[] values) {
        this.values = values;
    }

    public Double[] getValues() {
        return values;
    }

    public void setValues(Double[] values) {
        this.values = values;
    }

    public double getCrowdingDistance() {
        return crowdingDistance;
    }

    public void setCrowdingDistance(double crowdingDistance) {
        this.crowdingDistance = crowdingDistance;
    }

    public Double[] getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Double[] evaluation) {
        this.evaluation = evaluation;
    }

    @Override
    public int compareTo(Subject s2) {
        return Double.compare(s2.crowdingDistance, this.crowdingDistance);
    }
}
