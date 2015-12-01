package samples;


import javafx.application.Application;
import javafx.stage.Stage;

public class MessageDialogSample extends Application {
 
    @Override public void start(Stage stage) {
    	
//    	MessageDialog messageDialog = new MessageDialog("It's not possible to define this alignment", 
//				"Please select another media as master of the alignment.", "OK", 190);
//    	messageDialog.showAndWait();
//    	
//    	InputDialog i = new InputDialog("It's not possible to define this alignment",null, "cancel","ok", "Delay in seconds: ", 190);
//    	System.out.println(i.showAndWaitAndReturn());
    	
//    	InputDialog showBlockedRelationInputDialog = new InputDialog("It's not possible to define this alignment for this media as slave: " + "Imagen 1", 
//    			"Would you like to continue defining this alignment?", "yes","no", null, 210);
//    	System.out.println(showBlockedRelationInputDialog.showAndWaitAndReturn());
    	
    	InteractiveMediaWindow interactiveMediaWindow = new InteractiveMediaWindow(firstSelectedMedia, mediaListDuringInteractivityTime);
    	interactiveMediaWindow.showAndWait();
    	
    }
    
    public static void main(String[] args) {
        launch(args);
    }
	
}
