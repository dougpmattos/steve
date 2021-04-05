package view.temporalViewPane;

import controller.ApplicationController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.common.MediaNode;
import model.common.enums.SensoryEffectType;
import view.common.Language;
import view.common.dialogs.InputDialog;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked","rawtypes"})
public class ExtractEffectsSelectionWindow extends Stage {
     
	private static final int HEIGHT = 530;
	private static final int WIDTH = 530;
    
	private ApplicationController applicationController;
	private TemporalViewPane temporalViewPane;
	private Boolean isEdition;
	private Boolean hasDiscarded;
	
    private Scene scene;

	private List<CheckBox> effectsCheckbox = new ArrayList<CheckBox>();
	private List<SensoryEffectType> SensoryEffectsList = new ArrayList();
    private GridPane formGridPane;

    
    public ExtractEffectsSelectionWindow(ApplicationController applicationController, TemporalViewPane temporalViewPane, MediaNode firstSelectedMediaNode) {

        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);

        this.temporalViewPane = temporalViewPane;
        this.applicationController = applicationController;
        isEdition = false;
		hasDiscarded = false;

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
    	Button extractButton = new Button(Language.translate("extract").toUpperCase());
    	extractButton.setId("save-button");
    	closeButton.setId("close-button");
    	createToolBarButtonActions(closeButton, extractButton);
    	
    	Label titleLabe;
    	titleLabe = new Label(Language.translate("new.sensory.effect.auto.extraction"));
   
    	titleLabe.setId("title-label");
    	titleLabe.setWrapText(true);

    	HBox titleHBox = new HBox();
    	titleHBox.setId("title-hbox");
    	titleHBox.getChildren().add(titleLabe);
    	
    	toolBarBorderPane.setLeft(closeButton);
    	toolBarBorderPane.setCenter(titleHBox);
    	toolBarBorderPane.setRight(extractButton);
    	
    	return toolBarBorderPane;
    	
    }

	private GridPane createForm(){

        Label selectEffectLabel = new Label(Language.translate("select.effects.to.extract"));

		selectEffectLabel.setId("sensory-effect-subtitle");

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
        stopActionSubtitleSeparatorContainer.getChildren().add(selectEffectLabel);
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

    private void createToolBarButtonActions(Button closeButton, Button extractButton) {
		
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
					hasDiscarded = true;
    	    	}

    		}
    	});

		extractButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {

				List<SensoryEffectType> selectedSensoryEffects = getSelectedSensoryEffects();
				if(selectedSensoryEffects.isEmpty()){

					InputDialog showOkInputDialog = new InputDialog(Language.translate("please.select.at.least.one.effect"), null, null, "OK", null, 120);
					showOkInputDialog.showAndWait();

				}else {
					ExtractEffectsSelectionWindow.this.close();
				}

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

	public Boolean getHasDiscarded() {
		return hasDiscarded;
	}
}
