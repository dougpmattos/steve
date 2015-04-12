package gui.temporalViewPane;

import gui.common.Language;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class TemporalViewButtonPane extends BorderPane {

	private Button meetsButton;
	private Button metByButton;
	private Button startsButton;
	private Button finishesButton;
	private Button beforeButton;
	private Button startsDelayButton;
	private Button finishesDelayButton;
	private Button duringButton;
	private Button equalsButton;
	private ZoomButton zoomButton;
	private HBox zoomShowLinksButtonPane;
	private CheckBox showAnchorsLinksButton;
	private HBox alignmentButtonPane;
	
	public TemporalViewButtonPane(){
		
		setId("button-pane");
	    getStylesheets().add("gui/temporalViewPane/styles/temporalViewButtonPane.css");
	    
	    createButtons();
	    
	    setLeft(alignmentButtonPane);
	    setRight(zoomShowLinksButtonPane);
	    
	    createButtonActions();
		
	}

	private void createButtons() {
		
		meetsButton = new Button();
	    meetsButton.setId("meets-button");
	    meetsButton.setTooltip(new Tooltip(Language.translate("right.alignment")));
	    meetsButton.setScaleX(1);
	    meetsButton.setScaleY(1);
	    			
	    metByButton = new Button();
	    metByButton.setId("met_by-button");
	    metByButton.setTooltip(new Tooltip(Language.translate("left.alignment")));
	    metByButton.setScaleX(1);
	    metByButton.setScaleY(1);
	        
        startsButton = new Button();
        startsButton.setId("starts-button");
        startsButton.setTooltip(new Tooltip(Language.translate("slave.begins.with.master.start")));
        startsButton.setScaleX(0.8);
        startsButton.setScaleY(0.8);
		       
        finishesButton = new Button();
        finishesButton.setId("finishes-button");
        finishesButton.setTooltip(new Tooltip(Language.translate("slave.finishes.with.master.end")));
        finishesButton.setScaleX(0.8);
        finishesButton.setScaleY(0.8);
	       
        beforeButton = new Button();
        beforeButton.setId("before-button");
        beforeButton.setTooltip(new Tooltip(Language.translate("distributes.sequentially.with.value.t")));
        beforeButton.setScaleX(1);
        beforeButton.setScaleY(1);
       
        startsDelayButton = new Button();
        startsDelayButton.setId("starts_delay-button");
        startsDelayButton.setTooltip(new Tooltip(Language.translate("slave.begins.with.delay.when.master.starts")));
        startsDelayButton.setScaleX(0.8);
        startsDelayButton.setScaleY(0.8);
	       
        finishesDelayButton = new Button();
        finishesDelayButton.setId("finishes_delay-button");
        finishesDelayButton.setTooltip(new Tooltip(Language.translate("slave.finishes.t.before.master.end")));
        finishesDelayButton.setScaleX(0.8);
        finishesDelayButton.setScaleY(0.8);
       
        duringButton = new Button();
        duringButton.setId("during-button");
        duringButton.setTooltip(new Tooltip(Language.translate("media.contains.other")));
        duringButton.setScaleX(0.8);
        duringButton.setScaleY(0.8);
       
        equalsButton = new Button();
        equalsButton.setId("equals-button");
        equalsButton.setTooltip(new Tooltip(Language.translate("all.has.same.duration")));
        equalsButton.setScaleX(0.8);
        equalsButton.setScaleY(0.8);
       
        zoomButton = new ZoomButton();
        showAnchorsLinksButton = new CheckBox(Language.translate("show.anchors.and.links"));
       
        zoomShowLinksButtonPane = new HBox();
        zoomShowLinksButtonPane.setId("button-pane");
        zoomShowLinksButtonPane.setSpacing(20);
        zoomShowLinksButtonPane.setFillHeight(false);
        zoomShowLinksButtonPane.setAlignment(Pos.CENTER);
        zoomShowLinksButtonPane.setPadding(new Insets(0, 5, 0, 0));
        zoomShowLinksButtonPane.getChildren().add(zoomButton);
        zoomShowLinksButtonPane.getChildren().add(showAnchorsLinksButton);
	       
        alignmentButtonPane = new HBox();
        alignmentButtonPane.setId("button-pane");
        alignmentButtonPane.setAlignment(Pos.CENTER);
        alignmentButtonPane.setPadding(new Insets(0, 0, 0, 5));
        alignmentButtonPane.setSpacing(20);
        alignmentButtonPane.getChildren().add(meetsButton);
        alignmentButtonPane.getChildren().add(metByButton);
        alignmentButtonPane.getChildren().add(startsButton);
        alignmentButtonPane.getChildren().add(finishesButton);
        alignmentButtonPane.getChildren().add(beforeButton);
        alignmentButtonPane.getChildren().add(startsDelayButton);
        alignmentButtonPane.getChildren().add(finishesDelayButton);
        alignmentButtonPane.getChildren().add(duringButton);
        alignmentButtonPane.getChildren().add(equalsButton);
        
	}
	
	private void createButtonActions(){
		
	}
	
}
