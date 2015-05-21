package controller;

import gui.stevePane.StevePane;

import java.io.IOException;

import javafx.stage.Stage;
import model.common.Media;
import model.repository.RepositoryMediaList;
import model.temporalView.TemporalChain;
import model.temporalView.TemporalView;
import br.uff.midiacom.ana.util.exception.XMLException;

public class Controller {
	
	private RepositoryMediaList repositoryMediaList;
	private TemporalView temporalView;
	
	private StevePane stevePane;
	
	public Controller(RepositoryMediaList repositoryMediaList, TemporalView temporalView, Stage stage) throws XMLException, IOException{
		
		this.repositoryMediaList = repositoryMediaList;
		this.temporalView = temporalView;

		stevePane = new StevePane(this, repositoryMediaList, temporalView);
		stevePane.createView(stage);
		
		repositoryMediaList.initialize();
		temporalView.initialize();
		
	}
	
	public Boolean addRepositoryMedia(Media media){
		return repositoryMediaList.add(media);
	}
	
	public void deleteRepositoryMedia(Media media){
		repositoryMediaList.delete(media);
	}
	
	public void clearRepositoryMediaList(){
		repositoryMediaList.clear();
	}
	
	public RepositoryMediaList getRepositoryMediaList(){
		return repositoryMediaList;
	}

	public void setMasterMedia(Media masterMedia, TemporalChain temporalChainModel) {
		temporalChainModel.setMasterMedia(masterMedia);
	}
	
}
