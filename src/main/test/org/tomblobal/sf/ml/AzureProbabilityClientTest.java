package org.tomblobal.sf.ml;

import com.google.common.base.Optional;
import com.google.common.collect.TreeBasedTable;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.guava.api.Assertions.assertThat;

public class AzureProbabilityClientTest {

    @Test
    public void testGetProbability() throws Exception {
        SinglePredicitioner predicitioner = new SinglePredicitioner(new DummyFeaturizer(new DummyNormalizer()),
                Double.MIN_VALUE, new AzureProbabilityClient());

        TreeBasedTable<Long, String, Double> dummyTable = TreeBasedTable.create();
        for (long i = 0; i < 670; i++) {
            dummyTable.put(0l, String.valueOf(i), 1.1);
        }

        Optional<String> result = predicitioner.predictSingle(dummyTable);
        assertThat(result).isPresent();
    }
}