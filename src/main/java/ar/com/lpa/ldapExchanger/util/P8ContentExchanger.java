package ar.com.lpa.ldapExchanger.util;

import ar.com.lpa.ldapExchanger.model.fnObjects.P8Realm;

import com.filenet.api.collection.*;
import com.filenet.api.constants.*;
import com.filenet.api.core.*;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.meta.PropertyDescription;
import com.filenet.api.property.Property;
import com.filenet.api.util.Id;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class P8ContentExchanger {
    private static final Logger logger = Logger.getLogger(P8ContentExchanger.class);
    private static final P8Realm p8realm = new P8Realm();

    public static void documentContentExchange(String osName, String documentSearch, String filePath) {
        try{
            logger.info(String.format("Searching a Document from Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,documentSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        Document sourceDoc = (Document) it.next();
                        downloadContentElements(sourceDoc,filePath);
                        logger.info(String.format("Source Document - Name: %s, Class: %s, Mime type: %s, # Properties: %d",
                                sourceDoc.get_Name(),sourceDoc.getClassName() ,sourceDoc.get_MimeType() ,sourceDoc.getProperties().size()));
                        FolderSet folderSet = sourceDoc.get_FoldersFiledIn();
                        ObjectStore os = Factory.ObjectStore.fetchInstance(p8realm.getP8domain().getDomain(), osName, null);
                        Document targetDoc = Factory.Document.createInstance(os, sourceDoc.getClassName());
                        setDocumentSystemProperties(sourceDoc,targetDoc);
                        setDocumentCustomProperties(sourceDoc,targetDoc);
                        String fileName = filePath + sourceDoc.get_Id().toString() + getContentFileExtension(sourceDoc);
                        String mimeType = Files.probeContentType(new File(fileName).toPath());
                        targetDoc.set_MimeType(mimeType);
                        setContentElementsToDocument(targetDoc, fileName);
                        deleteDocument(sourceDoc);
                        instanceDocument(targetDoc);
                        fileDocumentInFolders(folderSet, targetDoc);
                    } while (it.hasNext()) ;
                }
                logger.info("Total Documents: " + count);
            }
            else logger.info("No documents were found!");
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }

    private static void setDocumentSystemProperties(Document sourceDoc, Document targetDoc){
        targetDoc.getProperties().putValue("Id", sourceDoc.get_Id());
        targetDoc.getProperties().putValue("DocumentTitle", sourceDoc.get_Name());
        targetDoc.set_Owner(sourceDoc.get_Owner());
        targetDoc.set_DateCreated(sourceDoc.get_DateCreated());
        targetDoc.set_Creator(sourceDoc.get_Creator());
        targetDoc.set_Permissions(sourceDoc.get_Permissions());
        targetDoc.set_StoragePolicy(sourceDoc.get_StoragePolicy());
        targetDoc.set_SecurityPolicy(sourceDoc.get_SecurityPolicy());
    }

    private static void downloadContentElements(Document document, String filePath){
        ContentElementList docContentList = document.get_ContentElements();
        Iterator iter = docContentList.iterator();
        String fileName = null;
        try {
            fileName = filePath + document.get_Id().toString() + getContentFileExtension(document);
            FileOutputStream fos = new FileOutputStream(fileName);
            while (iter.hasNext()) {
                ContentTransfer ct = (ContentTransfer) iter.next();
                InputStream stream = ct.accessContentStream();
                byte[] buffer = new byte[4096000];
                int bytesRead = 0;
                int totalBytes = 0;
                while ((bytesRead = stream.read(buffer)) != -1) {
                    totalBytes += bytesRead;
                    fos.write(buffer,0,bytesRead);
                }
                logger.info(String.format("File created: %s, size: %d ", fileName, totalBytes));
                fos.close();
                stream.close();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            //throw new RuntimeException(e);
        }
    }

    private static void setContentElementsToDocument(Document document, String fileName) throws IOException {
        FileInputStream inputStream = new FileInputStream(fileName);
        if (inputStream != null) {
            ContentTransfer contentTransfer = Factory.ContentTransfer.createInstance();
            contentTransfer.setCaptureSource(inputStream);
            contentTransfer.set_RetrievalName(getFileName(fileName));
            ContentElementList contentElementList = Factory.ContentTransfer.createList();
            contentElementList.add(contentTransfer);
            document.set_ContentElements(contentElementList);
        }
    }

    private static void deleteDocument(Document document){
        String docId = document.get_Id().toString();
        document.delete();
        document.save(RefreshMode.NO_REFRESH);
        logger.info(String.format("Document deleted - Id: %s", docId));
    }

    private static void instanceDocument(Document document){
        document.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
        document.save(RefreshMode.REFRESH);
        logger.info(String.format("Document created, Id: %s - Name: %s - Document Class: %s", document.get_Id().toString(),
                document.getProperties().getStringValue("DocumentTitle"), document.getClassName()));

    }





    private static String getContentFileExtension(Document document) {
        String docName = document.get_Name();
        int lastSeparatorIndex = Math.max(docName.lastIndexOf('/'), docName.lastIndexOf('\\'));
        int lastDotIndex = docName.lastIndexOf('.');
        if (lastDotIndex > lastSeparatorIndex) {
            return ""; //Document Name already has an extension
        } else {
            switch (document.get_MimeType()) {
                case "image/jpeg":
                    return ".jpg";
                case "image/jpg":
                    return ".jpg";
                case "image/tiff":
                    return ".tif";
                case "application/pdf":
                    return  ".pdf";
                default:
                    return "txt";
            }
        }

    }

    private static void fileDocumentInFolders(FolderSet folderSet, Document document){
        int count = 0;
        Iterator it = folderSet.iterator();
        while (it.hasNext()){
            count ++;
            Folder folder = (Folder) it.next();
            logger.info(String.format("Doc Id: %s Filed - Folder #: %d, Id: %s",document.get_Id().toString()
                    ,count,folder.get_Id()));
            //Stores above document to the folder
            ReferentialContainmentRelationship rc = folder.file(document, AutoUniqueName.AUTO_UNIQUE, document.get_Name(),
                    DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
            rc.save(RefreshMode.REFRESH);
        }
    }

    private static void setDocumentCustomProperties(Document sourceDoc, Document targetDoc) {
        int count = 0;
        PropertyDescriptionList pdl = sourceDoc.get_ClassDescription().get_PropertyDescriptions();
        Iterator it = pdl.iterator();
        Map<String,TypeID> propertyMap = new HashMap<>(); //Contains all Property Definitions for a Document Class
        while (it.hasNext()) {
            PropertyDescription objPropDesc = (PropertyDescription) it.next();
            if (isCustomPropertyDescription(objPropDesc)){
                propertyMap.put(objPropDesc.get_SymbolicName(),objPropDesc.get_DataType());
            }
        }
        logger.info(String.format("Class: %s -Total custom Properties: %d", sourceDoc.getClassName(),  propertyMap.size()));
        Iterator it1 = sourceDoc.getProperties().iterator();
        while (it1.hasNext()){
            Property property = (Property) it1.next();
            if (propertyMap.containsKey(property.getPropertyName())) {
                TypeID propertyType = propertyMap.get(property.getPropertyName());
                if (propertyType.equals(TypeID.STRING) && property.getStringValue()  != null) {
                    count ++;
                    logger.debug(String.format("Copying Property #: %d, Name: %s, Type: %s, Value: %s",
                            count,property.getPropertyName(),propertyType,property.getStringValue()));
                    targetDoc.getProperties().putValue(property.getPropertyName(),property.getStringValue());
                } else if (propertyType.equals(TypeID.LONG) && property.getInteger32Value() != null) {
                    count ++;
                    logger.debug(String.format("Copying Property #: %d, Name: %s, Type: %s, Value: %d",
                            count,property.getPropertyName(),propertyType,property.getInteger32Value()));
                    targetDoc.getProperties().putValue(property.getPropertyName(),property.getInteger32Value());
                } else if (propertyType.equals(TypeID.BOOLEAN) && property.getBooleanValue() != null) {
                    count ++;
                    logger.debug(String.format("Copying Property #: %d, Name: %s, Type: %s, Value: %s",
                            count,property.getPropertyName(),propertyType,property.getBooleanValue()));
                    targetDoc.getProperties().putValue(property.getPropertyName(),property.getBooleanValue());
                }  else if (propertyType.equals(TypeID.DATE) && property.getDateTimeValue() != null) {
                    count ++;
                    logger.debug(String.format("Copying Property #: %d, Name: %s, Type: %s, Value: %s",
                            count,property.getPropertyName(),propertyType,property.getDateTimeValue()));
                    targetDoc.getProperties().putValue(property.getPropertyName(),property.getDateTimeValue());
                }  else if (propertyType.equals(TypeID.DOUBLE) && property.getFloat64Value() != null) {
                    count ++;
                    logger.debug(String.format("Copying Property #: %d, Name: %s, Type: %s, Value: %f",
                            count,property.getPropertyName(),propertyType,property.getFloat64Value()));
                    targetDoc.getProperties().putValue(property.getPropertyName(),property.getFloat64Value());
                }
            }
        }
    }

    private static boolean isCustomPropertyDescription(PropertyDescription propertyDescription){
        return !(propertyDescription.get_IsHidden() || propertyDescription.get_DataType().equals(TypeID.OBJECT) ||
                propertyDescription.get_IsSystemGenerated() || propertyDescription.get_SymbolicName().equals("DocumentTitle") ||
                propertyDescription.get_SymbolicName().equals("MimeType"));
    }

    private static String getFileName(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File Path cannot be Null or Empty");
        }
        int lastSeparatorIndex = filePath.lastIndexOf("\\"); // Para rutas en Windows
        return filePath.substring(lastSeparatorIndex + 1);
    }

    public static void createP8DocumentInFolder(String osName, String docClass, String folderPath, String inputFilePath) throws FileNotFoundException, IOException {
        ObjectStore os = Factory.ObjectStore.fetchInstance(p8realm.getP8domain().getDomain(), osName, null);
        Folder folder = Factory.Folder.fetchInstance(os,folderPath,null);
        InputStream inputStream = new FileInputStream(inputFilePath);
        String mimeType = Files.probeContentType(new File(inputFilePath).toPath());
        Document document = Factory.Document.createInstance(os, docClass);
        Id id = new Id("{2389C5AD-F14A-C1AB-8536-93DFC0600017}");
        document.getProperties().putValue("Id", id);
        document.getProperties().putValue("DocumentTitle",getFileName(inputFilePath));
        if (inputStream != null) {
            ContentTransfer contentTransfer = Factory.ContentTransfer.createInstance();
            contentTransfer.setCaptureSource(inputStream);
            contentTransfer.set_RetrievalName(getFileName(inputFilePath));
            ContentElementList contentElementList = Factory.ContentTransfer.createList();
            contentElementList.add(contentTransfer);
            document.set_ContentElements(contentElementList);
            document.set_MimeType(mimeType);
        }
        //Check-in the doc
        document.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
        //Get and put the doc properties
        document.save(RefreshMode.REFRESH);
        //Stores above document to the folder
        ReferentialContainmentRelationship rc = folder.file(document, AutoUniqueName.AUTO_UNIQUE,getFileName(inputFilePath),
                DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
        rc.save(RefreshMode.REFRESH);
        logger.info(String.format("Document created, Id: %s", document.get_Id().toString()));
    }

    public static void changeClassDocBSE(String osName, String documentSearch) {
        try {
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm, logger, osName, documentSearch);
            if (!(independentObjectSet.isEmpty())) {
                int count1 = 0;
                @SuppressWarnings("rawtypes")
                Iterator it1 = independentObjectSet.iterator();
                if (it1.hasNext()) {
                    do {
                        count1++;
                        Document document = (Document) it1.next();
                        String docClass = document.get_ClassDescription().get_SymbolicName();
                        String superClass = document.get_ClassDescription().get_SuperclassDescription().get_SymbolicName();
                        String newClass = document.getProperties().getStringValue("TipoDocumentoBSE");
                        boolean validClass = (docClass.equals("DOCUMENTOBSE") || superClass.equals("DOCUMENTOBSE")) &&
                                !(docClass.equals(newClass));
                        boolean changeClass = document.getProperties().getBooleanValue("cambioClaseBSE");
                        if (validClass && changeClass) {
                            document.changeClass(newClass);
                            document.getProperties().putValue("cambioClaseBSE",false);
                            document.save(RefreshMode.REFRESH);
                            logger.info(String.format("Document Id: %s changing document class - from %s to %s",
                                    document.get_Id().toString(),
                                    docClass, newClass));
                        } else {
                            logger.info(String.format("Document Id: %s - Class: %s does not apply to change class",
                                    document.get_Id().toString(), document.getClassName()));

                        }
                    } while (it1.hasNext());
                    logger.info("Total Documents: " + count1);
                }
            } else {
                logger.info("No documents were found!");
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public static void changeDocumentClass(String osName, String documentSearch, String newClass) {
        try {
            ObjectStore os = Factory.ObjectStore.fetchInstance(p8realm.getP8domain().getDomain(), osName, null);
            ClassDescriptionSet classDescriptionSet = os.get_ClassDescriptions(); //1° Validate newClass exists
            Iterator it = classDescriptionSet.iterator();
            boolean existsClass = false;
            while (it.hasNext()) {
                ClassDescription classDescription = (ClassDescription) it.next();
                if (classDescription.get_SymbolicName().equals(newClass)) {
                    existsClass = true;
                    IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm, logger, osName, documentSearch);
                    if (!(independentObjectSet.isEmpty())) {
                        int count1 = 0;
                        @SuppressWarnings("rawtypes")
                        Iterator it1 = independentObjectSet.iterator();
                        if (it1.hasNext()) {
                            do {
                                count1++;
                                Document document = (Document) it1.next();
                                String docClass = document.getClassName();
                                if (docClass.equals("Email")) {
                                    document.changeClass(newClass);
                                    document.save(RefreshMode.REFRESH);
                                    logger.info(String.format("Document Id: %s changing document class - from %s to %s",
                                            document.get_Id().toString(),
                                            docClass, newClass));
                                } else {
                                    logger.info(String.format("Document Id: %s - Class: %s is not Document class",
                                            document.get_Id().toString(),document.getClassName()));
                                }
                            } while (it.hasNext());
                            logger.info("Total Documents: " + count1);
                        }
                    } else {
                        logger.info("No documents were found!");
                    }
                }
            }
            if (!(existsClass)) {
                logger.info(String.format("Class: %s  - does not exists in Object Store: %s", newClass, osName));
            }
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String configPath = "config.properties";
        ConfigLoader configLoader = new ConfigLoader(configPath);
        // Load attribute values from configuration file
        P8ContentExchanger.p8realm.setConnectionCeUri(configLoader.getProperty("ceURI"));
        P8ContentExchanger.p8realm.setConnectionUser(configLoader.getProperty("userName"));
        P8ContentExchanger.p8realm.setConnectionPswd(configLoader.getProperty("password"));
        p8realm.setRealm(logger);

        String objectStore = configLoader.getProperty("objectStore");
        String oneDocumentSearch = configLoader.getProperty("oneDocumentSearch");
        //createP8DocumentInFolder("TESTING","Document","/Carga","C:\\LOGS\\Module5.pdf");
        /*changeDocumentClass("TESTING",
          "Select * FROM Document where Id={2389C5AD-F14A-C1AB-8536-93DFC0600002}",
          "FormData");*/
        changeClassDocBSE("TESTING",
                "Select * FROM Document where Id={727A6D0D-305F-C872-86FD-9398ECF00000}");
        //documentContentExchange(objectStore,oneDocumentSearch,"C:\\LOGS\\");
    }
}
