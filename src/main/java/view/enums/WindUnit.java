package view.enums;

public enum WindUnit {


    BEAUFORT,
    CUSTOM;

    @Override
    public String toString(){

        switch(this) {

            case BEAUFORT:
                return "Beaufort";

            case CUSTOM:
                return "Custom";

        }

        return null;

    }

}
