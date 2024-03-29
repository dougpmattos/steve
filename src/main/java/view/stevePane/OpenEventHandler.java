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
import view.common.Language;
import view.common.dialogs.MessageDialog;
import controller.ApplicationController;
import view.common.dialogs.ReturnMessage;
import view.utility.AnimationUtil;

public class OpenEventHandler implements EventHandler<ActionEvent> {

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
		    	SpatialTemporalApplication existingSpatialTemporalView = (SpatialTemporalApplication) objectInputStream.readObject();
		    	RepositoryMediaList existingRepositoryMediaList = (RepositoryMediaList) objectInputStream.readObject();
		    	objectInputStream.close();
		    	
		    	applicationController.openExistingRepositoryMediaList(existingRepositoryMediaList);
		    	applicationController.openExistingSpatialTemporalView(existingSpatialTemporalView);

				String message = Language.translate("project.opened.successfully");

				ReturnMessage returnMessage = new ReturnMessage(message, 450);
				returnMessage.show();
				AnimationUtil.applyFadeInOut(returnMessage);
                
			}
            
		} catch (IOException | ClassNotFoundException e) {
			MessageDialog messageDialog = new MessageDialog(e.getMessage(), "OK", 150);
	        messageDialog.showAndWait();
		}
		
		
	}

}
