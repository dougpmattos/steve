package gui.repositoryPanel;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.SplitPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import model.NCLSupport.extendedAna.Media;
import model.repository.ListUpdateOperation;
import model.repository.MediaList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.MediaController;

/**
 *
 * @author Douglas
 */
public class MediaListPane extends FlowPane implements Observer {
    
	private MediaController mediaController = MediaController.getMediaController();
	
	final Logger logger = LoggerFactory.getLogger(MediaListPane.class);
	private final int ADD = 1;
	private final int REMOVE = 2;
	private final int CLEAR = 3;
	
	public MediaListPane(){
		
		setPadding(new Insets(5, 0, 5, 0));
	    setVgap(8);
	    setHgap(15);

	    setPrefWrapLength(170);
	    
	    mediaController.getMediaList().addObserver(this);
		
	}

	@Override
	public void update(Observable observable, Object obj) {
		
		if (obj instanceof ListUpdateOperation) {
			
			ListUpdateOperation listUpdateOperation = (ListUpdateOperation) obj;
			
			switch(listUpdateOperation.getOperationType()){
		        case ADD:
		            getChildren().add(createRepositoryMediaItem(listUpdateOperation.getMedia()));
		            break;
		            
		        case REMOVE:
		        	Media mediaRemoved = listUpdateOperation.getMedia();
		        	Iterator it = getChildren().iterator();
		        	Boolean mediaFound = false;
		        	RepositoryMediaItemContainer repositoryMedia = null;
		        	while(!mediaFound && it.hasNext()){
		        		repositoryMedia = (RepositoryMediaItemContainer) it.next();
		        		if(repositoryMedia.getMedia().equals(mediaRemoved)){
		        			mediaFound = true;
		        		}
		        	}
		          	getChildren().remove(repositoryMedia);
		            break;
				
		        case CLEAR:
		        	getChildren().clear();
		        	break;
			}
	            
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

}
