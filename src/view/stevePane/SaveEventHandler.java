package view.stevePane;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import model.repository.RepositoryMediaList;
import model.temporalView.TemporalView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import view.common.Language;
import view.common.MessageDialog;

public class SaveEventHandler implements EventHandler<ActionEvent> {
	
	final Logger logger = LoggerFactory.getLogger(SaveEventHandler.class);
	
	private TemporalView temporalView;
	private RepositoryMediaList repositoryMediaList;
	
	public SaveEventHandler(TemporalView temporalView, RepositoryMediaList repositoryMediaList){
		
		this.temporalView = temporalView;
		this.repositoryMediaList = repositoryMediaList;
		
	}

	@Override
	public void handle(ActionEvent event) {
		
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Language.translate("save.project"));
        File file = fileChooser.showSaveDialog(null);
        
		try {
			
			if(file != null){

				file.setExecutable(true);
			   	FileOutputStream fileOutputStream = new FileOutputStream(file);
		    	ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		    	objectOutputStream.writeObject(temporalView);
		    	objectOutputStream.writeObject(repositoryMediaList);
		    	objectOutputStream.close();
                
			}
            
		} catch (IOException e) {
			logger.error(e.getMessage());
			MessageDialog messageDialog = new MessageDialog(e.getMessage(), "OK", 150);
	        messageDialog.showAndWait();
		}
		
	}

}
