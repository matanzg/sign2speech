package org.tomblobal.sf.ml;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Created by Matan on 9/13/2015.
 */
public class MyoOnlyFeaturizer extends RealSenseAndMyoFeaturizer {
    public MyoOnlyFeaturizer(Normalizer normalizer) {
        super(normalizer);
    }

    @Override
    protected Set<String> getIncludedHeaders() {
        return super.getIncludedHeaders().stream().filter(t -> t.toLowerCase().contains("myo1")).collect(toSet());
    }
}
