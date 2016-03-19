package view.stevePane;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCombination;
import javafx.scene.shape.Rectangle;
import model.HTMLSupport.HTMLExportEventHandler;
import model.NCLSupport.NCLExportEventHandler;
import model.NCLSupport.NCLImportEventHandler;
import model.common.SpatialTemporalView;
import model.repository.RepositoryMediaList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import view.common.Language;
import view.common.MessageDialog;
import view.temporalViewPane.TemporalChainPane;
import view.temporalViewPane.TemporalViewPane;
import view.temporalViewPane.TimeLineXYChartData;
import controller.Controller;

@SuppressWarnings({"rawtypes","unchecked"})
public class SteveMenuBar extends MenuBar{

	final Logger logger = LoggerFactory.getLogger(SteveMenuBar.class);

	private Controller controller;
	private SpatialTemporalView spatialTemporalView;
	private RepositoryMediaList repositoryMediaList;
	private TemporalViewPane temporalViewPane;
	
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
	private  MenuItem menuItemExit;
	private  MenuItem menuItemUndo;
	private  MenuItem menuItemRedo;
	private  MenuItem menuItemCut;
	private  MenuItem menuItemCopy;
	private  MenuItem menuItemPaste; 
	private  MenuItem menuItemDelete; 
	private  MenuItem menuItemSelectAll;
	private  MenuItem menuItemSimulation;
	private  MenuItem menuItemHelpContents;
	private  MenuItem menuItemAbout;
	
	private  CheckMenuItem checkMenuItemMediaView;  
	private  CheckMenuItem checkMenuItemTemporalView;       
	private  CheckMenuItem checkMenuItemSpatialView; 
	private CheckMenuItem checkMenuItemShowRelations;
	
	public SteveMenuBar(Controller controller, SpatialTemporalView spatialTemporalView, RepositoryMediaList repositoryMediaList, TemporalViewPane temporalViewPane){
		
		this.controller = controller;
		this.spatialTemporalView = spatialTemporalView;
		this.repositoryMediaList = repositoryMediaList;
		this.temporalViewPane = temporalViewPane;
		
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
		
		menuFile.getItems().addAll(menuItemNew, menuItemOpen, new SeparatorMenuItem(), menuItemClose, new SeparatorMenuItem(), 
								   menuItemSave, new SeparatorMenuItem(), menuItemImportNCL, menuItemExportNCL, new SeparatorMenuItem(), menuItemExportHTML5, new SeparatorMenuItem(), menuItemExit);
		menuEdit.getItems().addAll(menuItemUndo, menuItemRedo, new SeparatorMenuItem(), menuItemCut, menuItemCopy, menuItemPaste, new SeparatorMenuItem(), 
								   menuItemDelete, menuItemSelectAll);
		menuView.getItems().addAll(checkMenuItemMediaView, checkMenuItemTemporalView, checkMenuItemSpatialView, checkMenuItemShowRelations);
		menuTools.getItems().addAll(menuItemSimulation);
		menuHelp.getItems().addAll(menuItemHelpContents, new SeparatorMenuItem(), menuItemAbout);
		    
		getMenus().addAll(menuFile, menuEdit, menuView, menuTools, menuHelp);
		
		
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
		
		menuItemSave.setOnAction(new SaveEventHandler(spatialTemporalView, repositoryMediaList));
		
		menuItemImportNCL.setOnAction(new NCLImportEventHandler());
		
		menuItemExportNCL.setOnAction(new NCLExportEventHandler(spatialTemporalView));
		
		menuItemExportHTML5.setOnAction(new HTMLExportEventHandler(spatialTemporalView));
		
		menuItemExit.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
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

}
