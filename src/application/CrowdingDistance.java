package application;

import models.Subject;

import java.util.Collections;
import java.util.List;

public class CrowdingDistance {
    public static List<Subject> evaluate(List<Subject> border){
        int borderSize = border.size();

        for (Subject subject: border) {
            subject.setCrowdingDistance(0);
        }

        Subject subject0 = border.get(0);

        int objectives = subject0.getEvaluation().length;
        for (int i = 0; i < objectives; i++) {
            sort(border, i);
            border.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
            border.get(borderSize - 1).setCrowdingDistance(Double.POSITIVE_INFINITY);

            for (int j = 1; j < borderSize - 1; j++) {
                Subject previous = border.get(j-1);
                Subject next = border.get(j+1);

                double aux = (next.getEvaluation()[i] - previous.getEvaluation()[i])/
                        (border.get(borderSize - 1).getEvaluation()[i] - border.get(0).getEvaluation()[i]);

                double crowdingDistance = border.get(j).getCrowdingDistance() + aux;
                border.get(j).setCrowdingDistance(crowdingDistance);
            }
        }

        Collections.sort(border);

        return border;
    }

    private static void sort(List<Subject> border, int objectives){
        for (int i = 0; i < border.size() - 1; i++) {
            for (int j = i + 1; j < border.size(); j++) {
                if(border.get(i).getEvaluation()[objectives] > border.get(j).getEvaluation()[objectives]){
                    Subject aux = border.get(i);
                    border.set(i, border.get(j));
                    border.set(j, aux);
                }
            }
        }
    }
}

