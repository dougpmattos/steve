package model.common.enums;

public enum SensoryEffectType {

//	ACTIVE_KINESTHETIC("Active Kinesthetic Effect"),
//	RIGID_BODY_MOTION("Rigid Body Motion Effect"),
//	PASSIVE_KINESTHETIC_MOTION("Passive Kinesthetic Motion Effect"),
	WIND("Wind Effect"),
	LIGHT("Light Effect"),
	FLASH("Flash Effect"),
	SCENT("Scent Effect"),
	WATER_SPRAYER("Water Sprayer Effect"),
	FOG("Fog Effect"),
//	BUBBLE("Bubble"),
//	SNOW("Snow"),
	VIBRATION("Vibration Effect"),
//	COLOR_CORRECTION("Color Correction Effect"),
//	TACTILE("Tactile Effect"),
	COLD("Cold Effect"),
	HOT("Hot Effect"),
//	PASSIVE_KINESTHETIC_FORCE("Passive Kinesthetic Force Effect"),
	RAINSTORM("Rainstorm Effect");
	
	private String name;
	
	private SensoryEffectType(String name) {
        this.name = name;
    }
	
	@Override
	public String toString() {
		return name;
	}
	
}
