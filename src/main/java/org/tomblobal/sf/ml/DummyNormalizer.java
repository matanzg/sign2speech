package org.tomblobal.sf.ml;

import com.google.common.collect.TreeBasedTable;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.stream.Collectors;

public class DummyNormalizer implements Normalizer {
    @Override
    public TreeBasedTable<Long, String, Double> normalize(TreeBasedTable<Long, String, Double> rawFeatures) {
//        long numOfRows = rawFeatures.rowMap().size();
//        Set<String> columnsToZero = rawFeatures.columnMap().entrySet().stream()
//                .filter(t -> t.getValue().entrySet().stream().filter(c -> c.getValue() > 0d).count() <= (numOfRows / 10))
//                .map(Map.Entry::getKey)
//                .collect(Collectors.toSet());
//
//        for (Map<String, Double> r : rawFeatures.rowMap().values()) {
//            for (String c : columnsToZero) {
//                r.put(c, 0d);
//            }
//        }

        return rawFeatures;
    }
}
