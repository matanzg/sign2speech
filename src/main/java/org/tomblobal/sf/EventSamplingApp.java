package org.tomblobal.sf;

import org.tomblobal.sf.leapmotion.LeapMotionEventSampler;
import org.tomblobal.sf.myo.MyoEventSampler;
//import org.tomblobal.sf.realsense.RealSenseSampler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static org.tomblobal.sf.UglyUtils.createFolderStructure;

/**
 * Hello world!
 */
public class EventSamplingApp {

    public static void main(String[] args) throws IOException {

        //String outputPath = args[0];

        String outputPath = "C:\\TEMP\\New\\";
        createFolderStructure(outputPath);

        List<String> words = args.length > 1
                ? Files.readAllLines(Paths.get(args[1]))
                : Arrays.asList("THANK_YOU");

        try (
                //IEventSampler leapMotionSampler = new LeapMotionEventSampler();
             IEventSampler myoSampler = new MyoEventSampler();) {
            //printData(leapMotionSampler, myoSampler);

            words.stream().forEach(w -> {
                System.out.println("new word! " + w);
                for (int i = 0; i < 50; i++) {
                    final int index = i;
                    //try (IEventSampler realSenseSampler = new RealSenseSampler()) {
                        sampleWord(w, index, outputPath,
                                //leapMotionSampler,
                                myoSampler
                                //, realSenseSampler
                                );
//                    } catch (Exception e) {
//                        System.err.println("Error: ");
//                        e.printStackTrace();
//                        System.exit(1);
//                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Error: ");
            e.printStackTrace();
            System.exit(1);
        }

        System.exit(0);
    }

    private static void printData(IEventSampler... samplers) throws InterruptedException {
        while (true) {
            Map<String, Double> sample = collect(samplers);

            String orderData = sample.entrySet().stream()
                    .sorted((t1, t2) -> t2.getKey().compareTo(t1.getKey()))
                    .map(t -> t.toString())
                    .collect(joining(","));

            System.out.println(orderData);
            Thread.sleep(1000);
        }
    }

    private static Map<String, Double> collect(IEventSampler[] samplers) {
        return Arrays.stream(samplers)
                .flatMap(t -> t.sample().entrySet().stream())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static void sampleWord(String word, int index, String outputPath, IEventSampler... samplers) {
        try {
            int hz = 50;
            System.out.println("Sampling word " + word + " number " + index + ", press Enter to start...");
            System.in.read();

            final AtomicBoolean isSampling = new AtomicBoolean(true);

            ExecutorService taskManager = Executors.newSingleThreadExecutor();
            Future recording = taskManager.submit(() -> {
                try {
                    System.in.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isSampling.set(false);
            });

            Map<Long, Map<String, Double>> samples = new HashMap<>();

            System.out.println("Starting sample, press Enter to stop.");

            long eventCounter = 1;
            while (isSampling.get()) {
                Thread.sleep(1000 / hz);
                Map<String, Double> sample = collect(samplers);
                if (sample.keySet().stream().findAny().isPresent()) {
                    samples.put(eventCounter++, new HashMap<>(sample));
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

            String outputFileName = outputPath + word + "_" + index + ".csv";
            try (FileWriter writer = new FileWriter(outputFileName)) {
                writer.write(headerRow);
                writer.write("\n");
                for (String row : rows) {
                    writer.append(row).append("\n");
                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String createRow(String word, Long
            timestamp, Map<String, Double> features, List<String> headers) {
        String valueColumns = headers.stream()
                .map(h -> String.format("%.5f", features.getOrDefault(h, 0d)))
                .collect(joining(","));

        return String.format("%s,%s,%s", word, timestamp, valueColumns);
    }
}