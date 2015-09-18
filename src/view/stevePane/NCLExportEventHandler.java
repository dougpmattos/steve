package view.stevePane;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import model.common.Media;
import model.spatialView.PositionProperty;
import model.spatialView.SizeProperty;
import model.spatialView.enums.Size;
import model.temporalView.Asynchronous;
import model.temporalView.Relation;
import model.temporalView.Synchronous;
import model.temporalView.TemporalChain;
import model.temporalView.TemporalView;
import model.temporalView.enums.RelationType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import view.common.Language;
import view.common.MessageDialog;
import br.uff.midiacom.ana.NCLBody;
import br.uff.midiacom.ana.NCLDoc;
import br.uff.midiacom.ana.NCLHead;
import br.uff.midiacom.ana.connector.NCLConnectorBase;
import br.uff.midiacom.ana.descriptor.NCLDescriptor;
import br.uff.midiacom.ana.descriptor.NCLDescriptorBase;
import br.uff.midiacom.ana.descriptor.NCLDescriptorParam;
import br.uff.midiacom.ana.interfaces.NCLPort;
import br.uff.midiacom.ana.node.NCLMedia;
import br.uff.midiacom.ana.region.NCLRegion;
import br.uff.midiacom.ana.region.NCLRegionBase;
import br.uff.midiacom.ana.util.SrcType;
import br.uff.midiacom.ana.util.TimeType;
import br.uff.midiacom.ana.util.enums.NCLAttributes;
import br.uff.midiacom.ana.util.enums.NCLMimeType;
import br.uff.midiacom.ana.util.enums.NCLUriType;
import br.uff.midiacom.ana.util.exception.XMLException;

@SuppressWarnings({"rawtypes", "unchecked"})
public class NCLExportEventHandler implements EventHandler<ActionEvent>{

	final Logger logger = LoggerFactory.getLogger(NCLExportEventHandler.class);

	private TemporalView temporalView;
	
	public NCLExportEventHandler(TemporalView temporalView){
		this.temporalView = temporalView;
	}
	
	@Override
	public void handle(ActionEvent actionEvent) {
		   
		NCLDoc nclDoc = createNCLDoc();
		
    	saveNCLDoc(nclDoc);
    	
    }
	
	private NCLDoc createNCLDoc(){
		
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

            nclHead.addRegionBase(nclRegBaseList.get(0));
            nclHead.setDescriptorBase(nclDescBase);
            nclHead.setConnectorBase(nclConBase);
            
            for(TemporalChain temporalChain : temporalView.getTemporalChainList()){
            	
            	for(Media media :  temporalChain.getMediaAllList()){
            		
            		NCLRegion nclRegion = createNCLRegion(nclRegBase, media);
            		NCLDescriptor nclDescriptor = createNCLDescriptor(nclDescBase, temporalChain, media, nclRegion);
            		NCLMedia nclMedia = createNCLMedia(media, nclDescriptor);
            		
            		nclRegBase.addRegion(nclRegion);
    				nclDescBase.addDescriptor(nclDescriptor);
    				nclBody.addNode(nclMedia);
    				
    				if(temporalChain.getId() == 0){
    					
            			NCLPort nclPort = new NCLPort();
    		            nclPort.setId("port_" + temporalChain.getMasterMedia().getName());
    		            nclPort.setComponent(nclMedia);
    		            
    		            nclBody.addPort(nclPort);
    		            
            		}
            		
            	}
            }
            
        } catch (XMLException ex) {
        	
        	logger.error(ex.getMessage());
        	new MessageDialog(ex.getMessage(), MessageDialog.ICON_INFO);
        	
        }
		
		return nclDoc;
		
	}

	private NCLMedia createNCLMedia(Media media,
			NCLDescriptor nclDescriptor) throws XMLException {
		NCLMedia nclMedia = new NCLMedia();
		nclMedia.setId(media.getName());
		nclMedia.setSrc(new SrcType(NCLUriType.FILE, media.getName()));
		if(media.getMimeType() != null){
			nclMedia.setType(NCLMimeType.getEnumType(media.getMimeType().toString()));
		}
		nclMedia.setDescriptor(nclDescriptor);
		return nclMedia;
	}

	private NCLDescriptor createNCLDescriptor(NCLDescriptorBase nclDescBase, TemporalChain temporalChain, Media media, NCLRegion nclRegion) throws XMLException {
		
		NCLDescriptor nclDescriptor = new NCLDescriptor();
		nclDescriptor.setId("desc_" + media.getName());
		nclDescriptor.setRegion(nclRegion);
		if(!isThereRelationFinishesMedia(media, temporalChain)){
			nclDescriptor.setExplicitDur(new TimeType(media.getDuration()));
		}
		NCLDescriptorParam nclDescriptorParam = new NCLDescriptorParam();
		nclDescriptorParam.setName(NCLAttributes.TRANSPARENCY);
		nclDescriptorParam.setValue(media.getPresentationProperty().getStyleProperty().getTransparency()/100);
		nclDescriptor.addDescriptorParam(nclDescriptorParam);
		
		return nclDescriptor;
		
	}

	private NCLRegion createNCLRegion(NCLRegionBase nclRegBase, Media media) throws XMLException { 
		
		NCLRegion nclRegion = new NCLRegion();
		nclRegion.setId("rg_" + media.getName());
		
		PositionProperty mediaPositionProperty= media.getPresentationProperty().getPositionProperty();
		SizeProperty mediaSizeProperty = media.getPresentationProperty().getSizeProperty();
		
		nclRegion.setLeft(treatPositionSizeValue(mediaPositionProperty.getLeft()));
		nclRegion.setRight(treatPositionSizeValue(mediaPositionProperty.getRight()));
		nclRegion.setTop(treatPositionSizeValue(mediaPositionProperty.getTop()));
		nclRegion.setBottom(treatPositionSizeValue(mediaPositionProperty.getBottom()));
		nclRegion.setWidth(treatPositionSizeValue(mediaSizeProperty.getWidth()));
		nclRegion.setHeight(treatPositionSizeValue(mediaSizeProperty.getHeight()));
		
		return nclRegion;
		
	}
	
	private Boolean isThereRelationFinishesMedia(Media media, TemporalChain temporalChain){

		for(Relation relation :  temporalChain.getRelationList()){
			
			if(relation instanceof Synchronous){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				RelationType relationType = synchronousRelation.getType();
				
				if(relationType.equals(RelationType.MET_BY) || relationType.equals(RelationType.MET_BY_DELAY)
				   || relationType.equals(RelationType.FINISHES) || relationType.equals(RelationType.FINISHES_DELAY)){
					
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
				
					File auxFile = new File(file.getAbsolutePath() + ".ncl");
					FileWriter fileWriter = new FileWriter(auxFile);
					fileWriter.write(nclCode);
                    fileWriter.close();
                    
				}
                
			} catch (IOException e) {
				logger.error(e.getMessage());
				new MessageDialog(e.getMessage(), MessageDialog.ICON_INFO).showAndWait();
			}
            
        }

	}

}