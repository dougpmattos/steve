package view.common;

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
		slider.setMinWidth(sliderWitdh);
        slider.setMaxWidth(sliderWitdh);
		
		progressBar = new ProgressBar();
        progressBar.setMaxWidth(sliderWitdh);

        setSliderValue(def);
        
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
    			editableNumericValue.setPrefWidth(70);
    			editableNumericValue.setId("editable-numeric-value");
    			getChildren().add(editableNumericValue);
    		}
        	
        }
        
        createListeners(max);
		
	}

	private void createListeners(Double max) {
		
		if(max == 100.0 && editableNumericValue != null){
			
			slider.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
	                progressBar.setProgress(new_val.doubleValue() / max);
	                DecimalFormat fmt = new DecimalFormat("0.00");    
	                String formatedValue = fmt.format(new_val.doubleValue());
	                editableNumericValue.setText(formatedValue + " %");
	            }
	        });
	        
	        if(editableNumericValue != null){
	        	
	        	editableNumericValue.textProperty().addListener(new ChangeListener<String>() {

					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						
						String percentageValue = newValue;
						String value = "";
						if(percentageValue != null && percentageValue.indexOf("%") != -1){
							value = percentageValue.substring(0, percentageValue.indexOf("%"));
						} else{
							value = percentageValue;
						}
						value = value.replace(",", ".");

						slider.setValue(Double.parseDouble(value));
						progressBar.setProgress(Double.parseDouble(value) / max);
					
					}
	               
	            });
	        
	        }
			
		} else{
			
			slider.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
	            	progressBar.setProgress(new_val.doubleValue() / max);
	            }
	        });
			
		}
		
	}
	
	public void setSliderValue(Double value){
		
		slider.setValue(value);
		progressBar.setProgress(value / max);
		
		if(editableNumericValue != null && value == 0.0){
			
			if(max == 100.0){
				editableNumericValue.setText("0,00%");
			} else{
				editableNumericValue.setText("0,0");
			}
			
		} else if(editableNumericValue != null && value == 50.0){
			editableNumericValue.setText("50,00%");
		}
		
	}
	
	public Double getSliderValue(){
		return slider.getValue();
	}
	
	public Slider getSlider(){
		return slider;
	}
	
	public TextField getEditableNumericValue(){
		return editableNumericValue;
	}

}
