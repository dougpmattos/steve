package gui.repositoryPanel;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import model.NCLSupport.extendedAna.Media;
import model.repository.ListUpdateOperation;
import model.repository.MediaList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.repository.MediaController;

/**
 *
 * @author Douglas
 */
public class MediaListPane extends StackPane implements Observer {
    
	private MediaController mediaController = MediaController.getMediaController();
	
	final Logger logger = LoggerFactory.getLogger(MediaListPane.class);
	private final int ADD = 1;
	private final int REMOVE = 2;
	private final int CLEAR = 3;
	
	private final String OTHERS = "Others";
	private final String TEXT = "Text";
	private final String AUDIO = "Audio";
	private final String VIDEO = "Video";
	private final String IMAGE = "Image";
	private final String MEDIA_FILES = "Media Files";
	
	private final FlowPane allTypes = new FlowPane();
	private final FlowPane image = new FlowPane();
	private final FlowPane video = new FlowPane();
	private final FlowPane audio = new FlowPane();
	private final FlowPane text = new FlowPane();
	private final FlowPane others = new FlowPane();
	
	public MediaListPane(){
		
		setLayout();
		getChildren().add(allTypes);

	    mediaController.getMediaList().addObserver(this);
		
	}

	private void setLayout() {
		allTypes.setPadding(new Insets(5, 0, 5, 0));
		allTypes.setVgap(8);
		allTypes.setHgap(15);
		allTypes.setPrefWrapLength(170);
		
		image.setPadding(new Insets(5, 0, 5, 0));
		image.setVgap(8);
		image.setHgap(15);
		image.setPrefWrapLength(170);
		
		video.setPadding(new Insets(5, 0, 5, 0));
		video.setVgap(8);
		video.setHgap(15);
		video.setPrefWrapLength(170);
		
		audio.setPadding(new Insets(5, 0, 5, 0));
		audio.setVgap(8);
		audio.setHgap(15);
		audio.setPrefWrapLength(170);
		
		text.setPadding(new Insets(5, 0, 5, 0));
		text.setVgap(8);
		text.setHgap(15);
		text.setPrefWrapLength(170);
		
		others.setPadding(new Insets(5, 0, 5, 0));
		others.setVgap(8);
		others.setHgap(15);
		others.setPrefWrapLength(170);
	}

	public void setSelectPaneToFront(String paneName){
		
		switch(paneName){
		case MEDIA_FILES:
			getChildren().clear();
			getChildren().add(allTypes);
			break;
			
		case IMAGE:
			getChildren().clear();
			getChildren().add(image);
            break;
            
        case VIDEO:
        	getChildren().clear();
			getChildren().add(video);
            break;
            
        case AUDIO:
        	getChildren().clear();
			getChildren().add(audio);
            break;
            
        case TEXT:
        	getChildren().clear();
			getChildren().add(text);
            break;
            
        case OTHERS:
        	getChildren().clear();
			getChildren().add(others);
            break;
		}
		
	}
	
	@Override
	public void update(Observable observable, Object obj) {
		
		if (obj instanceof ListUpdateOperation) {
			
			ListUpdateOperation listUpdateOperation = (ListUpdateOperation) obj;
			
			switch(listUpdateOperation.getOperationType()){
		        case ADD:
		            add(listUpdateOperation.getMedia());
		            break;
		            
		        case REMOVE:
		        	remove(listUpdateOperation.getMedia());
		            break;
				
		        case CLEAR:
		        	allTypes.getChildren().clear();
		        	image.getChildren().clear();
		        	video.getChildren().clear();
		        	audio.getChildren().clear();
		        	text.getChildren().clear();
		        	others.getChildren().clear();
		        	break;
			}
	            
		}
		
	}
	
	public void add(Media media){
		
		allTypes.getChildren().add(createRepositoryMediaItem(media));
		
		switch(media.getImportedMediaType()){
		    case IMAGE:
		        image.getChildren().add(createRepositoryMediaItem(media));
		        break;
		        
		    case VIDEO:
		    	video.getChildren().add(createRepositoryMediaItem(media));
		        break;
		        
		    case AUDIO:
		    	audio.getChildren().add(createRepositoryMediaItem(media));
		        break;
		        
		    case TEXT:
		    	text.getChildren().add(createRepositoryMediaItem(media));
		        break;
		        
		    case OTHER:
		    case PROCEDURAL:
		    	others.getChildren().add(createRepositoryMediaItem(media));
		        break;
		}
		
		
	}
	
	public void remove(Media media){
		
		Iterator it = allTypes.getChildren().iterator();
    	Boolean mediaFound = false;
    	RepositoryMediaItemContainer repositoryMedia = null;
    	while(!mediaFound && it.hasNext()){
    		repositoryMedia = (RepositoryMediaItemContainer) it.next();
    		if(repositoryMedia.getMedia().equals(media)){
    			mediaFound = true;
    		}
    	}
    	allTypes.getChildren().remove(repositoryMedia);
		
		switch(media.getImportedMediaType()){
		    case IMAGE:
		    	Iterator imageIt = image.getChildren().iterator();
		    	Boolean mediaImageFound = false;
		    	RepositoryMediaItemContainer repositoryImageMedia = null;
		    	while(!mediaImageFound && imageIt.hasNext()){
		    		repositoryImageMedia = (RepositoryMediaItemContainer) imageIt.next();
		    		if(repositoryImageMedia.getMedia().equals(media)){
		    			mediaImageFound = true;
		    		}
		    	}
		    	image.getChildren().remove(repositoryImageMedia);
		        break;
		        
		    case VIDEO:
		    	Iterator videoIt = video.getChildren().iterator();
		    	Boolean mediaVideoFound = false;
		    	RepositoryMediaItemContainer repositoryVideoMedia = null;
		    	while(!mediaVideoFound && videoIt.hasNext()){
		    		repositoryVideoMedia = (RepositoryMediaItemContainer) videoIt.next();
		    		if(repositoryVideoMedia.getMedia().equals(media)){
		    			mediaVideoFound = true;
		    		}
		    	}
		    	image.getChildren().remove(repositoryVideoMedia);
		        break;
		        
		    case AUDIO:
		    	Iterator audioIt = audio.getChildren().iterator();
		    	Boolean mediaAudioFound = false;
		    	RepositoryMediaItemContainer repositoryAudioMedia = null;
		    	while(!mediaAudioFound && audioIt.hasNext()){
		    		repositoryAudioMedia = (RepositoryMediaItemContainer) audioIt.next();
		    		if(repositoryAudioMedia.getMedia().equals(media)){
		    			mediaAudioFound = true;
		    		}
		    	}
		    	audio.getChildren().remove(repositoryAudioMedia);
		        break;
		        
		    case TEXT:
		    	Iterator textIt = text.getChildren().iterator();
		    	Boolean mediaTextFound = false;
		    	RepositoryMediaItemContainer repositoryTextMedia = null;
		    	while(!mediaTextFound && textIt.hasNext()){
		    		repositoryTextMedia = (RepositoryMediaItemContainer) textIt.next();
		    		if(repositoryTextMedia.getMedia().equals(media)){
		    			mediaTextFound = true;
		    		}
		    	}
		    	text.getChildren().remove(repositoryTextMedia);
		        break;
		        
		    case OTHER:
		    case PROCEDURAL:
		    	Iterator othersIt = others.getChildren().iterator();
		    	Boolean mediaOthersFound = false;
		    	RepositoryMediaItemContainer repositoryOthersMedia = null;
		    	while(!mediaOthersFound && othersIt.hasNext()){
		    		repositoryOthersMedia = (RepositoryMediaItemContainer) othersIt.next();
		    		if(repositoryOthersMedia.getMedia().equals(media)){
		    			mediaOthersFound = true;
		    		}
		    	}
		    	others.getChildren().remove(repositoryOthersMedia);
		        break;
		}
		
    	
		
	}

	private RepositoryMediaItemContainer createRepositoryMediaItem(Media media) {
		
		RepositoryMediaItemContainer repositoryMediaItemContainer = new RepositoryMediaItemContainer(media, this);
	    
		createDragDropEffect(media, repositoryMediaItemContainer);
		
		return repositoryMediaItemContainer;
	
	}

	private void createDragDropEffect(final Media media, final RepositoryMediaItemContainer repositoryMediaItemContainer) {
		
		final BorderPane borderPane = (BorderPane) getScene().getRoot();
		final SplitPane sp = (SplitPane) borderPane.getCenter();
		final SplitPane spRepoSpatial  = (SplitPane) sp.getItems().get(0);
		final BorderPane temporalViewPane  = (BorderPane) sp.getItems().get(1);
		
		repositoryMediaItemContainer.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
			
		        Dragboard dragBoard = repositoryMediaItemContainer.startDragAndDrop(TransferMode.COPY);
		        ClipboardContent content = new ClipboardContent();
		        RepositoryMediaItemContainer source = (RepositoryMediaItemContainer) mouseEvent.getSource();
		        
//		        //TODO Tentar passar o objeto Media
//		        Teste teste = new Teste("Name");
//		        DataFormat dataFormat = new DataFormat("model.repository.Teste");
//		        content.put(dataFormat, teste);
		        
		        ArrayList<File> fileList = new ArrayList<File>();
		        fileList.add(source.getMedia().getMediaFile());
		        content.putFiles(fileList);
		        
		        dragBoard.setContent(content);
		        
		        mouseEvent.consume();
				
			}
       });
		
		temporalViewPane.setOnDragOver(new EventHandler<DragEvent>() {
			
			public void handle(DragEvent dragEvent) {
        	   
               if (dragEvent.getGestureSource() != temporalViewPane && dragEvent.getDragboard().hasFiles()) {
                   dragEvent.acceptTransferModes(TransferMode.COPY);
               }
               
               dragEvent.consume();
	        }  
	    });
				
	}
	
	public ObservableList<Node> getAllTypes(){
		return allTypes.getChildren();
	}
	
	public ObservableList<Node> getImagePane(){
		return image.getChildren();
	}
	
	public ObservableList<Node> getVideoPane(){
		return video.getChildren();
	}
	
	public ObservableList<Node> getAudioPane(){
		return audio.getChildren();
	}
	
	public ObservableList<Node> getTextPane(){
		return text.getChildren();
	}
	
	public ObservableList<Node> getOthersPane(){
		return others.getChildren();
	}

}
