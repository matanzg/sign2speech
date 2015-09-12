package org.tomblobal.sf.ml;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Matan on 9/11/2015.
 */
public class NaiveFeaturizer extends NormalizingHeaderBasedFeaturizer {
    public NaiveFeaturizer(Normalizer normalizer) {
        super(normalizer);
    }

    @Override
    protected Set<String> getIncludedHeaders() {
        return Sets.newHashSet(
                "myo1_acceleration_X",
                "myo1_acceleration_Y",
                "myo1_acceleration_Z",
                "myo1_emg0",
                "myo1_emg1",
                "myo1_emg2",
                "myo1_emg3",
                "myo1_emg4",
                "myo1_emg5",
                "myo1_emg6",
                "myo1_emg7",
                "myo1_gyro_",
                "myo1_gyro_X",
                "myo1_gyro_Y",
                "myo1_gyro_Z",
                "myo1_rotation_W",
                "myo1_rotation_X",
                "myo1_rotation_Y",
                "myo1_rotation_Z",

                //"Leap__R_isLeft",
                "Leap__R_pitch",
                "Leap__R_roll",
                "Leap__R_yaw",

                "Leap__R_palmDirectionX",
                "Leap__R_palmDirectionY",
                "Leap__R_palmDirectionZ",
                "Leap__R_palmNormalX",
                "Leap__R_palmNormalY",
                "Leap__R_palmNormalZ",
                "Leap__R_palmPositionX",
                "Leap__R_palmPositionY",
                "Leap__R_palmPositionZ",

                "Leap__R_arm_directionX",
                "Leap__R_arm_directionY",
                "Leap__R_arm_directionZ",
                "Leap__R_arm_elbowPosX",
                "Leap__R_arm_elbowPosY",
                "Leap__R_arm_elbowPosZ",
                "Leap__R_arm_wristPosX",
                "Leap__R_arm_wristPosY",
                "Leap__R_arm_wristPosZ",

                "Leap__R__Finger_TYPE_INDEX__BoneType_TYPE_DISTAL_directionX",
                "Leap__R__Finger_TYPE_INDEX__BoneType_TYPE_DISTAL_directionY",
                "Leap__R__Finger_TYPE_INDEX__BoneType_TYPE_DISTAL_directionZ",
                "Leap__R__Finger_TYPE_INDEX__BoneType_TYPE_INTERMEDIATE_directionX",
                "Leap__R__Finger_TYPE_INDEX__BoneType_TYPE_INTERMEDIATE_directionY",
                "Leap__R__Finger_TYPE_INDEX__BoneType_TYPE_INTERMEDIATE_directionZ",
                "Leap__R__Finger_TYPE_INDEX__BoneType_TYPE_METACARPAL_directionX",
                "Leap__R__Finger_TYPE_INDEX__BoneType_TYPE_METACARPAL_directionY",
                "Leap__R__Finger_TYPE_INDEX__BoneType_TYPE_METACARPAL_directionZ",
                "Leap__R__Finger_TYPE_INDEX__BoneType_TYPE_PROXIMAL_directionX",
                "Leap__R__Finger_TYPE_INDEX__BoneType_TYPE_PROXIMAL_directionY",
                "Leap__R__Finger_TYPE_INDEX__BoneType_TYPE_PROXIMAL_directionZ",

                "Leap__R__Finger_TYPE_MIDDLE__BoneType_TYPE_DISTAL_directionX",
                "Leap__R__Finger_TYPE_MIDDLE__BoneType_TYPE_DISTAL_directionY",
                "Leap__R__Finger_TYPE_MIDDLE__BoneType_TYPE_DISTAL_directionZ",
                "Leap__R__Finger_TYPE_MIDDLE__BoneType_TYPE_INTERMEDIATE_directionX",
                "Leap__R__Finger_TYPE_MIDDLE__BoneType_TYPE_INTERMEDIATE_directionY",
                "Leap__R__Finger_TYPE_MIDDLE__BoneType_TYPE_INTERMEDIATE_directionZ",
                "Leap__R__Finger_TYPE_MIDDLE__BoneType_TYPE_METACARPAL_directionX",
                "Leap__R__Finger_TYPE_MIDDLE__BoneType_TYPE_METACARPAL_directionY",
                "Leap__R__Finger_TYPE_MIDDLE__BoneType_TYPE_METACARPAL_directionZ",
                "Leap__R__Finger_TYPE_MIDDLE__BoneType_TYPE_PROXIMAL_directionX",
                "Leap__R__Finger_TYPE_MIDDLE__BoneType_TYPE_PROXIMAL_directionY",
                "Leap__R__Finger_TYPE_MIDDLE__BoneType_TYPE_PROXIMAL_directionZ",

                "Leap__R__Finger_TYPE_PINKY__BoneType_TYPE_DISTAL_directionX",
                "Leap__R__Finger_TYPE_PINKY__BoneType_TYPE_DISTAL_directionY",
                "Leap__R__Finger_TYPE_PINKY__BoneType_TYPE_DISTAL_directionZ",
                "Leap__R__Finger_TYPE_PINKY__BoneType_TYPE_INTERMEDIATE_directionX",
                "Leap__R__Finger_TYPE_PINKY__BoneType_TYPE_INTERMEDIATE_directionY",
                "Leap__R__Finger_TYPE_PINKY__BoneType_TYPE_INTERMEDIATE_directionZ",
                "Leap__R__Finger_TYPE_PINKY__BoneType_TYPE_METACARPAL_directionX",
                "Leap__R__Finger_TYPE_PINKY__BoneType_TYPE_METACARPAL_directionY",
                "Leap__R__Finger_TYPE_PINKY__BoneType_TYPE_METACARPAL_directionZ",
                "Leap__R__Finger_TYPE_PINKY__BoneType_TYPE_PROXIMAL_directionX",
                "Leap__R__Finger_TYPE_PINKY__BoneType_TYPE_PROXIMAL_directionY",
                "Leap__R__Finger_TYPE_PINKY__BoneType_TYPE_PROXIMAL_directionZ",

                "Leap__R__Finger_TYPE_RINK__BoneType_TYPE_DISTAL_directionX",
                "Leap__R__Finger_TYPE_RINK__BoneType_TYPE_DISTAL_directionY",
                "Leap__R__Finger_TYPE_RINK__BoneType_TYPE_DISTAL_directionZ",
                "Leap__R__Finger_TYPE_RINK__BoneType_TYPE_INTERMEDIATE_directionX",
                "Leap__R__Finger_TYPE_RINK__BoneType_TYPE_INTERMEDIATE_directionY",
                "Leap__R__Finger_TYPE_RINK__BoneType_TYPE_INTERMEDIATE_directionZ",
                "Leap__R__Finger_TYPE_RINK__BoneType_TYPE_METACARPAL_directionX",
                "Leap__R__Finger_TYPE_RINK__BoneType_TYPE_METACARPAL_directionY",
                "Leap__R__Finger_TYPE_RINK__BoneType_TYPE_METACARPAL_directionZ",
                "Leap__R__Finger_TYPE_RINK__BoneType_TYPE_PROXIMAL_directionX",
                "Leap__R__Finger_TYPE_RINK__BoneType_TYPE_PROXIMAL_directionY",
                "Leap__R__Finger_TYPE_RINK__BoneType_TYPE_PROXIMAL_directionZ"
        );
    }
}
