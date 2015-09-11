package org.tomblobal.sf.myo;


import java.util.ArrayList;
import java.util.List;

import com.thalmic.myo.*;
import com.thalmic.myo.enums.Arm;
import com.thalmic.myo.enums.WarmupResult;
import com.thalmic.myo.enums.WarmupState;
import com.thalmic.myo.enums.XDirection;

public class MyoEventsLogger implements DeviceListener {

    private final List<Myo> knownMyos = new ArrayList<>();

    @Override
    public void onPair(Myo myo, long timestamp, FirmwareVersion firmwareVersion) {
        System.out.println(String.format("Myo %s has paired.", identifyMyo(myo)));
    }

    @Override
    public void onUnpair(Myo myo, long timestamp) {
        System.out.println(String.format("Myo %s has unpaired.", identifyMyo(myo)));
        if (knownMyos.contains(myo)) {
            knownMyos.remove(myo);
        }
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
    }

    @Override
    public void onAccelerometerData(Myo myo, long timestamp, Vector3 accel) {
    }

    @Override
    public void onGyroscopeData(Myo myo, long timestamp, Vector3 gyro) {
    }

    @Override
    public void onRssi(Myo myo, long timestamp, int rssi) {
    }

    @Override
    public void onEmgData(Myo myo, long timestamp, byte[] emg) {
    }

    @Override
    public void onBatteryLevelReceived(Myo myo, long timestamp, int level) {
    }

    @Override
    public void onWarmupCompleted(Myo myo, long timestamp, WarmupResult warmupResult) {
    }

    @Override
    public void onPose(Myo myo, long timestamp, Pose pose) {

    }

    @Override
    public void onConnect(Myo myo, long timestamp, FirmwareVersion firmwareVersion) {
        System.out.println(String.format("Myo %s has connected.", identifyMyo(myo)));
    }


    @Override
    public void onDisconnect(Myo myo, long timestamp) {
        System.out.println(String.format("Myo %s has disconnected.", identifyMyo(myo)));
        knownMyos.remove(myo);
    }

    private int identifyMyo(Myo myo) {
        int index = knownMyos.indexOf(myo);
        if (index >= 0) {
            return index + 1;
        }

        knownMyos.add(myo);
        return knownMyos.indexOf(myo) + 1;
    }
}