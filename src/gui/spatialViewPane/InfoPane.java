package gui.spatialViewPane;

import gui.common.Language;
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

public class InfoPane extends ScrollPane{

	private Controller controller;
	private Media media;
	
	private TextField name;
	private TextField type;
	private TextField startTime;
	private TextField endTime;
	private TextField duration;
	private CheckBox interactive;

	private GridPane infoPropertyGridPane;
	
	public InfoPane(Controller controller, Media media){
		
		this.controller = controller;
		this.media = media;
	
		setId("info-pane");
		
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
		
		infoPropertyGridPane = new GridPane();
		infoPropertyGridPane.setId("info-property-grid-pane");
		infoPropertyGridPane.add(nameLabel, 0, 0);
		infoPropertyGridPane.add(typeLabel, 1, 0);
		infoPropertyGridPane.add(startTimeLabel, 0, 3);
		infoPropertyGridPane.add(endTimeLabel, 1, 3);
		infoPropertyGridPane.add(durationLabel, 0, 6);
		infoPropertyGridPane.add(name, 0, 1);
		infoPropertyGridPane.add(type, 1, 1);
		infoPropertyGridPane.add(startTime, 0, 4);
		infoPropertyGridPane.add(endTime, 1, 4);
		infoPropertyGridPane.add(duration, 0, 7);
		infoPropertyGridPane.add(interactive, 0, 9);
		
		setContent(infoPropertyGridPane);
		
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
		
		controller.populateInfoPropertyJavaBean(this, media);
		
	}
	
}
