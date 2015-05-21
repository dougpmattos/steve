package gui.repositoryPane;
    
import gui.common.Language;
import gui.common.MessageDialog;

import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import model.common.Media;
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
    private Button gridButton;
    private Button listButton;
    private HBox mediaButtonPane;
    private HBox viewButtonPane;
    private FileChooser fileChooser;
    private List <File> fileList;
    
    public RepositoryButtonPane(Controller controller, ScrollPane scrollPaneTree, MediaTreePane mediaTreePane, MediaListPane mediaListPane, RepositoryPane repositoryPane){
        
        setId("button-pane");
        getStylesheets().add("gui/repositoryPane/styles/repositoryButtonPane.css");
        
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
        
        gridButton = new Button();
        gridButton.setId("grid-button");
        gridButton.setTooltip(new Tooltip(Language.translate("grid.view")));
		
        listButton = new Button();
        listButton.setId("list-button");
        listButton.setTooltip(new Tooltip(Language.translate("list.view")));
        
        viewButtonPane = new HBox();
        viewButtonPane.setId("view-button-pane");
        viewButtonPane.getChildren().addAll(gridButton, listButton);
        
	}

    private void createButtonActions(ScrollPane scrollPaneTree, MediaTreePane mediaTreePane, MediaListPane mediaListPane, RepositoryPane repositoryPane) {
    	
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                fileChooser = new FileChooser();
                fileChooser.setTitle("Select Media");
                fileList = fileChooser.showOpenMultipleDialog(null);
                
                if(fileList != null){
                	for (File file : fileList) {
                		if(!controller.addRepositoryMedia(new Media(file))){
                			new MessageDialog("Media's already added.", MessageDialog.ICON_INFO).showAndWait();
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
            		new MessageDialog("Please, select a media to delete.", MessageDialog.ICON_INFO).showAndWait();
            	}
            	
            	
            	
            }
        });
        
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	controller.clearRepositoryMediaList();
            }
        });
        
        gridButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	repositoryPane.setCenter(mediaListPane);
            }
        });
        
        listButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	repositoryPane.setCenter(scrollPaneTree);
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
