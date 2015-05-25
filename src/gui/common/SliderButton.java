package gui.common;

import java.text.DecimalFormat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class SliderButton extends HBox{

	private Label icon;
	private Slider slider;
	private ProgressBar progressBar;
	private StackPane sliderStackPane;
	private Double max;
	private Boolean hasEditableNumericValue;
	private TextField editableNumericValue;
	
	public SliderButton(Double min, Double max, Double def, Double sliderWitdh, Label icon, Boolean hasEditableNumericValue){

		setId("slider-button-container");
		
		this.icon = icon;
		this.max = max;
		this.hasEditableNumericValue = hasEditableNumericValue;
		
		slider = new Slider();
		slider.setMin(min);
		slider.setMax(max);
		slider.setValue(def);
		slider.setMinWidth(sliderWitdh);
        slider.setMaxWidth(sliderWitdh);
		
		progressBar = new ProgressBar();
        progressBar.setMaxWidth(sliderWitdh);

        sliderStackPane = new StackPane();
        sliderStackPane.getChildren().addAll(progressBar, slider);
        
        if(icon != null){
        	
        	HBox iconSliderContainer = new HBox();
        	iconSliderContainer.setSpacing(5);
        	iconSliderContainer.getChildren().add(icon);
        	iconSliderContainer.getChildren().add(sliderStackPane);
        	getChildren().add(iconSliderContainer);
        	
        } else{
        	
        	getChildren().add(sliderStackPane);
        	
        	if(hasEditableNumericValue){
    			editableNumericValue = new TextField();
    			editableNumericValue.setId("editable-numeric-value");
    			getChildren().add(editableNumericValue);
    		}
        	
        }
        
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                progressBar.setProgress(new_val.doubleValue() / max);
                DecimalFormat fmt = new DecimalFormat("0.00");   //limita o número de casas decimais      
                String formatedValue = fmt.format(new_val.doubleValue());
                editableNumericValue.setText(formatedValue + " %");
            }
        });
        
		
	}
	
	public void setSliderValue(Double value){
		slider.setValue(value);
		progressBar.setProgress(value / max);
	}
	
	public Double getSliderValue(){
		return slider.getValue();
	}
	
	public Slider getSlider(){
		return slider;
	}

}
