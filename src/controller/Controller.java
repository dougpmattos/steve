package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.prefs.Preferences;

import javafx.stage.Stage;
import model.common.InteractivityKeyMapping;
import model.common.Media;
import model.common.Node;
import model.common.SpatialTemporalView;
import model.repository.RepositoryMediaList;
import model.temporalView.Interactivity;
import model.temporalView.Synchronous;
import model.temporalView.TemporalChain;

import org.json.simple.JSONObject;

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
	private SpatialTemporalView temporalView;
	private InteractivityKeyMapping interactivityKeyMapping; 
	private Preferences preferences;
	private JSONObject interactiveKeyMappingJSON;
	
	
	private StevePane stevePane;
	
	public Controller(RepositoryMediaList repositoryMediaList, SpatialTemporalView temporalView, Stage stage) throws XMLException, IOException{
		this.preferences = Preferences.userRoot();
		
		
		this.interactivityKeyMapping = new InteractivityKeyMapping();
		this.interactivityKeyMapping.setInteractivityKeyMapping(
				this.preferences.get("red", "0"),
				this.preferences.get("green","1"),
				this.preferences.get("blue", "2"),
				this.preferences.get("yellow", "3"));
		this.setJson();
		
		this.repositoryMediaList = repositoryMediaList;
		this.temporalView = temporalView;
		

		stevePane = new StevePane(this, repositoryMediaList, temporalView);
		stevePane.createView(stage);

		TemporalChain temporalChain = new TemporalChain(Language.translate("main.temporal.chain"));
		addTemporalChain(temporalChain);
		
		
	}
	
	@SuppressWarnings("unchecked")
	private void setJson(){
		JSONObject jObj= new JSONObject();
		
		jObj.put("red", this.interactivityKeyMapping.getInteractivityKeyMapping("red"));
		jObj.put("green", this.interactivityKeyMapping.getInteractivityKeyMapping("green"));
		jObj.put("blue", this.interactivityKeyMapping.getInteractivityKeyMapping("blue"));
		jObj.put("yellow", this.interactivityKeyMapping.getInteractivityKeyMapping("yellow"));
		
		this.interactiveKeyMappingJSON = jObj;		
		try{
			File saida = new File("saida.json");
			FileWriter fw = new FileWriter("src/view/HTMLSupport/saida.json"); // Vai sobrescrever o aquivo
			//FileWriter fw = new FileWriter("saida.json", true); // Nao vai sobrescrever o aquivo
			fw.write(this.interactiveKeyMappingJSON.toString());			
			fw.close();
		}catch(IOException e){
			e.printStackTrace();
		}

	}
	
	public Preferences getPreferences(){
		return this.preferences;
	}
	
	public void setPreferences(String key, String value){
		if(key.equals("red")) this.preferences.put("red", value);
		if(key.equals("green")) this.preferences.put("green", value);
		if(key.equals("blue")) this.preferences.put("blue", value);
		if(key.equals("yellow")) this.preferences.put("yellow", value);
		this.setJson();
	}
	
	public void setPreferences(String r, String g, String b, String y){
		this.preferences.put("red", r);
		this.preferences.put("green", g);
		this.preferences.put("blue", b);
		this.preferences.put("yellow", y);
		this.setJson();
		
	}
	
	
	
	public InteractivityKeyMapping getInteractivityKeyMapping(){
		return this.interactivityKeyMapping;
	}
	
	public void setInteractivityKeyMapping(InteractivityKeyMapping interactivityKeyMapping){
		this.interactivityKeyMapping =  interactivityKeyMapping;
	}
	
	public void addTemporalChain(TemporalChain temporalChain){
		temporalView.addTemporalChain(temporalChain);
	}
	
	public void removeTemporalChain(TemporalChain temporalChain) {
		temporalView.removeTemporalChain(temporalChain);
	}
	
	public void openExistingRepositoryMediaList(RepositoryMediaList existingRepositoryMediaList) {
		repositoryMediaList.openExistingRepositoryMediaList(existingRepositoryMediaList);
	}
	
	public void openExistingTemporalView(SpatialTemporalView existingTemporalView) {
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

	public void setMasterNode(Node masterNode, TemporalChain temporalChainModel) {
		temporalChainModel.setMasterNode(masterNode);
	}
	
	public void addNodeTemporalChain(Node droppedNode, TemporalChain temporalChainModel) {
		temporalChainModel.addNode(droppedNode);
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
	
	public void addSynchronousRelation(TemporalChain temporalChain, Synchronous synchronousRelation){
		temporalChain.addSynchronousRelation(synchronousRelation);
	}

	public void removeMediaTemporalChain(Node node, TemporalChain temporalChainModel, Boolean isDeleteButton) {
		temporalChainModel.removeNode(node, isDeleteButton);
	}
	
	public void removeSynchronousRelation(TemporalChain temporalChain, Synchronous synchronousRelation){
		temporalChain.removeSynchronousRelation(synchronousRelation);
	}
	
	public void removeInteractivityRelation(TemporalChain temporalChain, Interactivity<Media> interactivityRelation){
		temporalChain.removeInteractivityRelation(interactivityRelation);
	}

	public void dragMediaTemporalChain(TemporalChain temporalChain, Node node, Double droppedTime) {
		temporalChain.dragNode(temporalChain, node, droppedTime);
	}

	public void addInteractivityRelation(TemporalChain temporalChainModel, Interactivity<Media> interactivityRelation) {
		temporalChainModel.addInteractivityRelation(interactivityRelation);
	}
	
	public void updateInteractivityRelation(TemporalChain temporalChainModel, Interactivity<Media> interactivityRelation) {
		temporalChainModel.updateInteractivityRelation(interactivityRelation);
	}

}
