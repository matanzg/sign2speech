package org.tomblobal.sf.ml;

import com.google.common.collect.TreeBasedTable;

/**
 * Created by Matan on 9/11/2015.
 */
public class SegmentingFuterizer implements Featurizer {

    private final Featurizer inner;
    private final int numberOfSegments;

    public SegmentingFuterizer(Featurizer inner, int numberOfSegments) {
        this.inner = inner;
        this.numberOfSegments = numberOfSegments;
    }

    @Override
    public double[] extract(TreeBasedTable<Long, String, Double> rawFeatures) {
        TreeBasedTable<Long, String, Double> selectedFeatures = TreeBasedTable.create();
        long segmentSize = rawFeatures.rowMap().size() / numberOfSegments;
        for (int currentSegment = 1; currentSegment < numberOfSegments; currentSegment++) {
            final long id = currentSegment;
            rawFeatures.row(segmentSize * currentSegment).entrySet().forEach(t -> selectedFeatures.put(id, t.getKey(), t.getValue()));
        }

        return inner.extract(selectedFeatures);
    }
}
