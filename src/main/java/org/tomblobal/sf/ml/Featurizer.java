package org.tomblobal.sf.ml;

import com.google.common.collect.TreeBasedTable;

public interface Featurizer {
    double[] extract(TreeBasedTable<Long, String, Double> rawFeatures);

    static Featurizer create() {
        return new AggregatingMyoFeaturizer(new DummyNormalizer());
        //SamplingAveragingFeaturizer(
        //SamplingFuterizer(
        //new NaiveFeaturizer(
        //new RealSenseAndMyoFeaturizer(
        //new MyoOnlyFeaturizer(
        //        new DummyNormalizer()),
        //5);
    }
}
