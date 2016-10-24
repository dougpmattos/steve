package view.HTMLSupport;

import java.io.File;

import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

class Browser extends Region {

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    
    public Browser(File filePath) {
        //apply the styles
        getStyleClass().add("browser");
                        
        browser.getEngine().load("file:///" + filePath.getAbsolutePath());
        getChildren().add(browser);
    }
}
