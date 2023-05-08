package application;

import models.Subject;
import utils.FileHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

public class DifferentialEvolution {
    private static final int N_GENES = 2;
    private static final int N_PROBLEM = 2;
    private static Random rdn = new Random();
    private static Double F = 0.5;
    private static int MAX_GENERATION = 1000;
    private static final List<Integer> gensToPlot = Arrays.asList(20, 40, 60, 80, 100, MAX_GENERATION);
    private static Double CROSSOVER = 0.5;
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

            for (int i = 0; i < N_POP; i++) {
                int r1 = rdn.nextInt(N_POP - 1);
                int r2 = rdn.nextInt(N_POP - 1);
                int r3 = rdn.nextInt(N_POP - 1);

                Subject subject3 = initialPop.get(r1);
                Subject subject2 = initialPop.get(r2);
                Subject subject1 = initialPop.get(r3);

                Subject u = generateU(subject1, subject2, subject3);

                Subject exp = generateExperimental(initialPop.get(i), u);
                evaluateSubject(exp);

                intermediatePopulation.add(exp);
            }

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
            CURRENT_GENERATION++;

            if (gensToPlot.contains(CURRENT_GENERATION)) {
                FileHelper.createPlotFile(initialPop, CURRENT_GENERATION);
            }
        }
        logSubject(initialPop, CURRENT_GENERATION);
    }

    private static Subject generateU(Subject subject1, Subject subject2, Subject subject3) {
        Subject intermediarySubject = new Subject();

        Double[] values = new Double[N_GENES];

        for (int i = 0; i < N_GENES; i++) {
            values[i] = subject3.getValues()[i] + (F * (subject1.getValues()[i] - subject2.getValues()[i]));
        }

        intermediarySubject.setValues(values);
        return intermediarySubject;
    }

    private static Subject generateExperimental(Subject subject, Subject u) {
        Subject child = new Subject();
        boolean verifyGenes = false;
        Double[] values = new Double[N_GENES];

        // Indices dos genes
        for (int i = 0; i < N_GENES; i++) {
            // 0 < r < 1
            Double r = rdn.nextDouble();

            if (r < CROSSOVER) {
                values[i] = subject.getValues()[i];
            } else {
                verifyGenes = true;
                values[i] = u.getValues()[i];
            }

            if (!verifyGenes) {
                values[i] = subject.getValues()[rdn.nextInt(N_GENES)];
            }
        }

        child.setValues(values);

        return child;
    }

    private static void evaluateSubject(Subject subject) {
        Double[] evaluation = null;
        switch (N_PROBLEM) {
            case 1 -> {
                evaluation = new Double[2];
                evaluation[0] = (Math.pow(subject.getValues()[0], 2));
                evaluation[1] = Math.pow(subject.getValues()[0] - 1, 2);
            }
            case 2 -> {
                evaluation = new Double[2];
                evaluation[0] = (Math.pow(subject.getValues()[0], 2)) + (Math.pow(subject.getValues()[1], 2));
                evaluation[1] = (Math.pow(subject.getValues()[0], 2)) + (Math.pow(subject.getValues()[1] - 2, 2));
            }
            case 3 -> {
                evaluation = new Double[3];
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
        Double[] values = new Double[N_GENES];
        for (int i = 0; i < N_GENES; i++) {
            values[i] = rdn.nextDouble(a, b);
        }
        subject.setValues(values);
    }
}