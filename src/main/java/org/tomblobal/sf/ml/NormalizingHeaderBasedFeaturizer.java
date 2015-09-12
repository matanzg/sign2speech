package org.tomblobal.sf.ml;

import com.google.common.collect.TreeBasedTable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public abstract class NormalizingHeaderBasedFeaturizer implements Featurizer {

    private final Normalizer normalizer;
    private final Set<String> headers = getIncludedHeaders();

    public NormalizingHeaderBasedFeaturizer(Normalizer normalizer) {
        this.normalizer = normalizer;
    }

    @Override
    public double[] extract(TreeBasedTable<Long, String, Double> rawFeatures) {

        TreeBasedTable<Long, String, Double> normalizedFeatures = normalizer.normalize(rawFeatures);
        return normalizedFeatures.rowMap().entrySet().stream()
                .sorted((t1, t2) -> t1.getKey().compareTo(t2.getKey()))
                .flatMapToDouble(this::getOrderedRequiredColumns)
                .toArray();
    }

    private DoubleStream getOrderedRequiredColumns(Map.Entry<Long, Map<String, Double>> row) {
        return headers.stream().sorted().mapToDouble(h -> row.getValue().getOrDefault(h, 0d));
    }

    protected abstract Set<String> getIncludedHeaders();
}
