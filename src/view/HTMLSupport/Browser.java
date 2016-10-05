package view.HTMLSupport;

import java.io.File;

import javafx.application.Platform;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

class Browser extends Region {

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    
    public Browser(/*File htmlFile*/) {
        //apply the styles
        getStyleClass().add("browser");

        
        String url2 = WebView.class.getResource("HTMLExportado.html").toExternalForm();  
        webEngine.load(url2);
        
        getChildren().add(browser);
    }
}
/*
    // JavaScript interface object
    public class JavaApp {

        public void exit() {
            Platform.exit();
        }
    }
    
    @Override
    protected double computePrefWidth(double height) {
        return 750;
    }

    @Override
    protected double computePrefHeight(double width) {
        return 600;
    }
}*/