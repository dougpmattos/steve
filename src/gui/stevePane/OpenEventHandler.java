package gui.stevePane;

import gui.common.Language;
import gui.common.MessageDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import model.repository.RepositoryMediaList;
import model.temporalView.TemporalView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;

public class OpenEventHandler implements EventHandler<ActionEvent> {

	final Logger logger = LoggerFactory.getLogger(OpenEventHandler.class);
	
	private Controller controller;
	private TemporalView temporalView;
	private RepositoryMediaList repositoryMediaList;
	
	public OpenEventHandler(Controller controller, TemporalView temporalView, RepositoryMediaList repositoryMediaList){
		
		this.controller = controller;
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
		    	TemporalView existingTemporalView = (TemporalView) objectInputStream.readObject();
		    	RepositoryMediaList existingRepositoryMediaList = (RepositoryMediaList) objectInputStream.readObject();
		    	objectInputStream.close();
		    	
		    	controller.openExistingRepositoryMediaList(existingRepositoryMediaList);
		    	controller.openExistingTemporalView(existingTemporalView);
                
			}
            
		} catch (IOException | ClassNotFoundException e) {
			logger.error(e.getMessage());
			new MessageDialog(e.getMessage(), MessageDialog.ICON_INFO).showAndWait();
		}
		
		
	}

}
