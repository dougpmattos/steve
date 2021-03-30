package view.HTMLSupport;

import java.io.File;
import java.lang.reflect.Field;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.w3c.dom.Document;

public class Browser extends StackPane {

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();

    public Browser(File filePath) {
//
//
//    	setStyle("-fx-background-color: #000000;");
//    	webEngine.documentProperty().addListener(new ChangeListener<Document>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Document> observable,
//					Document oldValue, Document newValue) {
//
//				try {
//
//		            Field field = webEngine.getClass().getDeclaredField("page");
//		            field.setAccessible(true);
//		            WebPage page = (WebPage) field.get(webEngine);
//		            page.setBackgroundColor((new java.awt.Color(0, 0, 0, 0)).getRGB());
//		        } catch (Exception e) {
//		            System.out.println("Error: " + e);
//		        }
//
//			}
//
//		});
//
//        browser.getEngine().load("file:///" + filePath.getAbsolutePath());
//        getChildren().add(browser);
//
//
    }

}
