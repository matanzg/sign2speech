package org.tomblobal.sf.ml;

import com.google.common.collect.TreeBasedTable;
import com.google.common.primitives.Doubles;

public class DummyFeaturizer implements Featurizer {

    private final Normalizer normalizer;

    public DummyFeaturizer(Normalizer normalizer) {
        this.normalizer = normalizer;
    }

    @Override
    public double[] extract(TreeBasedTable<Long, String, Double> rawFeatures) {

        TreeBasedTable<Long, String, Double> normalizedFeatures = normalizer.normalize(rawFeatures);

        return Doubles.toArray(rawFeatures.row(normalizedFeatures.rowKeySet().last()).values());
    }
}
