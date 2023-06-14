package application;

import models.Subject;
import utils.FileHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

public class NSGA2 {
    private static final int N_GENES = 3;
    private static final int N_PROBLEM = 3;
    private static Random rdn = new Random();
    private static int MAX_GENERATION = 1000;
    private static final List<Integer> gensToPlot = Arrays.asList(20, 40, 60, 80, 100, MAX_GENERATION);
    private static int N_POP = 20;
    private static int CURRENT_GENERATION = 1;
    private static int MAX_RANGE = 20;
    private static int MIN_RANGE = -20;

    public static void main(String[] args) {
        List<Subject> initialPop = new ArrayList<>(N_POP);

        generateInitialPop(initialPop);

        FileHelper.createPlotFile(initialPop, CURRENT_GENERATION);

        while (CURRENT_GENERATION <= MAX_GENERATION) {
            List<Subject> newPopulation = new ArrayList<>();
            List<Subject> intermediatePopulation = new ArrayList<>(initialPop);

            createOffspring(intermediatePopulation, initialPop);

            List<List<Subject>> borders = FNDS.run(intermediatePopulation);

            for (List<Subject> border : borders) {
                if (newPopulation.size() >= N_POP)
                    break;

                if (border.size() + newPopulation.size() > N_POP) {
                    List<Subject> subjectCD = CrowdingDistance.evaluate(border);
                    for (Subject subject : subjectCD) {
                        if (newPopulation.size() < N_POP) {
                            newPopulation.add(subject);
                        } else
                            break;
                    }

                } else {
                    newPopulation.addAll(border);
                }
            }

            initialPop = newPopulation;

            if (gensToPlot.contains(CURRENT_GENERATION)) {
                FileHelper.createPlotFile(initialPop, CURRENT_GENERATION);
            }
            CURRENT_GENERATION++;
        }
        logSubject(initialPop, CURRENT_GENERATION);
    }

    private static void evaluateSubject(Subject subject) {
        double[] evaluation = null;
        switch (N_PROBLEM) {
            case 1 -> {
                evaluation = new double[2];
                evaluation[0] = (Math.pow(subject.getValues()[0], 2));
                evaluation[1] = Math.pow(subject.getValues()[0] - 1, 2);
            }
            case 2 -> {
                evaluation = new double[2];
                evaluation[0] = (Math.pow(subject.getValues()[0], 2)) + (Math.pow(subject.getValues()[1], 2));
                evaluation[1] = (Math.pow(subject.getValues()[0], 2)) + (Math.pow(subject.getValues()[1] - 2, 2));
            }
            case 3 -> {
                evaluation = new double[3];
                evaluation[0] = (Math.pow(subject.getValues()[0] - 1, 2)) + (Math.pow(subject.getValues()[1], 2))
                        + (Math.pow(subject.getValues()[2], 2));
                evaluation[1] = (Math.pow(subject.getValues()[0], 2)) + (Math.pow(subject.getValues()[1] - 1, 2))
                        + (Math.pow(subject.getValues()[2], 2));
                evaluation[2] = (Math.pow(subject.getValues()[0], 2)) + (Math.pow(subject.getValues()[1], 2))
                        + (Math.pow(subject.getValues()[2] - 1, 2));
            }
        }
        subject.setEvaluation(evaluation);
    }

    public static void logSubject(List<Subject> subjects, int generation) {
        for (int i = 1; i < subjects.size(); i++) {
            System.out.println("Indivíduo " + i + ": ");
            System.out.println("Genes: " + Arrays.toString(subjects.get(i).getValues()));
            System.out.println("Avaliação: " + Arrays.toString(subjects.get(i).getEvaluation()));
        }
    }

    private static void generateInitialPop(List<Subject> initialPop) {
        for (int i = 0; i < N_POP; i++) {
            Subject subject = new Subject();
            generateGenes(subject, MIN_RANGE, MAX_RANGE);

            evaluateSubject(subject);
            initialPop.add(subject);
        }
    }

    private static void generateGenes(Subject subject, int a, int b) {
        double[] values = new double[N_GENES];
        for (int i = 0; i < N_GENES; i++) {
            values[i] = rdn.nextDouble(a, b);
        }
        subject.setValues(values);
    }

    private static void createOffspring(List<Subject> intermediatePopulation, List<Subject> individuals) {

        List<Subject> popAux = new ArrayList<>(individuals.size());
        popAux.addAll(individuals);

        while (popAux.size() > 1) {
            int idxR1 = rdn.nextInt(popAux.size());
            Subject p1 = popAux.remove(idxR1);
            int idxR2 = rdn.nextInt(popAux.size());
            Subject p2 = popAux.remove(idxR2);

            List<Subject> children = p1.recombine(p2);
            Subject f1 = children.get(0);
            if (rdn.nextDouble() > 0.9) {
                f1.mutate();
            }
            Subject f2 = children.get(1);
            if (rdn.nextDouble() > 0.9) {
                f2.mutate();
            }

            evaluateSubject(f1);
            evaluateSubject(f2);
            intermediatePopulation.add(f1);
            intermediatePopulation.add(f2);
        }
    }
}