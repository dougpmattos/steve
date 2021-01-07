package view.stevePane;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import model.HTMLSupport.HTMLExportEventHandler;
import model.NCLSupport.NCLExportEventHandler;
import model.NCLSupport.NCLImportEventHandler;
import model.common.SpatialTemporalApplication;
import model.repository.RepositoryMediaList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import com.sun.xml.internal.txw2.Document;

import model.common.CommonMethods;
import view.common.Language;
import view.common.dialogs.InputDialog;
import view.common.dialogs.MessageDialog;
import view.temporalViewPane.TemporalChainPane;
import view.temporalViewPane.TemporalViewPane;
import view.temporalViewPane.TimeLineXYChartData;
import br.uff.midiacom.ana.util.exception.XMLException;
import controller.ApplicationController;

@SuppressWarnings({"rawtypes","unchecked"})
public class SteveMenuBar extends MenuBar{

	final Logger logger = LoggerFactory.getLogger(SteveMenuBar.class);

	private ApplicationController applicationController;
	private SpatialTemporalApplication spatialTemporalApplication;
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
	
	public SteveMenuBar(ApplicationController applicationController, SpatialTemporalApplication spatialTemporalApplication, RepositoryMediaList repositoryMediaList, TemporalViewPane temporalViewPane, Stage stage){
		
		this.applicationController = applicationController;
		this.spatialTemporalApplication = spatialTemporalApplication;
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
		
		//menuFile.getItems().addAll(menuItemNew, menuItemOpen, new SeparatorMenuItem(), menuItemClose, new SeparatorMenuItem(), menuItemExportNCL, new SeparatorMenuItem(), menuItemExportHTML5, new SeparatorMenuItem(), menuItemRun, new SeparatorMenuItem(), menuItemExit); 
		menuFile.getItems().addAll(menuItemNew, menuItemOpen, new SeparatorMenuItem(), menuItemSave, new SeparatorMenuItem(), menuItemExportNCL, menuItemExportHTML5, new SeparatorMenuItem(), menuItemRun, new SeparatorMenuItem(), menuItemExit);
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

				InputDialog inputDialog = new InputDialog("STEVE", "Spatio-Temporal View Editor - Version 3.0-0" + "\n\n"
		    			+ "Copyright 2021. Douglas Paulo de Mattos. MidiaCom Lab-UFF." + "\n\n"
		    			+ "All rights reserved." + "\n\n"
		    			+ "This product includes software developed by other MidiaCom Lab projects (aNa and NCL4WEB)." + "\n\n"
		    			+ "https://www.aNa.com.br" + "\n"
		    			+ "https://www.NCL4WEB.com.br", "OK",null, null, 320);
				inputDialog.showAndWait();
	            
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
				PreferencesWindow preferencesWindow = new PreferencesWindow(applicationController);
	    		
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

					temporalViewPane.clearSelectedNodeList();
		    		
		    		for(ArrayList<TimeLineXYChartData> timeLineXYChartDataList : temporalChainPane.getTimeLineXYChartDataLineList()){
			    		for(TimeLineXYChartData timeLineXYChartData : timeLineXYChartDataList){
							
							temporalViewPane.addSelectedNode(timeLineXYChartData.getNode());
							if(timeLineXYChartData.getContainerNode().getStylesheets().isEmpty()){
								timeLineXYChartData.getContainerNode().requestFocus();
								timeLineXYChartData.getContainerNode().getStylesheets().add("styles/temporalViewPane/mousePressedTemporalMediaNode.css");
							}
							
						}
			    	}
		    		
		    	}
	
		    }
		});

	}

	private void createFileMenuItemActions(){
		
		menuItemOpen.setOnAction(new OpenEventHandler(applicationController, spatialTemporalApplication, repositoryMediaList));
		
		menuItemClose.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});
		
		menuItemNew.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   try {
			   	ApplicationController.getInstance().createNewProject(stage);
			} catch (XMLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    }
		});
		
		menuItemSave.setOnAction(new SaveEventHandler(spatialTemporalApplication, repositoryMediaList));
		
		menuItemImportNCL.setOnAction(new NCLImportEventHandler());
		
		menuItemExportNCL.setOnAction(new NCLExportEventHandler(spatialTemporalApplication));
		
		menuItemExportHTML5.setOnAction(new HTMLExportEventHandler(spatialTemporalApplication));
		
		menuItemRun.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
		    	CommonMethods.runApplication(spatialTemporalApplication);
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
		menuItemAbout.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.SHIFT_DOWN));
		
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
		
		checkMenuItemShowRelations = new CheckMenuItem (Language.translate("show.nodes.linked"), null);
		checkMenuItemShowRelations.setSelected(true);
		
	}

	private void createEditMenuItems() {
		
		menuItemUndo= new MenuItem (Language.translate("undo"));
		menuItemUndo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
		
		menuItemRedo = new MenuItem (Language.translate("redo"));
		menuItemRedo.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
		
		menuItemCut = new MenuItem (Language.translate("cut"));
		menuItemCut.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
		
		menuItemCopy = new MenuItem (Language.translate("copy"));
		menuItemCopy.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
		
		menuItemPaste = new MenuItem (Language.translate("paste"));
		menuItemPaste.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));
		
		menuItemDelete = new MenuItem (Language.translate("delete"));
		menuItemDelete.setAccelerator(new KeyCodeCombination(KeyCode.DELETE, KeyCombination.CONTROL_DOWN));
		
		menuItemPreferences = new MenuItem (Language.translate("preferences"));
		menuItemPreferences.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
		
		menuItemSelectAll = new MenuItem (Language.translate("select.all"));
		menuItemSelectAll.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));
		
	}

	private void createFileMenuItems() {
		
		menuItemNew = new MenuItem (Language.translate("new.project"));
		menuItemNew.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		
		menuItemOpen = new MenuItem (Language.translate("open.project"));
		menuItemOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
		
		menuItemClose = new MenuItem (Language.translate("close"));
		menuItemClose.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN));
		
		menuItemSave = new MenuItem (Language.translate("save.project"));
		menuItemSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		
		menuItemImportNCL = new MenuItem (Language.translate("import.ncl.document"));
		menuItemImportNCL.setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN));
		
		menuItemExportNCL = new MenuItem (Language.translate("export.ncl.document"));
		menuItemExportNCL.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
		
		menuItemExportHTML5 = new MenuItem (Language.translate("export.html5.document"));
		menuItemExportHTML5.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));
		
		menuItemRun = new MenuItem (Language.translate("run.application"));
		menuItemRun.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));
		
		menuItemExit = new MenuItem (Language.translate("exit"));
		menuItemExit.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
		
	}
	
	private void createMenu() {
		
		menuFile = new Menu(Language.translate("file"));
		menuEdit = new Menu(Language.translate("edit"));
		menuView = new Menu(Language.translate("view"));
		menuTools = new Menu(Language.translate("tools"));
		menuHelp = new Menu(Language.translate("help"));
		
	}	

}

