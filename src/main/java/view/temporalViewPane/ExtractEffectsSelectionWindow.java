package view.temporalViewPane;

import controller.ApplicationController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.common.Media;
import model.common.Node;
import model.common.enums.SensoryEffectType;
import model.temporalView.Interactivity;
import model.temporalView.enums.InteractivityKeyType;
import view.common.dialogs.InputDialog;
import view.common.Language;
import view.common.dialogs.ReturnMessage;
import view.utility.AnimationUtil;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked","rawtypes"})
public class ExtractEffectsSelectionWindow extends Stage {
     
	private static final int HEIGHT = 610;
	private static final int WIDTH = 650;
    
	private ApplicationController applicationController;
	private TemporalViewPane temporalViewPane;
	private Boolean isEdition;
	private Interactivity interactivityRelation;
	
    private Scene scene;
    
    private ChoiceBox<InteractivityKeyType> interactivityKeyTypeField;
	private ChoiceBox interactivityKeyField;
	//private List<CheckBox> effectsCheckbox;
	private List<CheckBox> effectsCheckbox = new ArrayList<CheckBox>();
	
	private List<SensoryEffectType> SensoryEffectsList = new ArrayList();



    private ChoiceBox<Node> nodeToBeStoppedField;
    private ChoiceBox timelineToBeStartedField;
    private TextField stopDelayField;
    private TextField startDelayField;
    
    private Button addNewButton;
    private Button removeMediaButton;
    
    private boolean isLastOne;
    
    private GridPane formGridPane;
    private VBox mediaCloseNewVBoxContainer;
    private VBox timelineCloseNewVBoxContainer;

    private Media interactiveMedia;
	private ObservableList timelineFieldOptions;
    
    public ExtractEffectsSelectionWindow(ApplicationController applicationController, TemporalViewPane temporalViewPane, Media firstSelectedMedia) {

        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);

        this.temporalViewPane = temporalViewPane;
        this.applicationController = applicationController;
        isEdition = false;
        
        this.interactiveMedia = firstSelectedMedia;

        BorderPane containerBorderPane = new BorderPane();
        containerBorderPane.setId("container-border-pane");
        containerBorderPane.getStylesheets().add("styles/temporalViewPane/popupWindow.css");
        
        formGridPane = createForm();
        ScrollPane scrollPaneContainer = new ScrollPane();
        scrollPaneContainer.setContent(formGridPane);
        scrollPaneContainer.setId("scroll-pane-container");
        
        containerBorderPane.setTop(createToolBar());
        containerBorderPane.setCenter(scrollPaneContainer);

        scene = new Scene(containerBorderPane, WIDTH, HEIGHT);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);

    }
    
    public ExtractEffectsSelectionWindow(ApplicationController applicationController, TemporalViewPane temporalViewPane, Interactivity<Media> interactivityToLoad){
    	
    	setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);

        this.temporalViewPane = temporalViewPane;
        this.applicationController = applicationController;
        isEdition=true;
        this.interactivityRelation = interactivityToLoad;
        
        this.interactiveMedia = (Media) interactivityToLoad.getMasterNode();
        
        BorderPane containerBorderPane = new BorderPane();
        containerBorderPane.setId("container-border-pane");
        containerBorderPane.getStylesheets().add("styles/temporalViewPane/popupWindow.css");
        
        formGridPane = createForm();
        ScrollPane scrollPaneContainer = new ScrollPane();
        scrollPaneContainer.setContent(formGridPane);
        scrollPaneContainer.setId("scroll-pane-container");
        
        containerBorderPane.setTop(createToolBar());
        containerBorderPane.setCenter(scrollPaneContainer);

        scene = new Scene(containerBorderPane, WIDTH, HEIGHT);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
        
    	
    }

	private BorderPane createToolBar(){
    	
    	BorderPane toolBarBorderPane = new BorderPane();
    	toolBarBorderPane.setId("tool-bar-pane");
    	
    	Button closeButton = new Button();
    	Button saveButton = new Button(Language.translate("extract").toUpperCase());
    	saveButton.setId("save-button");
    	closeButton.setId("close-button");
    	createToolBarButtonActions(closeButton, saveButton);
    	
    	Label titleLabe;
    	titleLabe = new Label(Language.translate("new.sensory.effect.auto.extraction"));
   
    	titleLabe.setId("title-label");
    	titleLabe.setWrapText(true);

    	HBox titleHBox = new HBox();
    	titleHBox.setId("title-hbox");
    	titleHBox.getChildren().add(titleLabe);
    	
    	toolBarBorderPane.setLeft(closeButton);
    	toolBarBorderPane.setCenter(titleHBox);
    	toolBarBorderPane.setRight(saveButton);
    	
    	return toolBarBorderPane;
    	
    }

	private GridPane createForm(){
    	
		Label interactivityKeySubtitle = new Label(Language.translate("interactivity.key").toUpperCase());
        Label stopActionSubtitle = new Label(Language.translate("select.effects.to.extract"));
        
        stopActionSubtitle.setId("subtitle-label");



        // popular as checkboxes com os efeitos sensoriais 
        for (SensoryEffectType SensoryEffect : SensoryEffectType.values()) { 
            effectsCheckbox.add(new CheckBox(SensoryEffect.toString()));
            SensoryEffectsList.add(SensoryEffect);

        }


        
        for(CheckBox checkbox : effectsCheckbox) {
           checkbox.setId("new-interactive-media-field");
           checkbox.setSelected(false);
       }



       
        HBox stopActionSeparator = new HBox();
        stopActionSeparator.setId("separator");
        VBox stopActionSubtitleSeparatorContainer = new VBox();
        stopActionSubtitleSeparatorContainer.setId("subtitle-separator-container");
        stopActionSubtitleSeparatorContainer.getChildren().add(stopActionSubtitle);
        stopActionSubtitleSeparatorContainer.getChildren().add(stopActionSeparator);
        


        
        GridPane formGridPane = new GridPane();
        formGridPane.setId("form-grid-pane");
        formGridPane.add(stopActionSubtitleSeparatorContainer, 0, 2, 2, 1);
        
        
        int i=3;
        for(CheckBox checkbox : effectsCheckbox) {
            formGridPane.add(checkbox, 0, i);
            i++;
        }
       
        return formGridPane;
    	
    }

    private void createToolBarButtonActions(Button closeButton, Button saveButton) {
		
    	closeButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {
    			
    			InputDialog showContinueQuestionInputDialog;

    			if(isEdition){
    				showContinueQuestionInputDialog = new InputDialog(Language.translate("discard.auto.extraction"), null, Language.translate("no"), Language.translate("discard"), null, 140);
    			}else {
    				showContinueQuestionInputDialog = new InputDialog(Language.translate("discard.auto.extraction"), null, Language.translate("no"), Language.translate("discard"), null, 140);
    			}
    			
    			String answer = showContinueQuestionInputDialog.showAndWaitAndReturn();
    			
    			if(answer.equalsIgnoreCase("right")){
    				ExtractEffectsSelectionWindow.this.close();
    	    	}

    		}
    	});

		saveButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {
	
    			StringBuilder errorMessage = new StringBuilder();
    			int high = 190;
    			int count = 0;

				ExtractEffectsSelectionWindow.this.close();
				
				ReturnMessage returnMessage = new ReturnMessage(Language.translate("saved.effects.to.extract"), 350);
				returnMessage.show();
				AnimationUtil.applyFadeInOut(returnMessage);
				
    		}
    		
    	});
		
	}

	private void createFormButtonActions(Button removeMediaButton, Button addNewButton, HBox fieldCloseHBoxContainer, VBox fieldCloseNewVBoxContainer) {
		
		removeMediaButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {

    			ChoiceBox<Media> field = (ChoiceBox<Media>) fieldCloseHBoxContainer.getChildren().get(0);
				
    			if(fieldCloseNewVBoxContainer.getChildren().size() == 2){
    				
    				field.setValue(null);
    				
    				fieldCloseNewVBoxContainer.getChildren().remove(1);
    				fieldCloseHBoxContainer.getChildren().remove(1);

    			}else {
    				
    				fieldCloseNewVBoxContainer.getChildren().remove(fieldCloseHBoxContainer);
    				
    			}

    		}
    	});
		
		addNewButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {
    			
    			ChoiceBox choiceBoxField;
    			HBox fieldCloseHBoxContainer = new HBox();
    			

    				choiceBoxField = new ChoiceBox<Media>(FXCollections.observableArrayList(timelineFieldOptions));
    				fieldCloseHBoxContainer.setId("timeline-close-button-hbox-container");
    			
    			
    			choiceBoxField.setId("new-interactive-media-field");
    		    fieldCloseHBoxContainer.getChildren().add(choiceBoxField);
    
    			fieldCloseNewVBoxContainer.getChildren().remove(addNewButton);
    			fieldCloseNewVBoxContainer.getChildren().add(fieldCloseHBoxContainer);

    		    choiceBoxField.setOnAction(new EventHandler<ActionEvent>() {

    				@Override
    				public void handle(ActionEvent event) {
    	
    					if(choiceBoxField.getProperties().get("previousValue") == null){
    						
    						Button removeMediaButton = new Button();
        					removeMediaButton.setId("media-to-be-stopped-close-button");
        			    	fieldCloseHBoxContainer.getChildren().add(removeMediaButton);
        			    	
        			    	Button addNewButton = new Button(Language.translate("add.new"));
        			    	addNewButton.setId("add-new-button");
        			    	fieldCloseNewVBoxContainer.getChildren().add(addNewButton);
        			    	
        			    	createFormButtonActions(removeMediaButton, addNewButton, fieldCloseHBoxContainer, fieldCloseNewVBoxContainer);
        			    	
    					}
    					
    					choiceBoxField.getProperties().put("previousValue", choiceBoxField.getValue());

    				}
    				
    			});
    		        
    		}
    	});
		
	}

	public List<SensoryEffectType> getSelectedSensoryEffects() {

		List<SensoryEffectType> selectedEffects = new ArrayList();

		Object[] current = effectsCheckbox.toArray();
    	for(int i=0;i<current.length;i++)
		if (((CheckBox) current[i]).isSelected())
		{
			selectedEffects.add(SensoryEffectsList.get(i));
		}

		return selectedEffects;

	}

}
