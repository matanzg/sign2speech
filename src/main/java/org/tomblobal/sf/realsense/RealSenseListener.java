package org.tomblobal.sf.realsense;

import com.google.common.primitives.Doubles;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class RealSenseListener extends TailerListenerAdapter {

    private final AtomicReference<Map<String, Double>> lastRow;
    private final AtomicBoolean stop = new AtomicBoolean(false);

    public RealSenseListener(AtomicReference<Map<String, Double>> lastRow) {
        this.lastRow = lastRow;
    }

    @Override
    public void handle(String line) {
        lastRow.set(parseRow(line));
    }

    private Map<String, Double> parseRow(String lastRow) {
        String[] keysAndValues = lastRow.split(",");
        Map<String, Double> lastRowMap = new HashMap<>(keysAndValues.length);
        Arrays.stream(keysAndValues).forEach(keyAndValue -> {
            String[] postEqualMark = keyAndValue.split("=");
            Double value = Doubles.tryParse(postEqualMark[1]);
            if (value == null) value = 0.0;
            lastRowMap.put(postEqualMark[0], value);
        });

        return lastRowMap;
    }
}
