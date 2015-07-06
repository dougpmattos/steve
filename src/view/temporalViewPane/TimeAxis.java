package view.temporalViewPane;

import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

public class TimeAxis extends HBox{

	public TimeAxis(){
		final Slider slider1 = new Slider();
		slider1.setMin(1);
		slider1.setMax(50);
		slider1.setValue(15);
		slider1.setShowTickLabels(true);
		slider1.setShowTickMarks(true);
		slider1.setMajorTickUnit(5);
		slider1.setMinorTickCount(0);
		slider1.setBlockIncrement(1);
		slider1.setLayoutX(20);
		slider1.setLayoutY(200);

		NumberAxis xAxis = new NumberAxis();
    	xAxis.setAutoRanging(true);
    	xAxis.setUpperBound(1000);
		
		getChildren().add(slider1);
	}
	
}
