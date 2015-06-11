package controller;

import gui.spatialViewPane.CropPane;
import gui.spatialViewPane.InfoPane;
import gui.spatialViewPane.LevelPane;
import gui.spatialViewPane.PositionPane;
import gui.spatialViewPane.SizePane;
import gui.spatialViewPane.StylePane;
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

		TemporalChain temporalChain = new TemporalChain();
		this.temporalView.addTemporalChain(temporalChain);
		
	}
	
	public void openExistingRepositoryMediaList(RepositoryMediaList existingRepositoryMediaList) {
		repositoryMediaList.openExistingRepositoryMediaList(existingRepositoryMediaList);
	}
	
	public void openExistingTemporalView(TemporalView existingTemporalView) {
		temporalView.openExistingTemporalView(existingTemporalView);
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
	
	public void addMediaTemporalChain(Media droppedMedia, TemporalChain temporalChainModel) {
		temporalChainModel.addMedia(droppedMedia);
	}

	public void populatePositionPropertyJavaBean(PositionPane positionPane, Media media) {
		media.getPresentationProperty().populatePositionPropertyJavaBean(positionPane);
	}

	public void populateSizePropertyJavaBean(SizePane sizePane, Media media) {
		media.getPresentationProperty().populateSizePropertyJavaBean(sizePane);
	}

	public void populateCropPropertyJavaBean(CropPane cropPane, Media media) {
		media.getPresentationProperty().populateCropPropertyJavaBean(cropPane);
	}

	public void populateStylePropertyJavaBean(StylePane stylePane, Media media) {
		media.getPresentationProperty().populateStylePropertyJavaBean(stylePane);
	}

	public void populateTextStylePropertyJavaBean(StylePane stylePane, Media media) {
		media.getPresentationProperty().populateTextStylePropertyJavaBean(stylePane);
	}

	public void populateLevelPropertyJavaBean(LevelPane levelPane, Media media) {
		media.getPresentationProperty().populateLevelPropertyJavaBean(levelPane);
	}

	public void populateInfoPropertyJavaBean(InfoPane infoPane, Media media) {
		media.populateInfoPropertyJavaBean(infoPane);
	}
	
}
