package view.stevePane;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import model.common.SpatialTemporalApplication;
import model.repository.RepositoryMediaList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import view.common.Language;
import view.common.dialogs.MessageDialog;
import view.common.dialogs.ReturnMessage;
import view.utility.AnimationUtil;

public class SaveEventHandler implements EventHandler<ActionEvent> {
	
	final Logger logger = LoggerFactory.getLogger(SaveEventHandler.class);
	
	private SpatialTemporalApplication spatialTemporalView;
	private RepositoryMediaList repositoryMediaList;
	
	public SaveEventHandler(SpatialTemporalApplication spatialTemporalView, RepositoryMediaList repositoryMediaList){
		
		this.spatialTemporalView = spatialTemporalView;
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
		    	objectOutputStream.writeObject(spatialTemporalView);
		    	objectOutputStream.writeObject(repositoryMediaList);
		    	objectOutputStream.close();

				String message = Language.translate("project.saved.successfully");

				ReturnMessage returnMessage = new ReturnMessage(message, 450);
				returnMessage.show();
				AnimationUtil.applyFadeInOut(returnMessage);

			}
            
		} catch (IOException e) {
			logger.error(e.getMessage());
			MessageDialog messageDialog = new MessageDialog(e.getMessage(), "OK", 150);
	        messageDialog.showAndWait();
		}
		
	}

}
