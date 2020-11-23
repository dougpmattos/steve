package view.spatialViewPane;

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
import model.common.MediaNode;
import model.common.Node;
import model.common.SensoryEffectNode;
import view.common.Language;
import controller.ApplicationController;
import view.common.dialogs.MessageDialog;

public class TemporalMediaInfoPane extends ScrollPane{

	private boolean isLinked = false;
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
		infoPropertyGridPane.add(typeLabel, 9, 0);
		infoPropertyGridPane.add(typeTextField, 10, 0);
		infoPropertyGridPane.add(startTimeLabel, 0, 2);
		infoPropertyGridPane.add(startTimeTextField, 1, 2);
		infoPropertyGridPane.add(linkButton, 5, 2);
		infoPropertyGridPane.add(endTimeLabel, 9, 2);
		infoPropertyGridPane.add(endTimeTextField, 10, 2);
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
		
	}

	private void createListeners() {

		startTimeTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if(event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.TAB)) {

					Double newValue = Double.parseDouble(getStartTimeValue());

					if(newValue > node.getEnd() && !isLinked){

						MessageDialog warningMessageDialog = new MessageDialog(Language.translate("begin.greater.than.end"),
								"OK", 130);
						warningMessageDialog.showAndWait();

					}else {

						if(!node.getBegin().equals(newValue)){
								ApplicationController.getInstance().updateNodeStartTime(node, newValue, isLinked);
						}
						endTimeTextField.setText(String.valueOf(node.getEnd()));
						if(!isLinked){
							durationTextField.setText(String.valueOf(node.getDuration()));
						}
						endTimeTextField.requestFocus();

					}

				}
			}

		});

		endTimeTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if(event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.TAB)) {

					Double newValue = Double.parseDouble(getEndTimeValue());
					Double isLinkedNewBeginFromNewEnd = newValue - node.getDuration();

					if(newValue < node.getBegin() && !isLinked){

						MessageDialog warningMessageDialog = new MessageDialog(Language.translate("end.less.than.begin"),
								"OK", 130);
						warningMessageDialog.showAndWait();

					}else if(isLinkedNewBeginFromNewEnd < 0 && isLinked) {

						MessageDialog warningMessageDialog = new MessageDialog(Language.translate("start.less.zero"),
								"OK", 130);
						warningMessageDialog.showAndWait();

					}else {

							if(!node.getEnd().equals(newValue)){
								ApplicationController.getInstance().updateNodeEndTime(node, newValue, isLinked);
							}
							startTimeTextField.setText(String.valueOf(node.getBegin()));
							if(!isLinked){
								durationTextField.setText(String.valueOf(node.getDuration()));
							}
							durationTextField.requestFocus();

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
					startTimeTextField.requestFocus();
				}
			}

		});

		linkButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override public void handle(ActionEvent e) {

				if(isLinked == true){
					isLinked = false;
					linkButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/spatialViewPane/link-off-24dp.png"))));
				}else{
					isLinked = true;
					linkButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/spatialViewPane/link-on-24dp.png"))));
				}

			}
		});

		linkButton.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override public void handle(MouseEvent mouseEvent) {

				if(isLinked == true){
					linkButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/spatialViewPane/link-on-24dp-hover.png"))));
				}else{
					linkButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/spatialViewPane/link-off-24dp-hover.png"))));
				}

			}
		});

		linkButton.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override public void handle(MouseEvent mouseEvent) {

				if(isLinked == true){
					linkButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/spatialViewPane/link-on-24dp.png"))));
				}else{
					linkButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/spatialViewPane/link-off-24dp.png"))));
				}

			}
		});
		
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
			setPriorityValue(Double.toString(sensoryEffectNode.getPresentationProperty().getPriority()));
		}
		
	}
	
	public void populateInfoPropertyJavaBean(){
		
		applicationController.populateTemporalInfoPropertyJavaBean(this, mediaNode);
		
	}
	
}
