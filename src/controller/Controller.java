package controller;

import java.io.IOException;

import javafx.stage.Stage;
import model.common.Media;
import model.repository.RepositoryMediaList;
import model.temporalView.Interactivity;
import model.temporalView.Synchronous;
import model.temporalView.TemporalChain;
import model.temporalView.TemporalView;
import model.temporalView.enums.NumericInteractivityKey;
import view.common.Language;
import view.spatialViewPane.CropPane;
import view.spatialViewPane.LevelPane;
import view.spatialViewPane.PositionPane;
import view.spatialViewPane.SizePane;
import view.spatialViewPane.StylePane;
import view.spatialViewPane.TemporalMediaInfoPane;
import view.stevePane.StevePane;
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

		TemporalChain temporalChain = new TemporalChain(Language.translate("main.temporal.chain"));
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

	public void populateTemporalInfoPropertyJavaBean(TemporalMediaInfoPane infoPane, Media media) {
		media.populateTemporalInfoPropertyJavaBean(infoPane);
	}
	
	public void addSynchronousRelation(TemporalChain temporalChain, Synchronous<Media> synchronousRelation){
		temporalChain.addSynchronousRelation(synchronousRelation);
	}

	public void removeMediaTemporalChain(Media media, TemporalChain temporalChainModel, Boolean isDeleteButton) {
		temporalChainModel.removeMedia(media, isDeleteButton);
	}
	
	public void removeSynchronousRelation(TemporalChain temporalChain, Synchronous<Media> synchronousRelation){
		temporalChain.removeSynchronousRelation(synchronousRelation);
	}
	
	public void removeInteractivityRelation(TemporalChain temporalChain, Interactivity<Media, ?> interactivityRelation){
		temporalChain.removeInteractivityRelation(interactivityRelation);
	}

	public void dragMediaTemporalChain(TemporalChain temporalChain, Media media, Double droppedTime) {
		temporalChain.dragMedia(temporalChain, media, droppedTime);
	}

	public void addInteractivityRelation(TemporalChain temporalChainModel, Interactivity<Media, ?> interactivityRelation) {
		temporalChainModel.addInteractivityRelation(interactivityRelation);
	}
	
	public void updateInteractivityRelation(TemporalChain temporalChainModel, Interactivity<Media, ?> interactivityRelation) {
		temporalChainModel.updateInteractivityRelation(interactivityRelation);
	}

}
