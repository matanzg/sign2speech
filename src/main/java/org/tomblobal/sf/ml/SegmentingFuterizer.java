package org.tomblobal.sf.ml;

import com.google.common.collect.TreeBasedTable;

/**
 * Created by Matan on 9/11/2015.
 */
public class SegmentingFuterizer implements Featurizer {

    private final Featurizer inner;

    public SegmentingFuterizer(Featurizer inner) {
        this.inner = inner;
    }

    @Override
    public double[] extract(TreeBasedTable<Long, String, Double> rawFeatures) {
        TreeBasedTable<Long, String, Double> selectedFeatures = TreeBasedTable.create();
        long segmentSize = rawFeatures.rowMap().size() / 4;
        rawFeatures.row(segmentSize).entrySet().forEach(t -> selectedFeatures.put(1L, t.getKey(), t.getValue()));
        rawFeatures.row(segmentSize * 2).entrySet().forEach(t -> selectedFeatures.put(2L, t.getKey(), t.getValue()));
        rawFeatures.row(segmentSize * 3).entrySet().forEach(t -> selectedFeatures.put(3L, t.getKey(), t.getValue()));
        return inner.extract(selectedFeatures);
    }
}
