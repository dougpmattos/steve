package model.common.enums;

public enum SensoryEffectType {

	WIND("Wind Effect"),
	LIGHT("Light Effect"),
	SCENT("Scent Effect"),
	WATER_SPRAYER("Water Sprayer Effect"),
	IMPACT("Impact Effect"),
	OLFATIVE("Olfative Effect"),
	TACTILE("Tactile Effect"),
	COLD("Cold Effect"),
	HOT("Hot Effect"),
	RIGID_BODY_MOTION("Rigid Body Motion Effect"),
	FOG("Fog Effect"),
	COLORED_LIGHT("Colored Light Effect"),
	FLASH_LIGHT("Flash Light Effect"),
	VIBRATION("Vibration Effect"),
	COLOR_CORRECTION("Color Correction Effect"),
	PASSIVE_KINESTHETIC_MOTION("Passive Kinesthetic Motion Effect"),
	PASSIVE_KINESTHETIC_FORCE("Passive Kinesthetic Force Effect"),
	ACTIVE_KINESTHETIC("Active Kinesthetic Effect"),
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
