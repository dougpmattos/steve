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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import model.common.Media;
import model.common.SpatialTemporalView;
import model.repository.RepositoryMediaList;

import model.spatialView.PositionProperty;
import model.spatialView.PresentationProperty;
import model.spatialView.SizeProperty;
import model.spatialView.StyleProperty;
import model.spatialView.enums.AspectRatio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import com.sun.xml.internal.txw2.Document;
import org.w3c.dom.Document;

import view.HTMLSupport.RunWindow;
import view.common.CommonMethods;
import view.common.Language;
import view.common.MessageDialog;
import view.common.ReturnMessage;
import view.spatialViewPane.PositionPane;
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
	private Menu menuAlignment;
	private Menu menuDistribution;


	private  MenuItem menuItemAlignmentTop;
	private  MenuItem menuItemAlignmentBottom;
	private  MenuItem menuItemAlignmentLeft;
	private  MenuItem menuItemAlignmentRight;
	private  MenuItem menuItemAlignmentEqual;
	private  MenuItem menuItemAlignmentCenter;

	private  MenuItem menuItemDistributionVertical;
	private  MenuItem menuItemDistributionHorizontal;

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
		createAlignmentMenuItems();
		createDistributionMenuItems();
		
		createFileMenuItemActions();
		createEditMenuItemActions();
		createViewMenuItemActions();
		createToolMenuItemActions();
		createHelpMenuItemActions();
		createAlignmentMenuItemActions();
		createDistributionMenuItemActions();


		//menuFile.getItems().addAll(menuItemNew, menuItemOpen, new SeparatorMenuItem(), menuItemClose, new SeparatorMenuItem(), menuItemExportNCL, new SeparatorMenuItem(), menuItemExportHTML5, new SeparatorMenuItem(), menuItemRun, new SeparatorMenuItem(), menuItemExit); 
		menuFile.getItems().addAll(menuItemNew, menuItemOpen, new SeparatorMenuItem(), menuItemExportNCL, new SeparatorMenuItem(), menuItemExportHTML5, new SeparatorMenuItem(), menuItemRun, new SeparatorMenuItem(), menuItemExit);
//		menuEdit.getItems().addAll(menuItemUndo, menuItemRedo, new SeparatorMenuItem(), menuItemCut, menuItemCopy, menuItemPaste, new SeparatorMenuItem(), 
		menuEdit.getItems().addAll(menuItemPreferences, new SeparatorMenuItem(), menuItemSelectAll);
		menuView.getItems().addAll(checkMenuItemMediaView, checkMenuItemTemporalView, checkMenuItemSpatialView, checkMenuItemShowRelations);
		menuTools.getItems().addAll(menuItemSimulation);
//		menuHelp.getItems().addAll(menuItemHelpContents, new SeparatorMenuItem(), menuItemAbout);
		menuHelp.getItems().addAll(menuItemAbout);
		menuAlignment.getItems().addAll(menuItemAlignmentTop, menuItemAlignmentBottom, menuItemAlignmentEqual, menuItemAlignmentCenter);
		menuAlignment.getItems().addAll(menuItemAlignmentLeft, menuItemAlignmentRight);

		menuDistribution.getItems().addAll(menuItemDistributionVertical);
		menuDistribution.getItems().addAll(menuItemDistributionHorizontal);
		    
//		getMenus().addAll(menuFile, menuEdit, menuView, menuTools, menuHelp);
		getMenus().addAll(menuFile, menuEdit, menuAlignment, menuDistribution, menuHelp);
		
		
	}

	private void createHelpMenuItemActions(){
		
		menuItemHelpContents.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});
		
		menuItemAbout.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
		    	
		    	MessageDialog messageDialog = new MessageDialog("STEVE", "Spatio-Temporal View Editor - Version 3.0-0" + "\n"
		    			+ "Copyright 2017 Douglas Paulo de Mattos. MidiaCom Lab. UFF." + "\n"
		    			+ "All rights reserved." + "\n"
		    			+ "This product includes software developed by other MidiaCom Lab projects (aNa and NCL4WEB)." + "\n"
		    			+ "https://www.aNa.com.br" + "\n"
		    			+ "https://www.NCL4WEB.com.br", "OK", 300);
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
				PreferencesWindow preferencesWindow = new PreferencesWindow(controller);
	    		
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
		    	CommonMethods.runApplication(spatialTemporalView);
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

	private void createAlignmentMenuItemActions(){
		menuItemAlignmentTop.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {

				MessageDialog messageDialog = new MessageDialog("ALIGNMENT TOP", "MOVE THIS TOP", "OK", 300);
				messageDialog.showAndWait();

			}
		});

		menuItemAlignmentBottom.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {

				MessageDialog messageDialog = new MessageDialog("ALIGNMENT BOTTOM", "MOVE THIS BOTTOM", "OK", 300);
				messageDialog.showAndWait();

			}
		});

		menuItemAlignmentLeft.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {

				MessageDialog messageDialog = new MessageDialog("ALIGNMENT LEFT", "MOVE THIS LEFT", "OK", 300);
				messageDialog.showAndWait();

			}
		});

		menuItemAlignmentRight.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {

				MessageDialog messageDialog = new MessageDialog("ALIGNMENT RIGHT", "MOVE THIS RIGHT", "OK", 300);
				messageDialog.showAndWait();

			}
		});

		menuItemAlignmentEqual.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {

				MessageDialog messageDialog = new MessageDialog("ALIGNMENT EQUAL", "MOVE THIS EQUAL", "OK", 300);
				messageDialog.showAndWait();

			}
		});

		menuItemAlignmentEqual.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {

				MessageDialog messageDialog = new MessageDialog("ALIGNMENT CENTER", "MOVE THIS CENTER", "OK", 300);
				messageDialog.showAndWait();

			}
		});
	}

	private void createDistributionMenuItemActions(){

		menuItemDistributionVertical.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {

				System.out.println("Size is "+temporalViewPane.getSelectedMediaList().size()+". ");
				// pega 1% da tela entre as midias (midia - 1: 1%)
				// pega o resto da tela que sobrou divide pelo n de midia (ex 98%/2)
				int spaces = temporalViewPane.getSelectedMediaList().size()-1;
				double mediaSpace = (100 - spaces)/(spaces+1);
				int i=0;
				controller.getStevePane().getSpatialViewPane().getDisplayPane().getScreen().getChildren().clear();

				for(Media media: temporalViewPane.getSelectedMediaList()){

					SizeProperty sp = new SizeProperty ();

					sp.setAspectRatio(AspectRatio.FILL);
					sp.setHeight(Integer.toString((int)(mediaSpace)));


					PositionProperty pp = new PositionProperty();
					pp.setTop(Integer.toString((int) ((i*mediaSpace) +i)));

					media.getPresentationProperty().setSizeProperty(sp);
					media.getPresentationProperty().setPositionProperty(pp);

					ImageView mediaContent = new ImageView(new Image(media.getFile().toURI().toString()));
					controller.getStevePane().getSpatialViewPane().getDisplayPane().getControlButtonPane().setImagePresentationProperties(mediaContent,media);
					controller.getStevePane().getSpatialViewPane().getDisplayPane().getScreen().getChildren().add(mediaContent);

					i++;
					// seta tudo pra fill
					// pega 1% da tela entre as midias (midia - 1: 1%)
					// pega o resto da tela que sobrou divide pelo n de midia (ex 98%/2)
					// cada midia vai ter resultado % de height (49%)
					// left de cada midia vai ser index * resultado ( 0 * 49, 1 * 49 )


				}

			}
		});

		menuItemDistributionHorizontal.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {

                System.out.println("Size is "+temporalViewPane.getSelectedMediaList().size()+". ");
                // pega 1% da tela entre as midias (midia - 1: 1%)
                // pega o resto da tela que sobrou divide pelo n de midia (ex 98%/2)
                int spaces = temporalViewPane.getSelectedMediaList().size()-1;
                double mediaSpace = (100 - spaces)/(spaces+1);//24
                int i=0;

				controller.getStevePane().getSpatialViewPane().getDisplayPane().getScreen().getChildren().clear();
                for(Media media: temporalViewPane.getSelectedMediaList()){
					SizeProperty sp = new SizeProperty ();

                    sp.setAspectRatio(AspectRatio.FILL);
                    sp.setWidth(Integer.toString((int)(mediaSpace)));

					PositionProperty pp = new PositionProperty();
                    pp.setLeft(Integer.toString((int) ((i*mediaSpace) +i)));

                    media.getPresentationProperty().setSizeProperty(sp);
                    media.getPresentationProperty().setPositionProperty(pp);

					ImageView mediaContent = new ImageView(new Image(media.getFile().toURI().toString()));
					controller.getStevePane().getSpatialViewPane().getDisplayPane().getControlButtonPane().setImagePresentationProperties(mediaContent,media);
					controller.getStevePane().getSpatialViewPane().getDisplayPane().getScreen().getChildren().add(mediaContent);


//                    pp.getSizeProperty().setAspectRatio(AspectRatio.FILL);
//                    pp.getSizeProperty().setWidth(Integer.toString((int)(mediaSpace))); 
//                    pp.getPositionProperty().setLeft(Integer.toString((int) ((i*mediaSpace) +i)));
                    i++;
                    // seta tudo pra fill
                    // pega 1% da tela entre as midias (midia - 1: 1%)
                    // pega o resto da tela que sobrou divide pelo n de midia (ex 98%/2)
                    // cada midia vai ter resultado % de width (49%)
                    // left de cada midia vai ser index * resultado ( 0 * 49, 1 * 49 )

                }
			}
		});

	}

	private void createAlignmentMenuItems() {
		menuItemAlignmentTop = new MenuItem (Language.translate("alignment.top"));
		Image alignmentTop = new Image(getClass().getResourceAsStream("alignment.top.png"));
		ImageView alignmentTopView = new ImageView(alignmentTop);
		alignmentTopView.setFitWidth(15);
		alignmentTopView.setFitHeight(15);
		menuItemAlignmentTop.setGraphic(alignmentTopView);

		menuItemAlignmentBottom = new MenuItem (Language.translate("alignment.bottom"));
		Image alignmentBottom = new Image(getClass().getResourceAsStream("alignment.bottom.png"));
		ImageView alignmentBottomView = new ImageView(alignmentBottom);
		alignmentBottomView.setFitWidth(15);
		alignmentBottomView.setFitHeight(15);
		menuItemAlignmentBottom.setGraphic(alignmentBottomView);

		menuItemAlignmentCenter = new MenuItem (Language.translate("alignment.center"));
		Image alignmentCenter = new Image(getClass().getResourceAsStream("alignment.center.png"));
		ImageView alignmentCenterView = new ImageView(alignmentCenter);
		alignmentCenterView.setFitWidth(15);
		alignmentCenterView.setFitHeight(15);
		menuItemAlignmentCenter.setGraphic(alignmentCenterView);

		menuItemAlignmentLeft = new MenuItem (Language.translate("alignment.left"));
		Image alignmentLeft = new Image(getClass().getResourceAsStream("alignment.left.png"));
		ImageView alignmentLeftView = new ImageView(alignmentLeft);
		alignmentLeftView.setFitWidth(15);
		alignmentLeftView.setFitHeight(15);
		menuItemAlignmentLeft.setGraphic(alignmentLeftView);

		menuItemAlignmentRight = new MenuItem (Language.translate("alignment.right"));
		Image alignmentRight = new Image(getClass().getResourceAsStream("alignment.right.png"));
		ImageView alignmentRightView = new ImageView(alignmentRight);
		alignmentRightView.setFitWidth(15);
		alignmentRightView.setFitHeight(15);
		menuItemAlignmentRight.setGraphic(alignmentRightView);

		menuItemAlignmentEqual = new MenuItem (Language.translate("alignment.equal"));
		Image alignmentEqual = new Image(getClass().getResourceAsStream("alignment.equal.png"));
		ImageView alignmentEqualView = new ImageView(alignmentEqual);
		alignmentEqualView.setFitWidth(15);
		alignmentEqualView.setFitHeight(15);
		menuItemAlignmentEqual.setGraphic(alignmentEqualView);
	}

	private void createDistributionMenuItems() {
		menuItemDistributionVertical = new MenuItem (Language.translate("distribution.vertical"));
		Image distributionVertical = new Image(getClass().getResourceAsStream("distribution.vertical.png"));
		ImageView distributionVerticalView = new ImageView(distributionVertical);
		distributionVerticalView.setFitWidth(15);
		distributionVerticalView.setFitHeight(15);
		menuItemDistributionVertical.setGraphic(distributionVerticalView);

		menuItemDistributionHorizontal = new MenuItem (Language.translate("distribution.horizontal"));
		Image distributionHorizontal = new Image(getClass().getResourceAsStream("distribution.horizontal.png"));
		ImageView distributionHorizontalView = new ImageView(distributionHorizontal);
		distributionHorizontalView.setFitWidth(15);
		distributionHorizontalView.setFitHeight(15);
		menuItemDistributionHorizontal.setGraphic(distributionHorizontalView);
	}
	
	private void createMenu() {
		
		menuFile = new Menu(Language.translate("file"));
		menuEdit = new Menu(Language.translate("edit"));
		menuView = new Menu(Language.translate("view"));
		menuTools = new Menu(Language.translate("tools"));
		menuHelp = new Menu(Language.translate("help"));
		menuAlignment = new Menu(Language.translate("alignment"));
		
		Image alignment = new Image(getClass().getResourceAsStream("alignment.png"));
		ImageView alignmentView = new ImageView(alignment);
		alignmentView.setFitWidth(15);
		alignmentView.setFitHeight(15);
		menuAlignment.setGraphic(alignmentView);

		menuDistribution= new Menu(Language.translate("distribution"));

		Image distribution = new Image(getClass().getResourceAsStream("distribution.png"));
		ImageView distributionView = new ImageView(distribution);
		distributionView.setFitWidth(15);
		distributionView.setFitHeight(15);
		menuDistribution.setGraphic(distributionView);


	}	

}

