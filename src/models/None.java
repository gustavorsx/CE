package models;

public class None implements IMutation {
    @Override
    public double[] getMutation(double[] x, double[] lowerBound, double[] upperBound) {
        return x;
    }
}