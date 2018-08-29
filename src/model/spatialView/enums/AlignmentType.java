package model.spatialView.enums;

public enum AlignmentType {

    TOP,
    BOTTOM,
    LEFT,
    RIGHT,
    CENTER,
    EQUAL;

    public String getDescription() {

        switch (this) {

            case TOP: return "Top Alignment Relative to the Master";
            case BOTTOM: return "Bottom Alignment Relative to the Master";
            case LEFT: return "Left Alignment Relative to the Master";
            case RIGHT: return "Right Alignment Relative to the Master";
            case CENTER: return "Center Alignment Relative to the Master";
            case EQUAL: return "Equal Alignment Relative to Master";

        }

        return this.toString();

    }

}
