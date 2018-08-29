package model.spatialView.enums;

public enum DistributionType {

    VERTICAL,
    HORIZONTAL;

    public String getDescription() {

        switch (this) {

            case VERTICAL: return "Medias equally aligned vertically";
            case HORIZONTAL: return "Medias equally aligned horizontally";

        }

        return this.toString();

    }

}
