package view.stevePane;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import model.common.SpatialTemporalApplication;
import model.repository.RepositoryMediaList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import view.common.Language;
import view.common.dialogs.MessageDialog;
import controller.ApplicationController;

public class OpenEventHandler implements EventHandler<ActionEvent> {

	final Logger logger = LoggerFactory.getLogger(OpenEventHandler.class);
	
	private ApplicationController applicationController;
	private SpatialTemporalApplication temporalView;
	private RepositoryMediaList repositoryMediaList;
	
	public OpenEventHandler(ApplicationController applicationController, SpatialTemporalApplication temporalView, RepositoryMediaList repositoryMediaList){
		
		this.applicationController = applicationController;
		this.temporalView = temporalView;
		this.repositoryMediaList = repositoryMediaList;

	}
	
	@Override
	public void handle(ActionEvent event) {
		
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Language.translate("open.project"));
        //fileChooser.setSelectedExtensionFilter(new ExtensionFilter("Teste", "*.steve"));
        File file = fileChooser.showOpenDialog(null);
        
		try {
			
			if(file != null){
			
			   	FileInputStream fileInputStream = new FileInputStream(file);
		    	ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
		    	SpatialTemporalApplication existingTemporalView = (SpatialTemporalApplication) objectInputStream.readObject();
		    	RepositoryMediaList existingRepositoryMediaList = (RepositoryMediaList) objectInputStream.readObject();
		    	objectInputStream.close();
		    	
		    	applicationController.openExistingRepositoryMediaList(existingRepositoryMediaList);
		    	applicationController.openExistingTemporalView(existingTemporalView);
                
			}
            
		} catch (IOException | ClassNotFoundException e) {
			logger.error(e.getMessage());
			MessageDialog messageDialog = new MessageDialog(e.getMessage(), "OK", 150);
	        messageDialog.showAndWait();
		}
		
		
	}

}
