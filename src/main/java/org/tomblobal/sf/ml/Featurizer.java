package org.tomblobal.sf.ml;

import com.google.common.collect.TreeBasedTable;

public interface Featurizer {
    double[] extract(TreeBasedTable<Long, String, Double> rawFeatures);
}
