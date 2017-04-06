package model.HTMLSupport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

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

import model.NCLSupport.NCLExportEventHandler;
import model.common.SpatialTemporalView;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import view.common.Language;
import view.common.MessageDialog;
import view.common.ReturnMessage;
import view.utility.AnimationUtil;
import br.uff.midiacom.ana.NCLDoc;

public class HTMLExportEventHandler implements EventHandler<ActionEvent>{

	final Logger logger = LoggerFactory.getLogger(HTMLExportEventHandler.class);
	
	private static final String EXPORTED_JSON = "saida.json";
	private static final String EXPORTED_NCL_COMPLEMENTS = "ncl-complements.js";
	private static final String EXPORTED_HTML_DOCUMENT = " Exported HTML Document";
	private static final String TEMP_NCL_DOCUMENT = "tempNCLDocument";
	
	static Document document;
	private NCLExportEventHandler nclExportEventHandler;
	
	public HTMLExportEventHandler(SpatialTemporalView spatialTemporalView){

		nclExportEventHandler = new NCLExportEventHandler(spatialTemporalView);
	}

	@Override
	public void handle(ActionEvent event) {
		
		File tempNCLDocumentFile = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //factory.setNamespaceAware(true);
        //factory.setValidating(true);
        	
        File stylesheet = new File("src/model/HTMLSupport/XSLFile/ncl4web.xsl");
        String tempNCLDocumentDir = "";
        
        try {
        	
        	NCLDoc nclDoc = nclExportEventHandler.exportToNCL(true);
        
        	String nclCode = nclDoc.parse(0);
		
        	if(nclCode != null){
        	
        		FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle(Language.translate("export.html5.document"));
                File file = fileChooser.showSaveDialog(null);
 
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
	        
	        return;
	        
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
                
                String exportedHTMLDocumentDir = tempNCLDocumentDir + "/" + EXPORTED_HTML_DOCUMENT;
				String mediaDir = exportedHTMLDocumentDir + "/media";
				Boolean mediaDirCreated = (new File(mediaDir)).mkdirs();
				
				if (mediaDirCreated || (new File(mediaDir)).exists()) {
					nclExportEventHandler.copyMediaFiles(mediaDir);
				}
				
				File auxFile = new File(exportedHTMLDocumentDir + "/" + "HTMLExportado" + ".html");
				System.out.println(auxFile.getAbsolutePath());
				FileWriter fileWriter = new FileWriter(auxFile);
                StreamResult result = new StreamResult();
                result.setWriter(fileWriter);
                transformer.transform(source, result);
                
                
                File jsonSource = new File("src/view/HTMLSupport/saida.json");
                
                File dest = new File(exportedHTMLDocumentDir);
                
                File jsSource = new File("src/view/HTMLSupport/ncl-complements.js");
                                
                File jQuerySource = new File("src/view/HTMLSupport/jquery.min.js");
                
                try {
                	FileUtils.copyFileToDirectory(jsonSource, dest);
                	FileUtils.copyFileToDirectory(jsSource, dest);
                	FileUtils.copyFileToDirectory(jQuerySource, dest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                

                
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
        } catch (SAXException sxe) {
            // Error generated by this application
            // (or a parser-initialization error)
            Exception x = sxe;

            if (sxe.getException() != null) {
                x = sxe.getException();
            }

            x.printStackTrace();
        } catch (ParserConfigurationException pce) {
            // Parser with specified options can't be built
            pce.printStackTrace();
        } catch (IOException ioe) {
            // I/O error
            ioe.printStackTrace();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}


}
