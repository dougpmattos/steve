package view.utility;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import view.common.dialogs.ReturnMessage;

public class AnimationUtil {

	public static void applyFadeInOut(ReturnMessage returnMessage){
		
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.3), returnMessage.getScene().getRoot());
		fadeIn.setFromValue(0.0);
		fadeIn.setToValue(1.0);
		fadeIn.play();
		fadeIn.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.3), returnMessage.getScene().getRoot());
				fadeOut.setFromValue(1.0);
				fadeOut.setToValue(0.0);
				fadeOut.play();
				fadeOut.setOnFinished(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						
						returnMessage.close();
						
					}
				});
				
			}
		});
		
	}
	
}
