package view.spatialViewPane;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.common.MediaNode;
import model.common.enums.MediaType;
import view.common.Language;
import controller.ApplicationController;

public class RepositoryMediaInfoPane extends ScrollPane{

	private ApplicationController applicationController;
	private MediaNode mediaNode;
	
	private TextField name;
	private TextField type;
	private TextField duration;

	private GridPane infoPropertyGridPane;
	
	public RepositoryMediaInfoPane(ApplicationController applicationController, MediaNode mediaNode){
		
		this.applicationController = applicationController;
		this.mediaNode = mediaNode;
	
		setId("repository-info-pane");
		
		Label nameLabel = new Label(Language.translate("name"));
		Label typeLabel = new Label(Language.translate("type"));
		Label durationLabel = null;
		nameLabel.setId("spatial-view-label");
		typeLabel.setId("spatial-view-label");
		
		name = new TextField();
		type = new TextField();
		name.setId("name-text-field");
		type.setId("type-text-field");
		name.setEditable(false);
		type.setEditable(false);
		
		Boolean isContinousMedia = false;
		if(mediaNode.getType() == MediaType.AUDIO || mediaNode.getType() == MediaType.VIDEO){
			isContinousMedia = true; 
		}

		if(isContinousMedia){
			
			durationLabel = new Label(Language.translate("duration"));
			durationLabel.setId("spatial-view-label");
			
			duration = new TextField();
			duration.setId("duration-text-field");
			duration.setEditable(false);
		}
		
		infoPropertyGridPane = new GridPane();
		infoPropertyGridPane.setId("info-property-grid-pane");
		infoPropertyGridPane.add(nameLabel, 0, 0);
		infoPropertyGridPane.add(name, 1, 0);
		infoPropertyGridPane.add(typeLabel, 9, 0);
		infoPropertyGridPane.add(type, 10, 0);
		
		if(durationLabel != null && duration != null){
			
			infoPropertyGridPane.add(durationLabel, 0, 2);
			infoPropertyGridPane.add(duration, 1, 2);
			
		}
		
		VBox infoPaneContainerVBox = new VBox();
		infoPaneContainerVBox.setId("info-pane-vbox");
		infoPaneContainerVBox.getChildren().add(infoPropertyGridPane);
		
		setContent(infoPaneContainerVBox);
		
		populateInfoPane();
		
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
	
	public void setDurationValue(String value){
		this.duration.setText(value);
	}
	
	public String getDurationValue(){
		return duration.getText();
	}

	private void populateInfoPane(){
		
		setNameValue(mediaNode.getName());
		setTypeValue(mediaNode.getType().toString());
		
		Boolean isContinousMedia = false;
		if(mediaNode.getType() == MediaType.AUDIO || mediaNode.getType() == MediaType.VIDEO){
			isContinousMedia = true; 
		}

		if(mediaNode.getDuration() != null && isContinousMedia){
			setDurationValue(Double.toString(mediaNode.getDuration()));
		}
		
		
	}
	
}
