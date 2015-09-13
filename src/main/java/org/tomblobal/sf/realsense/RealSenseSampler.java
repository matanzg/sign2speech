package org.tomblobal.sf.realsense;

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
        return lastRow.getAndSet(null);
    }

    @Override
    public void close() throws Exception {
        tailer.stop();
    }
}
