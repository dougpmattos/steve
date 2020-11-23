package model.spatialView.sensoryEffect;

public class FlashPresentationProperty extends LightPresentationProperty {

    private int frequency;

    public FlashPresentationProperty(){
        super();
        frequency=0;
    }

    public int getFrequency() {
        return frequency;
    }

    /**
     * @param frequency must be positive
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
