package model.repository;

import model.nclDocument.extendedAna.Media;
import gui.repositoryPanel.MediaCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.util.Callback;

/**
 *
 * @author Douglas
 */
public class MediaList extends ListView<Object>{
    
    public static final ObservableList<Media> images = FXCollections.observableArrayList();
    public static final ObservableList<Media> video = FXCollections.observableArrayList();
    public static final ObservableList<Media> audio = FXCollections.observableArrayList();
    public static final ObservableList<Media> text = FXCollections.observableArrayList();
    public static final ObservableList<Media> others = FXCollections.observableArrayList();
    @SuppressWarnings("rawtypes")
	public static final ObservableList allTypes = FXCollections.observableArrayList();
    
    @SuppressWarnings("unchecked")
	public MediaList() {
        getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        setItems(allTypes);
        setCellFactory(new Callback<ListView<Object>, 
            ListCell<Object>>() {
                @Override 
                public ListCell<Object> call(ListView<Object> list) {
                    return new MediaCellFactory();
                }
            }
        );
    }
    
    @SuppressWarnings("unchecked")
	public void add(Media media) {
        allTypes.add(media);
        switch(media.getImportedMediaType()){
            case IMAGE:
                images.add(media);
                break;
                
            case VIDEO:
                video.add(media);
                break;
                
            case AUDIO:
                audio.add(media);
                break;
                
            case TEXT:
                text.add(media);
                break;
                
            case OTHER:
            case PROCEDURAL:
                others.add(media);  
                break;
                
        }
    }
    
    public void clear(){
        images.clear();
        video.clear();
        audio.clear();
        text.clear();
        others.clear();
        allTypes.clear();
    }
    
    public ObservableList<Media> getImageList(){
        return images;
    }
    
    public ObservableList<Media> getVideoList(){
        return video;
    }
    
    public ObservableList<Media> getAudioList(){
        return audio;
    }
    
    public ObservableList<Media> getTextList(){
        return text;
    }
    
    public ObservableList<Media> getOthersList(){
        return others;
    }
    
    @SuppressWarnings("rawtypes")
	public ObservableList getAllTypesList(){
        return allTypes;
    }
}
