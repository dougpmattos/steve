package model.spatialView.sensoryEffect.rigidBodyMotion;

public class InclineAngle {

    private int angle;

    public int getAngle() {
        return angle;
    }

    /**
     * @param   angle must be a value between -359 and 359
     */
    public void setAngle(int angle) {
        this.angle = angle;
    }

}
