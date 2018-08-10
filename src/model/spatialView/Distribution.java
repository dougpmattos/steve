package model.spatialView;

import javafx.scene.media.Media;

import java.util.ArrayList;

public class Distribution<T> extends SpatialRelation<T> {

    //SERIALIZE???

    private String space;
    private ArrayList<Media> mediaList;

    Distribution(){

    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public ArrayList<Media> getMediaList() {
        return mediaList;
    }

    public void setMediaList(ArrayList<Media> mediaList) {
        this.mediaList = mediaList;
    }
}
