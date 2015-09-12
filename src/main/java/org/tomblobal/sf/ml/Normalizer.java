package org.tomblobal.sf.ml;

import com.google.common.collect.TreeBasedTable;

public interface Normalizer {

    TreeBasedTable<Long,String,Double> normalize(TreeBasedTable<Long, String, Double> rawFeatures);
}
