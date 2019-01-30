package model.spatialView;


import java.io.Serializable;
import java.util.Observable;

public abstract class SpatialRelation<T>  extends Observable implements Serializable {

    private static final long serialVersionUID=3044752885230365391L;

    public static int spatialRelationNumber = 0;

    private int id;

    private T type; //this should be AlignmentType or DistributionType


    SpatialRelation(){
        this.id = spatialRelationNumber;
        spatialRelationNumber++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }

    public SpatialRelation getSpatialRelation(){
        return this;
    }




}
