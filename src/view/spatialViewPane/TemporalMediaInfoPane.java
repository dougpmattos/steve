package view.spatialViewPane;

import view.common.Language;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.common.Media;
import controller.Controller;

public class TemporalMediaInfoPane extends ScrollPane{

	private Controller controller;
	private Media media;
	
	private TextField name;
	private TextField type;
	private TextField startTime;
	private TextField endTime;
	private TextField duration;
	private CheckBox interactive;

	private GridPane infoPropertyGridPane;
	
	public TemporalMediaInfoPane(Controller controller, Media media){
		
		this.controller = controller;
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
		
		name = new TextField();
		type = new TextField();
		startTime = new TextField();
		endTime = new TextField();
		duration = new TextField();
		interactive  =new CheckBox(Language.translate("interactive"));
		name.setId("info-pane-field");
		type.setId("info-pane-field");
		startTime.setId("info-pane-field");
		endTime.setId("info-pane-field");
		duration.setId("info-pane-field");
		
		infoPropertyGridPane = new GridPane();
		infoPropertyGridPane.setId("info-property-grid-pane");
		infoPropertyGridPane.add(nameLabel, 0, 0);
		infoPropertyGridPane.add(name, 1, 0);
		infoPropertyGridPane.add(typeLabel, 9, 0);
		infoPropertyGridPane.add(type, 10, 0);
		infoPropertyGridPane.add(startTimeLabel, 0, 2);
		infoPropertyGridPane.add(startTime, 1, 2);
		infoPropertyGridPane.add(endTimeLabel, 9, 2);
		infoPropertyGridPane.add(endTime, 10, 2);
		infoPropertyGridPane.add(durationLabel, 0, 4);
		infoPropertyGridPane.add(duration, 1, 4);
		infoPropertyGridPane.add(interactive, 0, 6, 10, 1);
		
		VBox infoPaneContainerVBox = new VBox();
		infoPaneContainerVBox.setId("info-pane-vbox");
		infoPaneContainerVBox.getChildren().add(infoPropertyGridPane);
		
		setContent(infoPaneContainerVBox);
		
		populateInfoPane();
		
		createListeners();
		
	}
	
	private void createListeners() {
		
		setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				
				populateInfoPropertyJavaBean();
				
			}
			
		});
		
	}
	
	public void setNameValue(String value){
		this.name.setText(value);
	}
	
	public String getNameValue(){
		return name.getText();
	}
	
	public void setTypeValue(String value){
		this.type.setText(value);
	}
	
	public String getTypeValue(){
		return type.getText();
	}
	
	public void setStartTimeValue(String value){
		this.startTime.setText(value);
	}
	
	public String getStartTimeValue(){
		return startTime.getText();
	}
	
	public void setEndTimeValue(String value){
		this.endTime.setText(value);
	}
	
	public String getEndTimeValue(){
		return endTime.getText();
	}
	
	public void setDurationValue(String value){
		this.duration.setText(value);
	}
	
	public String getDurationValue(){
		return duration.getText();
	}
	
	public void setInteractiveValue(Boolean value){
		this.interactive.setSelected(value);
	}
	
	public Boolean getInteractiveValue(){
		return interactive.isSelected();
	}
	
	private void populateInfoPane(){
		
		setNameValue(media.getName());
		setTypeValue(media.getMediaType().toString());
		setStartTimeValue(Double.toString(media.getBegin()));
		setEndTimeValue(Double.toString(media.getEnd()));
		setDurationValue(Double.toString(media.getDuration()));
		setInteractiveValue(media.getInteractive());
		
	}
	
	public void populateInfoPropertyJavaBean(){
		
		controller.populateTemporalInfoPropertyJavaBean(this, media);
		
	}
	
}
