package org.tomblobal.sf;

import com.google.common.base.Optional;
import com.google.common.collect.TreeBasedTable;
import org.tomblobal.sf.leapmotion.LeapMotionEventSampler;
import org.tomblobal.sf.ml.AzureProbabilityClient;
import org.tomblobal.sf.ml.DummyFeaturizer;
import org.tomblobal.sf.ml.DummyNormalizer;
import org.tomblobal.sf.ml.SinglePredicitioner;
import org.tomblobal.sf.myo.MyoEventSampler;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Hello world!
 */
public class PredictingApp {

    public static void main(String[] args) {

        try (IEventSampler leapMotionSampler = new LeapMotionEventSampler()) {
            try (IEventSampler myoSampler = new MyoEventSampler()) {
                //printData(leapMotionSampler, myoSampler);
                ExecutorService taskManager = Executors.newSingleThreadExecutor();
                SinglePredicitioner predicitioner = new SinglePredicitioner(new DummyFeaturizer(new DummyNormalizer()), 0, new AzureProbabilityClient());

                String guess = guessWord(predicitioner, taskManager, leapMotionSampler, myoSampler);
                System.out.println("Could it be........... " + guess);

                System.exit(0);
            }
        } catch (Exception e) {
            System.err.println("Error: ");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static Map<String, Double> collect(IEventSampler[] samplers) {
        return Arrays.stream(samplers)
                .flatMap(t -> t.sample().entrySet().stream())
                .collect(toMap(Entry::getKey, Entry::getValue));
    }

    private static String guessWord(SinglePredicitioner predicitioner, ExecutorService taskManager, IEventSampler... samplers) throws InterruptedException, IOException {
        Map<Long, Map<String, Double>> samples = sample(taskManager, samplers);

        List<String> allHeaderKeys = samples.values().stream()
                .flatMap(t -> t.entrySet().stream().map(Entry::getKey))
                .distinct()
                .sorted()
                .collect(toList());

        TreeBasedTable<Long, String, Double> rawFeatures = TreeBasedTable.create();
        for (Entry<Long, Map<String, Double>> row : samples.entrySet()) {
            Map<String, Double> rowSamples = row.getValue();
            allHeaderKeys.forEach(header -> rawFeatures.put(row.getKey(), header, rowSamples.getOrDefault(header, 0d)));
        }

        Optional<String> guess = predicitioner.predictSingle(rawFeatures);

        return guess.or("Couldn't guess :-(");
    }

    private static Map<Long, Map<String, Double>> sample(ExecutorService taskManager, IEventSampler[] samplers) throws IOException, InterruptedException {
        int hz = 50;
        System.out.println("Sampling word, press Enter to start...");
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

        Map<Long, Map<String, Double>> samples = new HashMap<>();

        System.out.println("Starting sample, press Enter to stop.");

        while (isSampling.get()) {
            Thread.sleep(1000 / hz);
            Map<String, Double> sample = collect(samplers);
            if (sample.keySet().stream().findAny().isPresent()) {
                samples.put(System.currentTimeMillis(), new HashMap<>(sample));
            }
        }

        recording.cancel(true);
        return samples;
    }
}