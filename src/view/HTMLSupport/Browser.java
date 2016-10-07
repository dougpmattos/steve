package view.HTMLSupport;

import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

class Browser extends Region {

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    
    public Browser(/*File htmlFile*/) {
        //apply the styles
        getStyleClass().add("browser");
        
        ///Users/bruno/git/steve/src/view/HTMLSupport

        //File file = new File("HTMLExportado.html");
        String url2 = Browser.class.getResource("HTMLExportado.html").toExternalForm(); 
        //String url2 = "/Users/bruno/git/steve/src/view/HTMLSupport/HTMLExportado.html";
        webEngine.load(url2);
        
        getChildren().add(browser);
    }
}
