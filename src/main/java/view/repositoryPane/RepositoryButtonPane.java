package view.repositoryPane;
    
import java.io.File;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import model.common.MediaNode;
import model.common.enums.MediaType;
import model.utility.MediaUtil;
import view.common.Language;
import view.common.dialogs.MessageDialog;
import controller.ApplicationController;

/**
 *
 * @author Douglas
 */
public class RepositoryButtonPane extends BorderPane{
	
	private ApplicationController applicationController;
	
    private Button addButton;
    private Button deleteButton;
    private Button clearButton;
    private Button viewTypeButton;
    private HBox mediaButtonPane;
    private HBox viewButtonPane;
    private FileChooser fileChooser;
    private List <File> fileList;
    
    public RepositoryButtonPane(ApplicationController applicationController, ScrollPane scrollPaneTree, MediaTreePane mediaTreePane, RepositoryMediaItemContainerListPane mediaListPane, RepositoryPane repositoryPane){
        
        setId("button-pane");
        getStylesheets().add("styles/repositoryPane/repositoryButtonPane.css");
        
        createButtons();

        setLeft(mediaButtonPane);
        setRight(viewButtonPane);

        createButtonActions(scrollPaneTree, mediaTreePane, mediaListPane, repositoryPane);
        
        this.applicationController = applicationController;
        
    }

	private void createButtons() {
		
		addButton = new Button();
		addButton.setId("add-button");
		addButton.setTooltip(new Tooltip(Language.translate("add.media")));
	    
        deleteButton = new Button();
        deleteButton.setId("delete-button");
        deleteButton.setTooltip(new Tooltip(Language.translate("delete.media")));
        deleteButton.setDisable(true);
   
        clearButton = new Button();
        clearButton.setId("clear-button");
        clearButton.setTooltip(new Tooltip(Language.translate("clear.repository")));
        clearButton.setDisable(true);
        
        mediaButtonPane = new HBox();
        mediaButtonPane.setId("media-button-pane");
        mediaButtonPane.getChildren().addAll(addButton, deleteButton, clearButton);
        
        viewTypeButton = new Button();
        viewTypeButton.setId("view-type-button");
        viewTypeButton.getProperties().put("viewType", "grid");
        viewTypeButton.setTooltip(new Tooltip(Language.translate("list.view")));
        
        viewButtonPane = new HBox();
        viewButtonPane.setId("view-button-pane");
        viewButtonPane.getChildren().addAll(viewTypeButton);
        
	}

    private void createButtonActions(ScrollPane scrollPaneTree, MediaTreePane mediaTreePane, RepositoryMediaItemContainerListPane mediaListPane, RepositoryPane repositoryPane) {
    	
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                fileChooser = new FileChooser();
                fileChooser.setTitle("Select Media");
                fileList = fileChooser.showOpenMultipleDialog(null);
                
                if(fileList != null){
                	for (File file : fileList) {
                		MediaNode mediaNode = new MediaNode();
                		mediaNode.setFile(file);

                        if((mediaNode.getType() == MediaType.AUDIO)||(mediaNode.getType() == MediaType.VIDEO)){

                            javafx.scene.media.Media javaFXMedia = new javafx.scene.media.Media(mediaNode.getFile().toURI().toString());

                            MediaPlayer mediaPlayer = new MediaPlayer(javaFXMedia);

                            Runnable playerRunnable = new Runnable() {
                                boolean firstTime = true;
                                @Override
                                public void run() {

                                    if(firstTime){

                                        firstTime = false;

                                        Duration dur = javaFXMedia.getDuration();
                                        mediaNode.setDuration(MediaUtil.approximateDouble(dur.toSeconds()));

                                        if(mediaNode.getType() == null){

                                            MessageDialog messageDialog = new MessageDialog(Language.translate("media.type.not.supported"), "OK", 110);
                                            messageDialog.showAndWait();

                                        } else {

                                            if(!applicationController.addRepositoryMedia(mediaNode)){
                                                MessageDialog messageDialog = new MessageDialog(Language.translate("media.has.already.imported") + ": " + mediaNode.getName(),
                                                        Language.translate("select.other.media"), "OK", 150);
                                                messageDialog.showAndWait();
                                            }

                                        }
                                    }

                                }

                            };

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }

                            mediaPlayer.setOnReady(playerRunnable);

                        }else{

                            if(mediaNode.getType() == null){

                                MessageDialog messageDialog = new MessageDialog(Language.translate("media.type.not.supported"), "OK", 110);
                                messageDialog.showAndWait();

                            } else {

                                if(!applicationController.addRepositoryMedia(mediaNode)){
                                    MessageDialog messageDialog = new MessageDialog(Language.translate("media.has.already.imported") + ": " + mediaNode.getName(),
                                            Language.translate("select.other.media"), "OK", 150);
                                    messageDialog.showAndWait();
                                }

                            }

                        }

                    }
                }                      
            }
        });
        
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	if(mediaListPane.getSelectedMedia() != null){
            		applicationController.deleteRepositoryMedia(mediaListPane.getSelectedMedia());
            	} else if(mediaTreePane.getSelectedMedia() != null){
            		applicationController.deleteRepositoryMedia(mediaTreePane.getSelectedMedia());
            	} else{
            		MessageDialog messageDialog = new MessageDialog(Language.translate("no.media.selected"), 
														Language.translate("select.a.media"), "OK", 150);
            		messageDialog.showAndWait();
            	}
            	
            	
            	
            }
        });
        
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	applicationController.clearRepositoryMediaList();
            }
        });
        
        viewTypeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            
            	String currentViewType = (String) viewTypeButton.getProperties().get("viewType");
            	
            	if(currentViewType.equalsIgnoreCase("grid")){
            		repositoryPane.setCenter(scrollPaneTree);
            		viewTypeButton.getProperties().put("viewType", "list");
            		viewTypeButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/repositoryPane/grid.png"))));
            		viewTypeButton.setTooltip(new Tooltip(Language.translate("grid.view")));
            	}else{
            		repositoryPane.setCenter(mediaListPane);
            		viewTypeButton.getProperties().put("viewType", "grid");
            		viewTypeButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/repositoryPane/list.png"))));
            		viewTypeButton.setTooltip(new Tooltip(Language.translate("list.view")));
            	}
            }
        });

        viewTypeButton.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override public void handle(MouseEvent mouseEvent) {

                String currentViewType = (String) viewTypeButton.getProperties().get("viewType");

                if(currentViewType.equalsIgnoreCase("grid")){
                    viewTypeButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/repositoryPane/list-hover.png"))));
                }else{
                    viewTypeButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/repositoryPane/grid-hover.png"))));
                }

            }
        });

        viewTypeButton.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override public void handle(MouseEvent mouseEvent) {

                String currentViewType = (String) viewTypeButton.getProperties().get("viewType");

                if(currentViewType.equalsIgnoreCase("grid")){
                    viewTypeButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/repositoryPane/list.png"))));
                }else{
                    viewTypeButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/repositoryPane/grid.png"))));
                }

            }
        });
        
    }
    
    public Button getDeleteButton(){
    	return deleteButton;
    }
    
    public Button getClearButton(){
    	return clearButton;
    }
    
}
