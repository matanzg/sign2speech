package org.tomblobal.sf.ml;

import com.google.common.collect.TreeBasedTable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matan on 9/13/2015.
 */
public class SamplingAveragingFeaturizer implements Featurizer {

    private final Featurizer inner;
    private final int numberOfSegments;

    public SamplingAveragingFeaturizer(Featurizer inner, int numberOfSegments) {
        this.inner = inner;
        this.numberOfSegments = numberOfSegments;
    }

    @Override
    public double[] extract(TreeBasedTable<Long, String, Double> rawFeatures) {
        TreeBasedTable<Long, String, Double> selectedFeatures = TreeBasedTable.create();
        long segmentSize = rawFeatures.rowMap().size() / (numberOfSegments + 1);
        for (long currentSegment = 0; currentSegment < numberOfSegments; currentSegment++) {
            Map<String, Double> columnValues = new HashMap<>();
            for (long row = currentSegment * segmentSize; row < currentSegment * (segmentSize + 1); row++) {
                Map<String, Double> currentRow = rawFeatures.row(row);
                for (String column : currentRow.keySet()) {
                    if (columnValues.containsKey(column)) {
                        columnValues.put(column, columnValues.get(column) + currentRow.get(column));
                    } else {
                        columnValues.put(column, currentRow.get(column));
                    }
                }
            }

            for (String column : columnValues.keySet()) {
                selectedFeatures.put(currentSegment, column, columnValues.get(column) / segmentSize);
            }
        }

        return inner.extract(selectedFeatures);
    }
}
