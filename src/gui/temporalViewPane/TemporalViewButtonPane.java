package gui.temporalViewPane;

import gui.common.Language;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class TemporalViewButtonPane extends BorderPane {

	private Button meetsButton;
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
	private ZoomButton zoomButton;
	private HBox zoomShowLinksButtonPane;
	private CheckBox showAnchorsLinksButton;
	private HBox alignmentButtonPane;
	
	public TemporalViewButtonPane(TabPane temporalChainTabPane){
		
		setId("button-pane");
	    getStylesheets().add("gui/temporalViewPane/styles/temporalViewButtonPane.css");
	    
	    createButtons();
	    
	    setLeft(alignmentButtonPane);
	    setRight(zoomShowLinksButtonPane);
	    
	    createButtonActions(temporalChainTabPane);
		
	}

	private void createButtons() {
		
		meetsButton = new Button();
	    meetsButton.setId("meets-button");
	    meetsButton.setTooltip(new Tooltip(Language.translate("right.alignment")));
	    			
	    metByButton = new Button();
	    metByButton.setId("met-by-button");
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
        startsDelayButton.setId("starts-delay-button");
        startsDelayButton.setTooltip(new Tooltip(Language.translate("slave.begins.with.delay.when.master.starts")));
	       
        finishesDelayButton = new Button();
        finishesDelayButton.setId("finishes-delay-button");
        finishesDelayButton.setTooltip(new Tooltip(Language.translate("slave.finishes.t.after.master.end")));
       
        metByDelayButton= new Button();
        metByDelayButton.setId("met-by-delay-button");
        metByDelayButton.setTooltip(new Tooltip(Language.translate("slave.finishes.t.after.master.begin")));
        
        duringButton = new Button();
        duringButton.setId("during-button");
        duringButton.setTooltip(new Tooltip(Language.translate("media.contains.other")));
        
        overlapsButton = new Button();
        overlapsButton.setId("overlaps-button");
        overlapsButton.setTooltip(new Tooltip(Language.translate("slave.begins.with.delay.when.master.starts.and.finishes.t.after.master.end")));
       
        equalsButton = new Button();
        equalsButton.setId("equals-button");
        equalsButton.setTooltip(new Tooltip(Language.translate("all.has.same.duration")));
        
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
        alignmentButtonPane.getChildren().add(startsButton);
        alignmentButtonPane.getChildren().add(startsDelayButton);
        alignmentButtonPane.getChildren().add(finishesButton);
        alignmentButtonPane.getChildren().add(finishesDelayButton);
        alignmentButtonPane.getChildren().add(meetsButton);
        alignmentButtonPane.getChildren().add(metByButton);
        alignmentButtonPane.getChildren().add(metByDelayButton);
        alignmentButtonPane.getChildren().add(duringButton);
        alignmentButtonPane.getChildren().add(overlapsButton);
        alignmentButtonPane.getChildren().add(beforeButton);
        
	}
	
	private void createButtonActions(TabPane temporalChainTabPane){
		
		zoomButton.getSlider().valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
            	ScrollPane teste = (ScrollPane) temporalChainTabPane.getTabs().get(0).getContent();
            	TemporalChainPane teste2 = (TemporalChainPane) teste.getContent();
            	((NumberAxis) teste2.getXAxis()).setUpperBound(25);
            	//new_val.doubleValue()
            }
        });
		
	}
	
}
