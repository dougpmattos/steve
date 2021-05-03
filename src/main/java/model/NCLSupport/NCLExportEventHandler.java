package model.NCLSupport;

import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Locale;

import br.uff.midiacom.ana.node.NCLEffect;
import br.uff.midiacom.ana.util.enums.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import model.NCLSupport.enums.ImportedNCLCausalConnectorType;
import model.common.MediaNode;
import model.common.Node;
import model.common.SensoryEffectNode;
import model.common.SpatialTemporalApplication;
import model.spatialView.media.MediaPositionProperty;
import model.spatialView.media.SizeProperty;
import model.spatialView.media.enums.AspectRatio;
import model.spatialView.media.enums.Size;
import model.spatialView.sensoryEffect.*;
import model.spatialView.sensoryEffect.enums.XPositionType;
import model.spatialView.sensoryEffect.temperature.ColdPresentationProperty;
import model.spatialView.sensoryEffect.temperature.HotPresentationProperty;
import model.temporalView.*;
import model.temporalView.enums.TemporalRelationType;
import view.common.dialogs.InputDialog;
import view.common.Language;
import view.common.dialogs.MessageDialog;
import view.common.dialogs.ReturnMessage;
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
import br.uff.midiacom.ana.link.NCLBindParam;
import br.uff.midiacom.ana.link.NCLLink;
import br.uff.midiacom.ana.link.NCLLinkParam;
import br.uff.midiacom.ana.node.NCLMedia;
import br.uff.midiacom.ana.region.NCLRegion;
import br.uff.midiacom.ana.region.NCLRegionBase;
import br.uff.midiacom.ana.reuse.NCLImportBase;
import br.uff.midiacom.ana.util.SrcType;
import br.uff.midiacom.ana.util.TimeType;
import br.uff.midiacom.ana.util.exception.XMLException;
import br.uff.midiacom.ana.util.reference.ExternalReferenceType;

@SuppressWarnings({"rawtypes", "unchecked"})
public class NCLExportEventHandler implements EventHandler<ActionEvent>{

	private static final String EXPORTED_NCL_DOCUMENT = " Exported NCL Document";
	private static final String DELAY = "delay";
	private static final String STOP_DELAY = "stopDelay";
	private static final String START_DELAY = "startDelay";

	private SpatialTemporalApplication spatialTemporalApplication;
	private FileInputStream fileInputStream;
	private FileOutputStream fileOutputStream;
	private File causalConnectorBaseFile;
	
	public NCLExportEventHandler(SpatialTemporalApplication temporalView){
		
		this.spatialTemporalApplication = temporalView;

		URL pathName = NCLExportEventHandler.class.getResource("/NCLFiles/causalConnectorBase.ncl");

		causalConnectorBaseFile = new File(pathName.getPath());
		
	}
	
	@Override
	public void handle(ActionEvent actionEvent) {

		if(isThereNoApplication()){
			
			MessageDialog messageDialog = new MessageDialog(Language.translate("no.multimedia.application.to.be.exported"), "OK", 150);
            messageDialog.showAndWait();
            
		} else{
			
			String temporalChains = chainMasterMediaStartsInInstantDifferentOfZero();
			
			if(!temporalChains.isEmpty()){
				
				InputDialog showContinueQuestionInputDialog = new InputDialog(Language.translate("primary.media.of.the.following.temporal.chain.will.be.moved.to.instant.0s.in.the.ncl.document"),
						temporalChains + "\n" + Language.translate("would.you.like.to.continue.exporting.to.NCL"), "yes","no", null, 250);
				
				String answer = showContinueQuestionInputDialog.showAndWaitAndReturn();
				
				if(answer.equalsIgnoreCase("left")){
					
					exportToNCL(false);
					
		    	}
				
			} else{
				
				exportToNCL(false);
				
			}
			
		}

    }
	
	public boolean isThereNoApplication(){
		
		ArrayList<TemporalChain> temporalChainList = spatialTemporalApplication.getTemporalChainList();
		
		if(temporalChainList.size() == 1 && temporalChainList.get(0).getMediaNodeAllList().isEmpty()){
			return true;
		} else{
			return false;
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
		
		for(TemporalChain temporalChain : spatialTemporalApplication.getTemporalChainList()){
			
			if(temporalChain.getMasterNode() != null && temporalChain.getMasterNode().getBegin() > 0){
				temporalChains.append(temporalChain.getName() + "\n");
			}
			
		}	
		
		return temporalChains.toString();

	}
	
	private NCLDoc createNCLDoc(Boolean isForHTMLExport){
		
		NCLDoc nclDoc = new NCLDoc();
		
		try {
			System.out.println(nclDoc.toString());
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
            
            for(TemporalChain temporalChain : spatialTemporalApplication.getTemporalChainList()){
            	
            	createNCLNodeDescriptorRegion(nclBody, nclRegBase, nclDescBase, temporalChain);

            }
            
            for(TemporalChain temporalChain : spatialTemporalApplication.getTemporalChainList()){
            	
            	createNCLPort(nclBody, nclRegBase, nclDescBase, temporalChain);
            	createNCLLinks(nclBody, nclImportBase, temporalChain, isForHTMLExport);
            	createStartNCLLinks(nclBody, nclImportBase, temporalChain, isForHTMLExport);
            	
            }
            
        } catch (XMLException ex) {

        	MessageDialog messageDialog = new MessageDialog(Language.translate("error"), 
					Language.translate("error.during.the.export") + ": " + ex.getMessage(), "OK", 250);
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
			Node masterNode = temporalChain.getMasterNode();
			
			if(masterNode != null){

				nclPort.setId("port_" + masterNode.getNCLName());

				if(masterNode instanceof MediaNode){

					MediaNode mediaNode = (MediaNode) masterNode;

					NCLRegion nclRegion = createMediaNCLRegion(nclRegBase, mediaNode);
					NCLDescriptor nclDescriptor = createMediaNCLDescriptor(nclDescBase, temporalChain, mediaNode, nclRegion);
					NCLMedia nclMedia = createNCLMedia(mediaNode, nclDescriptor);
					nclPort.setComponent(nclMedia);

				}else {

					SensoryEffectNode effectNode = (SensoryEffectNode) masterNode;

					NCLRegion nclRegion = createEffectNCLRegion(nclRegBase, effectNode);
					NCLDescriptor nclDescriptor = createEffectNCLDescriptor(nclDescBase, temporalChain, effectNode, nclRegion);
					NCLEffect nclEffect = createNCLEffect(effectNode, nclDescriptor);
					nclPort.setComponent(nclEffect);

				}

			}
		    
		    nclBody.addPort(nclPort);
		    
		}
	}

	private void createNCLNodeDescriptorRegion(NCLBody nclBody,
											   NCLRegionBase nclRegBase, NCLDescriptorBase nclDescBase,
											   TemporalChain temporalChain) throws XMLException {
		
		for(MediaNode mediaNode :  temporalChain.getMediaNodeAllList()){
			
			NCLRegion nclRegion = createMediaNCLRegion(nclRegBase, mediaNode);
			NCLDescriptor nclDescriptor = createMediaNCLDescriptor(nclDescBase, temporalChain, mediaNode, nclRegion);
			NCLMedia nclMedia = createNCLMedia(mediaNode, nclDescriptor);
			
			nclRegBase.addRegion(nclRegion);
			nclDescBase.addDescriptor(nclDescriptor);
			nclBody.addNode(nclMedia);
			
		}

		for(SensoryEffectNode effectNode : temporalChain.getSensoryEffectNodeAllList()){

			NCLRegion nclRegion = createEffectNCLRegion(nclRegBase, effectNode);
			NCLDescriptor nclDescriptor = createEffectNCLDescriptor(nclDescBase, temporalChain, effectNode, nclRegion);
			NCLEffect nclEffect = createNCLEffect(effectNode, nclDescriptor);

			nclRegBase.addRegion(nclRegion);
			nclDescBase.addDescriptor(nclDescriptor);
			nclBody.addNode(nclEffect);

		}
	}

	private NCLEffect createNCLEffect(SensoryEffectNode effectNode, NCLDescriptor nclDescriptor) throws XMLException {

		NCLEffect nclEffect = new NCLEffect();
		nclEffect.setId(effectNode.getNCLName());
		nclEffect.setType(NCLEffectType.getEnumType(effectNode.getType().toString()));
		nclEffect.setDescriptor(nclDescriptor);

		return nclEffect;

	}

	private NCLDescriptor createEffectNCLDescriptor(NCLDescriptorBase nclDescBase, TemporalChain temporalChain, SensoryEffectNode effectNode, NCLRegion nclRegion) throws XMLException {

		NCLDescriptor nclDescriptor = new NCLDescriptor();
		nclDescriptor.setId("desc_" + effectNode.getNCLName());
		nclDescriptor.setRegion(nclRegion);
		if(!isThereRelationFinishesMedia(effectNode, temporalChain)){
			nclDescriptor.setExplicitDur(new TimeType(effectNode.getDuration()));
		}

		NCLDescriptorParam priorityParam = new NCLDescriptorParam();
		priorityParam.setName(NCLAttributes.PRIORITY);
		priorityParam.setValue(effectNode.getPresentationProperty().getPriority());

		NCLDescriptorParam intensityValueParam = null;
		NCLDescriptorParam intensityRangeParam = null;
		String intensityRangeAsString;

		switch (effectNode.getType()) {

			case WIND:

				WindPresentationProperty windEffectProperty = (WindPresentationProperty) effectNode.getPresentationProperty();

				intensityValueParam = new NCLDescriptorParam();
				intensityValueParam.setName(NCLAttributes.INTENSITY_VALUE);
				intensityValueParam.setValue(windEffectProperty.getIntensityValue().getValue());

				intensityRangeParam = new NCLDescriptorParam();
				intensityRangeParam.setName(NCLAttributes.INTENSITY_RANGE);
				intensityRangeAsString = windEffectProperty.getIntensityRange().getFromValue() + " " +
						windEffectProperty.getIntensityRange().getToValue();
				intensityRangeParam.setValue(intensityRangeAsString);

				break;
			case WATER_SPRAYER:

				WaterSprayerPresentationProperty waterSprayerEffectProperty = (WaterSprayerPresentationProperty) effectNode.getPresentationProperty();

				intensityValueParam = new NCLDescriptorParam();
				intensityValueParam.setName(NCLAttributes.INTENSITY_VALUE);
				intensityValueParam.setValue(waterSprayerEffectProperty.getIntensityValue().getValue());

				intensityRangeParam = new NCLDescriptorParam();
				intensityRangeParam.setName(NCLAttributes.INTENSITY_RANGE);
				intensityRangeAsString = waterSprayerEffectProperty.getIntensityRange().getFromValue() + " " +
						waterSprayerEffectProperty.getIntensityRange().getToValue();
				intensityRangeParam.setValue(intensityRangeAsString);

				break;
			case VIBRATION:

				VibrationPresentationProperty vibrationPresentationProperty = (VibrationPresentationProperty) effectNode.getPresentationProperty();

				intensityValueParam = new NCLDescriptorParam();
				intensityValueParam.setName(NCLAttributes.INTENSITY_VALUE);
				intensityValueParam.setValue(vibrationPresentationProperty.getIntensityValue().getValue());

				intensityRangeParam = new NCLDescriptorParam();
				intensityRangeParam.setName(NCLAttributes.INTENSITY_RANGE);
				intensityRangeAsString = vibrationPresentationProperty.getIntensityRange().getFromValue() + " " +
						vibrationPresentationProperty.getIntensityRange().getToValue();
				intensityRangeParam.setValue(intensityRangeAsString);

				break;
			case COLD:

				ColdPresentationProperty coldPresentationProperty = (ColdPresentationProperty) effectNode.getPresentationProperty();

				intensityValueParam = new NCLDescriptorParam();
				intensityValueParam.setName(NCLAttributes.INTENSITY_VALUE);
				intensityValueParam.setValue(coldPresentationProperty.getIntensityValue().getValue());

				intensityRangeParam = new NCLDescriptorParam();
				intensityRangeParam.setName(NCLAttributes.INTENSITY_RANGE);
				intensityRangeAsString = coldPresentationProperty.getIntensityRange().getFromValue() + " " +
						coldPresentationProperty.getIntensityRange().getToValue();
				intensityRangeParam.setValue(intensityRangeAsString);

				break;
			case HOT:

				HotPresentationProperty hotPresentationProperty = (HotPresentationProperty) effectNode.getPresentationProperty();

				intensityValueParam = new NCLDescriptorParam();
				intensityValueParam.setName(NCLAttributes.INTENSITY_VALUE);
				intensityValueParam.setValue(hotPresentationProperty.getIntensityValue().getValue());

				intensityRangeParam = new NCLDescriptorParam();
				intensityRangeParam.setName(NCLAttributes.INTENSITY_RANGE);
				intensityRangeAsString = hotPresentationProperty.getIntensityRange().getFromValue() + " " +
						hotPresentationProperty.getIntensityRange().getToValue();
				intensityRangeParam.setValue(intensityRangeAsString);

				break;
			case SCENT:

				ScentPresentationProperty scentPresentationProperty = (ScentPresentationProperty) effectNode.getPresentationProperty();

				intensityValueParam = new NCLDescriptorParam();
				intensityValueParam.setName(NCLAttributes.INTENSITY_VALUE);
				intensityValueParam.setValue(scentPresentationProperty.getIntensityValue().getValue());

				intensityRangeParam = new NCLDescriptorParam();
				intensityRangeParam.setName(NCLAttributes.INTENSITY_RANGE);
				intensityRangeAsString = scentPresentationProperty.getIntensityRange().getFromValue() + " " +
						scentPresentationProperty.getIntensityRange().getToValue();
				intensityRangeParam.setValue(intensityRangeAsString);

				NCLDescriptorParam scentTypeParam = new NCLDescriptorParam();
				scentTypeParam.setName(NCLAttributes.SCENT);
				scentTypeParam.setValue(scentPresentationProperty.getScentType());

				nclDescriptor.addDescriptorParam(scentTypeParam);

				break;
			case LIGHT:

				LightPresentationProperty lightPresentationProperty = (LightPresentationProperty) effectNode.getPresentationProperty();

				intensityValueParam = new NCLDescriptorParam();
				intensityValueParam.setName(NCLAttributes.INTENSITY_VALUE);
				intensityValueParam.setValue(lightPresentationProperty.getIntensityValue().getValue());

				intensityRangeParam = new NCLDescriptorParam();
				intensityRangeParam.setName(NCLAttributes.INTENSITY_RANGE);
				intensityRangeAsString = lightPresentationProperty.getIntensityRange().getFromValue() + " " +
						lightPresentationProperty.getIntensityRange().getToValue();
				intensityRangeParam.setValue(intensityRangeAsString);

				NCLDescriptorParam colorParam = new NCLDescriptorParam();
				colorParam.setName(NCLAttributes.COLOR);
				//TODO verify value returned for colors
				colorParam.setValue(lightPresentationProperty.getColor());

				nclDescriptor.addDescriptorParam(colorParam);

				break;
			case FOG:

				FogPresentationProperty fogPresentationProperty = (FogPresentationProperty) effectNode.getPresentationProperty();

				intensityValueParam = new NCLDescriptorParam();
				intensityValueParam.setName(NCLAttributes.INTENSITY_VALUE);
				intensityValueParam.setValue(fogPresentationProperty.getIntensityValue().getValue());

				intensityRangeParam = new NCLDescriptorParam();
				intensityRangeParam.setName(NCLAttributes.INTENSITY_RANGE);
				intensityRangeAsString = fogPresentationProperty.getIntensityRange().getFromValue() + " " +
						fogPresentationProperty.getIntensityRange().getToValue();
				intensityRangeParam.setValue(intensityRangeAsString);

				break;
			case FLASH:

				FlashPresentationProperty flashPresentationProperty = (FlashPresentationProperty) effectNode.getPresentationProperty();

				intensityValueParam = new NCLDescriptorParam();
				intensityValueParam.setName(NCLAttributes.INTENSITY_VALUE);
				intensityValueParam.setValue(flashPresentationProperty.getIntensityValue().getValue());

				intensityRangeParam = new NCLDescriptorParam();
				intensityRangeParam.setName(NCLAttributes.INTENSITY_RANGE);
				intensityRangeAsString = flashPresentationProperty.getIntensityRange().getFromValue() + " " +
						flashPresentationProperty.getIntensityRange().getToValue();
				intensityRangeParam.setValue(intensityRangeAsString);

				NCLDescriptorParam frequencyParam = new NCLDescriptorParam();
				frequencyParam.setName(NCLAttributes.FREQUENCY);
				frequencyParam.setValue(flashPresentationProperty.getFrequency());

				NCLDescriptorParam flashColorParam = new NCLDescriptorParam();
				flashColorParam.setName(NCLAttributes.COLOR);
				//TODO verify value returned for colors
				flashColorParam.setValue(flashPresentationProperty.getColor());

				nclDescriptor.addDescriptorParam(frequencyParam);
				nclDescriptor.addDescriptorParam(flashColorParam);

				break;
			case RAINSTORM:
				//TODO remove this case since the rainstorm defines two different
				// effects in the NCL 4.0: water and flash. This case not yet implemented.
				break;
			default:
				break;
		}

		nclDescriptor.addDescriptorParam(priorityParam);
		if(intensityValueParam != null){
			nclDescriptor.addDescriptorParam(intensityValueParam);
		}
		if(intensityRangeParam != null){
			nclDescriptor.addDescriptorParam(intensityRangeParam);
		}

		return nclDescriptor;

	}

	private NCLRegion createEffectNCLRegion(NCLRegionBase nclRegBase, SensoryEffectNode effectNode) throws XMLException{

		NCLRegion nclRegion = new NCLRegion();
		nclRegion.setId("rg_" + effectNode.getNCLName());

		EffectPositionProperty effectPositionProperty = effectNode.getPresentationProperty().getPositionProperty();

		String xPosition = effectPositionProperty.getxPosition().toString();
		String normalizedXPosition = xPosition.replaceAll("\\s+","");
		normalizedXPosition = normalizedXPosition.toLowerCase(Locale.ROOT);

		String yPosition = effectPositionProperty.getyPosition().toString();
		String normalizedYPosition = yPosition.replaceAll("\\s+","");
		normalizedYPosition = normalizedYPosition.toLowerCase(Locale.ROOT);

		String zPosition = effectPositionProperty.getzPosition().toString();
		String normalizedZPosition = zPosition.replaceAll("\\s+","");
		normalizedZPosition = normalizedZPosition.toLowerCase(Locale.ROOT);

		String positionValueAsString = normalizedXPosition + ":" + normalizedYPosition + ":" + normalizedZPosition;

		nclRegion.setLocation(positionValueAsString);

		return nclRegion;

	}

	private void createNCLLinks(NCLBody nclBody, NCLImportBase nclImportBase, TemporalChain temporalChain, Boolean isForHTMLExport) throws XMLException {

		NCLDoc casualConnectorBaseNCLDoc = nclImportBase.getImportedDoc();
		NCLHead connectorBaseNCLHead = casualConnectorBaseNCLDoc.getHead();
		NCLConnectorBase nclConnectorBaseOfImportedBase = connectorBaseNCLHead.getConnectorBase();

		NCLCausalConnector importedNCLCausalConnector;
		ExternalReferenceType externalReferenceType;
		NCLBind conditionNCLBind;
		NCLLinkParam nclLinkParamDelay;

		for(TemporalRelation relation : temporalChain.getRelationList()){

			if(relation instanceof Synchronous){

				Synchronous synchronousRelation = (Synchronous) relation;

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
						conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getPrimaryNode().getNCLName()));
						nclLink.addBind(conditionNCLBind);

						for(Node secondaryNode : synchronousRelation.getSecondaryNodeList()){
							NCLBind startNCLBind = new NCLBind();
			    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.START.toString()));
			    			startNCLBind.setComponent(nclBody.findNode(secondaryNode.getNCLName()));
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
						conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getPrimaryNode().getNCLName()));
						nclLink.addBind(conditionNCLBind);

						for(Node secondaryNode : synchronousRelation.getSecondaryNodeList()){
							NCLBind startNCLBind = new NCLBind();
			    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.START.toString()));
			    			startNCLBind.setComponent(nclBody.findNode(secondaryNode.getNCLName()));
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
						conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getPrimaryNode().getNCLName()));
						nclLink.addBind(conditionNCLBind);

						for(Node secondaryNode : synchronousRelation.getSecondaryNodeList()){
							NCLBind startNCLBind = new NCLBind();
			    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.STOP.toString()));
			    			startNCLBind.setComponent(nclBody.findNode(secondaryNode.getNCLName()));
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
						conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getPrimaryNode().getNCLName()));
						nclLink.addBind(conditionNCLBind);

						for(Node secondaryNode : synchronousRelation.getSecondaryNodeList()){
							NCLBind startNCLBind = new NCLBind();
			    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.STOP.toString()));
			    			startNCLBind.setComponent(nclBody.findNode(secondaryNode.getNCLName()));
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
						conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getPrimaryNode().getNCLName()));
						nclLink.addBind(conditionNCLBind);

						for(Node secondaryNode : synchronousRelation.getSecondaryNodeList()){
							NCLBind startNCLBind = new NCLBind();
			    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.START.toString()));
			    			startNCLBind.setComponent(nclBody.findNode(secondaryNode.getNCLName()));
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
						conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getPrimaryNode().getNCLName()));
						nclLink.addBind(conditionNCLBind);

						for(Node secondaryNode : synchronousRelation.getSecondaryNodeList()){
							NCLBind startNCLBind = new NCLBind();
			    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.START.toString()));
			    			startNCLBind.setComponent(nclBody.findNode(secondaryNode.getNCLName()));
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
						conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getPrimaryNode().getNCLName()));
						nclLink.addBind(conditionNCLBind);

						for(Node secondaryNode : synchronousRelation.getSecondaryNodeList()){
							NCLBind startNCLBind = new NCLBind();
			    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.STOP.toString()));
			    			startNCLBind.setComponent(nclBody.findNode(secondaryNode.getNCLName()));
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
						conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getPrimaryNode().getNCLName()));
						nclLink.addBind(conditionNCLBind);

						for(Node secondaryNode : synchronousRelation.getSecondaryNodeList()){
							NCLBind startNCLBind = new NCLBind();
			    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.STOP.toString()));
			    			startNCLBind.setComponent(nclBody.findNode(secondaryNode.getNCLName()));
			    			nclLink.addBind(startNCLBind);
						}

						nclLinkParamDelay = new NCLLinkParam();
						nclLinkParamDelay.setName(importedNCLCausalConnector.getConnectorParam(DELAY));
						nclLinkParamDelay.setValue(new TimeType(synchronousRelation.getDelay()));
						nclLink.addLinkParam(nclLinkParamDelay);

						nclBody.addLink(nclLink);

						break;

					case BEFORE:

						for(int i = 0; i < synchronousRelation.getSecondaryNodeList().size(); i++){

							NCLLink beforeNCLLink = new NCLLink<>();
							beforeNCLLink.setId("link_" + synchronousRelation.getId() + i);

							Node secondaryNode = synchronousRelation.getSecondaryNodeList().get(i);

							importedNCLCausalConnector = nclConnectorBaseOfImportedBase.getCausalConnector(ImportedNCLCausalConnectorType.ONEND_START_DELAY.getDescription());

							if(!isForHTMLExport){
								externalReferenceType = new ExternalReferenceType<>(nclImportBase, importedNCLCausalConnector);
								beforeNCLLink.setXconnector(externalReferenceType);
							}else {
								beforeNCLLink.setXconnector(importedNCLCausalConnector);
							}

							conditionNCLBind = new NCLBind();
							conditionNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultConditionRole.ONEND.toString()));

							if(i == 0){
								conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getPrimaryNode().getNCLName()));
							}else {
								conditionNCLBind.setComponent(nclBody.findNode(synchronousRelation.getSecondaryNodeList().get(i-1).getNCLName()));
							}

							beforeNCLLink.addBind(conditionNCLBind);

							NCLBind startNCLBind = new NCLBind();
			    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.START.toString()));
			    			startNCLBind.setComponent(nclBody.findNode(secondaryNode.getNCLName()));
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

				Interactivity<MediaNode> interactivityRelation = (Interactivity<MediaNode>) relation;

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
					conditionNCLBind.setComponent(nclBody.findNode(interactivityRelation.getPrimaryNode().getNCLName()));
					NCLBindParam nclBindParam = new NCLBindParam();
					nclBindParam.setName(importedNCLCausalConnector.getConnectorParam("interactivityKey"));
					nclBindParam.setValue(interactivityRelation.getInteractivityKey().toString().toUpperCase());
					conditionNCLBind.addBindParam(nclBindParam);
					nclLink.addBind(conditionNCLBind);

					for(TemporalChain temporalChainToBeStarted : interactivityRelation.getTemporalChainList()){
						NCLBind startNCLBind = new NCLBind();
		    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.START.toString()));
		    			startNCLBind.setComponent(nclBody.findNode(temporalChainToBeStarted.getMasterNode().getNCLName()));
		    			nclLink.addBind(startNCLBind);
					}

					for(Node secondaryNode : interactivityRelation.getSecondaryNodeList()){
						NCLBind stopNCLBind = new NCLBind();
		    			stopNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.STOP.toString()));
		    			stopNCLBind.setComponent(nclBody.findNode(secondaryNode.getNCLName()));
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
					conditionNCLBind.setComponent(nclBody.findNode(interactivityRelation.getPrimaryNode().getNCLName()));

					NCLBindParam nclBindParam = new NCLBindParam();
					nclBindParam.setName(importedNCLCausalConnector.getConnectorParam("interactivityKey"));
					nclBindParam.setValue(interactivityRelation.getInteractivityKey());
					conditionNCLBind.addBindParam(nclBindParam);
					nclLink.addBind(conditionNCLBind);

					for(TemporalChain temporalChainToBeStarted : interactivityRelation.getTemporalChainList()){
						NCLBind startNCLBind = new NCLBind();
		    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.START.toString()));
		    			startNCLBind.setComponent(nclBody.findNode(temporalChainToBeStarted.getMasterNode().getNCLName()));
		    			nclLink.addBind(startNCLBind);
					}

					for(Node secondaryNode : interactivityRelation.getSecondaryNodeList()){
						NCLBind stopNCLBind = new NCLBind();
		    			stopNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.STOP.toString()));
		    			stopNCLBind.setComponent(nclBody.findNode(secondaryNode.getNCLName()));
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
		
		for(Node node :  temporalChain.getMediaNodeAllList()){
			
			if(!node.equals(temporalChain.getMasterNode()) && !isThereRelationStartsNode(node, temporalChain)){
				
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
				conditionNCLBind.setComponent(nclBody.findNode(temporalChain.getMasterNode().getNCLName()));
				nclLink.addBind(conditionNCLBind);

				NCLBind startNCLBind = new NCLBind();
    			startNCLBind.setRole(importedNCLCausalConnector.findRole(NCLDefaultActionRole.START.toString()));
    			startNCLBind.setComponent(nclBody.findNode(node.getNCLName()));
    			nclLink.addBind(startNCLBind);
				
				nclLinkParamDelay = new NCLLinkParam();
				nclLinkParamDelay.setName(importedNCLCausalConnector.getConnectorParam(DELAY));
				nclLinkParamDelay.setValue(new TimeType(getDelayBetweenMasterAndSecondary(node, temporalChain)));
				nclLink.addLinkParam(nclLinkParamDelay);
				
				nclBody.addLink(nclLink);
				
			}
			
		}

	}
	
	private Double getDelayBetweenMasterAndSecondary(Node node, TemporalChain temporalChain){
		
		Double mediaBegin = node.getBegin();
		Double masterMediaBegin = temporalChain.getMasterNode().getBegin();
	
		return mediaBegin - masterMediaBegin;
		
	}
	
	private boolean isThereRelationStartsNode(Node node, TemporalChain temporalChain){

		for(TemporalRelation relation :  temporalChain.getRelationList()){
			
			if(relation instanceof Synchronous){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				TemporalRelationType relationType = synchronousRelation.getType();
				
				if(relationType == TemporalRelationType.STARTS || relationType == TemporalRelationType.STARTS_DELAY ||relationType == TemporalRelationType.MEETS
						   || relationType == TemporalRelationType.MEETS_DELAY || relationType == TemporalRelationType.BEFORE){
					
					if(synchronousRelation.getSecondaryNodeList().contains(node)){
						 return true;
					}
					
				}
				
			}
			
		}
		
		return false;
		
	}
	
	private NCLMedia createNCLMedia(MediaNode mediaNode, NCLDescriptor nclDescriptor) throws XMLException {
		
		NCLMedia nclMedia = new NCLMedia();
		nclMedia.setId(mediaNode.getNCLName());
		nclMedia.setSrc(new SrcType("media/" + mediaNode.getFile().getAbsoluteFile().getName()));
		if(mediaNode.getMimeType() != null){
			nclMedia.setType(NCLMimeType.getEnumType(mediaNode.getMimeType().toString()));
		}
		nclMedia.setDescriptor(nclDescriptor);
		return nclMedia;
		
	}

	private NCLDescriptor createMediaNCLDescriptor(NCLDescriptorBase nclDescBase, TemporalChain temporalChain, MediaNode mediaNode, NCLRegion nclRegion) throws XMLException {
		
		NCLDescriptor nclDescriptor = new NCLDescriptor();
		nclDescriptor.setId("desc_" + mediaNode.getNCLName());
		nclDescriptor.setRegion(nclRegion);
		if(!isThereRelationFinishesMedia(mediaNode, temporalChain)){
			nclDescriptor.setExplicitDur(new TimeType(mediaNode.getDuration()));
		}
		NCLDescriptorParam transparencyParam = new NCLDescriptorParam();
		transparencyParam.setName(NCLAttributes.TRANSPARENCY);
		transparencyParam.setValue(mediaNode.getPresentationProperty().getStyleProperty().getTransparency()/100);

		NCLDescriptorParam fitParam = new NCLDescriptorParam();
		fitParam.setName(NCLAttributes.FIT);
		AspectRatio aspectRatio = mediaNode.getPresentationProperty().getSizeProperty().getAspectRatio();
		//if(aspectRatio != AspectRatio.NONE){
		fitParam.setValue(mediaNode.getPresentationProperty().getSizeProperty().getAspectRatio().toString().toLowerCase());
		//}
		
		nclDescriptor.addDescriptorParam(transparencyParam);
		nclDescriptor.addDescriptorParam(fitParam);
		
		return nclDescriptor;
		
	}

	private NCLRegion createMediaNCLRegion(NCLRegionBase nclRegBase, MediaNode mediaNode) throws XMLException {
		
		NCLRegion nclRegion = new NCLRegion();
		nclRegion.setId("rg_" + mediaNode.getNCLName());
		
		MediaPositionProperty mediaPositionProperty= mediaNode.getPresentationProperty().getPositionProperty();
		SizeProperty mediaSizeProperty = mediaNode.getPresentationProperty().getSizeProperty();
		
		nclRegion.setLeft(treatPositionSizeValue(mediaPositionProperty.getLeft()));
		nclRegion.setRight(treatPositionSizeValue(mediaPositionProperty.getRight()));
		nclRegion.setTop(treatPositionSizeValue(mediaPositionProperty.getTop()));
		nclRegion.setBottom(treatPositionSizeValue(mediaPositionProperty.getBottom()));
		nclRegion.setWidth(treatPositionSizeValue(mediaSizeProperty.getWidth()));
		nclRegion.setHeight(treatPositionSizeValue(mediaSizeProperty.getHeight()));
		nclRegion.setzIndex(mediaPositionProperty.getOrderZ());
		
		return nclRegion;
		
	}

	//TODO test this method
	private Boolean isThereRelationFinishesMedia(Node node, TemporalChain temporalChain){

		for(TemporalRelation relation :  temporalChain.getRelationList()){
			
			if(relation instanceof Synchronous){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				TemporalRelationType relationType = synchronousRelation.getType();
				
				if(relationType.equals(TemporalRelationType.MET_BY) || relationType.equals(TemporalRelationType.MET_BY_DELAY)
				   || relationType.equals(TemporalRelationType.FINISHES) || relationType.equals(TemporalRelationType.FINISHES_DELAY)){
					
					if(synchronousRelation.getSecondaryNodeList().contains(node)){
						 return true;
					}
					
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

				MessageDialog messageDialog = new MessageDialog(Language.translate("error.during.the.export"), 
						Language.translate("could.not.find.the.ncl.document.directory") + ": " + e.getMessage(), "OK", 250);
		        messageDialog.showAndWait();
		        
			}
            
        }

	}

	public void copyMediaFiles(String mediaDir) throws FileNotFoundException, IOException {
		
		for(TemporalChain temporalChain : spatialTemporalApplication.getTemporalChainList()){
			
			for(MediaNode mediaNode :  temporalChain.getMediaNodeAllList()){

			    fileInputStream = new FileInputStream(mediaNode.getFile());
				FileChannel sourceChannel = fileInputStream.getChannel();
				fileOutputStream = new FileOutputStream(new File(mediaDir + "/" + mediaNode.getFile().getAbsoluteFile().getName()));
				FileChannel destChannel = fileOutputStream.getChannel();
		        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
		        sourceChannel.close();
		        destChannel.close();
				
			}
			
		}
		
	}

}