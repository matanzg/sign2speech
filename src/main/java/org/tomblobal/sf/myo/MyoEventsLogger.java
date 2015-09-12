package org.tomblobal.sf.myo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thalmic.myo.*;
import com.thalmic.myo.enums.*;

public class MyoEventsLogger implements DeviceListener {

    private final List<Myo> _knownMyos = new ArrayList<>();
    private Map<String, Double> _currentData = new HashMap<>();

    @Override
    public void onPair(Myo myo, long timestamp, FirmwareVersion firmwareVersion) {
        System.out.println("a Myo has paired.");
    }

    @Override
    public void onUnpair(Myo myo, long timestamp) {
        System.out.println(String.format("Myo %s has unpaired.", identifyMyo(myo)));
    }

    @Override
    public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection, WarmupState warmupState) {
    }

    @Override
    public void onArmUnsync(Myo myo, long timestamp) {
    }

    @Override
    public void onUnlock(Myo myo, long timestamp) {
    }

    @Override
    public void onLock(Myo myo, long timestamp) {
    }

    @Override
    public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
        report(myo, "rotation_", rotation);
    }

    @Override
    public void onAccelerometerData(Myo myo, long timestamp, Vector3 accel) {
        report(myo, "acceleration_", accel);
    }

    @Override
    public void onGyroscopeData(Myo myo, long timestamp, Vector3 gyro) {
        report(myo, "gyro_", gyro);
    }

    @Override
    public void onRssi(Myo myo, long timestamp, int rssi) {
    }

    @Override
    public void onEmgData(Myo myo, long timestamp, byte[] emg) {
        for (int i = 0; i < emg.length; i++) {
            report(myo, "emg" + i, emg[i]);
        }
    }

    @Override
    public void onBatteryLevelReceived(Myo myo, long timestamp, int level) {
    }

    @Override
    public void onWarmupCompleted(Myo myo, long timestamp, WarmupResult warmupResult) {
    }

    @Override
    public void onPose(Myo myo, long timestamp, Pose pose) {
        report(myo, "pose", pose.getType().ordinal());
    }

    @Override
    public void onConnect(Myo myo, long timestamp, FirmwareVersion firmwareVersion) {
        System.out.println("a Myo has connected.");
    }

    @Override
    public void onDisconnect(Myo myo, long timestamp) {
        System.out.println(String.format("Myo %s has disconnected.", identifyMyo(myo)));
    }

    private int identifyMyo(Myo myo) {
        if (myo != null && !_knownMyos.contains(myo)) {
            _knownMyos.add(myo);
        }

        return _knownMyos.indexOf(myo) + 1;
    }

    private void report(Myo myo, String key, Quaternion value) {
        report(myo, key + "W", value.getW());
        report(myo, key + "X", value.getX());
        report(myo, key + "Y", value.getY());
        report(myo, key + "Z", value.getZ());
    }

    private void report(Myo myo, String key, Vector3 value) {
        report(myo, key + "X", value.getX());
        report(myo, key + "Y", value.getY());
        report(myo, key + "Z", value.getZ());
    }

    private void report(Myo myo, String key, double value) {
        if (myo == null) {
            return;
        }

        synchronized (this) {
            _currentData.put("myo" + identifyMyo(myo) + "_" + key, value);
        }
    }

    public Map<String, Double> getCurrentData() {
        synchronized (this) {
            HashMap<String, Double> stringStringHashMap = new HashMap<>(_currentData);
            _currentData = new HashMap<>();
            return stringStringHashMap;
        }
    }
}