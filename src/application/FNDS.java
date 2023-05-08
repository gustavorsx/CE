package application;

import models.Subject;
import models.Point;

import java.util.ArrayList;
import java.util.List;

public class FNDS {
    public static List<List<Subject>> run(List<Subject> listSubject){
        List<Point> listPoint = new ArrayList<>(listSubject.size());

        for (Subject subject : listSubject) {
            Point pointSubject = new Point(subject);
            listPoint.add(pointSubject);
        }

        List<List<Point>> frontiersList = new ArrayList<>();

        List<Point> frontier1 = new ArrayList<>();

        //Parte 1
        for (int i = 0; i < listPoint.size(); i++) {
            Point p = listPoint.get(i);
            p.S = new ArrayList<>();
            p.numDomin = 0;

            for (int j = 0; j < listPoint.size(); j++) {
                if(i != j){
                    Point q = listPoint.get(j);

                    if(dominates(p,q)){
                        p.S.add(q);
                    }else if(dominates(q,p)){
                        p.numDomin++;
                    }
                }
            }

            if(p.numDomin == 0){
                p.rank = 1;
                frontier1.add(p);
            }

        }
        frontiersList.add(frontier1);

        // Parte 2
        int i = 0;
        List<Point> frontierI = frontiersList.get(i);
        while(!frontierI.isEmpty()){
            // Q
            List<Point> newFrontier = new ArrayList<>();

            for (Point pontoSubject: frontierI) {
                List<Point> Sp = pontoSubject.S;

                for (Point pointSubject2 : Sp) {
                    pointSubject2.numDomin--;
                    if(pointSubject2.numDomin == 0){
                        pointSubject2.rank = i + 1;
                        newFrontier.add(pointSubject2);
                    }
                }
            }
            i++;
            frontierI = newFrontier;
            frontiersList.add(newFrontier);
        }

        List<List<Subject>> retornoSubject = new ArrayList<>();

        for (List<Point> frontiersJ : frontiersList) {
            List<Subject> frontierSubjectJ = new ArrayList<>();

            if (!frontiersJ.isEmpty()) {
                for (Point p : frontiersJ) {
                    Subject subject = p.getSubject();
                    frontierSubjectJ.add(subject);
                }

                retornoSubject.add(frontierSubjectJ);
            }
        }

        return retornoSubject;
    }

    private static boolean dominates(Point a, Point b) {
        boolean dominate = false;

        Double[] p1coordinates = a.getSubject().getEvaluation();
        Double[] p2coordinates = b.getSubject().getEvaluation();

        for (int i = 0; i < p1coordinates.length; i++) {
            if (p1coordinates[i] > p2coordinates[i]) {
                return false;
            }

            if (p1coordinates[i] < p2coordinates[i]) {
                dominate = true;
            }
        }

        return dominate;
    }
}