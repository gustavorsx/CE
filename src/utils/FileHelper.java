package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import models.Subject;

public class FileHelper {
    public static void createPlotFile(List<Subject> subjects, int numGen) {
        try {
            createGensFile(subjects, numGen);
            createFunctionValuesFile(subjects, numGen);
        } catch (IOException erro) {
            System.out.printf("Erro: %s", erro.getMessage());
        }
    }

    private static void createGensFile(List<Subject> subjects, int numGen) throws IOException {
        String path = "./src/files/" + numGen + "_genes.txt";
        createFile(path, subjects, false);
    }

    private static void createFunctionValuesFile(List<Subject> subjects, int numGen) throws IOException {
        String path = "./src/files/" + numGen + "_evaluation.txt";
        createFile(path, subjects, true);
    }

    private static void createFile(String path, List<Subject> subjects, boolean isFunctionValues) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for (Subject subject : subjects) {
            if (isFunctionValues) {
                String formattedArray = Arrays.toString(subject.getEvaluation()).replace("[", "(").replace("]", ")");
                bufferedWriter.write(formattedArray);
                bufferedWriter.newLine();
            } else {
                bufferedWriter.write(Arrays.toString(subject.getValues()));
                bufferedWriter.newLine();
            }
        }
        bufferedWriter.close();
        fileWriter.close();
    }
}
