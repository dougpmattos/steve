package gui.spatialViewPane;

import gui.common.Language;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
	private GridPane gridPaneContainer;
	
	public InfoPane(Controller controller, Media media){
		
		this.controller = controller;
		this.media = media;
	
		setId("info-pane");

		Label nameLabel = new Label(Language.translate("name"));
		Label typeLabel = new Label(Language.translate("type"));
		Label startTimeLabel = new Label(Language.translate("start.time"));
		Label endTimeLabel = new Label(Language.translate("end.time"));
		Label durationLabel = new Label(Language.translate("duration"));
		
		name = new TextField();
		type = new TextField();
		startTime = new TextField();
		endTime = new TextField();
		duration = new TextField();
		interactive  =new CheckBox(Language.translate("interactive"));
		
		gridPaneContainer = new GridPane();
		gridPaneContainer.setId("info-pane-grid-pane");
		gridPaneContainer.add(nameLabel, 0, 0);
		gridPaneContainer.add(typeLabel, 1, 0);
		gridPaneContainer.add(startTimeLabel, 0, 3);
		gridPaneContainer.add(endTimeLabel, 1, 3);
		gridPaneContainer.add(durationLabel, 0, 6);
		gridPaneContainer.add(name, 0, 1);
		gridPaneContainer.add(type, 1, 1);
		gridPaneContainer.add(startTime, 0, 4);
		gridPaneContainer.add(endTime, 1, 4);
		gridPaneContainer.add(duration, 0, 7);
		gridPaneContainer.add(interactive, 0, 9);
		
		setContent(gridPaneContainer);
		
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
	
	public void setInteractiveValue(String value){
		this.interactive.setText(value);
	}
	
	public Boolean getInteractiveValue(){
		return interactive.isSelected();
	}
	
}
