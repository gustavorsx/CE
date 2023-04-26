public class Subject {
    private double[] values;
    private double[] evaluation;

    public  Subject(){

    }
    public Subject(double[] values) {
        this.values = values;
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    public double[] getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(double[] evaluation) {
        this.evaluation = evaluation;
    }
}
