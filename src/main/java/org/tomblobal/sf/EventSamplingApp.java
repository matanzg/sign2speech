package org.tomblobal.sf;

import org.tomblobal.sf.leapmotion.LeapMotionEventSampler;
import org.tomblobal.sf.myo.MyoEventSampler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

/**
 * Hello world!
 */
public class EventSamplingApp {

    public static void main(String[] args) {

        try (IEventSampler leapMotionSampler = new LeapMotionEventSampler()) {
            try (IEventSampler myoSampler = new MyoEventSampler()) {
                //printData(leapMotionSampler, myoSampler);

                sampleWord("J", leapMotionSampler, myoSampler);
                sampleWord("U", leapMotionSampler, myoSampler);
                sampleWord("L", leapMotionSampler, myoSampler);
                sampleWord("I", leapMotionSampler, myoSampler);
                sampleWord("A", leapMotionSampler, myoSampler);

                System.exit(0);
            }
        } catch (Exception e) {
            System.err.println("Error: ");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void printData(IEventSampler... samplers) throws InterruptedException {
        while (true) {
            Map<String, String> sample = collect(samplers);

            String orderData = sample.entrySet().stream()
                    .sorted((t1, t2) -> t2.getKey().compareTo(t1.getKey()))
                    .map(t -> t.toString())
                    .collect(joining(","));

            System.out.println(orderData);
            Thread.sleep(1000);
        }
    }

    private static Map<String, String> collect(IEventSampler[] samplers) {
        return Arrays.stream(samplers)
                .flatMap(t -> t.sample().entrySet().stream())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static void sampleWord(String word, IEventSampler... samplers) throws InterruptedException, IOException {

        ExecutorService taskManager = Executors.newSingleThreadExecutor();
        int hz = 50;
        System.out.println("Sampling word " + word + ", press Enter to start...");
        System.in.read();

        final AtomicBoolean isSampling = new AtomicBoolean(true);

        Future recording = taskManager.submit(() -> {
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            isSampling.set(false);
        });

        Map<Long, Map<String, String>> samples = new HashMap<>();

        System.out.println("Starting sample, press Enter to stop.");

        while (isSampling.get()) {
            Thread.sleep(1000 / hz);
            Map<String, String> sample = collect(samplers);
            if (sample.keySet().stream().findAny().isPresent()) {
                samples.put(System.currentTimeMillis(), new HashMap<>(sample));
            }
        }

        recording.cancel(true);

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

        String osName = System.getProperty("os.name");
        String osTempPath = osName.startsWith("Windows") ? "C:\\Temp\\" : "/tmp/";
        String outputFileName = osTempPath + word + ".csv";
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