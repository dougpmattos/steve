package view.spatialViewPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import model.common.MediaNode;
import model.common.Node;
import model.common.SensoryEffectNode;
import model.temporalView.Synchronous;
import model.temporalView.TemporalChain;
import model.temporalView.TemporalRelation;
import view.common.Language;
import controller.ApplicationController;
import view.common.dialogs.MessageDialog;
import view.temporalViewPane.TemporalChainPane;

import java.util.ArrayList;
import java.util.function.UnaryOperator;

public class TemporalMediaInfoPane extends ScrollPane{

	private ApplicationController applicationController;
	private Node node;
	private MediaNode mediaNode;
	private SensoryEffectNode sensoryEffectNode;
	private boolean isSensoryEffect;
	
	private TextField nameTextField;
	private TextField typeTextField;
	private TextField startTimeTextField;
	private TextField endTimeTextField;
	private TextField durationTextField;
	private CheckBox interactiveTextField;
	private TextField priorityTextField;
	private Button linkButton;

	private GridPane infoPropertyGridPane;
	
	public TemporalMediaInfoPane(ApplicationController applicationController, Node node){
		
		this.applicationController = applicationController;

		this.node = node;

		if(node instanceof MediaNode){
			this.mediaNode = (MediaNode) node;
		}else if(node instanceof SensoryEffectNode){
			this.sensoryEffectNode = (SensoryEffectNode) node;
			isSensoryEffect = true;
		}
	
		setId("temporal-info-pane");
		
		Label nameLabel = new Label(Language.translate("name"));
		Label typeLabel = new Label(Language.translate("type"));
		Label startTimeLabel = new Label(Language.translate("start.time"));
		Label endTimeLabel = new Label(Language.translate("end.time"));
		Label durationLabel = new Label(Language.translate("duration"));
		Label priorityLabel = new Label(Language.translate("priority"));
		nameLabel.setId("spatial-view-label");
		typeLabel.setId("spatial-view-label");
		startTimeLabel.setId("spatial-view-label-time");
		endTimeLabel.setId("spatial-view-label-time");
		durationLabel.setId("spatial-view-label");
		durationLabel.setDisable(true);
		priorityLabel.setId("spatial-view-label");
		
		nameTextField = new TextField();
		typeTextField = new TextField();
		startTimeTextField = new TextField();
		endTimeTextField = new TextField();
		durationTextField = new TextField();
		durationTextField.setDisable(true);

		if(!isSensoryEffect){
			interactiveTextField =new CheckBox(Language.translate("interactive"));
		}else{
			priorityTextField = new TextField();
		}

		linkButton = new Button();

		nameTextField.setId("name-text-field");
		typeTextField.setId("type-text-field");
		startTimeTextField.setId("start-text-field");
		endTimeTextField.setId("end-text-field");
		durationTextField.setId("duration-text-field");
		if(isSensoryEffect){
			priorityTextField.setId("priority-text-field");
		}
		nameTextField.setEditable(false);
		typeTextField.setEditable(false);
		startTimeTextField.setEditable(true);
		endTimeTextField.setEditable(true);
		durationTextField.setEditable(true);
		if(!isSensoryEffect){
			interactiveTextField.setDisable(true);
		}else {
			priorityTextField.setEditable(true);
		}
		linkButton.setId("link-button");
		linkButton.setTooltip(new Tooltip(Language.translate("link.start.end")));

		if(node instanceof MediaNode){
			MediaNode mediaNode = (MediaNode) node;
			if(mediaNode.isContinousMedia()){
				startTimeTextField.setDisable(true);
				endTimeTextField.setDisable(true);
				linkButton.setDisable(true);
			}
		}

		infoPropertyGridPane = new GridPane();
		infoPropertyGridPane.setId("info-property-grid-pane");
		infoPropertyGridPane.add(nameLabel, 0, 0);
		infoPropertyGridPane.add(nameTextField, 1, 0);
		infoPropertyGridPane.add(typeLabel, 4, 0);
		infoPropertyGridPane.add(typeTextField, 5, 0);
		infoPropertyGridPane.add(startTimeLabel, 0, 2);
		infoPropertyGridPane.add(startTimeTextField, 1, 2);
		//infoPropertyGridPane.add(linkButton, 5, 2);
		infoPropertyGridPane.add(endTimeLabel, 4, 2);
		infoPropertyGridPane.add(endTimeTextField, 5, 2);
		infoPropertyGridPane.add(durationLabel, 0, 4);
		infoPropertyGridPane.add(durationTextField, 1, 4);
		if(!isSensoryEffect){
			infoPropertyGridPane.add(interactiveTextField, 0, 6, 10, 1);
		}else{
			infoPropertyGridPane.add(priorityLabel, 0, 6, 10, 1);
			infoPropertyGridPane.add(priorityTextField, 1, 6, 10, 1);
		}
		
		VBox infoPaneContainerVBox = new VBox();
		infoPaneContainerVBox.setId("info-pane-vbox");
		infoPaneContainerVBox.getChildren().add(infoPropertyGridPane);
		
		setContent(infoPaneContainerVBox);
		
		populateInfoPane();
		
		createListeners();

		createValidatorListeners();
		
	}

	private void createValidatorListeners(){

		UnaryOperator<TextFormatter.Change> startTimeFieldFilter = change -> {

			String newTextInput = "";
			int changeStartIndex = change.getRangeStart();

			if(!change.isReplaced()){

				if(change.isAdded()){
					String stringChange = change.getText();
					StringBuilder sb = new StringBuilder(startTimeTextField.getText());
					sb.insert(changeStartIndex, stringChange);
					newTextInput = sb.toString();
				}else if(change.isDeleted()){
					String stringChange = startTimeTextField.getText().substring(changeStartIndex, changeStartIndex+1);
					newTextInput = startTimeTextField.getText().replace(stringChange, "");
				}

				if(change.isAdded() || change.isDeleted()){

					String regex = "^[0-9]*(\\.)?(\\d{1,2})?$";
					if (newTextInput.matches(regex)) {
						return change;
					}else{
						change.setText("");
					}

				}

			}

			return change;

		};

		TextFormatter<String> startTextFormatter = new TextFormatter<>(startTimeFieldFilter);
		startTimeTextField.setTextFormatter(startTextFormatter);

		UnaryOperator<TextFormatter.Change> endTimeFieldFilter = change -> {

			String newTextInput = "";
			int changeStartIndex = change.getRangeStart();

			if(!change.isReplaced()) {

				if (change.isAdded()) {
					String stringChange = change.getText();
					StringBuilder sb = new StringBuilder(endTimeTextField.getText());
					sb.insert(changeStartIndex, stringChange);
					newTextInput = sb.toString();
				} else if (change.isDeleted()) {
					String stringChange = endTimeTextField.getText().substring(changeStartIndex, changeStartIndex + 1);
					newTextInput = endTimeTextField.getText().replace(stringChange, "");
				}

				if(change.isAdded() || change.isDeleted()){

					String regex = "^[0-9]*(\\.)?(\\d{1,2})?$";
					if (newTextInput.matches(regex)) {
						return change;
					} else {
						change.setText("");
					}

				}

			}

			return change;

		};

		TextFormatter<String> endTextFormatter = new TextFormatter<>(endTimeFieldFilter);
		endTimeTextField.setTextFormatter(endTextFormatter);

		if(priorityTextField != null){
			UnaryOperator<TextFormatter.Change> priorityFieldFilter = change -> {

				String newTextInput = "";
				int changeStartIndex = change.getRangeStart();

				if(change.isAdded()){
					String stringChange = change.getText();
					StringBuilder sb = new StringBuilder(priorityTextField.getText());
					sb.insert(changeStartIndex, stringChange);
					newTextInput = sb.toString();
				}else if(change.isDeleted()){
					String stringChange = priorityTextField.getText().substring(changeStartIndex, changeStartIndex+1);
					newTextInput = priorityTextField.getText().replace(stringChange, "");
				}

				String regex = "^[0-9]*$";
				if (newTextInput.matches(regex)) {
					return change;
				}else{
					change.setText("");
				}
				return change;
			};

			TextFormatter<String> priorityTextFormatter = new TextFormatter<>(priorityFieldFilter);
			priorityTextField.setTextFormatter(priorityTextFormatter);
		}

	}
	private boolean updateStartTimeOfNode(){

		boolean wasUpdated = false;

		if(!getStartTimeValue().isEmpty()){

			Double newValue = Double.parseDouble(getStartTimeValue());

			if(newValue < node.getEnd() && !node.getBegin().equals(newValue)){

				ApplicationController.getInstance().updateNodeStartTime(node, newValue, false);
				durationTextField.setText(String.valueOf(node.getDuration()));
				wasUpdated = true;

			}
		}

		return wasUpdated;

	}

	private boolean updateEndTimeOfNode(){

		boolean wasUpdated = false;

		if(!getEndTimeValue().isEmpty()) {

			Double newValue = Double.parseDouble(getEndTimeValue());

			if (newValue > node.getBegin() && !node.getEnd().equals(newValue)) {

				ApplicationController.getInstance().updateNodeEndTime(node, newValue, false);
				durationTextField.setText(String.valueOf(node.getDuration()));
				wasUpdated = true;

			}
		}

		return wasUpdated;

	}

	private void createListeners() {

		startTimeTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldPropertyValue, Boolean newPropertyValue) {

				if(!isSecondaryNodeBeginDefinedByRelation()){
					startTimeTextField.setDisable(false);
					if(!newPropertyValue){
						if(!updateStartTimeOfNode()){
							showStartGreaterMessage();
						}
					}
				}else{
					startTimeTextField.setDisable(true);

					MessageDialog warningMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.edit.start.time"),
							Language.translate("begin.has.already.been.defined"), "OK", 160);

					if(newPropertyValue == true){
						warningMessageDialog.showAndWait();
					}

				}

			}
		});

		startTimeTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if(event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.TAB)) {
					if(!updateStartTimeOfNode()){
						showStartGreaterMessage();
					}
				}
			}

		});

		endTimeTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldPropertyValue, Boolean newPropertyValue) {

				if(!isSecondaryNodeEndDefinedByRelation()){
					endTimeTextField.setDisable(false);
					if(!newPropertyValue){
						if(!updateEndTimeOfNode()){
							showEndLessMessage();
						}
					}
				}else{
					endTimeTextField.setDisable(true);

					MessageDialog warningMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.edit.end.time"),
							Language.translate("end.has.already.been.defined"), "OK", 160);

					if(newPropertyValue == true){
						warningMessageDialog.showAndWait();
					}

				}

			}
		});

		endTimeTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if(event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.TAB)) {
					if(!updateEndTimeOfNode()){
						showEndLessMessage();
					}
				}
			}

		});

		durationTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.TAB)) {
					Double newValue = Double.parseDouble(getDurationValue());
					if(!node.getDuration().equals(newValue)){
						ApplicationController.getInstance().updateNodeDurationTime(node, newValue);
					}
					endTimeTextField.setText(String.valueOf(node.getEnd()));
				}
			}

		});
		
	}

	private void showStartGreaterMessage() {

		if(!getStartTimeValue().isEmpty()){
			Double newValue = Double.parseDouble(getStartTimeValue());

			if(newValue > node.getEnd()){

				startTimeTextField.setText(String.valueOf(node.getBegin()));

				MessageDialog warningMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.edit.start.time"),
						Language.translate("begin.greater.than.end"), "OK", 155);
				warningMessageDialog.showAndWait();

			}
		}
	}

	private void showEndLessMessage() {

		if(!getEndTimeValue().isEmpty()){
			Double newValue = Double.parseDouble(getEndTimeValue());

			if(newValue < node.getBegin()){

				endTimeTextField.setText(String.valueOf(node.getEnd()));

				MessageDialog warningMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.edit.end.time"),
						Language.translate("end.less.than.begin"), "OK", 155);
				warningMessageDialog.showAndWait();

			}
		}
	}

	public void setNameValue(String value){
		this.nameTextField.setText(value);
	}
	
	public String getNameValue(){
		return nameTextField.getText();
	}
	
	public void setTypeValue(String value){
		this.typeTextField.setText(value);
	}
	
	public String getTypeValue(){
		return typeTextField.getText();
	}
	
	public void setStartTimeValue(String value){
		this.startTimeTextField.setText(value);
	}
	
	public String getStartTimeValue(){
		return startTimeTextField.getText();
	}
	
	public void setEndTimeValue(String value){
		this.endTimeTextField.setText(value);
	}
	
	public String getEndTimeValue(){
		return endTimeTextField.getText();
	}
	
	public void setDurationValue(String value){
		this.durationTextField.setText(value);
	}

	public void setPriorityValue(String value){
		this.priorityTextField.setText(value);
	}
	
	public String getDurationValue(){
		return durationTextField.getText();
	}
	
	public void setInteractiveValue(Boolean value){
		this.interactiveTextField.setSelected(value);
	}
	
	public Boolean getInteractiveValue(){
		return interactiveTextField.isSelected();
	}
	
	private void populateInfoPane(){
		
		setNameValue(node.getName());
		setTypeValue(node.getType().toString());
		setStartTimeValue(Double.toString(node.getBegin()));
		setEndTimeValue(Double.toString(node.getEnd()));
		setDurationValue(Double.toString(node.getDuration()));
		if(!isSensoryEffect){
			setInteractiveValue(mediaNode.isInteractive());
		}else{
			setPriorityValue(Integer.toString(sensoryEffectNode.getPresentationProperty().getPriority()));
		}
		
	}
	
	public void populateInfoPropertyJavaBean(){
		
		applicationController.populateTemporalInfoPropertyJavaBean(this, mediaNode);
		
	}

	private boolean isSecondaryNodeBeginDefinedByRelation(){

		javafx.scene.Node content = applicationController.getSteveScene().getTemporalViewPane().getTemporalChainTabPane().getSelectionModel().getSelectedItem().getContent();
		TemporalChainPane temporalChainPane = (TemporalChainPane) content;
		TemporalChain temporalChainModel = temporalChainPane.getTemporalChainModel();

		ArrayList<TemporalRelation> listOfRelationsWhereNodeIsSecondary = temporalChainModel.getListOfRelationsWhereNodeIsSecondary(node);

		for(TemporalRelation temporalRelation : listOfRelationsWhereNodeIsSecondary){

			Synchronous synchronousRelation = (Synchronous) temporalRelation;

			if(temporalChainModel.relationDefinesBegin(synchronousRelation)){
				return true;
			}

		}

		return false;

	}

	private boolean isSecondaryNodeEndDefinedByRelation(){

		javafx.scene.Node content = applicationController.getSteveScene().getTemporalViewPane().getTemporalChainTabPane().getSelectionModel().getSelectedItem().getContent();
		TemporalChainPane temporalChainPane = (TemporalChainPane) content;
		TemporalChain temporalChainModel = temporalChainPane.getTemporalChainModel();

		ArrayList<TemporalRelation> listOfRelationsWhereNodeIsSecondary = temporalChainModel.getListOfRelationsWhereNodeIsSecondary(node);

		for(TemporalRelation temporalRelation : listOfRelationsWhereNodeIsSecondary){

			Synchronous synchronousRelation = (Synchronous) temporalRelation;

			if(temporalChainModel.relationDefinesEnd(synchronousRelation)){
				return true;
			}

		}

		return false;

	}

}
