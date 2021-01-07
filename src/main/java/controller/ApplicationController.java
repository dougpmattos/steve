package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.prefs.Preferences;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.common.*;
import model.repository.RepositoryMediaList;
import model.temporalView.Interactivity;
import model.temporalView.Synchronous;
import model.temporalView.TemporalChain;

import org.json.simple.JSONObject;

import view.common.Language;
import view.spatialViewPane.*;
import view.stevePane.SteveScene;
import br.uff.midiacom.ana.util.exception.XMLException;

public class ApplicationController {
	
	private RepositoryMediaList repositoryMediaList;
	private SpatialTemporalApplication spatialTemporalApplication;
	private InteractivityKeyMapping interactivityKeyMapping; 
	private Preferences preferences;
	private JSONObject interactiveKeyMappingJSON;

	private SteveScene steveScene;
	private static ApplicationController instance;
	private Stage primaryStage;

	public static ApplicationController getInstance() {

		if (ApplicationController.instance == null){
			ApplicationController.instance = new ApplicationController();
		}

		return instance;

	}

	private ApplicationController() {

		this.preferences = Preferences.userRoot();
		Locale.setDefault(Locale.ENGLISH);
		this.interactivityKeyMapping = new InteractivityKeyMapping();
		this.interactivityKeyMapping.setInteractivityKeyMapping(
				this.preferences.get("red", "0"),
				this.preferences.get("green","1"),
				this.preferences.get("blue", "2"),
				this.preferences.get("yellow", "3"));
		this.setJson();

		this.repositoryMediaList = new RepositoryMediaList();
		this.spatialTemporalApplication = new SpatialTemporalApplication(this);

	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void createView(Stage stage) throws IOException, XMLException {

		this.primaryStage = stage;

		steveScene = new SteveScene(this, repositoryMediaList, spatialTemporalApplication);
		steveScene.createView(stage);
	}

	public void createMainTemporalView(){
		TemporalChain temporalChain = new TemporalChain(Language.translate("main.temporal.chain"));
		addTemporalChain(temporalChain);
	}

	public void createNewProject(Stage stage) throws IOException, XMLException {
		this.repositoryMediaList = new RepositoryMediaList();
		this.spatialTemporalApplication = new SpatialTemporalApplication(this);
		createView(stage);
		createMainTemporalView();
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
			FileWriter fw = new FileWriter("src/main/java/view/HTMLSupport/saida.json"); // Vai sobrescrever o aquivo
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
		spatialTemporalApplication.addTemporalChain(temporalChain);
	}

	public ArrayList<TemporalChain> getAllTemporalChains(){
		return spatialTemporalApplication.getTemporalChainList();
	}
	
	public boolean removeTemporalChain(TemporalChain temporalChain) {
		Boolean isRemoveTemporalChainConfirmed = checkInteractivityRelationConsistency(temporalChain);
		if(isRemoveTemporalChainConfirmed){
			spatialTemporalApplication.removeTemporalChain(temporalChain);
			return true;
		}else{
			return false;
		}
	}
	
	public void openExistingRepositoryMediaList(RepositoryMediaList existingRepositoryMediaList) {
		repositoryMediaList.openExistingRepositoryMediaList(existingRepositoryMediaList);
	}
	
	public void openExistingSpatialTemporalView(SpatialTemporalApplication existingSpatialTemporalView) {
		spatialTemporalApplication.openExistingSpatialTemporalView(existingSpatialTemporalView);
	}
	
	public Boolean addRepositoryMedia(MediaNode mediaNode){
		return repositoryMediaList.add(mediaNode);
	}
	
	public void deleteRepositoryMedia(MediaNode mediaNode){
		repositoryMediaList.delete(mediaNode);
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

	public void populatePositionPropertyJavaBean(PositionPane positionPane, MediaNode mediaNode) {
		mediaNode.getPresentationProperty().populatePositionPropertyJavaBean(positionPane);
	}

	public void populateSizePropertyJavaBean(SizePane sizePane, MediaNode mediaNode) {
		mediaNode.getPresentationProperty().populateSizePropertyJavaBean(sizePane);
	}

	public void populateCropPropertyJavaBean(CropPane cropPane, MediaNode mediaNode) {
		mediaNode.getPresentationProperty().populateCropPropertyJavaBean(cropPane);
	}

	public void populateStylePropertyJavaBean(StylePane stylePane, MediaNode mediaNode) {
		mediaNode.getPresentationProperty().populateStylePropertyJavaBean(stylePane);
	}

	public void populateTextStylePropertyJavaBean(StylePane stylePane, MediaNode mediaNode) {
		mediaNode.getPresentationProperty().populateTextStylePropertyJavaBean(stylePane);
	}

	public void populateLevelPropertyJavaBean(LevelPane levelPane, MediaNode mediaNode) {
		mediaNode.getPresentationProperty().populateLevelPropertyJavaBean(levelPane);
	}

	public void populateTemporalInfoPropertyJavaBean(TemporalMediaInfoPane infoPane, MediaNode mediaNode) {
		mediaNode.populateTemporalInfoPropertyJavaBean(infoPane);
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
	
	public void removeInteractivityRelation(TemporalChain temporalChain, Interactivity<MediaNode> interactivityRelation){
		temporalChain.removeInteractivityRelation(interactivityRelation);
	}

	/**
	 * Update the view and model using TemporalChain's dragNode method.
	 */
	public void dragMediaTemporalChain(TemporalChain temporalChain, Node node, Double droppedTime) {
		temporalChain.dragNode(node, droppedTime, true);
	}

	public void addInteractivityRelation(TemporalChain temporalChainModel, Interactivity<MediaNode> interactivityRelation) {
		temporalChainModel.addInteractivityRelation(interactivityRelation);
	}
	
	public void updateInteractivityRelation(TemporalChain temporalChainModel, Interactivity<MediaNode> interactivityRelation) {
		temporalChainModel.updateInteractivityRelation(interactivityRelation);
	}

	/**
	 * Update the view and model using TemporalChain's methods.
	 */
	public void updateNodeStartTime(Node node, Double newValue, boolean isDrag) {
		node.getParentTemporalChain().updateNodeStartTimeView(node, newValue, isDrag);
	}

	/**
	 * Update the view and model using TemporalChain's methods.
	 */
	public void updateNodeEndTime(Node node, Double newValue, boolean isLinked) {
		node.getParentTemporalChain().updateNodeEndTimeView(node, newValue, isLinked);

	}

	/**
	 * Update the view and model using TemporalChain's methods.
	 */
	public void updateNodeDurationTime(Node node, Double newValue) {
		node.getParentTemporalChain().updateNodeDurationTimeView(node, newValue);
	}

	/**
	 * Update sensory effect properties according to user's interactions.
	 */
	public void updateSensoryEffectProperties(SensoryEffectPropertyPane sensoryEffectPropertyPane, SensoryEffectNode sensoryEffectNode) {
		sensoryEffectNode.updateProperties(sensoryEffectPropertyPane);
	}

	public void updateSensoryEffectPositions(EffectPositionPane effectPositionPane, SensoryEffectNode sensoryEffectNode) {
		sensoryEffectNode.updateSensoryEffectPositions(effectPositionPane);
	}

	public void removeNodeOfSpatialView(Node node) {
		steveScene.getSpatialViewPane().removeNodeOfSpatialView(node);
	}

	public SteveScene getSteveScene() {
		return steveScene;
	}

	public Boolean checkInteractivityRelationConsistency(TemporalChain removedTemporalChainModel) {
		return spatialTemporalApplication.checkInteractivityRelationConsistency(removedTemporalChainModel);
	}

	public StackPane getScreen(){
		return steveScene.getSpatialViewPane().getDisplayPane().getScreen();
	}

	public HBox getEffectIconsContainer(){
		return steveScene.getSpatialViewPane().getDisplayPane().getControlButtonPane().getEffectIconsContainer();
	}

}
