package view.spatialViewPane;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import model.HTMLSupport.HTMLExportEventHandler;
import model.NCLSupport.NCLExportEventHandler;
import model.common.SpatialTemporalView;
import model.temporalView.TemporalChain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.lang.reflect.Field;
import com.sun.webkit.*;

import view.common.Language;
import view.common.MessageDialog;
import view.common.ReturnMessage;
import view.stevePane.SteveMenuBar;
import view.temporalViewPane.TemporalChainPane;
import view.temporalViewPane.TemporalViewPane;
import br.uff.midiacom.ana.NCLDoc;

public class ControlButtonPane extends BorderPane{
	
	private Button fullScreen;
	private Button play;
	private Button stop;
	private Button previousScene;
	private Button nextScene;
	private Button refresh;
	private HBox fullButtonPane;
	private HBox centerButtonPane;
	private HBox refreshButtonPane;
	private StackPane screen;
	private TemporalChain temporalChainModel;
	private TemporalChainPane selectedTemporalChainPane;
	private TemporalViewPane temporalViewPane;
	private SpatialTemporalView spatialTemporalView;
	private SteveMenuBar steveMenuBar;
	private WebView webView;
	
	public ControlButtonPane(StackPane screen, TemporalViewPane temporalViewPane,SteveMenuBar steveMenuBar, SpatialTemporalView spatialTemporalView){
		
		setId("control-button-pane");
		this.steveMenuBar = steveMenuBar;
		this.screen = screen;
		this.temporalViewPane = temporalViewPane;
		this.spatialTemporalView = spatialTemporalView;
		this.webView = new WebView();
		
	    createButtons();
	  
	    setLeft(fullButtonPane);
		setCenter(centerButtonPane);
		setRight(refreshButtonPane);
				
		
		createButtonActions( screen, temporalViewPane);
		
		createListeners();
		

	}
	
	private void createListeners(){
		
		temporalViewPane.getTemporalChainTabPane().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				
				TemporalChainPane temporalChainPane = (TemporalChainPane) newValue.getContent();
				selectedTemporalChainPane = temporalChainPane;
				temporalChainModel = selectedTemporalChainPane.getTemporalChainModel();
				
			}
			
		});
		
				

	}
	
	public void createButtons(){
		
		fullScreen = new Button();
		fullScreen.setId("full-button");
		fullScreen.setTooltip(new Tooltip(Language.translate("full.screen")));
		
		play = new Button();
		play.setId("play-button");
		play.setTooltip(new Tooltip(Language.translate("play")));
		
		stop = new Button();
		//stop.setDisable(true);
		stop.setId("stop-button");
		stop.setTooltip(new Tooltip(Language.translate("stop")));
		
		previousScene = new Button();
		previousScene.setId("previous-scene-button");
		previousScene.setTooltip(new Tooltip(Language.translate("previous.scene")));
		
		nextScene = new Button();
		nextScene.setId("next-scene-button");
		nextScene.setTooltip(new Tooltip(Language.translate("next.scene")));
		
		refresh = new Button();
		refresh.setId("refresh-button");
		refresh.setTooltip(new Tooltip(Language.translate("refresh")));
		
		fullButtonPane = new HBox();
	    fullButtonPane.setId("full-pane");
	    fullButtonPane.getChildren().add(fullScreen);
		
		centerButtonPane = new HBox();
		centerButtonPane.setId("center-button-pane");
		centerButtonPane.getChildren().add(play);
		centerButtonPane.getChildren().add(stop);
		//centerButtonPane.getChildren().add(previousScene);
		//centerButtonPane.getChildren().add(nextScene);
		
		refreshButtonPane = new HBox();
		refreshButtonPane.setId("refresh-pane");
		//refreshButtonPane.getChildren().add(refresh);
		
	}
	
	public void runPreviewScreen(){
		String htmlPath = "";
    	webView.setVisible(true);
		//RunWindow runWindow = null;
		try {
			//runWindow = new RunWindow(createTempHTML());
			
	        WebEngine webEngine = webView.getEngine();
	        File f = createTempHTML();
	        webEngine.load("file:///" + f.getAbsolutePath());
	        
	        //webEngine.load("http://www.google.com");
	        if(!screen.getChildren().contains(webView)){
	        	screen.getChildren().add(webView);
	        } else {
	        	webView.setVisible(true);
	        }
	        webView.requestFocus();
			System.out.println("Foco? "+webView.focusedProperty());

			

			try { // Set the background color of the page to be transparent. 
	            Field field = webEngine.getClass().getDeclaredField("page"); 
	            field.setAccessible(true); 
	            WebPage page = (WebPage) field.get(webEngine);  
	            page.setBackgroundColor((new java.awt.Color(0, 0, 0, 0)).getRGB()); 
	        } catch (Exception e) { 
	            System.out.println("Error: " + e);
	        } 
//			
//			Scene scene = new Scene(screen);
//			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//runWindow.showAndWait();
    	
    	String mediadir = "src/view/HTMLSupport/media";
    	String htmlexportado = "src/view/HTMLSupport/HTMLExportado.html";
    	File m = new File (mediadir);
    	File h = new File (htmlexportado);

	}
	

	public void createButtonActions(StackPane screen, TemporalViewPane temporalViewPane){			
		
		/*play.setOnAction(new EventHandler<ActionEvent>() {			
		    @Override public void handle(ActionEvent t) {
		    	String htmlPath = "";
		    	webView.setVisible(true);
				//RunWindow runWindow = null;
				try {
					//runWindow = new RunWindow(createTempHTML());
					
			        WebEngine webEngine = webView.getEngine();
			        File f = createTempHTML();
			        webEngine.load("file:///" + f.getAbsolutePath());
			        
			        //webEngine.load("http://www.google.com");

					screen.getChildren().add(webView);
					webView.requestFocus();
					System.out.println("Foco? "+webView.focusedProperty());
//					Scene scene = new Scene(screen);
//					
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	//runWindow.showAndWait();
		    	
		    	String mediadir = "src/view/HTMLSupport/media";
		    	String htmlexportado = "src/view/HTMLSupport/HTMLExportado.html";
		    	File m = new File (mediadir);
		    	File h = new File (htmlexportado);
		    			    	
		    	
//		    	String[] entries = m.list();
//		    	for(String s: entries){
//		    	    File currentFile = new File(m.getPath(),s);
//		    	    currentFile.delete();
//		    	}
		    			    	
		    	//m.delete();		    	
		    	//h.delete();
		    }
		});*/
		
		stop.setOnAction(new EventHandler<ActionEvent>() {			
		    @Override public void handle(ActionEvent t) {		    	
		    	webView.setVisible(false);	
		    	selectedTemporalChainPane.resetPlayheadPosition();		    	
		    }
		});

	}
	public void hideWebView(){
		webView.setVisible(false);
		webView.disableProperty();
	}
	public Button getPlayButton(){
		return play;
	}
	public StackPane getScreen(){
		return screen;
	}
	
	public File createTempHTML() throws org.xml.sax.SAXException{
		final Logger logger = LoggerFactory.getLogger(HTMLExportEventHandler.class);
		final String EXPORTED_HTML_DOCUMENT = "Exported HTML Document";
		final String TEMP_NCL_DOCUMENT = "tempNCLDocument";
		
		final Document document;
		final NCLExportEventHandler nclExportEventHandler;
		
		nclExportEventHandler = new NCLExportEventHandler(this.spatialTemporalView);
		
		File tempNCLDocumentFile = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
        File stylesheet = new File("src/model/HTMLSupport/XSLFile/ncl4web.xsl");
        String tempNCLDocumentDir = "";
        File auxF = null;
        
        try {
        	NCLDoc nclDoc = nclExportEventHandler.exportToNCL(true);
        	String nclCode = nclDoc.parse(0);
        	if(nclCode != null){        	        	
        		File file = new File("src/view/HTMLSupport/");        		
 
        		if(file != null){
					tempNCLDocumentDir = file.getAbsolutePath();
					System.out.println("tempNCLDocumentDir: "+tempNCLDocumentDir);
					tempNCLDocumentFile = new File(tempNCLDocumentDir + ".ncl");
					System.out.println(tempNCLDocumentFile.getAbsolutePath());			
					FileWriter fileWriter = new FileWriter(tempNCLDocumentFile);
					fileWriter.write(nclCode);
                    fileWriter.close();

        		}
        		        		
			}
				
		} catch (Exception e) {
			
			//logger.error(e.getMessage());
			MessageDialog messageDialog = new MessageDialog(Language.translate("error.during.the.temp.ncl.document.creation"), 
					Language.translate("could.not.find.the.temp.ncl.document.directory") + ": " + e.getMessage(), "OK", 250);
	        messageDialog.showAndWait();
	        
	        return  auxF;
	        
		}
        
        try {
        	
            if(tempNCLDocumentFile != null){
            	
            	DocumentBuilder builder = factory.newDocumentBuilder();
                document = builder.parse(tempNCLDocumentFile);

                // Use a Transformer for output
                TransformerFactory tFactory = TransformerFactory.newInstance();
                StreamSource stylesource = new StreamSource(stylesheet);
                Transformer transformer = tFactory.newTransformer(stylesource);

                DOMSource source = new DOMSource(document);
                
                String exportedHTMLDocumentDir = tempNCLDocumentDir;
                
				String mediaDir = exportedHTMLDocumentDir + "/media";
				Boolean mediaDirCreated = (new File(mediaDir)).mkdirs();
				
				if (mediaDirCreated || (new File(mediaDir)).exists()) {
					nclExportEventHandler.copyMediaFiles(mediaDir);
				}
				
				File auxFile = new File("src/view/HTMLSupport/HTMLExportado.html");
				auxF= new File (auxFile.getAbsolutePath());
				FileWriter fileWriter = new FileWriter(auxFile);
                StreamResult result = new StreamResult();
                result.setWriter(fileWriter);
                transformer.transform(source, result);
                fileWriter.close();
                
                tempNCLDocumentFile.delete();
                
                ReturnMessage returnMessage = new ReturnMessage(Language.translate("html.export.is.ready"), 300);
                //returnMessage.show();
                //AnimationUtil.applyFadeInOut(returnMessage);
  
            }
        
        } catch (TransformerConfigurationException tce) {
            // Error generated by the parser
            System.out.println("\n** Transformer Factory error");
            System.out.println("   " + tce.getMessage());

            // Use the contained exception, if any
            Throwable x = tce;

            if (tce.getException() != null) {
                x = tce.getException();
            }

            x.printStackTrace();
        } catch (TransformerException te) {
            // Error generated by the parser
            System.out.println("\n** Transformation error");
            System.out.println("   " + te.getMessage());

            // Use the contained exception, if any
            Throwable x = te;

            if (te.getException() != null) {
                x = te.getException();
            }

            x.printStackTrace();
        } catch (ParserConfigurationException pce) {
            // Parser with specified options can't be built
            pce.printStackTrace();
        } catch (IOException ioe) {
            // I/O error
            ioe.printStackTrace();
        }
        return auxF;
	}
}
