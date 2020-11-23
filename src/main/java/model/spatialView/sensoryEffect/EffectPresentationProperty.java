package model.spatialView.sensoryEffect;

import java.io.Serializable;

import model.spatialView.PresentationProperty;
import model.spatialView.sensoryEffect.enums.ScentType;

public abstract class EffectPresentationProperty extends PresentationProperty<EffectPositionProperty> implements Serializable {

	private static final long serialVersionUID = -4925711654365986227L;
	
	private int priority;
	
	public EffectPresentationProperty(){
		
		this.positionProperty = new EffectPositionProperty();
		
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
}
