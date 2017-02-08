package view.stevePane;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
import model.NCLSupport.NCLImportEventHandler;
import model.common.SpatialTemporalView;
import model.repository.RepositoryMediaList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import com.sun.xml.internal.txw2.Document;
import org.w3c.dom.Document;

import view.HTMLSupport.RunWindow;
import view.common.Language;
import view.common.MessageDialog;
import view.common.ReturnMessage;
import view.temporalViewPane.InteractiveMediaWindow;
import view.temporalViewPane.TemporalChainPane;
import view.temporalViewPane.TemporalViewPane;
import view.temporalViewPane.TimeLineXYChartData;
import view.utility.AnimationUtil;
import br.uff.midiacom.ana.NCLDoc;
import br.uff.midiacom.ana.util.exception.XMLException;
import controller.Controller;

@SuppressWarnings({"rawtypes","unchecked"})
public class SteveMenuBar extends MenuBar{

	final Logger logger = LoggerFactory.getLogger(SteveMenuBar.class);

	private Controller controller;
	private SpatialTemporalView spatialTemporalView;
	private RepositoryMediaList repositoryMediaList;
	private TemporalViewPane temporalViewPane;
	private Stage stage;
	
	private Menu menuFile;
	private Menu menuEdit;
	private Menu menuView;
	private Menu menuTools;
	private Menu menuHelp;
	
	private  MenuItem menuItemNew;
	private  MenuItem menuItemOpen;
	private  MenuItem menuItemClose; 
	private  MenuItem menuItemSave;
	private  MenuItem menuItemImportNCL;
	private  MenuItem menuItemExportNCL;
	private  MenuItem menuItemExportHTML5;
	private  MenuItem menuItemRun; 
	private  MenuItem menuItemExit;
	private  MenuItem menuItemUndo;
	private  MenuItem menuItemRedo;
	private  MenuItem menuItemCut;
	private  MenuItem menuItemCopy;
	private  MenuItem menuItemPaste; 
	private  MenuItem menuItemDelete; 
	private  MenuItem menuItemPreferences;
	private  MenuItem menuItemSelectAll;
	private  MenuItem menuItemSimulation;
	private  MenuItem menuItemHelpContents;
	private  MenuItem menuItemAbout;
	
	
	private  CheckMenuItem checkMenuItemMediaView;  
	private  CheckMenuItem checkMenuItemTemporalView;       
	private  CheckMenuItem checkMenuItemSpatialView; 
	private CheckMenuItem checkMenuItemShowRelations;
	
	public SteveMenuBar(Controller controller, SpatialTemporalView spatialTemporalView, RepositoryMediaList repositoryMediaList, TemporalViewPane temporalViewPane, Stage stage){
		
		this.controller = controller;
		this.spatialTemporalView = spatialTemporalView;
		this.repositoryMediaList = repositoryMediaList;
		this.temporalViewPane = temporalViewPane;
		this.stage = stage;
		
		createMenu();
		
		createFileMenuItems();
		createEditMenuItems();
		createViewMenuItems();
		createToolMenuItems();
		createHelpMenuItems();
		
		createFileMenuItemActions();
		createEditMenuItemActions();
		createViewMenuItemActions();
		createToolMenuItemActions();
		createHelpMenuItemActions();
		
		menuFile.getItems().addAll(menuItemNew, menuItemOpen, new SeparatorMenuItem(), menuItemClose, new SeparatorMenuItem(), menuItemExportNCL, new SeparatorMenuItem(), menuItemExportHTML5, new SeparatorMenuItem(), menuItemRun, new SeparatorMenuItem(), menuItemExit); 
		//menuFile.getItems().addAll(menuItemExportNCL, new SeparatorMenuItem(), menuItemExportHTML5, new SeparatorMenuItem(), menuItemExit);
//		menuEdit.getItems().addAll(menuItemUndo, menuItemRedo, new SeparatorMenuItem(), menuItemCut, menuItemCopy, menuItemPaste, new SeparatorMenuItem(), 
		menuEdit.getItems().addAll(menuItemPreferences, new SeparatorMenuItem(), menuItemSelectAll);
		menuView.getItems().addAll(checkMenuItemMediaView, checkMenuItemTemporalView, checkMenuItemSpatialView, checkMenuItemShowRelations);
		menuTools.getItems().addAll(menuItemSimulation);
//		menuHelp.getItems().addAll(menuItemHelpContents, new SeparatorMenuItem(), menuItemAbout);
		menuHelp.getItems().addAll(menuItemAbout);
		    
//		getMenus().addAll(menuFile, menuEdit, menuView, menuTools, menuHelp);
		getMenus().addAll(menuFile, menuEdit, menuHelp);
		
		
	}

	private void createHelpMenuItemActions(){
		
		menuItemHelpContents.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});
		
		menuItemAbout.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
		    	
		    	MessageDialog messageDialog = new MessageDialog("STEVE", "Spatio-Temporal View Editor              Version 1.0-0                   "
		    			+ "Copyright 2015 Douglas Paulo de Mattos. MidiaCom Lab. UFF. All rights reserved.                     "
		    			+ "This product includes software developed by other MidiaCom Lab projects including aNa and NCL4WEB, https://www.aNa.com.br, https://www.NCL4WEB.com.br", "OK", 300);
	            messageDialog.showAndWait();
	            
		    }
		});
		
	}
	
	private void createToolMenuItemActions(){
		
		menuItemSimulation.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});
		
	}
	
	private void createViewMenuItemActions(){
		
		checkMenuItemMediaView.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});
		
		checkMenuItemTemporalView.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});
		
		checkMenuItemSpatialView.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});
		
		checkMenuItemShowRelations.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});   
		
	}
	
	private void createEditMenuItemActions(){
		
		menuItemUndo.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});
		
		menuItemRedo.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});
		
		menuItemCut.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});
		
		menuItemCopy.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});
		
		menuItemPaste.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});
		
		menuItemDelete.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});
		
		menuItemPreferences.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t){
				
				/*
				 * private BorderPane containerBorderPane = new BorderPane();
    
					public StevePane(Controller controller, RepositoryMediaList repositoryMediaList, SpatialTemporalView temporalView) throws XMLException, IOException  {
						
						super(new BorderPane());
						setRoot(containerBorderPane);
						getStylesheets().add("view/stevePane/styles/stevePane.css");
						containerBorderPane.setPrefSize(STEVE_WITDH, STEVE_HEIGHT);
				 */
				//TODO EXIBIR JANELA DE PREFERENCIAS PARA MAPEAR AS TECLAS
				PreferencesWindow preferencesWindow = new PreferencesWindow();
	    		
		    	preferencesWindow.showAndWait();
		    	
						        

			}
		});
		menuItemSelectAll.setOnAction(new EventHandler<ActionEvent>() {
			
		    public void handle(ActionEvent t) {

		    	Tab selectedTab = null;
		    	TemporalChainPane temporalChainPane = null;
		    	
		    	for (Tab tab : temporalViewPane.getTemporalChainTabPane().getTabs()){
		    		if(tab.isSelected()){
		    			selectedTab = tab;
		    			break;
		    		}
		    	}
		    	if(selectedTab != null){
		    		temporalChainPane = (TemporalChainPane) selectedTab.getContent();
		    	}

		    	if(temporalChainPane != null){
		    		
		    		for(ArrayList<TimeLineXYChartData> timeLineXYChartDataList : temporalChainPane.getTimeLineXYChartDataLineList()){
			    		for(TimeLineXYChartData timeLineXYChartData : timeLineXYChartDataList){
							
							temporalViewPane.addSelectedMedia(timeLineXYChartData.getMedia());
							if(timeLineXYChartData.getContainerNode().getStylesheets().isEmpty()){
								timeLineXYChartData.getContainerNode().requestFocus();
								timeLineXYChartData.getContainerNode().getStylesheets().add("view/temporalViewPane/styles/mousePressedTemporalMediaNode.css");
								Rectangle mediaImageClip = (Rectangle) timeLineXYChartData.getContainerNode().getChildren().get(0).getClip();
								mediaImageClip.setHeight(mediaImageClip.getHeight()-5);
							}
							
						}
			    	}
		    		
		    	}
	
		    }
		});

	}

	private void createFileMenuItemActions(){
		
		menuItemOpen.setOnAction(new OpenEventHandler(controller, spatialTemporalView, repositoryMediaList));
		
		menuItemClose.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});
		
		menuItemNew.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   try {
				new Controller(new RepositoryMediaList(), new SpatialTemporalView(), stage);
			} catch (XMLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    }
		});
		
		
		
		menuItemSave.setOnAction(new SaveEventHandler(spatialTemporalView, repositoryMediaList));
		
		menuItemImportNCL.setOnAction(new NCLImportEventHandler());
		
		menuItemExportNCL.setOnAction(new NCLExportEventHandler(spatialTemporalView));
		
		menuItemExportHTML5.setOnAction(new HTMLExportEventHandler(spatialTemporalView));
		
		menuItemRun.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
		    	
		    	String htmlPath = "";
		    					
				RunWindow runWindow = null;
				try {
					runWindow = new RunWindow(createTempHTML());
				} catch (org.xml.sax.SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	runWindow.showAndWait();
		    	
		    	String mediadir = "src/view/HTMLSupport/media";
		    	String htmlexportado = "src/view/HTMLSupport/HTMLExportado.html";
		    	File m = new File (mediadir);
		    	File h = new File (htmlexportado);
		    			    	
		    	
		    	String[] entries = m.list();
		    	for(String s: entries){
		    	    File currentFile = new File(m.getPath(),s);
		    	    currentFile.delete();
		    	}
		    			    	
		    	m.delete();		    	
		    	h.delete();
		    }
		});
				
		
		
		menuItemExit.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   stage.close();
		    }
		});
		
	}
	
	private void createHelpMenuItems() {
		
		menuItemHelpContents= new MenuItem (Language.translate("help.contents"));
		menuItemAbout= new MenuItem (Language.translate("about"));
		
	}

	private void createToolMenuItems() {

		menuItemSimulation= new MenuItem (Language.translate("simulation"));
		
	}

	private void createViewMenuItems() {
		
		checkMenuItemMediaView = new CheckMenuItem (Language.translate("media.repository.view"), null);
		checkMenuItemMediaView.setSelected(true);
		
		checkMenuItemTemporalView = new CheckMenuItem (Language.translate("temporal.view"), null);
		checkMenuItemTemporalView.setSelected(true);
		
		checkMenuItemSpatialView = new CheckMenuItem (Language.translate("spatial.view"), null);
		checkMenuItemSpatialView.setSelected(true);
		
		checkMenuItemShowRelations = new CheckMenuItem (Language.translate("show.relations"), null);
		checkMenuItemShowRelations.setSelected(true);
		
	}

	private void createEditMenuItems() {
		
		menuItemUndo= new MenuItem (Language.translate("undo"));
		menuItemUndo.setAccelerator(KeyCombination.keyCombination("Ctrl+Z"));
		
		menuItemRedo = new MenuItem (Language.translate("redo"));
		menuItemRedo.setAccelerator(KeyCombination.keyCombination("Ctrl+Y"));
		
		menuItemCut = new MenuItem (Language.translate("cut"));
		menuItemCut.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
		
		menuItemCopy = new MenuItem (Language.translate("copy"));
		menuItemCopy.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
		
		menuItemPaste = new MenuItem (Language.translate("paste"));
		menuItemPaste.setAccelerator(KeyCombination.keyCombination("Ctrl+V"));
		
		menuItemDelete = new MenuItem (Language.translate("delete"));
		menuItemDelete.setAccelerator(KeyCombination.keyCombination("Delete"));
		
		menuItemPreferences = new MenuItem (Language.translate("preferences"));
		menuItemPreferences.setAccelerator(KeyCombination.keyCombination("⌘,"));
		
		menuItemSelectAll = new MenuItem (Language.translate("select.all"));
		menuItemSelectAll.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
		
	}

	private void createFileMenuItems() {
		
		menuItemNew = new MenuItem (Language.translate("new.project"));
		menuItemNew.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
		
		menuItemOpen = new MenuItem (Language.translate("open.project"));
		menuItemOpen.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
		
		menuItemClose = new MenuItem (Language.translate("close"));
		menuItemClose.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
		
		menuItemSave = new MenuItem (Language.translate("save.project"));
		menuItemSave.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
		
		menuItemImportNCL = new MenuItem (Language.translate("import.ncl.document"));
		menuItemImportNCL.setAccelerator(KeyCombination.keyCombination("Ctrl+I"));
		
		menuItemExportNCL = new MenuItem (Language.translate("export.ncl.document"));
		menuItemExportNCL.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
		
		menuItemExportHTML5 = new MenuItem (Language.translate("export.html5.document"));
		menuItemExportHTML5.setAccelerator(KeyCombination.keyCombination("Ctrl+H"));
		
		menuItemRun = new MenuItem (Language.translate("run.application"));
		menuItemRun.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
		
		menuItemExit = new MenuItem (Language.translate("exit"));
		menuItemExit.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
		
	}
	
	private void createMenu() {
		
		menuFile = new Menu(Language.translate("file"));
		menuEdit = new Menu(Language.translate("edit"));
		menuView = new Menu(Language.translate("view"));
		menuTools = new Menu(Language.translate("tools"));
		menuHelp = new Menu(Language.translate("help"));
		
	}	
	
	private File createTempHTML() throws org.xml.sax.SAXException{
		final Logger logger = LoggerFactory.getLogger(HTMLExportEventHandler.class);
		
		final String EXPORTED_HTML_DOCUMENT = "Exported HTML Document";
		final String TEMP_NCL_DOCUMENT = "tempNCLDocument";
		
		final Document document;
		final NCLExportEventHandler nclExportEventHandler;
		
		nclExportEventHandler = new NCLExportEventHandler(spatialTemporalView);
		
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
			
			logger.error(e.getMessage());
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
                returnMessage.show();
                AnimationUtil.applyFadeInOut(returnMessage);
  
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

