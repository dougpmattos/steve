package model.NCLSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import model.NCLSupport.enums.ImportedNCLCausalConnectorType;
import model.common.Media;
import model.common.SpatialTemporalView;
import model.spatialView.PositionProperty;
import model.spatialView.SizeProperty;
import model.spatialView.enums.AspectRatio;
import model.spatialView.enums.Size;
import model.temporalView.Asynchronous;
import model.temporalView.Interactivity;
import model.temporalView.Synchronous;
import model.temporalView.TemporalChain;
import model.temporalView.TemporalRelation;
import model.temporalView.enums.TemporalRelationType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import view.common.InputDialog;
import view.common.Language;
import view.common.MessageDialog;
import view.common.ReturnMessage;
import view.utility.AnimationUtil;
import br.uff.midiacom.ana.NCLBody;
import br.uff.midiacom.ana.NCLDoc;
import br.uff.midiacom.ana.NCLHead;
import br.uff.midiacom.ana.connector.NCLCausalConnector;
import br.uff.midiacom.ana.connector.NCLConnectorBase;
import br.uff.midiacom.ana.descriptor.NCLDescriptor;
import br.uff.midiacom.ana.descriptor.NCLDescriptorBase;
import br.uff.midiacom.ana.descriptor.NCLDescriptorParam;
import br.uff.midiacom.ana.interfaces.NCLPort;
import br.uff.midiacom.ana.link.NCLBind;
import br.uff.midiacom.ana.link.NCLLink;
import br.uff.midiacom.ana.link.NCLLinkParam;
import br.uff.midiacom.ana.node.NCLMedia;
import br.uff.midiacom.ana.region.NCLRegion;
import br.uff.midiacom.ana.region.NCLRegionBase;
import br.uff.midiacom.ana.reuse.NCLImportBase;
import br.uff.midiacom.ana.util.SrcType;
import br.uff.midiacom.ana.util.TimeType;
import br.uff.midiacom.ana.util.enums.NCLAttributes;
import br.uff.midiacom.ana.util.enums.NCLDefaultActionRole;
import br.uff.midiacom.ana.util.enums.NCLDefaultConditionRole;
import br.uff.midiacom.ana.util.enums.NCLMimeType;
import br.uff.midiacom.ana.util.exception.XMLException;
import br.uff.midiacom.ana.util.reference.ExternalReferenceType;

@SuppressWarnings({"rawtypes", "unchecked"})
public class NCLExportEventHandler implements EventHandler<ActionEvent>{

	private static final String EXPORTED_NCL_DOCUMENT = " Exported NCL Document";
	private static final String DELAY = "delay";
	private static final String STOP_DELAY = "stopDelay";
	private static final String START_DELAY = "startDelay";

	final Logger logger = LoggerFactory.getLogger(NCLExportEventHandler.class);

	private SpatialTemporalView spatialTemporalView;
	private FileInputStream fileInputStream;
	private FileOutputStream fileOutputStream;
	private File causalConnectorBaseFile;
	
	public NCLExportEventHandler(SpatialTemporalView temporalView){
		
		this.spatialTemporalView = temporalView;
		causalConnectorBaseFile = new File("src/model/NCLSupport/NCLFiles/causalConnectorBase.ncl");
		
	}
	
	@Override
	public void handle(ActionEvent actionEvent) {

		String temporalChains = chainMasterMediaStartsInInstantDifferentOfZero();
		
		if(!temporalChains.isEmpty()){
			
			InputDialog showContinueQuestionInputDialog = new InputDialog(Language.translate("master.media.of.the.following.temporal.chain.will.be.moved.to.instant.0s.in.the.ncl.document"), 
					temporalChains + "\n" + Language.translate("would.you.like.to.continue.exporting.to.NCL"), "yes","no", null, 250);
			
			String answer = showContinueQuestionInputDialog.showAndWaitAndReturn();
			
			if(answer.equalsIgnoreCase("left")){
				
				exportToNCL(false);
				
	    	}
			
		} else{
			
			exportToNCL(false);
			
		}

    }

	public NCLDoc exportToNCL(Boolean isForHTMLExport) {
		
		NCLDoc nclDoc = createNCLDoc(isForHTMLExport);
		
		if(nclDoc != null && !isForHTMLExport){
			saveNCLDoc(nclDoc);
		}
		
		return nclDoc;
		
	}

	private String chainMasterMediaStartsInInstantDifferentOfZero() {
		
		StringBuilder temporalChains = new StringBuilder(); 
		
		for(TemporalChain temporalChain : spatialTemporalView.getTemporalChainList()){
			
			if(temporalChain.getMasterMedia().getBegin() > 0){
				temporalChains.append(temporalChain.getName() + "\n");
			}
			
		}	
		
		return temporalChains.toString();

	}
	
	private NCLDoc createNCLDoc(Boolean isForHTMLExport){
		
		NCLDoc nclDoc = new NCLDoc();
		
		try {
			
			NCLBody nclBody = new NCLBody();
            nclDoc.setBody(nclBody);
			
            NCLHead nclHead = new NCLHead();
            nclDoc.setHead(nclHead);

            NCLRegionBase nclRegBase = new NCLRegionBase();
            ArrayList<NCLRegionBase> nclRegBaseList = new ArrayList<NCLRegionBase>();
            nclRegBaseList.add(nclRegBase);

            NCLDescriptorBase nclDescBase = new NCLDescriptorBase();
            
            NCLConnectorBase nclConBase = new NCLConnectorBase();
            
            NCLDoc importedNCLCausalConnectorBase = new NCLDoc();
            importedNCLCausalConnectorBase.loadXML(causalConnectorBaseFile);
            NCLImportBase nclImportBase = new NCLImportBase<>();
            nclImportBase.setDocumentURI(new SrcType("causalConnectorBase.ncl"));
            nclImportBase.setBaseId("causalConnectorBase");
            nclImportBase.setAlias("connectorBase");
            nclImportBase.setImportedDoc(importedNCLCausalConnectorBase);
            
            if(!isForHTMLExport){

                 nclConBase.addImportBase(nclImportBase);
            	
            }else {
            	
        		createConnectors(nclConBase, nclImportBase);
	
            }
           
            nclHead.addRegionBase(nclRegBaseList.get(0));
            nclHead.setDescriptorBase(nclDescBase);
            nclHead.setConnectorBase(nclConBase);
            
            for(TemporalChain temporalChain : spatialTemporalView.getTemporalChainList()){
            	
            	createNCLPort(nclBody, nclRegBase, nclDescBase, temporalChain);	
            	createNCLMediaDescriptorRegion(nclBody, nclRegBase, nclDescBase, temporalChain);
            	createNCLLinks(nclBody, nclImportBase, temporalChain, isForHTMLExport);
            	createStartNCLLinks(nclBody, nclImportBase, temporalChain, isForHTMLExport);
            	
            }
            
        } catch (XMLException ex) {
        	
        	logger.error(ex.getMessage());
        	MessageDialog messageDialog = new MessageDialog(Language.translate("error"), 
					Language.translate("error.during.the.export") + ": " + ex.getMessage(), "OK", 150);
            messageDialog.showAndWait();
            return null;
        	
        }
		
		return nclDoc;
		
	}

	private void createConnectors(NCLConnectorBase nclConBase, NCLImportBase nclImportBase) throws XMLException {
		
		NCLDoc casualConnectorBaseNCLDoc = nclImportBase.getImportedDoc();
		NCLHead connectorBaseNCLHead = casualConnectorBaseNCLDoc.getHead();
		NCLConnectorBase nclConnectorBaseOfImportedBase = connectorBaseNCLHead.getConnectorBase();
		
		NCLCausalConnector onBeginStartConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONBEGIN_START.getDescription());
		NCLCausalConnector onBeginStartDelayConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONBEGIN_START_DELAY.getDescription());
		NCLCausalConnector onEndStopConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONEND_STOP.getDescription());
		NCLCausalConnector onEndAbortConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONEND_ABORT.getDescription());
		NCLCausalConnector onEndStopDelayConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONEND_STOP_DELAY.getDescription());
		NCLCausalConnector onEndStartConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONEND_START.getDescription());
		NCLCausalConnector onEndStartDelayConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONEND_START_DELAY.getDescription());
		NCLCausalConnector onBeginStopConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONBEGIN_STOP.getDescription());
		NCLCausalConnector onBeginStopDelayConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONBEGIN_STOP_DELAY.getDescription());
		NCLCausalConnector onSelectionStartStopConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONSELECTION_START_STOP.getDescription());
		NCLCausalConnector onSelectionStartStopDelayConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONSELECTION_START_STOP_DELAY.getDescription());
		
		onBeginStartConnector.setParent(null);
		onBeginStartDelayConnector.setParent(null);
		onEndStopConnector.setParent(null);
		onEndAbortConnector.setParent(null);
		onEndStopDelayConnector.setParent(null);
		onEndStartConnector.setParent(null);
		onEndStartDelayConnector.setParent(null);
		onBeginStopConnector.setParent(null);
		onBeginStopDelayConnector.setParent(null);
		onSelectionStartStopConnector.setParent(null);
		onSelectionStartStopDelayConnector.setParent(null);
		
		nclConBase.addCausalConnector(onBeginStartConnector);
		nclConBase.addCausalConnector(onBeginStartDelayConnector);
		nclConBase.addCausalConnector(onEndStopConnector);
		nclConBase.addCausalConnector(onEndAbortConnector);
		nclConBase.addCausalConnector(onEndStopDelayConnector);
		nclConBase.addCausalConnector(onEndStartConnector);
		nclConBase.addCausalConnector(onEndStartDelayConnector);
		nclConBase.addCausalConnector(onBeginStopConnector);
		nclConBase.addCausalConnector(onBeginStopDelayConnector);
		nclConBase.addCausalConnector(onSelectionStartStopConnector);
		nclConBase.addCausalConnector(onSelectionStartStopDelayConnector);
		
	}

	private void createNCLPort(NCLBody nclBody, NCLRegionBase nclRegBase, NCLDescriptorBase nclDescBase, TemporalChain temporalChain) throws XMLException {
		
		if(temporalChain.getId() == 0){
			
			NCLPort nclPort = new NCLPort();
			
			if(temporalChain.getMasterMedia() != null){
				nclPort.setId("port_" + temporalChain.getMasterMedia().getNCLName());
				NCLRegion nclRegion = createNCLRegion(nclRegBase, temporalChain.getMasterMedia());
			    NCLDescriptor nclDescriptor = createNCLDescriptor(nclDescBase, temporalChain, temporalChain.getMasterMedia(), nclRegion);
			    NCLMedia nclMedia = createNCLMedia(temporalChain.getMasterMedia(), nclDescriptor);
			    nclPort.setComponent(nclMedia);
			}
		    
		    nclBody.addPort(nclPort);
		    
		}
	}

	private void createNCLMediaDescriptorRegion(NCLBody nclBody,
			NCLRegionBase nclRegBase, NCLDescriptorBase nclDescBase,
			TemporalChain temporalChain) throws XMLException {
		
		for(Media media :  temporalChain.getMediaAllList()){
			
			NCLRegion nclRegion = createNCLRegion(nclRegBase, media);
			NCLDescriptor nclDescriptor = createNCLDescriptor(nclDescBase, temporalChain, media, nclRegion);
			NCLMedia nclMedia = createNCLMedia(media, nclDescriptor);
			
			nclRegBase.addRegion(nclRegion);
			nclDescBase.addDescriptor(nclDescriptor);
			nclBody.addNode(nclMedia);
			
		}
	}

	private void createNCLLinks(NCLBody nclBody, NCLImportBase nclImportBase, TemporalChain temporalChain, Boolean isForHTMLExport) throws XMLException {
		
		NCLDoc casualConnectorBaseNCLDoc = nclImportBase.getImportedDoc();
		NCLHead connectorBaseNCLHead = casualConnectorBaseNCLDoc.getHead();
		NCLConnectorBase nclConnectorBaseOfImportedBase = connectorBaseNCLHead.getConnectorBase();
		
		NCLCausalConnector importedNCLCausalConnector;
		ExternalReferenceType externalReferenceType;
		NCLBind conditionNCLBind;
		NCLLinkParam nclLinkParamDelay;
		
		for(TemporalRelation<Media> relation : temporalChain.getRelationList()){
			
			if(relation instanceof Synchronous){
				
				Synchronous<Media> synchronousRelation = (Synchronous<Media>) relation;
				
				NCLLink nclLink = new NCLLink<>();
				nclLink.setId("link_" + synchronousRelation.getId());
				
				switch(synchronousRelation.getType()){

					case STARTS:
						
						importedNCLCausalConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONBEGIN_START.getDescription());
						
						if(!isForHTMLExport){
							externalReferenceType = new ExternalReferenceType<>(nclImportBase, importedNCLCausalConnector);
							nclLink.setXconnector(externalReferenceType);
						}else {
							nclLink.setXconnector(importedNCLCausalConnector);
						}
						
						conditionNCLBind = new NCLBind();
						conditionNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultConditionRole.ONBEGIN.toString()));
						conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getMasterMedia().getNCLName()));
						nclLink.addBind(conditionNCLBind);
						
						for(Media slaveMedia : synchronousRelation.getSlaveMediaList()){
							NCLBind startNCLBind = new NCLBind();
			    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.START.toString()));
			    			startNCLBind.setComponent(nclBody.findNode(slaveMedia.getNCLName()));
			    			nclLink.addBind(startNCLBind);
						}
						
						nclBody.addLink(nclLink);
						
						break;
						
					case STARTS_DELAY:
						
						importedNCLCausalConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONBEGIN_START_DELAY.getDescription());
						
						if(!isForHTMLExport){
							externalReferenceType = new ExternalReferenceType<>(nclImportBase, importedNCLCausalConnector);
							nclLink.setXconnector(externalReferenceType);
						}else {
							nclLink.setXconnector(importedNCLCausalConnector);
						}
						
						conditionNCLBind = new NCLBind();
						conditionNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultConditionRole.ONBEGIN.toString()));
						conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getMasterMedia().getNCLName()));
						nclLink.addBind(conditionNCLBind);
						
						for(Media slaveMedia : synchronousRelation.getSlaveMediaList()){
							NCLBind startNCLBind = new NCLBind();
			    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.START.toString()));
			    			startNCLBind.setComponent(nclBody.findNode(slaveMedia.getNCLName()));
			    			nclLink.addBind(startNCLBind);
						}
						
						nclLinkParamDelay = new NCLLinkParam();
						nclLinkParamDelay.setName(importedNCLCausalConnector.getConnectorParam(DELAY));
						nclLinkParamDelay.setValue(new TimeType(synchronousRelation.getDelay()));
						nclLink.addLinkParam(nclLinkParamDelay);
						
						nclBody.addLink(nclLink);
						
						break;
						
					case FINISHES:
						
						importedNCLCausalConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONEND_STOP.getDescription());

						if(!isForHTMLExport){
							externalReferenceType = new ExternalReferenceType<>(nclImportBase, importedNCLCausalConnector);
							nclLink.setXconnector(externalReferenceType);
						}else {
							nclLink.setXconnector(importedNCLCausalConnector);
						}
						
						conditionNCLBind = new NCLBind();
						conditionNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultConditionRole.ONEND.toString()));
						conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getMasterMedia().getNCLName()));
						nclLink.addBind(conditionNCLBind);
						
						for(Media slaveMedia : synchronousRelation.getSlaveMediaList()){
							NCLBind startNCLBind = new NCLBind();
			    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.STOP.toString()));
			    			startNCLBind.setComponent(nclBody.findNode(slaveMedia.getNCLName()));
			    			nclLink.addBind(startNCLBind);
						}
						
						nclBody.addLink(nclLink);

						break;
					
					case FINISHES_DELAY:

						importedNCLCausalConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONEND_STOP_DELAY.getDescription());

						if(!isForHTMLExport){
							externalReferenceType = new ExternalReferenceType<>(nclImportBase, importedNCLCausalConnector);
							nclLink.setXconnector(externalReferenceType);
						}else {
							nclLink.setXconnector(importedNCLCausalConnector);
						}
						
						conditionNCLBind = new NCLBind();
						conditionNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultConditionRole.ONEND.toString()));
						conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getMasterMedia().getNCLName()));
						nclLink.addBind(conditionNCLBind);
						
						for(Media slaveMedia : synchronousRelation.getSlaveMediaList()){
							NCLBind startNCLBind = new NCLBind();
			    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.STOP.toString()));
			    			startNCLBind.setComponent(nclBody.findNode(slaveMedia.getNCLName()));
			    			nclLink.addBind(startNCLBind);
						}
						
						nclLinkParamDelay = new NCLLinkParam();
						nclLinkParamDelay.setName(importedNCLCausalConnector.getConnectorParam(DELAY));
						nclLinkParamDelay.setValue(new TimeType(synchronousRelation.getDelay()));
						nclLink.addLinkParam(nclLinkParamDelay);

						nclBody.addLink(nclLink);
						
						break;
						
					case MEETS:
						
						importedNCLCausalConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONEND_START.getDescription());

						if(!isForHTMLExport){
							externalReferenceType = new ExternalReferenceType<>(nclImportBase, importedNCLCausalConnector);
							nclLink.setXconnector(externalReferenceType);
						}else {
							nclLink.setXconnector(importedNCLCausalConnector);
						}
						
						conditionNCLBind = new NCLBind();
						conditionNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultConditionRole.ONEND.toString()));
						conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getMasterMedia().getNCLName()));
						nclLink.addBind(conditionNCLBind);
						
						for(Media slaveMedia : synchronousRelation.getSlaveMediaList()){
							NCLBind startNCLBind = new NCLBind();
			    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.START.toString()));
			    			startNCLBind.setComponent(nclBody.findNode(slaveMedia.getNCLName()));
			    			nclLink.addBind(startNCLBind);
						}
						
						nclBody.addLink(nclLink);

						break;
					
					case MEETS_DELAY:
			
						importedNCLCausalConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONEND_START_DELAY.getDescription());

						if(!isForHTMLExport){
							externalReferenceType = new ExternalReferenceType<>(nclImportBase, importedNCLCausalConnector);
							nclLink.setXconnector(externalReferenceType);
						}else {
							nclLink.setXconnector(importedNCLCausalConnector);
						}
						
						conditionNCLBind = new NCLBind();
						conditionNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultConditionRole.ONEND.toString()));
						conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getMasterMedia().getNCLName()));
						nclLink.addBind(conditionNCLBind);
						
						for(Media slaveMedia : synchronousRelation.getSlaveMediaList()){
							NCLBind startNCLBind = new NCLBind();
			    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.START.toString()));
			    			startNCLBind.setComponent(nclBody.findNode(slaveMedia.getNCLName()));
			    			nclLink.addBind(startNCLBind);
						}
						
						nclLinkParamDelay = new NCLLinkParam();
						nclLinkParamDelay.setName(importedNCLCausalConnector.getConnectorParam(DELAY));
						nclLinkParamDelay.setValue(new TimeType(synchronousRelation.getDelay()));
						nclLink.addLinkParam(nclLinkParamDelay);
						
						nclBody.addLink(nclLink);
						
						break;
					
					case MET_BY:
						
						importedNCLCausalConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONBEGIN_STOP.getDescription());
						externalReferenceType = new ExternalReferenceType<>(nclImportBase, importedNCLCausalConnector);

						if(!isForHTMLExport){
							externalReferenceType = new ExternalReferenceType<>(nclImportBase, importedNCLCausalConnector);
							nclLink.setXconnector(externalReferenceType);
						}else {
							nclLink.setXconnector(importedNCLCausalConnector);
						}
						
						conditionNCLBind = new NCLBind();
						conditionNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultConditionRole.ONBEGIN.toString()));
						conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getMasterMedia().getNCLName()));
						nclLink.addBind(conditionNCLBind);
						
						for(Media slaveMedia : synchronousRelation.getSlaveMediaList()){
							NCLBind startNCLBind = new NCLBind();
			    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.STOP.toString()));
			    			startNCLBind.setComponent(nclBody.findNode(slaveMedia.getNCLName()));
			    			nclLink.addBind(startNCLBind);
						}
						
						nclBody.addLink(nclLink);
						
						break;
					
					case MET_BY_DELAY:
					
						importedNCLCausalConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONBEGIN_STOP_DELAY.getDescription());
						externalReferenceType = new ExternalReferenceType<>(nclImportBase, importedNCLCausalConnector);

						if(!isForHTMLExport){
							externalReferenceType = new ExternalReferenceType<>(nclImportBase, importedNCLCausalConnector);
							nclLink.setXconnector(externalReferenceType);
						}else {
							nclLink.setXconnector(importedNCLCausalConnector);
						}
						
						conditionNCLBind = new NCLBind();
						conditionNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultConditionRole.ONBEGIN.toString()));
						conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getMasterMedia().getNCLName()));
						nclLink.addBind(conditionNCLBind);
						
						for(Media slaveMedia : synchronousRelation.getSlaveMediaList()){
							NCLBind startNCLBind = new NCLBind();
			    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.STOP.toString()));
			    			startNCLBind.setComponent(nclBody.findNode(slaveMedia.getNCLName()));
			    			nclLink.addBind(startNCLBind);
						}
						
						nclLinkParamDelay = new NCLLinkParam();
						nclLinkParamDelay.setName(importedNCLCausalConnector.getConnectorParam(DELAY));
						nclLinkParamDelay.setValue(new TimeType(synchronousRelation.getDelay()));
						nclLink.addLinkParam(nclLinkParamDelay);
						
						nclBody.addLink(nclLink);
						
						break;
					
					case BEFORE:
						
						for(int i  = 0; i < synchronousRelation.getSlaveMediaList().size(); i++){
							
							NCLLink beforeNCLLink = new NCLLink<>();
							beforeNCLLink.setId("link_" + synchronousRelation.getId() + i);
							
							Media slaveMedia = synchronousRelation.getSlaveMediaList().get(i);
			
							importedNCLCausalConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONEND_START_DELAY.getDescription());

							if(!isForHTMLExport){
								externalReferenceType = new ExternalReferenceType<>(nclImportBase, importedNCLCausalConnector);
								nclLink.setXconnector(externalReferenceType);
							}else {
								nclLink.setXconnector(importedNCLCausalConnector);
							}
							
							conditionNCLBind = new NCLBind();
							conditionNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultConditionRole.ONEND.toString()));
							
							if(i == 0){
								conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getMasterMedia().getNCLName()));
							}else {
								conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getSlaveMediaList().get(i-1).getNCLName()));
							}
							
							beforeNCLLink.addBind(conditionNCLBind);
						
							NCLBind startNCLBind = new NCLBind();
			    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.START.toString()));
			    			startNCLBind.setComponent(nclBody.findNode(slaveMedia.getNCLName()));
			    			beforeNCLLink.addBind(startNCLBind);
						
							nclLinkParamDelay = new NCLLinkParam();
							nclLinkParamDelay.setName(importedNCLCausalConnector.getConnectorParam(DELAY));
							nclLinkParamDelay.setValue(new TimeType(synchronousRelation.getDelay()));
							beforeNCLLink.addLinkParam(nclLinkParamDelay);
							
							nclBody.addLink(beforeNCLLink);
							
						}
						
						break;
				
				}
				
			} else if(relation instanceof Interactivity){
				
				Interactivity<Media, ?> interactivityRelation = (Interactivity<Media, ?>) relation;
				
				NCLLink nclLink = new NCLLink<>();
				nclLink.setId("link_" + interactivityRelation.getId());
				
				if(interactivityRelation.getStartDelay() == 0.0 && interactivityRelation.getStopDelay() == 0.0){
					
					importedNCLCausalConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONSELECTION_START_STOP.getDescription());

					if(!isForHTMLExport){
						externalReferenceType = new ExternalReferenceType<>(nclImportBase, importedNCLCausalConnector);
						nclLink.setXconnector(externalReferenceType);
					}else {
						nclLink.setXconnector(importedNCLCausalConnector);
					}
					
					conditionNCLBind = new NCLBind();
					conditionNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultConditionRole.ONSELECTION.toString()));
					conditionNCLBind.setComponent(nclBody.findNode(interactivityRelation.getMasterMedia().getNCLName()));
					nclLink.addBind(conditionNCLBind);
					
					for(TemporalChain temporalChainToBeStarted : interactivityRelation.getTemporalChainList()){
						NCLBind startNCLBind = new NCLBind();
		    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.START.toString()));
		    			startNCLBind.setComponent(nclBody.findNode(temporalChainToBeStarted.getMasterMedia().getNCLName()));
		    			nclLink.addBind(startNCLBind);
					}
					
					for(Media slaveMedia : interactivityRelation.getSlaveMediaList()){
						NCLBind stopNCLBind = new NCLBind();
		    			stopNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.STOP.toString()));
		    			stopNCLBind.setComponent(nclBody.findNode(slaveMedia.getNCLName()));
		    			nclLink.addBind(stopNCLBind);
					}
					
				}else {
					
					importedNCLCausalConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONSELECTION_START_STOP_DELAY.getDescription());

					if(!isForHTMLExport){
						externalReferenceType = new ExternalReferenceType<>(nclImportBase, importedNCLCausalConnector);
						nclLink.setXconnector(externalReferenceType);
					}else {
						nclLink.setXconnector(importedNCLCausalConnector);
					}
					
					conditionNCLBind = new NCLBind();
					conditionNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultConditionRole.ONSELECTION.toString()));
					conditionNCLBind.setComponent(nclBody.findNode(interactivityRelation.getMasterMedia().getNCLName()));
					nclLink.addBind(conditionNCLBind);
					
					for(TemporalChain temporalChainToBeStarted : interactivityRelation.getTemporalChainList()){
						NCLBind startNCLBind = new NCLBind();
		    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.START.toString()));
		    			startNCLBind.setComponent(nclBody.findNode(temporalChainToBeStarted.getMasterMedia().getNCLName()));
		    			nclLink.addBind(startNCLBind);
					}
					
					for(Media slaveMedia : interactivityRelation.getSlaveMediaList()){
						NCLBind stopNCLBind = new NCLBind();
		    			stopNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.STOP.toString()));
		    			stopNCLBind.setComponent(nclBody.findNode(slaveMedia.getNCLName()));
		    			nclLink.addBind(stopNCLBind);
					}
					
					NCLLinkParam nclLinkParamStopDelay = new NCLLinkParam();
					nclLinkParamStopDelay.setName(importedNCLCausalConnector.getConnectorParam(STOP_DELAY));
					nclLinkParamStopDelay.setValue(new TimeType(interactivityRelation.getStopDelay()));
					
					NCLLinkParam nclLinkParamStartDelay = new NCLLinkParam();
					nclLinkParamStartDelay.setName(importedNCLCausalConnector.getConnectorParam(START_DELAY));
					nclLinkParamStartDelay.setValue(new TimeType(interactivityRelation.getStartDelay()));
					
					nclLink.addLinkParam(nclLinkParamStopDelay);
					nclLink.addLinkParam(nclLinkParamStartDelay);
					
				}

				nclBody.addLink(nclLink);
				
			}
	
		}
	}

	private void createStartNCLLinks(NCLBody nclBody, NCLImportBase nclImportBase, TemporalChain temporalChain, Boolean isForHTMLExport) throws XMLException {
		
		NCLDoc casualConnectorBaseNCLDoc = nclImportBase.getImportedDoc();
		NCLHead connectorBaseNCLHead = casualConnectorBaseNCLDoc.getHead();
		NCLConnectorBase nclConnectorBaseOfImportedBase = connectorBaseNCLHead.getConnectorBase();
		
		NCLCausalConnector importedNCLCausalConnector;
		ExternalReferenceType externalReferenceType;
		NCLBind conditionNCLBind;
		NCLLinkParam nclLinkParamDelay;

		int relationNumber = temporalChain.getRelationList().size();
		
		for(Media media :  temporalChain.getMediaAllList()){
			
			if(!media.equals(temporalChain.getMasterMedia()) && !isThereRelationStartsMedia(media, temporalChain)){
				
				NCLLink nclLink = new NCLLink<>();
				nclLink.setId("link_" + relationNumber++);
				
				importedNCLCausalConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONBEGIN_START_DELAY.getDescription());

				if(!isForHTMLExport){
					externalReferenceType = new ExternalReferenceType<>(nclImportBase, importedNCLCausalConnector);
					nclLink.setXconnector(externalReferenceType);
				}else {
					nclLink.setXconnector(importedNCLCausalConnector);
				}
				
				conditionNCLBind = new NCLBind();
				conditionNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultConditionRole.ONBEGIN.toString()));
				conditionNCLBind.setComponent(nclBody.findNode(temporalChain.getMasterMedia().getNCLName()));
				nclLink.addBind(conditionNCLBind);

				NCLBind startNCLBind = new NCLBind();
    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.START.toString()));
    			startNCLBind.setComponent(nclBody.findNode(media.getNCLName()));
    			nclLink.addBind(startNCLBind);
				
				nclLinkParamDelay = new NCLLinkParam();
				nclLinkParamDelay.setName(importedNCLCausalConnector.getConnectorParam(DELAY));
				nclLinkParamDelay.setValue(new TimeType(getDelayBetweenMasterAndSlave(media, temporalChain)));
				nclLink.addLinkParam(nclLinkParamDelay);
				
				nclBody.addLink(nclLink);
				
			}
			
		}

	}
	
	private Double getDelayBetweenMasterAndSlave(Media media, TemporalChain temporalChain){
		
		Double mediaBegin = media.getBegin();
		Double masterMediaBegin = temporalChain.getMasterMedia().getBegin();
	
		return mediaBegin - masterMediaBegin;
		
	}
	
	private boolean isThereRelationStartsMedia(Media media, TemporalChain temporalChain){

		for(TemporalRelation relation :  temporalChain.getRelationList()){
			
			if(relation instanceof Synchronous){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				TemporalRelationType relationType = synchronousRelation.getType();
				
				if(relationType == TemporalRelationType.STARTS || relationType == TemporalRelationType.STARTS_DELAY ||relationType == TemporalRelationType.MEETS
						   || relationType == TemporalRelationType.MEETS_DELAY || relationType == TemporalRelationType.BEFORE){
					
					if(synchronousRelation.getSlaveMediaList().contains(media)){
						 return true;
					}
					
				}
				
			}
			
		}
		
		return false;
		
	}
	
	private NCLMedia createNCLMedia(Media media, NCLDescriptor nclDescriptor) throws XMLException {
		
		NCLMedia nclMedia = new NCLMedia();
		nclMedia.setId(media.getNCLName());
		nclMedia.setSrc(new SrcType("media/" + media.getFile().getAbsoluteFile().getName()));
		if(media.getMimeType() != null){
			nclMedia.setType(NCLMimeType.getEnumType(media.getMimeType().toString()));
		}
		nclMedia.setDescriptor(nclDescriptor);
		return nclMedia;
		
	}

	private NCLDescriptor createNCLDescriptor(NCLDescriptorBase nclDescBase, TemporalChain temporalChain, Media media, NCLRegion nclRegion) throws XMLException {
		
		NCLDescriptor nclDescriptor = new NCLDescriptor();
		nclDescriptor.setId("desc_" + media.getNCLName());
		nclDescriptor.setRegion(nclRegion);
		if(!isThereRelationFinishesMedia(media, temporalChain)){
			nclDescriptor.setExplicitDur(new TimeType(media.getDuration()));
		}
		NCLDescriptorParam transparencyParam = new NCLDescriptorParam();
		transparencyParam.setName(NCLAttributes.TRANSPARENCY);
		transparencyParam.setValue(media.getPresentationProperty().getStyleProperty().getTransparency()/100);

		NCLDescriptorParam fitParam = new NCLDescriptorParam();
		fitParam.setName(NCLAttributes.FIT);
		AspectRatio aspectRatio = media.getPresentationProperty().getSizeProperty().getAspectRatio();
		if(aspectRatio != AspectRatio.NONE){
			fitParam.setValue(media.getPresentationProperty().getSizeProperty().getAspectRatio().toString().toLowerCase());
		}
		
		nclDescriptor.addDescriptorParam(transparencyParam);
		nclDescriptor.addDescriptorParam(fitParam);
		
		return nclDescriptor;
		
	}

	private NCLRegion createNCLRegion(NCLRegionBase nclRegBase, Media media) throws XMLException { 
		
		NCLRegion nclRegion = new NCLRegion();
		nclRegion.setId("rg_" + media.getNCLName());
		
		PositionProperty mediaPositionProperty= media.getPresentationProperty().getPositionProperty();
		SizeProperty mediaSizeProperty = media.getPresentationProperty().getSizeProperty();
		
		nclRegion.setLeft(treatPositionSizeValue(mediaPositionProperty.getLeft()));
		nclRegion.setRight(treatPositionSizeValue(mediaPositionProperty.getRight()));
		nclRegion.setTop(treatPositionSizeValue(mediaPositionProperty.getTop()));
		nclRegion.setBottom(treatPositionSizeValue(mediaPositionProperty.getBottom()));
		nclRegion.setWidth(treatPositionSizeValue(mediaSizeProperty.getWidth()));
		nclRegion.setHeight(treatPositionSizeValue(mediaSizeProperty.getHeight()));
		nclRegion.setzIndex(mediaPositionProperty.getOrderZ());
		
		return nclRegion;
		
	}
	
	private Boolean isThereRelationFinishesMedia(Media media, TemporalChain temporalChain){

		for(TemporalRelation relation :  temporalChain.getRelationList()){
			
			if(relation instanceof Synchronous){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				TemporalRelationType relationType = synchronousRelation.getType();
				
				if(relationType.equals(TemporalRelationType.MET_BY) || relationType.equals(TemporalRelationType.MET_BY_DELAY)
				   || relationType.equals(TemporalRelationType.FINISHES) || relationType.equals(TemporalRelationType.FINISHES_DELAY)){
					
					if(synchronousRelation.getSlaveMediaList().contains(media)){
						 return true;
					}
					
				}
				
			} else if(relation instanceof Asynchronous){
				
				Asynchronous asynchronousRelation = (Asynchronous) relation;
				if(asynchronousRelation.getSlaveMediaList().contains(media)){
					return true;
				}
				
			}
			
		}
		
		return false;
		
	}
	
	private String treatPositionSizeValue(String value){
		
		if(value.contains(Size.PX.toString())){
			return value.substring(0, value.indexOf('p'));
		}else {
			return value;
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
				
					String exportedNCLDocumentDir = file.getAbsolutePath() + EXPORTED_NCL_DOCUMENT;
					String mediaDir = exportedNCLDocumentDir + "/media";
					Boolean mediaDirCreated = (new File(mediaDir)).mkdirs();
					
					if (mediaDirCreated) {

						copyMediaFiles(mediaDir);
						
						fileInputStream = new FileInputStream(causalConnectorBaseFile);
						FileChannel sourceChannel = fileInputStream.getChannel();
						fileOutputStream = new FileOutputStream(new File(exportedNCLDocumentDir + "/" + causalConnectorBaseFile.getAbsoluteFile().getName()));
						FileChannel destChannel = fileOutputStream.getChannel();
				        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
				        sourceChannel.close();
				        destChannel.close();
						
						
					}
					
					File auxFile = new File(exportedNCLDocumentDir + "/" + file.getAbsoluteFile().getName() + ".ncl");
					FileWriter fileWriter = new FileWriter(auxFile);
					fileWriter.write(nclCode);
                    fileWriter.close();
                    ReturnMessage returnMessage = new ReturnMessage(Language.translate("ncl.export.is.ready"), 300);
                    returnMessage.show();
                    AnimationUtil.applyFadeInOut(returnMessage);
                    
				}
                
			} catch (Exception e) {
				
				logger.error(e.getMessage());
				MessageDialog messageDialog = new MessageDialog(Language.translate("error.during.the.export"), 
						Language.translate("could.not.find.the.ncl.document.directory") + ": " + e.getMessage(), "OK", 250);
		        messageDialog.showAndWait();
		        
			}
            
        }

	}

	public void copyMediaFiles(String mediaDir) throws FileNotFoundException, IOException {
		
		for(TemporalChain temporalChain : spatialTemporalView.getTemporalChainList()){
			
			for(Media media :  temporalChain.getMediaAllList()){

			    fileInputStream = new FileInputStream(media.getFile());
				FileChannel sourceChannel = fileInputStream.getChannel();
				fileOutputStream = new FileOutputStream(new File(mediaDir + "/" + media.getFile().getAbsoluteFile().getName()));
				FileChannel destChannel = fileOutputStream.getChannel();
		        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
		        sourceChannel.close();
		        destChannel.close();
				
			}
			
		}
		
	}

}