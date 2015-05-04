package gui.temporalViewPane;

import gui.common.Language;
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
	private Button overlapsButton;
	private Button equalsButton;
	private Button finishesDelayFinishesButton;
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
	    			
	    metByButton = new Button();
	    metByButton.setId("met_by-button");
	    metByButton.setTooltip(new Tooltip(Language.translate("left.alignment")));
	        
        startsButton = new Button();
        startsButton.setId("starts-button");
        startsButton.setTooltip(new Tooltip(Language.translate("slave.begins.with.master.start")));
		       
        finishesButton = new Button();
        finishesButton.setId("finishes-button");
        finishesButton.setTooltip(new Tooltip(Language.translate("slave.finishes.with.master.end")));
	       
        beforeButton = new Button();
        beforeButton.setId("before-button");
        beforeButton.setTooltip(new Tooltip(Language.translate("distributes.sequentially.with.value.t")));
       
        startsDelayButton = new Button();
        startsDelayButton.setId("starts_delay-button");
        startsDelayButton.setTooltip(new Tooltip(Language.translate("slave.begins.with.delay.when.master.starts")));
	       
        finishesDelayButton = new Button();
        finishesDelayButton.setId("finishes_delay-button");
        finishesDelayButton.setTooltip(new Tooltip(Language.translate("slave.finishes.t.after.master.end")));
       
        duringButton = new Button();
        duringButton.setId("during-button");
        duringButton.setTooltip(new Tooltip(Language.translate("media.contains.other")));
        
        overlapsButton = new Button();
        overlapsButton.setId("overlaps-button");
        overlapsButton.setTooltip(new Tooltip(Language.translate("slave.begins.with.delay.when.master.starts.and.finishes.t.after.master.end")));
       
        equalsButton = new Button();
        equalsButton.setId("equals-button");
        equalsButton.setTooltip(new Tooltip(Language.translate("all.has.same.duration")));
        
        finishesDelayFinishesButton = new Button();
        finishesDelayFinishesButton.setId("finishes-delay-finishes-button");
        finishesDelayFinishesButton.setTooltip(new Tooltip(Language.translate("slave.finishes.t.before.master.end")));
       
        zoomButton = new ZoomButton();
        showAnchorsLinksButton = new CheckBox(Language.translate("show.anchors.and.links"));
       
        zoomShowLinksButtonPane = new HBox();
        zoomShowLinksButtonPane.setId("zoom-links-pane");
        zoomShowLinksButtonPane.setFillHeight(false);
        zoomShowLinksButtonPane.getChildren().add(zoomButton);
        zoomShowLinksButtonPane.getChildren().add(showAnchorsLinksButton);
	       
        alignmentButtonPane = new HBox();
        alignmentButtonPane.setId("alignment-pane");
        alignmentButtonPane.getChildren().add(equalsButton);
        alignmentButtonPane.getChildren().add(meetsButton);
        alignmentButtonPane.getChildren().add(metByButton);
        alignmentButtonPane.getChildren().add(startsButton);
        alignmentButtonPane.getChildren().add(finishesButton);
        alignmentButtonPane.getChildren().add(beforeButton);
        alignmentButtonPane.getChildren().add(duringButton);
        alignmentButtonPane.getChildren().add(overlapsButton);
        alignmentButtonPane.getChildren().add(startsDelayButton);
        alignmentButtonPane.getChildren().add(finishesDelayButton);
        alignmentButtonPane.getChildren().add(finishesDelayFinishesButton);
        
	}
	
	private void createButtonActions(){
		
	}
	
}
