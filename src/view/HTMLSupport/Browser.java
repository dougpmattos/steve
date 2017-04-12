package view.HTMLSupport;

import java.io.File;
import java.lang.reflect.Field;

import com.sun.webkit.WebPage;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.JOptionPane;

class Browser extends StackPane {


    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    
    public Browser(File filePath) {
        //apply the styles
    	
    	try { // Set the background color of the page to be transparent. 
            Field field = webEngine.getClass().getDeclaredField("page"); 
            field.setAccessible(true); 
            WebPage page = (WebPage) field.get(webEngine);  
            page.setBackgroundColor((new java.awt.Color(0, 0, 0, 0)).getRGB()); 
            page.setBounds(0, 0, 1024, 768);
        } catch (Exception e) { 
            System.out.println("Error: " + e);
        } 
    	
        System.out.println(filePath.getAbsolutePath());  
        browser.getEngine().load("file:///" + filePath.getAbsolutePath());
        getChildren().add(browser);
        
                
    }    

}
