package view.temporalViewPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.common.Media;
import model.temporalView.Synchronous;
import model.temporalView.enums.RelationType;
import view.common.Language;
import view.common.SliderButton;
import controller.Controller;

public class TemporalViewButtonPane extends BorderPane {

	private Button meetsButton;
	private Button meetsDelayButton;
	private Button metByButton;
	private Button metByDelayButton;
	private Button startsButton;
	private Button finishesButton;
	private Button beforeButton;
	private Button startsDelayButton;
	private Button finishesDelayButton;
	private Button duringButton;
	private Button overlapsButton;
	private Button equalsButton;
	private SliderButton zoomButton;
	private HBox otherButtonPane;
	private Button synchronizeButton;
	private CheckBox showAnchorsLinksButton;
	private HBox alignmentButtonPane;
	private TemporalViewPane temporalViewPane;
	private Controller controller;
	
	public TemporalViewButtonPane(Controller controller, TabPane temporalChainTabPane, TemporalViewPane temporalViewPane){
		
		this.controller = controller;
		this.temporalViewPane = temporalViewPane;
		
		setId("button-pane");
	    getStylesheets().add("view/temporalViewPane/styles/temporalViewButtonPane.css");
	    
	    createButtons();
	    
	    setLeft(alignmentButtonPane);
	    setRight(otherButtonPane);
	    
	    createButtonActions(temporalChainTabPane);
		
	}

	private void createButtons() {
		
		equalsButton = new Button();
        equalsButton.setId("equals-button");
        equalsButton.setTooltip(new Tooltip(Language.translate("equals")));
        
        startsButton = new Button();
        startsButton.setId("starts-button");
        startsButton.setTooltip(new Tooltip(Language.translate("starts")));
        
        startsDelayButton = new Button();
        startsDelayButton.setId("starts-delay-button");
        startsDelayButton.setTooltip(new Tooltip(Language.translate("starts.delay")));
	       
        finishesButton = new Button();
        finishesButton.setId("finishes-button");
        finishesButton.setTooltip(new Tooltip(Language.translate("finishes")));
        
        finishesDelayButton = new Button();
        finishesDelayButton.setId("finishes-delay-button");
        finishesDelayButton.setTooltip(new Tooltip(Language.translate("finishes.delay")));
        
		meetsButton = new Button();
	    meetsButton.setId("meets-button");
	    meetsButton.setTooltip(new Tooltip(Language.translate("meets")));
	    
	    meetsDelayButton = new Button();
	    meetsDelayButton.setId("meets-delay-button");
	    meetsDelayButton.setTooltip(new Tooltip(Language.translate("meets.delay")));
	    			
	    metByButton = new Button();
	    metByButton.setId("met-by-button");
	    metByButton.setTooltip(new Tooltip(Language.translate("met.by")));
	        
	    metByDelayButton= new Button();
        metByDelayButton.setId("met-by-delay-button");
        metByDelayButton.setTooltip(new Tooltip(Language.translate("met.by.delay")));
        
        duringButton = new Button();
        duringButton.setId("during-button");
        duringButton.setTooltip(new Tooltip(Language.translate("during")));
        
        overlapsButton = new Button();
        overlapsButton.setId("overlaps-button");
        overlapsButton.setTooltip(new Tooltip(Language.translate("overlaps")));
 
        beforeButton = new Button();
        beforeButton.setId("before-button");
        beforeButton.setTooltip(new Tooltip(Language.translate("before")));

        synchronizeButton = new Button(Language.translate("synchronize"));
        synchronizeButton.setId("synchronize-button");
        synchronizeButton.setTooltip(new Tooltip(Language.translate("synchronize-button")));
        
        Label icon = new Label();
		icon.setId("zoom-icon");
        zoomButton = new SliderButton(0.0, 100.0, 50.0, 150.0, icon, false);
        showAnchorsLinksButton = new CheckBox(Language.translate("show.relations"));
       
        otherButtonPane = new HBox();
        otherButtonPane.setId("other-button-pane");
        otherButtonPane.setFillHeight(false);
        otherButtonPane.getChildren().add(synchronizeButton);
        otherButtonPane.getChildren().add(zoomButton);
        otherButtonPane.getChildren().add(showAnchorsLinksButton);
	       
        alignmentButtonPane = new HBox();
        alignmentButtonPane.setId("alignment-pane");
        alignmentButtonPane.getChildren().add(equalsButton);
        alignmentButtonPane.getChildren().add(startsButton);
        alignmentButtonPane.getChildren().add(startsDelayButton);
        alignmentButtonPane.getChildren().add(finishesButton);
        alignmentButtonPane.getChildren().add(finishesDelayButton);
        alignmentButtonPane.getChildren().add(meetsButton);
        alignmentButtonPane.getChildren().add(meetsDelayButton);
        alignmentButtonPane.getChildren().add(metByButton);
        alignmentButtonPane.getChildren().add(metByDelayButton);
        alignmentButtonPane.getChildren().add(duringButton);
        alignmentButtonPane.getChildren().add(overlapsButton);
        alignmentButtonPane.getChildren().add(beforeButton);
        
	}
	
	private void createButtonActions(TabPane temporalChainTabPane){
		
		startsButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    public void handle(ActionEvent t) {
		    	
		    	Media relationMasterMedia = temporalViewPane.getFirstSelectedMedia();
		    	
		    	Synchronous<Media> synchronousRelation = new Synchronous<Media>();
    			synchronousRelation.setType(RelationType.STARTS);
    			synchronousRelation.setMasterMedia(relationMasterMedia);
    			synchronousRelation.setExplicit(true);
	
		    	for(Media media : temporalViewPane.getSelectedMediaList()){
		    		
		    		if(media != relationMasterMedia){
		    			
		    			synchronousRelation.addSlaveMedia(media);
		    			
		    		  	Tab selectedTab = null;
				    	TemporalChainPane temporalChainPane = null;
				    	
				    	for (Tab tab : temporalViewPane.getTemporalChainTabPane().getTabs()){
				    		if(tab.isSelected()){
				    			selectedTab = tab;
				    			break;
				    		}
				    	}
				    	if(selectedTab != null){
				    		temporalChainPane = (TemporalChainPane) selectedTab.getContent();
				    	}
				    	
		    			controller.addSynchronousRelation(temporalChainPane.getTemporalChainModel(), synchronousRelation);
		    			
		    		}
		    		
		    	}
		    	
		    	
		    }
		    
		});
		
		zoomButton.getSlider().valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
//            	ScrollPane teste = (ScrollPane) temporalChainTabPane.getTabs().get(0).getContent();
//            	TemporalChainPane teste2 = (TemporalChainPane) teste.getContent();
//            	((NumberAxis) teste2.getXAxis()).setUpperBound(25);
//            	//new_val.doubleValue()
            }
        });
		
	}
	
}
