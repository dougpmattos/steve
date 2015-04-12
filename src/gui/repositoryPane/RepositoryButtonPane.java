package gui.repositoryPane;
    
import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import controller.repository.MediaController;

/**
 *
 * @author Douglas
 */
public class RepositoryButtonPane extends HBox{
    
	private MediaController mediaController = MediaController.getMediaController();
	
    private MediaListObserverButton addButton;
    private MediaListObserverButton deleteButton;
    private MediaListObserverButton clearButton;
    private FileChooser fileChooser;
    private List <File> fileList;
    
    public RepositoryButtonPane(){
        
        setId("button-pane");
        getStylesheets().add("gui/repositoryPane/styles/repositoryButtonPane.css");
        
        createButtons();

        getChildren().addAll(addButton, deleteButton, clearButton);

        createButtonActions();
        
    }

	private void createButtons() {
		
		addButton = new MediaListObserverButton("add-button", "add.media");
        deleteButton = new MediaListObserverButton("delete-button", "delete.media");
        deleteButton.setDisable(true);
        clearButton = new MediaListObserverButton("clear-button", "clear.repository");
        clearButton.setDisable(true);
        
	}

    private void createButtonActions() {
    	
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                fileChooser = new FileChooser();
                fileChooser.setTitle("Select Media");
                fileList = fileChooser.showOpenMultipleDialog(null);
                
                if(fileList != null){
                	for (File file : fileList) {
                		mediaController.addMedia(file);
                    }
                }                      
            }
        });
        
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	mediaController.deleteMedia();
            }
        });
        
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	mediaController.clearMediaList();
                deleteButton.setDisable(true);
                clearButton.setDisable(true);
            }
        });
    }
}
