package gui.common;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class SliderButton extends BorderPane{

	private static final int SLIDER_WIDTH = 200;
	private static final int DEFAULT = 100;
	private static final int MAX = 200;
	private static final int MIN = 0;

	private Label icon;
	private Slider slider;
	private ProgressBar progressBar;
	private StackPane sliderStackPane;
	private HBox sliderContainer;
	
	public SliderButton(){
		
		setId("zoom-button");
		
		icon = new Label();
		icon.setId("zoom-icon");
		
		slider = new Slider();
		slider.setMin(MIN);
		slider.setMax(MAX);
		slider.setValue(DEFAULT);
		slider.setMinWidth(SLIDER_WIDTH);
        slider.setMaxWidth(SLIDER_WIDTH);
		slider.setShowTickLabels(false);
		slider.setShowTickMarks(false);
		slider.setMajorTickUnit(25);
		slider.setBlockIncrement(25);
		
		progressBar = new ProgressBar(0);
		progressBar.setProgress(0.5);
        progressBar.setMinWidth(SLIDER_WIDTH);
        progressBar.setMaxWidth(SLIDER_WIDTH);

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                progressBar.setProgress(new_val.doubleValue() / MAX);
            }
        });
 
        sliderStackPane = new StackPane();
        sliderStackPane.getChildren().addAll(progressBar, slider);

        sliderContainer = new HBox();
        sliderContainer.setId("slider-container");
        sliderContainer.setSpacing(5);
        sliderContainer.setAlignment(Pos.CENTER);
        sliderContainer.getChildren().addAll(sliderStackPane);

		setLeft(icon);
		setCenter(sliderContainer);
		
	}
	
	public void setSliderValue(Double value){
		slider.setValue(value);
	}
	
	public Double getSliderValue(){
		return slider.getValue();
	}
	
	public Slider getSlider(){
		return slider;
	}

}
