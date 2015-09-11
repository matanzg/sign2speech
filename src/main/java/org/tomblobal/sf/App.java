package org.tomblobal.sf;

import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import org.tomblobal.sf.myo.MyoEventsLogger;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            Hub hub = new Hub("org.tomblobal.sf");

            DeviceListener printer = new MyoEventsLogger();
            hub.addListener(printer);

            while (true) {
                hub.run(10);
            }
        } catch (Exception e) {
            System.err.println("Error: ");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
