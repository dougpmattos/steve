package model.spatialView.sensoryEffect;

import model.spatialView.sensoryEffect.enums.XPositionType;
import model.spatialView.sensoryEffect.enums.YPositionType;
import model.spatialView.sensoryEffect.enums.ZPositionType;

public class EffectPositionProperty {

	private XPositionType xPosition;
	private YPositionType yPosition;
	private ZPositionType zPosition;
	
	public EffectPositionProperty(){

		xPosition = XPositionType.LEFT;
		yPosition = YPositionType.TOP;
		zPosition = ZPositionType.FRONT;

	}

	public XPositionType getxPosition() {
		return xPosition;
	}

	public void setxPosition(XPositionType xPosition) {
		this.xPosition = xPosition;
	}

	public YPositionType getyPosition() {
		return yPosition;
	}

	public void setyPosition(YPositionType yPosition) {
		this.yPosition = yPosition;
	}

	public ZPositionType getzPosition() {
		return zPosition;
	}

	public void setzPosition(ZPositionType zPosition) {
		this.zPosition = zPosition;
	}
	
}
