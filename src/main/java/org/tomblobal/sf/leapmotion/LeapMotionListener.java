package org.tomblobal.sf.leapmotion;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by Matan on 9/10/2015.
 */
public class LeapMotionListener extends Listener {
    private Controller currentController;
    private final AtomicBoolean isCollectingData = new AtomicBoolean(false);

    public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
    }

    public void onDisconnect(Controller controller) {
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    public void onFrame(Controller controller) {

        if (isCollectingData.get()) {
            return;
        }

        currentController = controller;
        getControllerData(controller);
    }

    private Map<String, Double> getControllerData(Controller controller) {

        if (controller == null) {
            return new HashMap<>();
        }

        Map<String, Double> trace = new HashMap<>();
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();

        report(trace, "hands_count", frame.hands().count());
        report(trace, "fingers_count", frame.fingers().count());
        report(trace, "gestures_count", frame.gestures().count());

        //Get hands
        for (Hand hand : frame.hands()) {
            String handPrefix = hand.isLeft() ? "_L_" : "_R_";
            report(trace, handPrefix + "isLeft", hand.isLeft() ? 1 : 0);
            report(trace, handPrefix + "palmPosition", hand.palmPosition());
            report(trace, handPrefix + "palmNormal", hand.palmNormal());
            report(trace, handPrefix + "palmDirection", hand.direction());

            Vector normal = hand.palmNormal();
            Vector direction = hand.direction();

            // Calculate the hand's pitch, roll, and yaw angles
            report(trace, handPrefix + "pitch", Math.toDegrees(direction.pitch()));
            report(trace, handPrefix + "roll", Math.toDegrees(normal.roll()));
            report(trace, handPrefix + "yaw", Math.toDegrees(direction.yaw()));

            // Get arm bone
            Arm arm = hand.arm();
            report(trace, handPrefix + "arm_direction", arm.direction());
            report(trace, handPrefix + "arm_wristPos", arm.wristPosition());
            report(trace, handPrefix + "arm_elbowPos", arm.elbowPosition());

            // Get fingers
            for (Finger finger : hand.fingers()) {

                String fingerPrefix = handPrefix + "_Finger_" + finger.type() + "_";
                report(trace, fingerPrefix + "lengthMM", finger.length());
                report(trace, fingerPrefix + "widthMM", finger.width());

                //Get Bones
                for (Bone.Type boneType : Bone.Type.values()) {
                    Bone bone = finger.bone(boneType);
                    String bonePrefix = fingerPrefix + "_BoneType_" + boneType + "_";
                    report(trace, bonePrefix + "start", bone.prevJoint());
                    report(trace, bonePrefix + "end", bone.nextJoint());
                    report(trace, bonePrefix + "direction", bone.direction());
                }
            }
        }

        GestureList gestures = frame.gestures();
        for (int i = 0; i < gestures.count(); i++) {
            Gesture gesture = gestures.get(i);
            String gestureId = "gesture_" + gesture.id() + "_";
            report(trace, gestureId + "_type", gesture.type().ordinal());
            report(trace, gestureId + "_state", gesture.state().ordinal());

            switch (gesture.type()) {
                case TYPE_CIRCLE:
                    CircleGesture circle = new CircleGesture(gesture);

                    // Calculate clock direction using the angle between circle normal and pointable
                    int clockwiseness;
                    if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI / 2) {
                        // Clockwise if angle is less than 90 degrees
                        clockwiseness = 0;
                    } else {
                        clockwiseness = 1;
                    }

                    // Calculate angle swept since last frame
                    double sweptAngle = 0;
                    if (circle.state() != State.STATE_START) {
                        CircleGesture previousUpdate = new CircleGesture(controller.frame(1).gesture(circle.id()));
                        sweptAngle = (circle.progress() - previousUpdate.progress()) * 2 * Math.PI;
                    }

                    report(trace, gestureId + "progress", circle.progress());
                    report(trace, gestureId + "radius", circle.radius());
                    report(trace, gestureId + "angle", Math.toDegrees(sweptAngle));
                    report(trace, gestureId + "direction", clockwiseness);

                    break;
                case TYPE_SWIPE:
                    SwipeGesture swipe = new SwipeGesture(gesture);
                    report(trace, gestureId + "position", swipe.position());
                    report(trace, gestureId + "direction", swipe.direction());
                    report(trace, gestureId + "speed", swipe.speed());
                    report(trace, gestureId + "startPosition", swipe.startPosition());

                    break;
                case TYPE_SCREEN_TAP:
                    ScreenTapGesture screenTap = new ScreenTapGesture(gesture);
                    report(trace, gestureId + "position", screenTap.position());
                    report(trace, gestureId + "direction", screenTap.direction());
                    report(trace, gestureId + "progress", screenTap.progress());
                    break;
                case TYPE_KEY_TAP:
                    KeyTapGesture keyTap = new KeyTapGesture(gesture);
                    report(trace, gestureId + "position", keyTap.position());
                    report(trace, gestureId + "direction", keyTap.direction());
                    report(trace, gestureId + "progress", keyTap.progress());
                    //+ ", direction: " + keyTap.pointable());
                    break;
            }
        }

        return trace;
    }

    private void report(Map<String, Double> trace, String event, double value) {
        synchronized (this) {
            trace.put("Leap_" + event, value);
        }
    }

    private void report(Map<String, Double> trace, String event, Vector value) {
        report(trace, event + "X", value.getX());
        report(trace, event + "Y", value.getY());
        report(trace, event + "Z", value.getZ());
    }

    public Map<String, Double> getCurrentData() {
        synchronized (this) {
            isCollectingData.set(true);
            Map<String, Double> controllerData = getControllerData(currentController);
            currentController = null;
            isCollectingData.set(false);
            return controllerData;
        }
    }
}

