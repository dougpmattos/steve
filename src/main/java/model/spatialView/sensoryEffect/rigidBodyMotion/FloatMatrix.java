package model.spatialView.sensoryEffect.rigidBodyMotion;

import java.util.ArrayList;

public class FloatMatrix {

    private ArrayList dim = new ArrayList<Integer>();
    private ArrayList floatVector = new ArrayList<Float>();

    public ArrayList getDim() {
        return dim;
    }

    public void setDim(ArrayList dim) {
        this.dim = dim;
    }

    public ArrayList getFloatVector() {
        return floatVector;
    }

    public void setFloatVector(ArrayList floatVector) {
        this.floatVector = floatVector;
    }
}
