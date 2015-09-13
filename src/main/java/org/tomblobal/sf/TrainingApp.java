package org.tomblobal.sf;

import com.google.common.collect.TreeBasedTable;
import com.google.common.primitives.Doubles;
import javafx.util.Pair;
import org.tomblobal.sf.ml.Featurizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

/**
 * Created by Matan on 9/11/2015.
 */
public class TrainingApp {

    public static void main(String[] args) throws IOException {
        String dataFolder = args[0];

        Featurizer featurizer = Featurizer.create();
        List<String> outputRows = Files.walk(Paths.get(dataFolder))
                .filter(t -> Files.isRegularFile(t) && !t.getFileName().toString().contains("output"))
                .map(f -> extract(f, featurizer))
                .map(TrainingApp::createOutputRow)
                .sorted()
                .collect(Collectors.toList());

        Path outputPath = Paths.get(dataFolder, "output_naive.csv");
        Files.write(outputPath, outputRows);
    }

    private static String createOutputRow(Pair<String, double[]> rowData) {
        String values = Doubles.asList(rowData.getValue()).stream().map(String::valueOf).collect(joining(","));
        return String.format("%s,%s", rowData.getKey(), values);
    }

    private static Pair<String, double[]> extract(Path file, Featurizer featurizer) throws RuntimeException {
        String word = file.getName(file.getNameCount() - 1).toString();
        word = word.substring(0, word.length() - 4);
        TreeBasedTable<Long, String, Double> rawFeatures = TreeBasedTable.create();
        try {
            List<String> rows = Files.readAllLines(file);
            String[] headers = rows.get(0).split(",");

            for (int currentRow = 1; currentRow < rows.size(); currentRow++) {
                String[] columns = rows.get(currentRow).split(",");
                for (int currentColumn = 0; currentColumn < headers.length; currentColumn++) {
                    if (currentColumn >= columns.length) {
                        rawFeatures.put(Integer.valueOf(currentRow).longValue(), headers[currentColumn], 0d);
                        continue;
                    }
                    Double value = Doubles.tryParse(columns[currentColumn]);
                    value = value == null ? 0d : value;
                    rawFeatures.put(Integer.valueOf(currentRow).longValue(), headers[currentColumn], value);
                }
            }
            double[] features = featurizer.extract(rawFeatures);
            return new Pair<>(word, features);
        } catch (Exception e) {
            System.out.println(file);
            throw new RuntimeException(e);
        }
    }
}
