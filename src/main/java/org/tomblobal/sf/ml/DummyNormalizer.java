package org.tomblobal.sf.ml;

import com.google.common.collect.TreeBasedTable;

public class DummyNormalizer implements Normalizer {
    @Override
    public TreeBasedTable<Long, String, Double> normalize(TreeBasedTable<Long, String, Double> rawFeatures) {
        return rawFeatures;
    }
}
