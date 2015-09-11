package org.tomblobal.sf.ml;

import com.google.common.base.Optional;
import org.junit.Test;

import java.util.SortedMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.guava.api.Assertions.assertThat;

public class AzureProbabilityClientTest {

    @Test
    public void testGetProbability() throws Exception {
        Optional<SortedMap<Character, Double>> probabilities =
                new AzureProbabilityClient().getProbability(new long[]{2,
                        8,
                        3,
                        5,
                        1,
                        8,
                        13,
                        0,
                        6,
                        6,
                        10,
                        8,
                        0,
                        8,
                        0,
                        8});

        assertThat(probabilities).isPresent();
        SortedMap<Character, Double> map = probabilities.get();
        assertThat(map.firstKey()).isEqualTo('T');
    }
}