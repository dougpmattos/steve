package model.spatialView.sensoryEffect.tactile;

import java.io.Serializable;

public class TactileResource implements Serializable {

    private String type;
    private String ref;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

}
