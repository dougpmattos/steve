package view.repositoryPane;
    
import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import model.common.Media;
import view.common.Language;
import view.common.MessageDialog;
import controller.Controller;

/**
 *
 * @author Douglas
 */
public class RepositoryButtonPane extends BorderPane{
	
	private Controller controller;
	
    private Button addButton;
    private Button deleteButton;
    private Button clearButton;
    private Button viewTypeButton;
    private HBox mediaButtonPane;
    private HBox viewButtonPane;
    private FileChooser fileChooser;
    private List <File> fileList;
    
    public RepositoryButtonPane(Controller controller, ScrollPane scrollPaneTree, MediaTreePane mediaTreePane, RepositoryMediaItemContainerListPane mediaListPane, RepositoryPane repositoryPane){
        
        setId("button-pane");
        getStylesheets().add("view/repositoryPane/styles/repositoryButtonPane.css");
        
        createButtons();

        setLeft(mediaButtonPane);
        setRight(viewButtonPane);

        createButtonActions(scrollPaneTree, mediaTreePane, mediaListPane, repositoryPane);
        
        this.controller = controller;
        
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
                		Media media = new Media();
                		media.setFile(file);
                		
                		if(media.getType() == null){
                			
                			MessageDialog messageDialog = new MessageDialog(Language.translate("media.type.not.supported"), "OK", 110);
                            messageDialog.showAndWait();
                            
                		} else {
                			
                			if(!controller.addRepositoryMedia(media)){
                    			MessageDialog messageDialog = new MessageDialog(Language.translate("media.has.already.imported") + ": " + media.getName(), 
                    												Language.translate("select.other.media"), "OK", 150);
                    	    	messageDialog.showAndWait();
                    		}
                			
                		}

                    }
                }                      
            }
        });
        
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	if(mediaListPane.getSelectedMedia() != null){
            		controller.deleteRepositoryMedia(mediaListPane.getSelectedMedia());
            	} else if(mediaTreePane.getSelectedMedia() != null){
            		controller.deleteRepositoryMedia(mediaTreePane.getSelectedMedia());
            	} else{
            		MessageDialog messageDialog = new MessageDialog(Language.translate("no.media.selected"), 
														Language.translate("select.a.media"), "OK", 150);
            		messageDialog.showAndWait();
            	}
            	
            	
            	
            }
        });
        
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	controller.clearRepositoryMediaList();
            }
        });
        
        viewTypeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            
            	String currentViewType = (String) viewTypeButton.getProperties().get("viewType");
            	
            	if(currentViewType.equalsIgnoreCase("grid")){ //botao é list
            		repositoryPane.setCenter(scrollPaneTree);
            		viewTypeButton.getProperties().put("viewType", "list");
            		viewTypeButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/view/repositoryPane/images/grid.png"))));
            		viewTypeButton.setTooltip(new Tooltip(Language.translate("grid.view")));
            	}else{
            		repositoryPane.setCenter(mediaListPane);
            		viewTypeButton.getProperties().put("viewType", "grid");
            		viewTypeButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/view/repositoryPane/images/list.png"))));
            		viewTypeButton.setTooltip(new Tooltip(Language.translate("list.view")));
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
