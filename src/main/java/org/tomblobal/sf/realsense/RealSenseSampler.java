package org.tomblobal.sf.realsense;

import com.google.common.collect.Maps;
import org.apache.commons.io.input.Tailer;
import org.tomblobal.sf.IEventSampler;

import java.io.File;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class RealSenseSampler implements IEventSampler {

    private final AtomicReference<Map<String, Double>> lastRow = new AtomicReference<>();
    private final Tailer tailer;

    public RealSenseSampler(File fileToListen) {
        this.tailer = Tailer.create(fileToListen, new RealSenseListener(lastRow), 10);
    }

    @Override
    public Map<String, Double> sample() {
        Map<String, Double> values = lastRow.getAndSet(null);
        if (values == null) values = Maps.newHashMap();
        return values;
    }

    @Override
    public void close() throws Exception {
        tailer.stop();
    }
}
