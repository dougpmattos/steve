package model.spatialView.sensoryEffect.rigidBodyMotion;

public class Collide {

    private MoveTowardAngle directionH;
    private MoveTowardAngle directionV;
    private float speed;

    public MoveTowardAngle getDirectionH() {
        return directionH;
    }

    public void setDirectionH(MoveTowardAngle directionH) {
        this.directionH = directionH;
    }

    public MoveTowardAngle getDirectionV() {
        return directionV;
    }

    public void setDirectionV(MoveTowardAngle directionV) {
        this.directionV = directionV;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
