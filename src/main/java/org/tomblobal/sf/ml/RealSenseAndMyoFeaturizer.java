package org.tomblobal.sf.ml;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Matan on 9/13/2015.
 */
public class RealSenseAndMyoFeaturizer extends NormalizingHeaderBasedFeaturizer {
    public RealSenseAndMyoFeaturizer(Normalizer normalizer) {
        super(normalizer);
    }

    @Override
    protected Set<String> getIncludedHeaders() {
        return new HashSet<>(Arrays.asList(
                "RS_Left_JOINT_CENTER_X",
                "RS_Left_JOINT_INDEX_BASE_X",
                "RS_Left_JOINT_INDEX_JT1_X",
                "RS_Left_JOINT_INDEX_JT2_X",
                "RS_Left_JOINT_INDEX_TIP_X",
                "RS_Left_JOINT_MIDDLE_BASE_X",
                "RS_Left_JOINT_MIDDLE_JT1_X",
                "RS_Left_JOINT_MIDDLE_JT2_X",
                "RS_Left_JOINT_MIDDLE_TIP_X",
                "RS_Left_JOINT_PINKY_BASE_X",
                "RS_Left_JOINT_PINKY_JT1_X",
                "RS_Left_JOINT_PINKY_JT2_X",
                "RS_Left_JOINT_PINKY_TIP_X",
                "RS_Left_JOINT_RING_BASE_X",
                "RS_Left_JOINT_RING_JT1_X",
                "RS_Left_JOINT_RING_JT2_X",
                "RS_Left_JOINT_RING_TIP_X",
                "RS_Left_JOINT_THUMB_BASE_X",
                "RS_Left_JOINT_THUMB_JT1_X",
                "RS_Left_JOINT_THUMB_JT2_X",
                "RS_Left_JOINT_THUMB_TIP_X",
                "RS_Left_JOINT_WRIST_X",
                "RS_Right_JOINT_CENTER_X",
                "RS_Right_JOINT_INDEX_BASE_X",
                "RS_Right_JOINT_INDEX_JT1_X",
                "RS_Right_JOINT_INDEX_JT2_X",
                "RS_Right_JOINT_INDEX_TIP_X",
                "RS_Right_JOINT_MIDDLE_BASE_X",
                "RS_Right_JOINT_MIDDLE_JT1_X",
                "RS_Right_JOINT_MIDDLE_JT2_X",
                "RS_Right_JOINT_MIDDLE_TIP_X",
                "RS_Right_JOINT_PINKY_BASE_X",
                "RS_Right_JOINT_PINKY_JT1_X",
                "RS_Right_JOINT_PINKY_JT2_X",
                "RS_Right_JOINT_PINKY_TIP_X",
                "RS_Right_JOINT_RING_BASE_X",
                "RS_Right_JOINT_RING_JT1_X",
                "RS_Right_JOINT_RING_JT2_X",
                "RS_Right_JOINT_RING_TIP_X",
                "RS_Right_JOINT_THUMB_BASE_X",
                "RS_Right_JOINT_THUMB_JT1_X",
                "RS_Right_JOINT_THUMB_JT2_X",
                "RS_Right_JOINT_THUMB_TIP_X",
                "RS_Right_JOINT_WRIST_X",
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
                "myo1_gyro_X",
                "myo1_gyro_Y",
                "myo1_gyro_Z",
                "myo1_rotation_W",
                "myo1_rotation_X",
                "myo1_rotation_Y",
                "myo1_rotation_Z",
                "myo2_acceleration_X",
                "myo2_acceleration_Y",
                "myo2_acceleration_Z",
                "myo2_emg0",
                "myo2_emg1",
                "myo2_emg2",
                "myo2_emg3",
                "myo2_emg4",
                "myo2_emg5",
                "myo2_emg6",
                "myo2_emg7",
                "myo2_gyro_X",
                "myo2_gyro_Y",
                "myo2_gyro_Z",
                "myo2_rotation_W",
                "myo2_rotation_X",
                "myo2_rotation_Y",
                "myo2_rotation_Z"
        ));
    }
}
