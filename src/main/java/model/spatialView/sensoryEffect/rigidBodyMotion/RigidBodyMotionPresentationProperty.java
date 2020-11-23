package model.spatialView.sensoryEffect.rigidBodyMotion;

import model.spatialView.sensoryEffect.EffectPresentationProperty;

public class RigidBodyMotionPresentationProperty extends EffectPresentationProperty {

    private MoveToward moveToward;
    private TrajectorySamples trajectorySamples;
    private Incline incline;
    private Shake shake;
    private Wave wave;
    private Spin spin;
    private Turn turn;
    private Collide collide;

    public MoveToward getMoveToward() {
        return moveToward;
    }

    public void setMoveToward(MoveToward moveToward) {
        this.moveToward = moveToward;
    }

    public TrajectorySamples getTrajectorySamples() {
        return trajectorySamples;
    }

    public void setTrajectorySamples(TrajectorySamples trajectorySamples) {
        this.trajectorySamples = trajectorySamples;
    }

    public Incline getIncline() {
        return incline;
    }

    public void setIncline(Incline incline) {
        this.incline = incline;
    }

    public Shake getShake() {
        return shake;
    }

    public void setShake(Shake shake) {
        this.shake = shake;
    }

    public Wave getWave() {
        return wave;
    }

    public void setWave(Wave wave) {
        this.wave = wave;
    }

    public Spin getSpin() {
        return spin;
    }

    public void setSpin(Spin spin) {
        this.spin = spin;
    }

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public Collide getCollide() {
        return collide;
    }

    public void setCollide(Collide collide) {
        this.collide = collide;
    }

}
