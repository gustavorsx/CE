import java.util.Random;
import java.util.Arrays;

public class Main {
    private static final int N_VALUES = 2;
    private static final int N_PROBLEM = 2;
    private static Random rdn = new Random();

    public static void main(String[] args) {
        int N_POP = 20;
        int N_GEN = 0;
        int MAX_GEN = 10000;
        double F = 0.5;
        int MAX_RANGE = 20;
        int MIN_RANGE = -20;
        double CROSSOVER = 0.8;

        Subject[] initialPop = new Subject[N_POP];


        for (int i = 0; i < N_POP; i++) {
            Subject subject = new Subject();
            generateValues(subject, MIN_RANGE, MAX_RANGE);

            evaluateSubject(subject);
            initialPop[i] = subject;
        }

        while (N_GEN <= MAX_GEN) {
            Subject[] newPopulation = new Subject[N_POP];

            for (int i = 0; i < N_POP; i++) {
                int r1 = rdn.nextInt(N_POP - 1);
                int r2 = rdn.nextInt(N_POP - 1);
                int r3 = rdn.nextInt(N_POP - 1);

                Subject subject3 = initialPop[r3];
                Subject subject2 = initialPop[r2];
                Subject subject1 = initialPop[r1];

                Subject u = generateU(subject1, subject2, subject3, F);

                Subject exp = generateExperimental(initialPop[i], u, CROSSOVER);
                evaluateSubject(exp);

                if (isDominated(exp.getEvaluation(), initialPop[i].getEvaluation())) {
                    newPopulation[i] = exp;
                } else if (isDominated(initialPop[i].getEvaluation(), exp.getEvaluation())) {
                    newPopulation[i] = initialPop[i];
                } else {
                    int randomPick = rdn.nextInt(0, 1);
                    if (randomPick == 1) {
                        newPopulation[i] = exp;
                    } else {
                        newPopulation[i] = initialPop[i];
                    }
                }

            }

            initialPop = newPopulation;
            N_GEN++;
        }
        logSubject(initialPop, N_GEN);
    }

    public static Subject generateU(Subject subject1, Subject subject2, Subject subject3, double F) {
        Subject intermediarySubject = new Subject();

        double[] values = new double[N_VALUES];

        for (int i = 0; i < N_VALUES; i++) {
            values[i] = subject3.getValues()[i] + (F * (subject1.getValues()[i] - subject2.getValues()[i]));
        }

        intermediarySubject.setValues(values);
        return intermediarySubject;
    }

    public static Subject generateExperimental(Subject subject, Subject u, double cr) {
        Subject child = new Subject();

        double[] values = new double[N_VALUES];

        // Indices dos genes
        for (int i = 0; i < N_VALUES; i++) {
            // 0 < r < 1
            double r = rdn.nextDouble();

            if (r < cr) {
                values[i] = subject.getValues()[i];
            } else {
                values[i] = u.getValues()[i];
            }
        }

        child.setValues(values);

        return child;
    }

    public static void evaluateSubject(Subject subject) {
        double[] evaluation = new double[N_VALUES];
        switch (N_PROBLEM) {
            case 1 -> {
                evaluation[0] = (Math.pow(subject.getValues()[0], 2));
                evaluation[1] = Math.pow(subject.getValues()[0] - 1, 2);
            }
            case 2 -> {
                evaluation[0] = (Math.pow(subject.getValues()[0], 2)) + (Math.pow(subject.getValues()[1], 2));
                evaluation[1] = (Math.pow(subject.getValues()[0], 2)) + (Math.pow(subject.getValues()[1] - 2, 2));
            }
            case 3 -> {
                evaluation[0] = (Math.pow(subject.getValues()[0] - 1, 2)) + (Math.pow(subject.getValues()[1], 2)) + (Math.pow(subject.getValues()[2], 2));
                evaluation[1] = (Math.pow(subject.getValues()[0], 2)) + (Math.pow(subject.getValues()[1] - 1, 2)) + (Math.pow(subject.getValues()[2], 2));
                evaluation[2] = (Math.pow(subject.getValues()[0], 2)) + (Math.pow(subject.getValues()[1], 2)) + (Math.pow(subject.getValues()[2] - 1, 2));
            }
        }
        subject.setEvaluation(evaluation);
    }

    public static void logSubject(Subject[] subjects, int generation) {
        for (int i = 1; i < subjects.length; i++) {
            System.out.println("Indivíduo " + i + ": ");
            System.out.println("Genes: " + Arrays.toString(subjects[i].getValues()));
            System.out.println("Avaliação: " + Arrays.toString(subjects[i].getEvaluation()));
        }
    }

    public static void generateValues(Subject subject, double a, double b) {
        double[] genes = new double[N_VALUES];
        for (int i = 0; i < N_VALUES; i++) {
            genes[i] = rdn.nextDouble(a, b);
        }
        subject.setValues(genes);
    }

    public static boolean isDominated(double[] evalSub, double[] evalSub2) {
        boolean dominates = false;
        for (int i = 0; i < evalSub.length; i++) {
            if (evalSub[i] < evalSub2[i]) {
                dominates = true;
            } else if ((evalSub[i] > evalSub2[i])) {
                return false;
            }
        }
        return dominates;
    }
}