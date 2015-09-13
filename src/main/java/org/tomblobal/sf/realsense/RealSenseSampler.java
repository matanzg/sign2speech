package org.tomblobal.sf.realsense;

import com.google.common.collect.Maps;
import org.apache.commons.io.input.Tailer;
import org.tomblobal.sf.IEventSampler;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class RealSenseSampler implements IEventSampler {

    private final AtomicReference<Map<String, Double>> lastRow = new AtomicReference<>();
    private final Tailer tailer;
    private final Process process;
    private final File fileToListen;

    public RealSenseSampler() throws Exception {
        // use temp later
        this.fileToListen = new File("C:\\Projects\\sign2speech\\realsenseoutput.csv");;

        fileToListen.createNewFile();

        process = new ProcessBuilder("C:\\Projects\\PrimeSenseTracker\\PrimeSenseTracker\\bin\\Debug\\PrimeSenseTracker.exe")
                .redirectOutput(fileToListen)
                .start();

        Thread.sleep(1000);

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


        process.destroy();
        Runtime.getRuntime().exec("taskkill /F /IM FF_HandsConsole.exe");

        Thread.sleep(1000);
        fileToListen.delete();
    }
}
