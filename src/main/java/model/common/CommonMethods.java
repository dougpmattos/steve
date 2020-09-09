package model.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import br.uff.midiacom.ana.NCLDoc;
import view.HTMLSupport.RunWindow;
import view.common.Language;
import view.common.dialogs.MessageDialog;
import view.common.dialogs.ReturnMessage;
import view.utility.AnimationUtil;

public class CommonMethods {

	public static void runApplication(SpatialTemporalApplication spatialTemporalApplication){
		
	 	String htmlPath = "";
		
		RunWindow runWindow = null;
		try {
			runWindow = new RunWindow(createTempHTML(spatialTemporalApplication));
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
	
	private static File createTempHTML(SpatialTemporalApplication spatialTemporalApplication) throws org.xml.sax.SAXException{
		
		final Logger logger = LoggerFactory.getLogger(HTMLExportEventHandler.class);
		
		final Document document;
		final NCLExportEventHandler nclExportEventHandler;
		
		nclExportEventHandler = new NCLExportEventHandler(spatialTemporalApplication);
		
		File tempNCLDocumentFile = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                
        File stylesheet = new File("xslFiles/ncl4web.xsl");
        String tempNCLDocumentDir = "";
        File auxF = null;
        
        try {
        	
        	NCLDoc nclDoc = nclExportEventHandler.exportToNCL(true);
        
        	String nclCode = nclDoc.parse(0);
		
        	if(nclCode != null){        	        	
        		
        		File file = new File("src/view/HTMLSupport/");        		
 
        		if(file != null){
			
					tempNCLDocumentDir = file.getAbsolutePath();
					tempNCLDocumentFile = new File(tempNCLDocumentDir + ".ncl");
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
