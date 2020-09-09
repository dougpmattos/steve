package view.spatialViewPane;

import br.uff.midiacom.ana.util.exception.XMLException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.common.Media;
import view.common.Language;
import controller.ApplicationController;
import view.common.dialogs.MessageDialog;

import java.io.IOException;

public class TemporalMediaInfoPane extends ScrollPane{

	private boolean isLinked = false;
	private ApplicationController applicationController;
	private Media media;
	
	private TextField nameTextField;
	private TextField typeTextField;
	private TextField startTimeTextField;
	private TextField endTimeTextField;
	private TextField durationTextField;
	private CheckBox interactiveTextField;
	private Button linkButton;

	private GridPane infoPropertyGridPane;
	
	public TemporalMediaInfoPane(ApplicationController applicationController, Media media){
		
		this.applicationController = applicationController;
		this.media = media;
	
		setId("temporal-info-pane");
		
		Label nameLabel = new Label(Language.translate("name"));
		Label typeLabel = new Label(Language.translate("type"));
		Label startTimeLabel = new Label(Language.translate("start.time"));
		Label endTimeLabel = new Label(Language.translate("end.time"));
		Label durationLabel = new Label(Language.translate("duration"));
		nameLabel.setId("spatial-view-label");
		typeLabel.setId("spatial-view-label");
		startTimeLabel.setId("spatial-view-label");
		endTimeLabel.setId("spatial-view-label");
		durationLabel.setId("spatial-view-label");
		
		nameTextField = new TextField();
		typeTextField = new TextField();
		startTimeTextField = new TextField();
		endTimeTextField = new TextField();
		durationTextField = new TextField();
		interactiveTextField =new CheckBox(Language.translate("interactive"));
		linkButton = new Button();

		nameTextField.setId("name-text-field");
		typeTextField.setId("type-text-field");
		startTimeTextField.setId("start-text-field");
		endTimeTextField.setId("end-text-field");
		durationTextField.setId("duration-text-field");
		nameTextField.setEditable(false);
		typeTextField.setEditable(false);
		startTimeTextField.setEditable(true);
		endTimeTextField.setEditable(true);
		durationTextField.setEditable(true);
		interactiveTextField.setDisable(true);
		linkButton.setId("link-button");
		linkButton.setTooltip(new Tooltip(Language.translate("link.start.end")));
		
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
		infoPropertyGridPane.add(interactiveTextField, 0, 6, 10, 1);
		
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

					if(newValue > media.getEnd() && !isLinked){

						MessageDialog warningMessageDialog = new MessageDialog(Language.translate("begin.greater.than.end"),
								"OK", 130);
						warningMessageDialog.showAndWait();

					}else {

						if(!media.getBegin().equals(newValue)){
							try {
								ApplicationController.getInstance().updateNodeStartTime(media, newValue, isLinked);
							} catch (IOException e) {
								e.printStackTrace();
							} catch (XMLException e) {
								e.printStackTrace();
							}
						}
						endTimeTextField.setText(String.valueOf(media.getEnd()));
						if(!isLinked){
							durationTextField.setText(String.valueOf(media.getDuration()));
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
					Double isLinkedNewBeginFromNewEnd = newValue - media.getDuration();

					if(newValue < media.getBegin() && !isLinked){

						MessageDialog warningMessageDialog = new MessageDialog(Language.translate("end.less.than.begin"),
								"OK", 130);
						warningMessageDialog.showAndWait();

					}else if(isLinkedNewBeginFromNewEnd < 0 && isLinked) {

						MessageDialog warningMessageDialog = new MessageDialog(Language.translate("start.less.zero"),
								"OK", 130);
						warningMessageDialog.showAndWait();

					}else {

							if(!media.getEnd().equals(newValue)){
								try {
									ApplicationController.getInstance().updateNodeEndTime(media, newValue, isLinked);
								} catch (IOException e) {
									e.printStackTrace();
								} catch (XMLException e) {
									e.printStackTrace();
								}
							}
							startTimeTextField.setText(String.valueOf(media.getBegin()));
							if(!isLinked){
								durationTextField.setText(String.valueOf(media.getDuration()));
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
					if(!media.getDuration().equals(newValue)){
						try {
							ApplicationController.getInstance().updateNodeDurationTime(media, newValue);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (XMLException e) {
							e.printStackTrace();
						}
					}
					endTimeTextField.setText(String.valueOf(media.getEnd()));
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
		
		setNameValue(media.getName());
		setTypeValue(media.getType().toString());
		setStartTimeValue(Double.toString(media.getBegin()));
		setEndTimeValue(Double.toString(media.getEnd()));
		setDurationValue(Double.toString(media.getDuration()));
		setInteractiveValue(media.isInteractive());
		
	}
	
	public void populateInfoPropertyJavaBean(){
		
		applicationController.populateTemporalInfoPropertyJavaBean(this, media);
		
	}
	
}
