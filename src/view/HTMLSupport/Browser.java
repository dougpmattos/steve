package view.HTMLSupport;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.JOptionPane;

class Browser extends Region {

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    
    public Browser(File filePath) {
        //apply the styles
    	
        getStyleClass().add("browser");
        System.out.println(filePath.getAbsolutePath());  
        browser.getEngine().load("file:///" + filePath.getAbsolutePath());
        //System.out.println(filePath.getAbsolutePath());
        getChildren().add(browser);
                
    }    

}
