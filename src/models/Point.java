package models;

import java.util.List;

public class Point {
    private double[] coordinates;
    public List<Point> S;
    public int numDomin;
    public int rank;
    private Subject subject;

    public Point(Subject subject) {
        this.subject = subject;
        this.coordinates = subject.getValues();
    }

    public Subject getSubject() {
        return this.subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public List<Point> getS() {
        return S;
    }

    public void setS(List<Point> s) {
        this.S = s;
    }

    public int getNumDomin() {
        return numDomin;
    }

    public void setNumDomin(int numDomin) {
        this.numDomin = numDomin;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}