package model.spatialView.sensoryEffect.rigidBodyMotion;

import model.spatialView.sensoryEffect.commonProperties.IntensityRange;

public class Incline {

    private float pitchSpeed;
    private float pitchAcceleration;
    private float rollSpeed;
    private float rollAcceleration;
    private float yawSpedd;
    private float yawAcceleration;
    private IntensityRange pitchRange;
    private IntensityRange rollRange;
    private IntensityRange yawRange;
    private InclineAngle pitch;
    private InclineAngle roll;
    private InclineAngle yaw;

    public float getPitchSpeed() {
        return pitchSpeed;
    }

    public void setPitchSpeed(float pitchSpeed) {
        this.pitchSpeed = pitchSpeed;
    }

    public float getPitchAcceleration() {
        return pitchAcceleration;
    }

    public void setPitchAcceleration(float pitchAcceleration) {
        this.pitchAcceleration = pitchAcceleration;
    }

    public float getRollSpeed() {
        return rollSpeed;
    }

    public void setRollSpeed(float rollSpeed) {
        this.rollSpeed = rollSpeed;
    }

    public float getRollAcceleration() {
        return rollAcceleration;
    }

    public void setRollAcceleration(float rollAcceleration) {
        this.rollAcceleration = rollAcceleration;
    }

    public float getYawSpedd() {
        return yawSpedd;
    }

    public void setYawSpedd(float yawSpedd) {
        this.yawSpedd = yawSpedd;
    }

    public float getYawAcceleration() {
        return yawAcceleration;
    }

    public void setYawAcceleration(float yawAcceleration) {
        this.yawAcceleration = yawAcceleration;
    }

    public IntensityRange getPitchRange() {
        return pitchRange;
    }

    public void setPitchRange(IntensityRange pitchRange) {
        this.pitchRange = pitchRange;
    }

    public IntensityRange getRollRange() {
        return rollRange;
    }

    public void setRollRange(IntensityRange rollRange) {
        this.rollRange = rollRange;
    }

    public IntensityRange getYawRange() {
        return yawRange;
    }

    public void setYawRange(IntensityRange yawRange) {
        this.yawRange = yawRange;
    }

    public InclineAngle getPitch() {
        return pitch;
    }

    public void setPitch(InclineAngle pitch) {
        this.pitch = pitch;
    }

    public InclineAngle getRoll() {
        return roll;
    }

    public void setRoll(InclineAngle roll) {
        this.roll = roll;
    }

    public InclineAngle getYaw() {
        return yaw;
    }

    public void setYaw(InclineAngle yaw) {
        this.yaw = yaw;
    }
}
