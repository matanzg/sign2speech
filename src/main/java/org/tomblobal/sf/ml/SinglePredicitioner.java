package org.tomblobal.sf.ml;

import com.google.common.base.Optional;
import com.google.common.collect.TreeBasedTable;

import java.util.SortedMap;

public class SinglePredicitioner {

    private final Featurizer featurizer;
    private final double threshold;
    private final AzureProbabilityClient client;

    public SinglePredicitioner(Featurizer featurizer, double threshold, AzureProbabilityClient client) {
        this.featurizer = featurizer;
        this.threshold = threshold;
        this.client = client;
    }

    public Optional<String> predictSingle(TreeBasedTable<Long, String, Double> rawFeatures) {
        double[] features = featurizer.extract(rawFeatures);
        Optional<SortedMap<String, Double>> probabilitiesOpt = client.getProbability(features);
        System.out.println(probabilitiesOpt);
        if (probabilitiesOpt.isPresent()) {
            SortedMap<String, Double> probabilities = probabilitiesOpt.get();
            if (probabilities.get(probabilities.firstKey()) > threshold) {
                return Optional.of(probabilities.firstKey());
            } else {
                return Optional.absent();
            }
        } else {
            return Optional.absent();
        }
    }
}
