package gui.repositoryPane;
    
import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import controller.RepositoryController;

/**
 *
 * @author Douglas
 */
public class RepositoryButtonPane extends BorderPane{
    
	private RepositoryController mediaListController = RepositoryController.getMediaController();
	
    private MediaListObserverButton addButton;
    private MediaListObserverButton deleteButton;
    private MediaListObserverButton clearButton;
    private MediaListObserverButton gridButton;
    private MediaListObserverButton listButton;
    private HBox mediaButtonPane;
    private HBox viewButtonPane;
    private FileChooser fileChooser;
    private List <File> fileList;
    
    public RepositoryButtonPane(ScrollPane scrollPaneTree, MediaListPane mediaListPane, RepositoryPane repositoryPane){
        
        setId("button-pane");
        getStylesheets().add("gui/repositoryPane/styles/repositoryButtonPane.css");
        
        createButtons();

        setLeft(mediaButtonPane);
        setRight(viewButtonPane);

        createButtonActions(scrollPaneTree, mediaListPane, repositoryPane);
        
    }

	private void createButtons() {
		
		addButton = new MediaListObserverButton("add-button", "add.media");
        deleteButton = new MediaListObserverButton("delete-button", "delete.media");
        deleteButton.setDisable(true);
        clearButton = new MediaListObserverButton("clear-button", "clear.repository");
        clearButton.setDisable(true);
        
        mediaButtonPane = new HBox();
        mediaButtonPane.setId("media-button-pane");
        mediaButtonPane.getChildren().addAll(addButton, deleteButton, clearButton);
        
        gridButton = new MediaListObserverButton("grid-button", "grid.view");
        listButton = new MediaListObserverButton("list-button", "list.view");
        
        viewButtonPane = new HBox();
        viewButtonPane.setId("view-button-pane");
        viewButtonPane.getChildren().addAll(gridButton, listButton);
        
	}

    private void createButtonActions(ScrollPane scrollPaneTree, MediaListPane mediaListPane, RepositoryPane repositoryPane) {
    	
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                fileChooser = new FileChooser();
                fileChooser.setTitle("Select Media");
                fileList = fileChooser.showOpenMultipleDialog(null);
                
                if(fileList != null){
                	for (File file : fileList) {
                		mediaListController.addMedia(file);
                    }
                }                      
            }
        });
        
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	mediaListController.deleteMedia();
            }
        });
        
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	mediaListController.clearMediaList();
                deleteButton.setDisable(true);
                clearButton.setDisable(true);
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
}
