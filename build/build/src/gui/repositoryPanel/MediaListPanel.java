package gui.repositoryPanel;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

import br.uff.midiacom.ana.util.enums.NCLMediaType;

/**
 *
 * @author Douglas
 */
public class MediaListPanel extends FlowPane implements Observer {
    
	final Logger logger = LoggerFactory.getLogger(MediaListPanel.class);
	private static final double DRAG_IMAGE_OPACITY = 0.3;
	private static int IMAGE_THUMBNAIL_WIDTH = 70;
	private static int ICON_WIDTH = 40;
	private final int ADD = 1;
	private final int REMOVE = 2;
	private final int CLEAR = 3;
	int i = 0;
	
	private ImageView dragImage;
	
	public MediaListPanel(MediaList mediaList){
		
		setPadding(new Insets(5, 0, 5, 0));
	    setVgap(8);
	    setHgap(15);

	    setPrefWrapLength(170);
	    createListenerEvent();
	    
	    mediaList.addObserver(this);
		
	}

	private void createListenerEvent() {
		
		
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
		        	getChildren().remove(listUpdateOperation.getMedia().getMediaIcon());
		            break;
				
		        case CLEAR:
		        	getChildren().clear();
		        	break;
			}
	            
		}
		
	}

	private BorderPane createRepositoryMediaItem(Media media) {
		
		RepositoryMediaItemContainer repositoryMediaItemContainer = new RepositoryMediaItemContainer(media, this);
	    
		createDragDropEffect(media, repositoryMediaItemContainer);
		
		return repositoryMediaItemContainer;
	
	}

	private void createDragDropEffect(final Media media, final RepositoryMediaItemContainer repositoryMediaItemContainer) {
		
		final BorderPane borderPane = (BorderPane) getScene().getRoot();
		final SplitPane sp = (SplitPane) borderPane.getCenter();
		final SplitPane spRepoSpatial  = (SplitPane) sp.getItems().get(0);
		final BorderPane temporalViewPane  = (BorderPane) sp.getItems().get(1);
		
		repositoryMediaItemContainer.setOnMousePressed(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent mouseEvent) {
	        	
	        	RepositoryMediaItemContainer source = (RepositoryMediaItemContainer) mouseEvent.getSource();
	        	Image selectedImage = source.getMedia().getMediaIcon().getImage();
	        	dragImage = new ImageView(selectedImage);
	        	dragImage.setFitHeight(IMAGE_THUMBNAIL_WIDTH);
	        	dragImage.setFitWidth(IMAGE_THUMBNAIL_WIDTH);
		        dragImage.setPreserveRatio(true);
				dragImage.setOpacity(DRAG_IMAGE_OPACITY);
	        	
	        }
	    });
		
		spRepoSpatial.setOnDragOver(new EventHandler<DragEvent>(){

			@Override
			public void handle(DragEvent event) {
			
				RepositoryMediaItemContainer source = (RepositoryMediaItemContainer) event.getGestureSource();
				NCLMediaType mediaType = source.getMedia().getRepoMediaType();
				
				dragImage.setVisible(true);
	        	if(mediaType == NCLMediaType.IMAGE || mediaType == NCLMediaType.VIDEO){
	        		dragImage.setFitHeight(IMAGE_THUMBNAIL_WIDTH);
		        	dragImage.setFitWidth(IMAGE_THUMBNAIL_WIDTH);
	        	} else {
	        		dragImage.setFitHeight(ICON_WIDTH);
		        	dragImage.setFitWidth(ICON_WIDTH);
	        	}
				dragImage.relocate(event.getSceneX() - dragImage.getBoundsInLocal().getWidth() / 2, event.getSceneY() - dragImage.getBoundsInLocal().getHeight() / 2);
				try{
					borderPane.getChildren().add(dragImage);
				}catch(IllegalArgumentException e){
					logger.debug("Objeto imagem representando o evento Drag da media.");
				}
			
			}
			
		});
		
		spRepoSpatial.setOnDragDone(new EventHandler<DragEvent>(){

			@Override
			public void handle(DragEvent event) {
				
				borderPane.getChildren().remove(dragImage);
			
			}
			
		});
		
		temporalViewPane.setOnDragOver(new EventHandler<DragEvent>() {
			
			public void handle(DragEvent dragEvent) {

        	   dragImage.setVisible(false);
        	   
               if (dragEvent.getGestureSource() != temporalViewPane && dragEvent.getDragboard().hasFiles()) {
                   dragEvent.acceptTransferModes(TransferMode.COPY);
               }
               
               dragEvent.consume();
	        }  
	    });
				
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
	}

}
