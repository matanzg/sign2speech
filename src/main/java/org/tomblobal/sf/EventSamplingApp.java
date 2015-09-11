package org.tomblobal.sf;

import org.tomblobal.sf.myo.MyoEventSampler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

/**
 * Hello world!
 */
public class EventSamplingApp {

    public static void main(String[] args) {

        //try (IEventSampler leapMotionSampler = new LeapMotionEventSampler()) {
        try (IEventSampler myoSampler = new MyoEventSampler()) {
            for (char alphabet = 'A'; alphabet <= 'D'; alphabet++) {
                sampleWord(String.valueOf(alphabet),
                        //leapMotionSampler,
                        myoSampler);
            }
//            }
        } catch (Exception e) {
            System.err.println("Error: ");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void sampleWord(String word, IEventSampler... samplers) throws InterruptedException, IOException {

        int hz = 50;
        System.out.println("Sampling word " + word + " in 3...");
        Thread.sleep(1000);
        System.out.println("2...");
        Thread.sleep(1000);
        System.out.println("1...");
        Thread.sleep(1000);
        System.out.println("Sampling at " + hz + "hz...");

        Map<Long, Map<String, String>> samples = new HashMap<>();
        int numberOfSamples = 1000 / hz;
        for (int i = 0; i <= numberOfSamples; i++) {
            Thread.sleep(hz);
            Map<String, String> sample = Arrays.stream(samplers)
                    .flatMap(t -> t.sample().entrySet().stream())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

            samples.put(System.currentTimeMillis(), new HashMap<>(sample));
        }

        List<String> headerColumns = samples.values().stream()
                .flatMap(t -> t.entrySet().stream().map(Map.Entry::getKey))
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        String headerRow = "word,timestamp," + headerColumns.stream().collect(joining(","));
        List<String> rows = samples.entrySet().stream()
                .sorted((t1, t2) -> t1.getKey().compareTo(t2.getKey()))
                .map(t -> createRow(word, t.getKey(), t.getValue(), headerColumns))
                .collect(Collectors.toList());

        String outputFileName = "C:\\TEMP\\" + word + ".csv";
        try (FileWriter writer = new FileWriter(outputFileName)) {
            writer.write(headerRow);
            writer.write("\n");
            for (String row : rows) {
                writer.append(row).append("\n");
            }
        }
    }

    private static String createRow(String word, Long timestamp, Map<String, String> features, List<String> headers) {
        String valueColumns = headers.stream().map(h -> features.getOrDefault(h, "")).collect(joining(","));
        return String.format("%s,%s,%s", word, timestamp, valueColumns);
    }
}
