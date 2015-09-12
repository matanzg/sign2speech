package org.tomblobal.sf.myo;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.enums.StreamEmgType;
import com.thalmic.myo.enums.VibrationType;
import org.tomblobal.sf.IEventSampler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by Matan on 9/11/2015.
 */
public class MyoEventSampler implements IEventSampler {

    private final Hub hub = new Hub("org.tomblobal.sf");
    private final MyoEventsLogger listener = new MyoEventsLogger();
    private boolean isRunning = true;

    public MyoEventSampler() throws IOException {
        int connectedDevices = 0;
        hub.addListener(listener);

        while (true) {
            System.out.println("Waiting 5s for a Myo to connect...");
            Myo myo = hub.waitForMyo(5000);
            boolean isConnected = myo != null;
            if (isConnected) {
                myo.setStreamEmg(StreamEmgType.STREAM_EMG_ENABLED);
                myo.vibrate(VibrationType.VIBRATION_LONG);
                System.out.println("Success! we have " + ++connectedDevices + " devices connected!");
            } else {
                System.out.println("No devices connected after 5s...");
            }

            if (!keepGoing())
                break;
        }

        Executors.defaultThreadFactory().newThread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    hub.runOnce(10);
                }
            }
        }).start();
    }

    private boolean keepGoing() throws IOException {
        System.out.println("Wait for another Myo? (y/n)");
        int response = System.in.read();
        boolean yes = response == (int) 'Y' || response == (int) 'y';
        boolean no = response == (int) 'N' || response == (int) 'n';
        System.in.skip(1);
        return yes || !no && keepGoing();
    }

    @Override
    public Map<String, String> sample() {
        return listener.getCurrentData();
    }

    @Override
    public void close() throws Exception {
        hub.removeListener(listener);
        isRunning = false;
    }
}
