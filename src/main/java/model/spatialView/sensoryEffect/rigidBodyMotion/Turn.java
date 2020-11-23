package model.spatialView.sensoryEffect.rigidBodyMotion;

public class Turn {

    private TurnAngle direction;
    private float speed;

    public TurnAngle getDirection() {
        return direction;
    }

    public void setDirection(TurnAngle direction) {
        this.direction = direction;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
