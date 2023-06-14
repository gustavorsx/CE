package models;

public interface IMutation {
    double[] getMutation(double[] x, double[] lowerBound, double[] upperBound);
}
