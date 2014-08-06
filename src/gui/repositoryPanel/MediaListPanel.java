package gui.repositoryPanel;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import model.nclDocument.extendedAna.Media;
import model.repository.ListUpdateOperation;
import model.repository.MediaList;

/**
 *
 * @author Douglas
 */
public class MediaListPanel extends FlowPane implements Observer {
    
	private static final int TEXT_LABEL_WITDH = 100;
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
		
		final File mediaFile = media.getMediaFile();
		final ImageView mediaIcon = media.getMediaIcon();
		
		BorderPane repositoryMediaItemContainer = new BorderPane();
		repositoryMediaItemContainer.setCenter(media.getMediaIcon());
		repositoryMediaItemContainer.setId("repo-media-item-container");
		
		Label label = new Label(media.getName());
		label.setId("label-media-item");
		label.setPrefWidth(TEXT_LABEL_WITDH);
		label.setAlignment(Pos.CENTER);
		repositoryMediaItemContainer.setBottom(label);
	    
		createListenerEventMediaItem(repositoryMediaItemContainer);
		createDragDropEffect(mediaFile, mediaIcon, repositoryMediaItemContainer);
		
	    
		return repositoryMediaItemContainer;
	
	}

	private void createDragDropEffect(final File mediaFile, final ImageView mediaIcon, final BorderPane repositoryMediaItemContainer) {
		
		final BorderPane borderPane = (BorderPane) getScene().getRoot();
		final SplitPane sp = (SplitPane) borderPane.getCenter();
		final SplitPane spRepoSpatial  = (SplitPane) sp.getItems().get(0);
		final BorderPane temporalViewPane  = (BorderPane) sp.getItems().get(1);
		
		repositoryMediaItemContainer.setOnMousePressed(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent mouseEvent) {
	        	
	        	dragImage = new ImageView(mediaIcon.getImage());
	        	dragImage.setFitHeight(100);
		        dragImage.setFitWidth(100);
		        dragImage.setPreserveRatio(true);
				dragImage.setOpacity(0.3);
	        	
	        }
	    });
		
		spRepoSpatial.setOnDragOver(new EventHandler<DragEvent>(){

			@Override
			public void handle(DragEvent event) {
			
				dragImage.setVisible(true);
				dragImage.setFitHeight(100);
		        dragImage.setFitWidth(100);
				dragImage.relocate(event.getSceneX() - dragImage.getBoundsInLocal().getWidth() / 2, event.getSceneY() - dragImage.getBoundsInLocal().getHeight() / 2);
				borderPane.getChildren().add(dragImage);
			
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
		
		temporalViewPane.setOnDragDone(new EventHandler<DragEvent>() {
			
			public void handle(DragEvent dragEvent) {

        	   borderPane.getChildren().remove(dragImage);
	        
			}  
	    });
		
		repositoryMediaItemContainer.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
			
		        Dragboard dragBoard = repositoryMediaItemContainer.startDragAndDrop(TransferMode.COPY);
		        
		        ClipboardContent content = new ClipboardContent();
		        
//		        //TODO Tentar passar o objeto Media
//		        Teste teste = new Teste("Name");
//		        DataFormat dataFormat = new DataFormat("model.repository.Teste");
//		        content.put(dataFormat, teste);
		        
		        ArrayList<File> fileList = new ArrayList<File>();
		        fileList.add(mediaFile);
		        content.putFiles(fileList);
		        
		        dragBoard.setContent(content);
		        
		        mouseEvent.consume();
				
			}
       });
		
		
		
		
	}

	private void createListenerEventMediaItem( final BorderPane repositoryMediaItemContainer) {
		
		repositoryMediaItemContainer.setOnMouseEntered(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	        	repositoryMediaItemContainer.getStylesheets().add("gui/styles/mouseEnteredMediaItem.css");
	        }
	    });
		
		repositoryMediaItemContainer.setOnMouseExited(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	        	repositoryMediaItemContainer.getStylesheets().remove("gui/styles/mouseEnteredMediaItem.css");
	        }
	    });
		
		repositoryMediaItemContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	        	repositoryMediaItemContainer.getStylesheets().add("gui/styles/mouseClickedMediaItem.css");
	        	
	        	for(Node media : getChildren()){
	        		
	        		BorderPane repoMediaItemContainer = (BorderPane) media;
	        		
	        		if(!repositoryMediaItemContainer.equals(repoMediaItemContainer)){
	        			repoMediaItemContainer.getStylesheets().remove("gui/styles/mouseClickedMediaItem.css");
	        		}
	        		
	        	}
	        }
	    });
		
	}
	
}
