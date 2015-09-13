package org.tomblobal.sf.ml;

import com.google.common.collect.TreeBasedTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Matan on 9/13/2015.
 */
public class AggregatingMyoFeaturizer extends MyoOnlyFeaturizer {
    public AggregatingMyoFeaturizer(Normalizer normalizer) {
        super(normalizer);
    }

    @Override
    public double[] extract(TreeBasedTable<Long, String, Double> rawFeatures) {

        List<List<Double>> featuresPerRow = rawFeatures.rowMap().values().stream()
                .map(this::calculateFeatures)
                .collect(Collectors.toList());

        List<Double> features = aggregate(featuresPerRow);

        return features.stream().mapToDouble(t -> t).toArray();
    }

    private List<Double> aggregate(List<List<Double>> featuresPerRow) {
        Double feature1 = featuresPerRow.stream()
                .mapToDouble(t -> t.get(0))
                .average().getAsDouble();

        Double feature2 = featuresPerRow.stream()
                .mapToDouble(t -> t.get(1))
                .filter(t -> t > 0)
                .average().orElseGet(() -> 0d);

        Double feature3 = feature1 * feature2;
        Double feature4 = featuresPerRow.stream()
                .mapToDouble(t -> t.get(2))
                .filter(t -> t != 0)
                .map(t -> Math.pow(Math.log(Math.abs(t)), 2))
                .average().orElseGet(() -> 0d);

        Double feature5 = feature3 * feature4;
        Double feature6 = Math.log(Math.abs(median(
                featuresPerRow.stream()
                        .mapToDouble(t -> t.get(3))
                        .filter(t -> t < 0)
                        .toArray())));

        feature6 = Double.isInfinite(feature6) ? 0d : feature6;
        Double feature7 = feature3 * feature6;

        Double feature8 = variance(
                featuresPerRow.stream()
                        .mapToDouble(t -> t.get(4))
                        .filter(t -> t != 0)
                        .map(t -> Math.abs(t))
                        .toArray());

        Double feature9 = variance(
                featuresPerRow.stream()
                        .mapToDouble(t -> t.get(5))
                        .filter(t -> t != 0)
                        .map(t -> Math.exp(t))
                        .toArray());

        return Arrays.asList(feature1, feature2, feature3, feature5, feature6, feature7, feature8, feature9);
    }

    private List<Double> calculateFeatures(Map<String, Double> row) {
        Double rotationY = row.get("myo1_rotation_Y");
        Double gyroY = row.get("myo1_gyro_Y");
        double feature1 = Math.pow(rotationY * gyroY, 2);
        if ((rotationY < 0 && gyroY > 0) || (gyroY < 0 && rotationY > 0)) {
            feature1 *= -1;
        }

        Double accelerationX = row.get("myo1_acceleration_X");
        Double accelerationY = row.get("myo1_acceleration_Y");
        Double accelerationZ = row.get("myo1_acceleration_Z");

        Double feature2 = accelerationX * accelerationY * accelerationZ;
        Double feature3 = accelerationZ;
        Double feature4 = accelerationZ;

        Double emg0 = row.get("myo1_emg0");

        Double feature5 = emg0;
        Double feature6 = accelerationZ;

        return Arrays.asList(feature1, feature2, feature3, feature4, feature5, feature6);
    }

    public static double median(double[] m) {
        if (m.length == 0)
            return 0d;

        int middle = m.length / 2;
        if (m.length % 2 == 1) {
            return m[middle];
        } else {
            return (m[middle - 1] + m[middle]) / 2.0;
        }
    }

    public static double variance(double[] list) {
        if (list.length == 0)
            return 0;

        Double sumMinusAverage = Arrays.stream(list).sum() - Arrays.stream(list).average().getAsDouble();
        return sumMinusAverage * sumMinusAverage / (list.length - 1);
    }
}
