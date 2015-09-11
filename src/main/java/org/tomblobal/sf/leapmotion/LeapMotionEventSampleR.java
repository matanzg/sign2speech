package org.tomblobal.sf.leapmotion;

import com.leapmotion.leap.Controller;
import org.tomblobal.sf.IEventSampler;

import java.util.Map;

/**
 * Created by Matan on 9/10/2015.
 */
public class LeapMotionEventSampler implements IEventSampler {

    private final Controller controller = new Controller();
    private final LeapMotionListener listener = new LeapMotionListener();

    public LeapMotionEventSampler() {
        controller.addListener(listener);
    }

    @Override
    public Map<String, String> sample() {
        return listener.getCurrentData();
    }

    @Override
    public void close() throws Exception {
        controller.removeListener(listener);
        controller.delete();
    }
}
