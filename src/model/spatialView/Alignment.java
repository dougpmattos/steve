package model.spatialView;

import javafx.scene.media.Media;

import java.util.ArrayList;

public class Alignment<T> extends SpatialRelation<T> {

    //SERIALIZE???

    private Media masterMedia;
    private ArrayList<Media> slaveMediaList;

    Alignment(){

    }

    public void setMasterMedia(Media masterMedia){
        this.masterMedia = masterMedia;

    }

    public Media getMasterMedia(){
        return this.masterMedia;
    }

    public void setSlaveMediaList(ArrayList<Media> slaveMediaList){
        this.slaveMediaList = slaveMediaList;
    }

    public ArrayList<Media> getSlaveMediaList(){
        return this.slaveMediaList;
    }


}
