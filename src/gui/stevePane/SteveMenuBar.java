package gui.stevePane;

import gui.common.Language;
import gui.spatialViewPane.SpatialViewPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import model.repository.RepositoryMediaList;
import model.temporalView.TemporalView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;

@SuppressWarnings({"rawtypes","unchecked"})
public class SteveMenuBar extends MenuBar{

	final Logger logger = LoggerFactory.getLogger(SteveMenuBar.class);

	private Controller controller;
	private TemporalView temporalView;
	private RepositoryMediaList repositoryMediaList;
	
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
	private  MenuItem menuItemExit;
	private  MenuItem menuItemUndo;
	private  MenuItem menuItemRedo;
	private  MenuItem menuItemCut;
	private  MenuItem menuItemCopy;
	private  MenuItem menuItemPaste; 
	private  MenuItem menuItemDelete; 
	private  MenuItem menuItemSelectAll;
	private  MenuItem menuItemNCL4WEB;
	private  MenuItem menuItemSimulation;
	private  MenuItem menuItemHelpContents;
	private  MenuItem menuItemAbout;
	
	private  CheckMenuItem checkMenuItemMediaView;  
	private  CheckMenuItem checkMenuItemTemporalView;       
	private  CheckMenuItem checkMenuItemSpatialView; 
	
	public SteveMenuBar(Controller controller, TemporalView temporalView, RepositoryMediaList repositoryMediaList){
		
		this.controller = controller;
		this.temporalView = temporalView;
		this.repositoryMediaList = repositoryMediaList;
		
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
								   menuItemSave, new SeparatorMenuItem(), menuItemImportNCL, menuItemExportNCL, new SeparatorMenuItem(), menuItemExit);
		menuEdit.getItems().addAll(menuItemUndo, menuItemRedo, new SeparatorMenuItem(), menuItemCut, menuItemCopy, menuItemPaste, new SeparatorMenuItem(), 
								   menuItemDelete, menuItemSelectAll);
		menuView.getItems().addAll(checkMenuItemMediaView, checkMenuItemTemporalView, checkMenuItemSpatialView);
		menuTools.getItems().addAll(menuItemNCL4WEB, menuItemSimulation);
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
			   //TODO
		    }
		});
		
	}
	
	private void createToolMenuItemActions(){
		
		menuItemNCL4WEB.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});
		
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
			   //TODO
		    }
		});

	}

	private void createFileMenuItemActions(){
		
		menuItemOpen.setOnAction(new OpenEventHandler(controller, temporalView, repositoryMediaList));
		
		menuItemClose.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});
		
		menuItemSave.setOnAction(new SaveEventHandler(temporalView, repositoryMediaList));
		
		menuItemImportNCL.setOnAction(new NCLImportEventHandler());
		
		menuItemExportNCL.setOnAction(new NCLExportEventHandler(temporalView));
		
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
		
		menuItemNCL4WEB= new MenuItem (Language.translate("ncl4web"));
		menuItemSimulation= new MenuItem (Language.translate("simulation"));
		
	}

	private void createViewMenuItems() {
		
		checkMenuItemMediaView = new CheckMenuItem (Language.translate("media.repository.view"), null);
		checkMenuItemMediaView.setSelected(true);
		
		checkMenuItemTemporalView = new CheckMenuItem (Language.translate("temporal.view"), null);
		checkMenuItemTemporalView.setSelected(true);
		
		checkMenuItemSpatialView = new CheckMenuItem (Language.translate("spatial.view"), null);
		checkMenuItemSpatialView.setSelected(true);
		
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
