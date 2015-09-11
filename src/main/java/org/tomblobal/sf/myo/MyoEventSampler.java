package org.tomblobal.sf.myo;

import com.thalmic.myo.Hub;
import org.tomblobal.sf.IEventSampler;

import java.util.Map;

/**
 * Created by Matan on 9/11/2015.
 */
public class MyoEventSampler implements IEventSampler {

    private final Hub hub = new Hub("org.tomblobal.sf");
    private final MyoEventsLogger listener = new MyoEventsLogger();

    public MyoEventSampler() {
        hub.addListener(listener);
    }

    @Override
    public Map<String, String> sample() {
        hub.runOnce(10);
        return listener.getCurrentData();
    }

    @Override
    public void close() throws Exception {
        hub.removeListener(listener);
    }
}
