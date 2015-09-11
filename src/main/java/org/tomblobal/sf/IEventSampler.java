package org.tomblobal.sf;

import java.util.Map;

/**
 * Created by Matan on 9/10/2015.
 */
public interface IEventSampler extends AutoCloseable {
    Map<String, String> sample();
}
