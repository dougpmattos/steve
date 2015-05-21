package gui.stevePane;

import gui.common.Language;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.uff.midiacom.ana.NCLBody;
import br.uff.midiacom.ana.NCLDoc;
import br.uff.midiacom.ana.NCLHead;
import br.uff.midiacom.ana.connector.NCLConnectorBase;
import br.uff.midiacom.ana.descriptor.NCLDescriptorBase;
import br.uff.midiacom.ana.region.NCLRegionBase;
import br.uff.midiacom.ana.util.exception.XMLException;

@SuppressWarnings({"rawtypes","unchecked"})
public class SteveMenuBar extends MenuBar{

	final Logger logger = LoggerFactory.getLogger(SteveMenuBar.class);
	
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
	
	public SteveMenuBar(){
		
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
		
		menuItemOpen.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});
		
		menuItemClose.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});
		
		menuItemSave.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   //TODO
		    }
		});
		
		menuItemImportNCL.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   
//		        try {
		    	//	        	
		    	//				openNCLDocument();
		    	//				collectDataForHTG();
		    	//				constructHTG();
		    	//	        	generatePresentationPlan();
		    	//	        	createTemporalView();
		    	//	        	if(temporalView != null){
		    	//	         	   for(int mediaInfoIndex=0; mediaInfoIndex<temporalView.getMainMediaInfoList().size(); mediaInfoIndex++){
		    	//	             	   
		    	//	             	   TemporalMediaInfo temporalMediaInfo = temporalView.getMainMediaInfoList().get(mediaInfoIndex);
		    	//	             	   Media media = temporalMediaInfo.getMedia();
		    	//	             	   media.setPath(media.getMediaAbsolutePath());
		    	//	             	   NCLMediaType mediaType = media.getMediaType();
		    	//	             	   if(mediaType == NCLMediaType.AUDIO){
		    	//	             		   audioTemporalMediainfoList.add(temporalMediaInfo);
		    	//	         		   } else {
		    	//	         			   videoTemporalMediainfoList.add(temporalMediaInfo);
		    	//	                	   }
		    	//	                	
		    	//	                }
		    	//	            }
		    	//	        	
		    	//	        	createTemporalViewPane();
		    	        	
		    	//	        } catch (XMLException e) {
		    	//				e.printStackTrace();
		    	//			} catch (IOException e) {
		    	//				// TODO Auto-generated catch block
		    	//				e.printStackTrace();
		    	//			}
		    	
//		    	 private void openNCLDocument() throws XMLException {
//		    		   nclDoc = new NCLDoc();
//		    		   nclDoc.loadXML(new File("NCL Documents/musica/musicAd.ncl"));
//		    		   //nclDoc.loadXML(new File("C:\\Users\\Douglas\\Workspace\\STVEN\\stven\\NCL Documents\\apresentacao\\ex.ncl"));
//		    	   }
//		    	   
//		    	   
//
//		    	   private void collectDataForHTG() throws XMLException, IOException {
//		    	       contextList = new NCLContextList(nclDoc); 
//		    	       mediaList = new NCLMediaList(nclDoc,contextList);
//		    	       linkList = new NCLLinkList(nclDoc, contextList);
//		    	   }
//		    	   
//		    	   
//		    	   
//		    	   
//		    	   private void constructHTG() throws XMLException, IOException {
//		    	       htg = new HypermediaTemporalGraph();
//		    	       constructMediaVertices();
//		    	       constructAnchorVertices();
//		    	       constructLinkEdges();
//		    	       htg.generateDOTFile();
//		    	   }
//		    	    
//		    	   private void constructMediaVertices() throws XMLException, IOException {
////		    	       for (NCLMedia media : mediaList){
////		    	           if(!media.hasRefer() && !media.isApplicationType()){
////		    	               HTGVertice v1 = new HTGVertice(NCLEventAction.START.toString(), media.getId(), NCLEventType.PRESENTATION.toString());
////		    	               htg.addVertice(v1);
////		    	               HTGVertice v2 = new HTGVertice(NCLEventAction.STOP.toString(), media.getId(), NCLEventType.PRESENTATION.toString());
////		    	               htg.addVertice(v2);
////		    	               if(media.getDuration()!=null){
////		    	                   HTGCondition c = new HTGCondition(media);
////		    	                   HTGEdge e = new HTGEdge(v1, v2, c);
////		    	                   htg.addEdge(e);
////		    	               } 
////		    	           }  
////		    	       }
//		    	    }
//		    	   
//		    	   private void constructAnchorVertices() throws XMLException, IOException {
////		    	        for (NCLMedia nclMedia : mediaList){
////		    	            if(!NCLMediaUtil.hasRefer(nclMedia) && !NCLMediaUtil.isApplicationType(nclMedia)){
////		    	                areaList = new NCLAreaList(nclDoc,nclMedia.getId());
////		    	                for (NCLArea area : areaList) {
////		    	                    HTGVertice ancV1 = new HTGVertice(NCLEventAction.START.toString(), area.getId(), NCLEventType.PRESENTATION.toString());
////		    	                    htg.addVertice(ancV1);
////		    	                    Double areaBegin = area.getBegin() != null ? area.getBegin().getTimeInSeconds() : null;
////		    	                    HTGCondition c1 = new HTGCondition(area, areaBegin, Boolean.FALSE);
////		    	                    HTGEdge e1 = new HTGEdge(htg.getVertice(NCLEventAction.START.toString(),nclMedia.getId(),NCLEventType.PRESENTATION.toString()), ancV1, c1);
////		    	                    htg.addEdge(e1);
////		    	                    if(area.getAreaEnd()!=null){
////		    	                        HTGVertice ancV2 = new HTGVertice(NCLEventAction.STOP.toString(), area.getId(), NCLEventType.PRESENTATION.toString());
////		    	                        htg.addVertice(ancV2);
////		    	                        HTGCondition c2 = new HTGCondition(area,area.getAreaEnd(),Boolean.TRUE);
////		    	                        HTGEdge e2 = new HTGEdge(ancV1, ancV2, c2);
////		    	                        htg.addEdge(e2);
////		    	                    }
////		    	                }
////		    	            }
////		    	        }
//		    	    }
//		    	   
//		    	@SuppressWarnings("rawtypes")
//		    	private void constructLinkEdges() throws XMLException {
//		    	       NCLCausalConnector causalConnector;
//		    	       for (NCLLink link : linkList) {
//		    	           causalConnector = NCLLinkUtil.getConnector(link);
//		    	           if(NCLConnectorUtil.hasCompound(causalConnector)){
//		    	               constructCompoundLinkEdges(link);
//		    	           }else{
//		    	               constructSimpleLinkEdges(link);
//		    	           }
//		    	       }  
//		    	   }
//		    	   
//		    	@SuppressWarnings("rawtypes")
//		    	private void constructSimpleLinkEdges(NCLLink link){
//		    	      HTGCondition condition = new HTGCondition(new Double(0.0));
//		    	      List<HTGVertice> outputVerticeList = new ArrayList<HTGVertice>();
//		    	      HTGCondition noDelayCondition = null;
//		    	      HTGEdge edge;
//		    	      NCLCausalConnector connector = NCLLinkUtil.getConnector(link);
//		    	      constructSimpleCondition(condition,link);
//		    	      constructSimpleAction(outputVerticeList,link);
//		    	      try {
//		    	          noDelayCondition = condition.clone();
//		    	      } catch (CloneNotSupportedException ex) {
//		    	          Logger.getLogger(StvePane.class.getName()).log(Level.SEVERE, null, ex);
//		    	      }
//		    	      for (int j = 0; j < outputVerticeList.size(); j++) {
//		    	          outputVertice = outputVerticeList.get(j);
//		    	          String simpleActionRole = outputVertice.getEventAction();
//		    	          NCLSimpleAction outputVerticeSimpleAction = NCLConnectorUtil.getSimpleAction(connector, simpleActionRole);
//		    	          Object objectDelay = outputVerticeSimpleAction.getDelay();
//		    	          if(objectDelay!=null){
//		    	              condition.addDelay(objectDelay,link);
//		    	              edge = new HTGEdge(inputVertice, outputVertice, condition);
//		    	          }else{
//		    	              edge = new HTGEdge(inputVertice, outputVertice, noDelayCondition);
//		    	          }
//		    	          htg.addEdge(edge);
//		    	      }
//		    	   }
//		    	   
//		    	@SuppressWarnings("rawtypes")
//		    	private void constructCompoundLinkEdges(NCLLink link) throws XMLException{
//		    	       HTGCondition condition = new HTGCondition(new Double(0.0));
//		    	       HTGCondition noDelayCondition = null;
//		    	       HTGEdge edge;
//		    	       NCLCausalConnector connector = NCLLinkUtil.getConnector(link);
//		    	       List<HTGVertice> outputVerticeList = new ArrayList<HTGVertice>();
//		    	       if(NCLConnectorUtil.hasCompoundCondition(connector)){
//		    	           constructCompoundCondition(condition,link);
//		    	       }else{//condicao simples, sem compoundCondition.
//		    	           constructSimpleCondition(condition,link);
//		    	       }
//		    	       if(NCLConnectorUtil.hasCompoundAction(connector)){
//		    	           constructCompoundAction(outputVerticeList,link);
//		    	       }else{//acoes simples, sem compoundAction.
//		    	           constructSimpleAction(outputVerticeList,link);
//		    	       }
//		    	       try {
//		    	           noDelayCondition = condition.clone();
//		    	       } catch (CloneNotSupportedException ex) {
//		    	           Logger.getLogger(StvePane.class.getName()).log(Level.SEVERE, null, ex);
//		    	       }
//		    	       for (int j = 0; j < outputVerticeList.size(); j++) {
//		    	          outputVertice = outputVerticeList.get(j);
//		    	          String simpleActionRole = HTGUtil.getSimpleActionRole(outputVertice.getEventAction(),outputVertice.getEventType());
//		    	          NCLSimpleAction outputVerticeSimpleAction = NCLConnectorUtil.getSimpleAction(connector, simpleActionRole);
//		    	          Object objectDelay = outputVerticeSimpleAction.getDelay();
//		    	          if(objectDelay!=null){
//		    	              condition.addDelay(objectDelay,link);
//		    	              edge = new HTGEdge(inputVertice, outputVertice, condition);
//		    	          }else{
//		    	              edge = new HTGEdge(inputVertice, outputVertice, noDelayCondition);
//		    	          }
//		    	          htg.addEdge(edge);
//		    	      }
//		    	   }
//		    	 
//		    	@SuppressWarnings("rawtypes")
//		    	private void constructSimpleCondition(HTGCondition condition, NCLLink link){
//		    	       NCLSimpleCondition simpleCondition = (NCLSimpleCondition)NCLLinkUtil.getConnector(link).getCondition();
//		    	       NCLBind bind;
//		    	       ArrayList<NCLBind> referencedBindList = HTGUtil.getReferencedBinds(simpleCondition,link); 
//		    	       for (int i = 0; i < referencedBindList.size(); i++) {
//		    	          HTGVertice defaultConditionVertice = null;
//		    	          bind = (NCLBind) referencedBindList.get(i);
//		    	          if(i==0){
//		    	              String roleName;
//		    	              roleName = NCLBindUtil.getRoleName(bind);
//		    	              if(roleName.equalsIgnoreCase(NCLDefaultConditionRole.ONSELECTION.toString())){
//		    	                  HTGVertice v  = htg.getVertice(NCLBindUtil.getEventAction(bind), getAnchorId(bind), NCLEventType.PRESENTATION.toString());
//		    	                  inputVertice = new HTGVertice(NCLBindUtil.getEventAction(bind), getAnchorId(bind), NCLBindUtil.getEventType(bind));
//		    	                  htg.addVertice(inputVertice);
//		    	                  HTGCondition selectionCondition = new HTGCondition();
//		    	                  selectionCondition.addInteractivity(link, bind);
//		    	                  HTGEdge e = new HTGEdge(v, inputVertice, selectionCondition);
//		    	                  htg.addEdge(e);
//		    	              }else {
//		    	                  inputVertice = htg.getVertice(NCLBindUtil.getEventAction(bind),getAnchorId(bind),NCLBindUtil.getEventType(bind));
//		    	              }
//		    	          }else{//adiciona outros objetos associados a mesma condicao(att max e qual)
//		    	              defaultConditionVertice = htg.getVertice(NCLBindUtil.getEventAction(bind),getAnchorId(bind),NCLBindUtil.getEventType(bind));
//		    	              if(NCLBindUtil.roleIsDefaultCondition(bind)){
//		    	                  if(defaultConditionVertice==null){
//		    	                      //TODO Adiciona outros objetos associados a mesma condicao(att max e qual)
//		    	                  }
//		    	                  condition.addDefaultCondition(defaultConditionVertice, simpleCondition, null);
//		    	              }else{
//		    	                  //TODO Condicao com papel definido pelo autor.
//		    	              }
//		    	          }
//		    	       }
//		    	   }
//		    	   
//		    	@SuppressWarnings("rawtypes")
//		    	private void constructSimpleAction(List<HTGVertice> outputVerticeList, NCLLink link){
//		    	      NCLSimpleAction simpleAction = (NCLSimpleAction)NCLLinkUtil.getConnector(link).getAction();
//		    	      NCLBind bind;
//		    	      ArrayList<NCLBind> referencedBindList = HTGUtil.getReferencedBinds(simpleAction,link); 
//		    	      for (int i = 0; i < referencedBindList.size(); i++) {
//		    	          bind = (NCLBind) referencedBindList.get(i);
//		    	          if(NCLBindUtil.getEventType(bind).equalsIgnoreCase(NCLEventType.PRESENTATION.toString())){
//		    	              HTGVertice v = htg.getVertice(NCLBindUtil.getEventAction(bind),getAnchorId(bind),NCLBindUtil.getEventType(bind));
//		    	              if(v == null && NCLBindUtil.getEventAction(bind).equalsIgnoreCase(NCLEventAction.ABORT.toString())){
//		    	                  v = new HTGVertice(NCLBindUtil.getEventAction(bind),getAnchorId(bind),NCLBindUtil.getEventType(bind));
//		    	                  htg.addVertice(v);
//		    	              }
//		    	              outputVerticeList.add(v);
//		    	          }else if(NCLBindUtil.getEventType(bind).equalsIgnoreCase(NCLEventType.ATTRIBUTION.toString())){
//		    	              String value = getAttributionValue(link,bind);
//		    	              HTGVertice v1 = new HTGVertice(NCLBindUtil.getEventAction(bind),NCLBindUtil.getComponentId(bind)+"."+getAnchorId(bind)+" = "+value,NCLBindUtil.getEventType(bind));
//		    	              htg.addVertice(v1);
//		    	              outputVerticeList.add(v1);
//		    	          }  
//		    	      }
//		    	   }
//		    	   
//		    	@SuppressWarnings({ "rawtypes", "unchecked" })
//		    	private void constructCompoundCondition(HTGCondition condition, NCLLink link) throws XMLException {
//		    	       NCLCompoundCondition compoundCondition = (NCLCompoundCondition)NCLLinkUtil.getConnector(link).getCondition();
//		    	       ElementList<NCLCondition> conditionList = compoundCondition.getConditions();
//		    	       NCLCondition nclCondition;
//		    	       String operator = compoundCondition.getOperator().toString();
//		    	       Boolean isFirstCondition = Boolean.TRUE;
//		    	       Iterator iterator = conditionList.iterator();
//		    	       NCLBind bind;
//		    	       while(iterator.hasNext()){
//		    	           nclCondition = (NCLCondition) iterator.next();
//		    	           HTGVertice defaultConditionVertice = null;
//		    	           if(nclCondition instanceof NCLSimpleCondition){
//		    	               NCLSimpleCondition simpleCondition = (NCLSimpleCondition) nclCondition;
//		    	               ArrayList<NCLBind> referencedBindList = HTGUtil.getReferencedBinds(simpleCondition,link); 
//		    	               for (int i = 0; i < referencedBindList.size(); i++) {
//		    	                   bind = (NCLBind) referencedBindList.get(i);
//		    	                   String roleName;
//		    	                   roleName = NCLBindUtil.getRoleName(bind);
//		    	                   if(roleName.equalsIgnoreCase(NCLDefaultConditionRole.ONSELECTION.toString())){
//		    	                       HTGVertice v  = htg.getVertice(NCLBindUtil.getEventAction(bind), getAnchorId(bind), NCLEventType.PRESENTATION.toString());
//		    	                       inputVertice = new HTGVertice(NCLBindUtil.getEventAction(bind), getAnchorId(bind), NCLBindUtil.getEventType(bind));
//		    	                       htg.addVertice(inputVertice);
//		    	                       HTGCondition selectionCondition = new HTGCondition();
//		    	                       selectionCondition.addInteractivity(link, bind);
//		    	                       HTGEdge e = new HTGEdge(v, inputVertice, selectionCondition);
//		    	                       htg.addEdge(e);
//		    	                   }else if(isFirstCondition && !NCLLinkUtil.hasOnSelectionCondition(link)){
//		    	                       isFirstCondition = Boolean.FALSE;
//		    	                       inputVertice = htg.getVertice(NCLBindUtil.getEventAction(bind),getAnchorId(bind),NCLBindUtil.getEventType(bind));
//		    	                   }else{
//		    	                       defaultConditionVertice = htg.getVertice(NCLBindUtil.getEventAction(bind),getAnchorId(bind),NCLBindUtil.getEventType(bind));
//		    	                       if(NCLBindUtil.roleIsDefaultCondition(bind)){
//		    	                           if(defaultConditionVertice!=null){
//		    	                               condition.addDefaultCondition(defaultConditionVertice, simpleCondition, operator);
//		    	                           }
//		    	                       }else{
//		    	                          //condição com papel definido pelo autor no simpleCondition.
//		    	                       }
//		    	                    }
//		    	               }
//		    	            }else if(nclCondition instanceof NCLCompoundCondition){
//		    	                   //tratar composição dentro de outra
//		    	            }
//		    	       }
//		    	       NCLStatement statement;
//		    	       ElementList<NCLStatement> statementList = compoundCondition.getStatements();
//		    	       iterator = statementList.iterator();
//		    	       while(iterator.hasNext()){
//		    	           statement = (NCLStatement) iterator.next();
//		    	           if(statement instanceof NCLAssessmentStatement){
//		    	               condition.addAssessmentStatement((NCLAssessmentStatement)statement,link);
//		    	           }else if(statement instanceof NCLCompoundStatement){
//		    	               //tratar varias statement.
//		    	           }
//		    	       }
//		    	   }
//		    	   
//		    	@SuppressWarnings({ "rawtypes", "unchecked" })
//		    	private void constructCompoundAction(List<HTGVertice> outputVerticeList, NCLLink link){
//		    	       NCLAction nclAction;
//		    	       NCLCompoundAction compoundAction = (NCLCompoundAction)NCLLinkUtil.getConnector(link).getAction();
//		    	       ElementList<NCLAction> actionList = compoundAction.getActions();
//		    	       Iterator iterator = actionList.iterator();
//		    	       NCLBind bind;
//		    	       while(iterator.hasNext()){
//		    	           nclAction = (NCLAction) iterator.next();
//		    	           if(nclAction instanceof NCLSimpleAction){
//		    	               NCLSimpleAction simpleAction = (NCLSimpleAction) nclAction;
//		    	               ArrayList<NCLBind> referencedBindList = HTGUtil.getReferencedBinds(simpleAction,link); 
//		    	               for (int i = 0; i < referencedBindList.size(); i++) {
//		    	                   bind = (NCLBind) referencedBindList.get(i);
//		    	                  if(NCLBindUtil.getEventType(bind).equalsIgnoreCase(NCLEventType.PRESENTATION.toString())){
//		    	                      HTGVertice v = htg.getVertice(NCLBindUtil.getEventAction(bind),getAnchorId(bind),NCLBindUtil.getEventType(bind));
//		    	                      if(v == null){
//		    	                          v = new HTGVertice(NCLBindUtil.getEventAction(bind),getAnchorId(bind),NCLBindUtil.getEventType(bind));
//		    	                          htg.addVertice(v);
//		    	                      }
//		    	                      outputVerticeList.add(v); 
//		    	                   }else if(NCLBindUtil.getEventType(bind).equalsIgnoreCase(NCLEventType.ATTRIBUTION.toString())){
//		    	                       String value = getAttributionValue(link,bind);
//		    	                       HTGVertice v1 = new HTGVertice(NCLBindUtil.getEventAction(bind),NCLBindUtil.getComponentId(bind)+"."+getAnchorId(bind)+" = "+value,NCLBindUtil.getEventType(bind));
//		    	                       htg.addVertice(v1);
//		    	                       outputVerticeList.add(v1); 
//		    	                  }
//		    	              }
//		    	           }else if(nclAction instanceof NCLCompoundAction){
//		    	               //tratar composição dentro de outra.
//		    	           }
//		    	       }
//		    	   }
//		    	   
//		    	@SuppressWarnings("rawtypes")
//		    	private String getAttributionValue(NCLLink link, NCLBind bind) {
//		    	       String value = null;
//		    	       NCLCausalConnector connector = NCLLinkUtil.getConnector(link);
//		    	       NCLAction action = connector.getAction();
//		    	       if(action instanceof NCLSimpleAction){
//		    	           NCLSimpleAction sAction = (NCLSimpleAction) action;
//		    	           Object objectValue = sAction.getValue();
//		    	           if(objectValue instanceof NCLConnectorParam){
//		    	               NCLConnectorParam connectorParam = (NCLConnectorParam) objectValue;
//		    	               value = NCLConnectorUtil.getParamValue(connectorParam,link);
//		    	           }else{
//		    	               value = objectValue.toString();
//		    	           }
//		    	       }else if(action instanceof NCLCompoundAction){
//		    	            NCLSimpleAction sAction = NCLConnectorUtil.getSimpleAction(connector,NCLBindUtil.getRoleName(bind));
//		    	            Object objectValue = sAction.getValue();
//		    	            if(objectValue instanceof NCLConnectorParam){
//		    	                NCLConnectorParam connectorParam = (NCLConnectorParam) objectValue;
//		    	                value = NCLConnectorUtil.getParamValue(connectorParam,link);
//		    	            }else{
//		    	                value = objectValue.toString();
//		    	            }
//		    	       }
//		    	       return value;
//		    	   }
//
//		    		@SuppressWarnings("rawtypes")
//		    		private String getAnchorId(NCLBind bind){
//		    			
//		    			String anchorId;
//		    			if(NCLBindUtil.hasInterface(bind)){
//		    				anchorId = NCLBindUtil.getInterfaceId(bind);
//		    			}else{
//		    			    NCLMedia nclMedia = (NCLMedia) bind.getComponent();
//		    			    if(NCLMediaUtil.hasRefer(nclMedia)){
//		    			        anchorId = ((NCLMedia) nclMedia.getRefer()).getId();
//		    			    }else{
//		    			        anchorId = NCLBindUtil.getComponentId(bind);
//		    			    }
//		    			}
//		    			
//		    			return anchorId;
//		    		
//		    		}
//
//		    	   
//		    	   private void generatePresentationPlan() {
//		    	        presentationPlan = new PresentationPlan(htg);
//		    	        removeInteractiveVertices(presentationPlan);
//		    	        //presentationPlan.sort();
//		    	        presentationPlan.generatePresentationPlanTextFile();
//		    	        if(presentationPlan.getSecMatrix()!=null){
//		    	             //presentationPlan.setX(insertInteractivityTimeValue());
//		    	        }
//		    	   }
//		    	   
//		    	   private void removeInteractiveVertices(PresentationPlan presentationPlan) {
//		    	        int count = 0;
//		    	        for (int i = 0; i < presentationPlan.getMatrix().length; i++) {
//		    	            if(presentationPlan.getMatrix()[i][1] != null){
//		    	                count++;
//		    	            }
//		    	        }
//		    	        Object[][] aux = new Object[count][2];
//		    	        int j=0;
//		    	        for (int i = 0; i < presentationPlan.getMatrix().length; i++) {
//		    	            if(presentationPlan.getMatrix()[i][1] != null){
//		    	                aux[j][1] = presentationPlan.getMatrix()[i][1];
//		    	                aux[j][0] = presentationPlan.getMatrix()[i][0];
//		    	                j++;
//		    	            }
//		    	        }
//		    	        presentationPlan.setMatrix(aux);
//		    	        
////		    	      PARA TRATAR SEGUNDA INTERATIVIDADE A PARTIR DA PRIMEIRA  
////		    	        int ct = 0;
////		    	        for (int i = 0; i < presentationPlan.getSecMatrix().length; i++) {
////		    	            if(presentationPlan.getSecMatrix()[i][1] != null){
////		    	                ct++;
////		    	            }
////		    	        }
////		    	        Object[][] aux2 = new Object[ct][2];
////		    	        int k=0;
////		    	        for (int i = 0; i < presentationPlan.getSecMatrix().length; i++) {
////		    	            if(presentationPlan.getSecMatrix()[i][1] != null){
////		    	                aux2[k][1] = presentationPlan.getSecMatrix()[i][1];
////		    	                aux2[k][0] = presentationPlan.getSecMatrix()[i][0];
////		    	                k++;
////		    	            }
////		    	        }
////		    	        presentationPlan.getSecPresentationPlan().setMatrix(aux2);
//		    	   
//		    	   }
//		    	   
//		    	//   private Double insertInteractivityTimeValue(){
////		    	        ImageIcon icon = new ImageIcon("C:\\Users\\Douglas\\Documents\\NetBeansProjects\\Temporal\\src\\myPlugin\\images\\interactivity.png");
////		    	        String s = (String)JOptionPane.showInputDialog(null,"Insert the Interactivity Time Value:\n"+
////		    	                    "The value must be within interactivity range  "+presentationPlan.getStartTime(presentationPlan.getInteractiveVertice())+"  to  "+presentationPlan.getStopTime(presentationPlan.getInteractiveVertice()),
////		    	                    "Interactivity Time Value", JOptionPane.PLAIN_MESSAGE, icon, null, null);
////		    	        if(s!=null && !s.isEmpty()){
////		    	            return Double.parseDouble(s);
////		    	        }else{
////		    	            return null;
////		    	        }
//		    	//   }
//		    	   
//		    	   
//		    	   
//		    	   private void createTemporalView() throws XMLException {
//		    	       //temporalView = new TemporalView(presentationPlan, mediaList);
//		    	       //displayTemporalView();
//		    	   }
//		    	   
//		    	   @Deprecated
//		    	   private void displayTemporalView(){
////		    	       System.out.println("");
////		    	       System.out.println("TEMPORAL VIEW");
////		    	       for (int i = 0; i < temporalView.getMainMediaInfoList().size(); i++) {
////		    	           System.out.println(temporalView.getMainMediaInfoList().get(i));
////		    	       }
////		    	       if(!temporalView.getSecMediaInfoList().isEmpty()){
////		    	           System.out.println("");
////		    	           for (int i = 0; i < temporalView.getSecMediaInfoList().size(); i++) {
////		    	               System.out.println(temporalView.getSecMediaInfoList().get(i));
////		    	           }
////		    	       }
//		    	   }
		    	
		    }
		});
		
		menuItemExportNCL.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent t) {
			   
		    	NCLDoc nclDoc = new NCLDoc(); 
				createNCLHead(nclDoc);
		        createNCLBody(nclDoc);
		    	
		    	saveNCLDoc(nclDoc);
		    	
		    }
			
			private void createNCLHead(NCLDoc nclDoc){
				
				try {
					
		            NCLHead nclHead = new NCLHead();
		            nclDoc.setHead(nclHead);

		            NCLRegionBase regBase = new NCLRegionBase();
		            ArrayList<NCLRegionBase> nclRegBaseList = new ArrayList<NCLRegionBase>();
		            nclRegBaseList.add(regBase);

		            NCLDescriptorBase nclDescBase = new NCLDescriptorBase();
		            
		            NCLConnectorBase nclConBase = new NCLConnectorBase();

		            nclHead.addRegionBase(nclRegBaseList.get(0));
		            nclHead.setDescriptorBase(nclDescBase);
		            nclHead.setConnectorBase(nclConBase);

		        } catch (XMLException ex) {
		        	logger.error(ex.getMessage());
		        }
				
			}
			
			private void createNCLBody(NCLDoc nclDoc){
				
				try {
					
		            NCLBody nclBody = new NCLBody();
		            nclDoc.setBody(nclBody);

		        } catch (XMLException ex) {
		        	logger.error(ex.getMessage());
		        }
				
			}
			
			private void saveNCLDoc(NCLDoc nclDoc){
				
				String nclCode = nclDoc.parse(0);
				
		        if(nclCode != null){
		        	
		        	FileChooser fileChooser = new FileChooser();
		            fileChooser.setTitle(Language.translate("export.ncl.document"));
		            File file = fileChooser.showSaveDialog(null);
		            
					try {
						
						if(file != null){
						
							File auxFile = new File(file.getAbsolutePath() + ".ncl");
							FileWriter fileWriter = new FileWriter(auxFile);
							fileWriter.write(nclCode);
		                    fileWriter.close();
		                    
						}
	                    
					} catch (IOException e) {
						logger.error(e.getMessage());
					}
                    
		        }

			}
			
		});
		
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
