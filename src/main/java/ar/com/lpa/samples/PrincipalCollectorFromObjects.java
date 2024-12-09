package ar.com.lpa.samples;

import java.util.Iterator;
import java.util.List;

import ar.com.lpa.samples.util.*;
import com.filenet.api.admin.ChoiceList;
import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.StorageArea;
import com.filenet.api.admin.StoragePolicy;
import com.filenet.api.collection.SecurityTemplateList;
import com.filenet.api.core.*;
import com.filenet.api.events.Event;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.security.SecurityPolicy;
import com.filenet.api.security.SecurityTemplate;
import org.apache.log4j.Logger;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.IndependentObjectSet;

import ar.com.lpa.samples.model.fnObjects.P8Realm;
import ar.com.lpa.samples.repository.PrincipalRepo;

public class PrincipalCollectorFromObjects
{
	private static final Logger logger = Logger.getLogger(PrincipalCollectorFromObjects.class);
    private static final P8Realm p8realm = new P8Realm();
    private static final PrincipalRepo currentPrincipals = new PrincipalRepo();
 
    public static void collectPrincipalsFromDocuments(String osName, String documentSearch) {
        try{
            logger.info(String.format("Collecting Principals from Documents - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,documentSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
				Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        Document document = (Document) it.next();
                        String documentOwner = document.get_Owner();
                        currentPrincipals.addPrincipalFromObjectOwner(documentOwner, p8realm);
                        AccessPermissionList permissions = document.get_Permissions();
                        P8Logger.logDocumentProperties(logger, document, count, permissions.size());
                        if (!(permissions.isEmpty())) {
                            currentPrincipals.addPrincipalsFromPermissions(permissions, p8realm);
                        }
                    }  while (it.hasNext()) ;

                }
                	logger.info("Total Documents: " + count);
                } else logger.info("No documents were found!");
            }
            catch(Exception e){
                e.printStackTrace();
            }
    }

    public static void collectPrincipalsFromCustomObjects(String osName, String customObjectSearch) {
        try{
            logger.info(String.format("Collecting Principals from Custom Objects - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm, logger, osName, customObjectSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        CustomObject customObject = (CustomObject) it.next();
                        String objectOwner = customObject.get_Owner();
                        currentPrincipals.addPrincipalFromObjectOwner(objectOwner, p8realm);
                        AccessPermissionList permissions = customObject.get_Permissions();
                        P8Logger.logCustomObjectProperties(logger, customObject, count, permissions.size());
                        if (!(permissions.isEmpty()))
                            currentPrincipals.addPrincipalsFromPermissions(permissions, p8realm);
                    }  while (it.hasNext()) ;

                }
                logger.info("Total Custom Objects: " + count);
            } else logger.info("No custom objects were found!");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void collectPrincipalsFromFolders(String osName, String folderSearch) {
        try{
            logger.info(String.format("Collecting Principals from Folders - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,folderSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
				Iterator it=independentObjectSet.iterator();
                while (true)
                {
                    if (it.hasNext()) {
                        count++;
                        Folder folder = (Folder) it.next();
                        String folderOwner = folder.get_Owner();
                        currentPrincipals.addPrincipalFromObjectOwner(folderOwner, p8realm);
                        AccessPermissionList permissions = folder.get_Permissions();
                        P8Logger.logFolderProperties(logger, folder, count, permissions.size());
                        if (!(permissions.isEmpty())) {
                            currentPrincipals.addPrincipalsFromPermissions(permissions, p8realm);
                        }
                    } else {
                        break;
                    }
                }
                	logger.info("Total Folders: " + count);
                } else logger.info("No folders were found!");
            }
            catch(Exception e){
                e.printStackTrace();
            }
    }

    public static void collectPrincipalsFromClassDefinitions(String osName, String classSearch) {
        try{
            logger.info(String.format("Collecting Principals from Class Definitions - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
                Iterator it=independentObjectSet.iterator();
                while (true)
                {
                    if (it.hasNext()) {
                        count++;
                        ClassDefinition classDefinition = (ClassDefinition) it.next();
                        String classOwner = classDefinition.get_Owner();
                        currentPrincipals.addPrincipalFromObjectOwner(classOwner, p8realm);
                        AccessPermissionList permissions = classDefinition.get_Permissions();
                        P8Logger.logClassProperties(logger, classDefinition, count, permissions.size());
                        if (!(permissions.isEmpty())) {
                            currentPrincipals.addPrincipalsFromPermissions(permissions, p8realm);
                        }
                    } else {
                        break;
                    }
                }
                logger.info("Total Class Definitions: " + count);
            } else logger.info("No Class Definitions were found!");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void collectPrincipalsFromAnnotations(String osName, String classSearch) {
        try{
            logger.info(String.format("Collecting Principals from Annotations - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
                Iterator it=independentObjectSet.iterator();
                while (true)
                {
                    if (it.hasNext()) {
                        count++;
                        Annotation annotation = (Annotation) it.next();
                        String annotationOwner = annotation.get_Owner();
                        currentPrincipals.addPrincipalFromObjectOwner(annotationOwner, p8realm);
                        AccessPermissionList permissions = annotation.get_Permissions();
                        P8Logger.logAnnotationProperties(logger, annotation, count, permissions.size());
                        if (!(permissions.isEmpty())) {
                            currentPrincipals.addPrincipalsFromPermissions(permissions, p8realm);
                        }
                    } else {
                        break;
                    }
                }
                logger.info("Total Annotations: " + count);
            } else logger.info("No Annotations were found!");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void collectPrincipalsFromChoiceLists(String osName, String classSearch) {
        try{
            logger.info(String.format("Collecting Principals from Choice Lists - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
                Iterator it=independentObjectSet.iterator();
                while (true)
                {
                    if (it.hasNext()) {
                        count++;
                        ChoiceList choiceList = (ChoiceList) it.next();
                        String choiceListOwner = choiceList.get_Owner();
                        currentPrincipals.addPrincipalFromObjectOwner(choiceListOwner, p8realm);
                        AccessPermissionList permissions = choiceList.get_Permissions();
                        P8Logger.logChoiceListProperties(logger, choiceList, count, permissions.size());
                        if (!(permissions.isEmpty())) {
                            currentPrincipals.addPrincipalsFromPermissions(permissions, p8realm);
                        }
                    } else {
                        break;
                    }
                }
                logger.info("Total Choice Lists: " + count);
            } else logger.info("No Choice Lists were found!");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void collectPrincipalsFromEvents(String osName, String classSearch) {
        try{
            logger.info(String.format("Collecting Principals from Events - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
                Iterator it=independentObjectSet.iterator();
                while (true)
                {
                    if (it.hasNext()) {
                        count++;
                        Event event = (Event) it.next();
                        String eventOwner = event.get_Owner();
                        currentPrincipals.addPrincipalFromObjectOwner(eventOwner, p8realm);
                        AccessPermissionList permissions = event.get_Permissions();
                        P8Logger.logEventProperties(logger, event, count, permissions.size());
                        if (!(permissions.isEmpty())) {
                            currentPrincipals.addPrincipalsFromPermissions(permissions, p8realm);
                        }
                    } else {
                        break;
                    }
                }
                logger.info("Total Events: " + count);
            } else logger.info("No Events were found!");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void collectPrincipalsFromStoragePolicies(String osName, String classSearch) {
        try{
            logger.info(String.format("Collecting Principals from Storage Policies - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
                Iterator it=independentObjectSet.iterator();
                while (true)
                {
                    if (it.hasNext()) {
                        count++;
                        StoragePolicy storagePolicy = (StoragePolicy) it.next();
                        String storagePolicyOwner = storagePolicy.get_Owner();
                        currentPrincipals.addPrincipalFromObjectOwner(storagePolicyOwner, p8realm);
                        AccessPermissionList permissions = storagePolicy.get_Permissions();
                        P8Logger.logStoragePolicyProperties(logger, storagePolicy, count, permissions.size());
                        if (!(permissions.isEmpty())) {
                            currentPrincipals.addPrincipalsFromPermissions(permissions, p8realm);
                        }
                    } else {
                        break;
                    }
                }
                logger.info("Total Storage Policies: " + count);
            } else logger.info("No Storage Policies were found!");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void collectPrincipalsFromStorageAreas(String osName, String classSearch) {
        try{
            logger.info(String.format("Collecting Principals from Storage Areas - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
                Iterator it=independentObjectSet.iterator();
                while (true)
                {
                    if (it.hasNext()) {
                        count++;
                        StorageArea storageArea = (StorageArea) it.next();
                        String storageAreaOwner = storageArea.get_Owner();
                        currentPrincipals.addPrincipalFromObjectOwner(storageAreaOwner, p8realm);
                        AccessPermissionList permissions = storageArea.get_Permissions();
                        P8Logger.logStorageAreaProperties(logger, storageArea, count, permissions.size());
                        if (!(permissions.isEmpty())) {
                            currentPrincipals.addPrincipalsFromPermissions(permissions, p8realm);
                        }
                    } else {
                        break;
                    }
                }
                logger.info("Total Storage Areas: " + count);
            } else logger.info("No Storage Areas were found!");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void collectPrincipalsFromSecurityPolicies(String osName, String classSearch) {
        try{
            logger.info(String.format("Collecting Principals from Security Policies - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
                Iterator it=independentObjectSet.iterator();
                while (true)
                {
                    if (it.hasNext()) {
                        count++;
                        SecurityPolicy securityPolicy = (SecurityPolicy) it.next();
                        String securityPolicyOwner = securityPolicy.get_Owner();
                        currentPrincipals.addPrincipalFromObjectOwner(securityPolicyOwner, p8realm);
                        AccessPermissionList permissions = securityPolicy.get_Permissions();
                        SecurityTemplateList securityTemplateList = securityPolicy.get_SecurityTemplates();
                        P8Logger.logSecurityPolicyProperties(logger, securityPolicy, count, permissions.size(),securityTemplateList.size());
                        if (!(permissions.isEmpty())) {
                            currentPrincipals.addPrincipalsFromPermissions(permissions, p8realm);
                        }
                        if (!(securityTemplateList.isEmpty())){
                            Iterator it1 = securityTemplateList.iterator();
                            while (true) {
                                if (it1.hasNext()) {
                                    SecurityTemplate securityTemplate = (SecurityTemplate) it1.next();
                                    AccessPermissionList templatePermissions = securityTemplate.get_TemplatePermissions();
                                    if (!(templatePermissions.isEmpty())){
                                        currentPrincipals.addPrincipalsFromPermissions(templatePermissions, p8realm);
                                    }
                                }
                                else {
                                    break;
                                }
                            }
                        }
                    } else {
                        break;
                    }
                }
                logger.info("Total Security Policies: " + count);
            } else logger.info("No Security Policies were found!");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }



	public static void main(String[] args)
	{
		String configPath = "config.properties";
		ConfigLoader configLoader = new ConfigLoader(configPath);
		// Load attribute values from configuration file
		PrincipalCollectorFromObjects.p8realm.setConnectionCeUri(configLoader.getProperty("ceURI"));
		PrincipalCollectorFromObjects.p8realm.setConnectionUser(configLoader.getProperty("userName"));
		PrincipalCollectorFromObjects.p8realm.setConnectionPswd(configLoader.getProperty("password"));
        p8realm.setRealm(logger);
		
		String objectStore = configLoader.getProperty("objectStore");
        String documentSearch = configLoader.getProperty("documentSearch");
        String folderSearch = configLoader.getProperty("folderSearch");
        String customObjectSearch = configLoader.getProperty("customObjectSearch");
        String classSearch = configLoader.getProperty("classSearch");
        String annotationSearch = configLoader.getProperty("annotationSearch");
        String choiceListSearch = configLoader.getProperty("choiceListSearch");
        String eventSearch = configLoader.getProperty("eventSearch");
        String storagePolicySearch = configLoader.getProperty("storagePolicySearch");
        String storageAreaSearch = configLoader.getProperty("storageAreaSearch");
        String securityPolicySearch = configLoader.getProperty("securityPolicySearch");

        String dbPort = configLoader.getProperty("dbPort");
        String dbHost = configLoader.getProperty("dbHost");
        String databaseName = configLoader.getProperty("databaseName");
        String schemaName = configLoader.getProperty("schemaName");
        String dbUserName = configLoader.getProperty("dbUserName");
        String dbUserPswd = configLoader.getProperty("dbUserPswd");
		//collectPrincipalsFromDocuments(objectStore, documentSearch);
		//collectPrincipalsFromFolders(objectStore, folderSearch);
        //collectPrincipalsFromCustomObjects(objectStore, customObjectSearch);
        //collectPrincipalsFromClassDefinitions(objectStore, classSearch);
        //collectPrincipalsFromAnnotations(objectStore, annotationSearch);
        //collectPrincipalsFromChoiceLists(objectStore, choiceListSearch);
        //collectPrincipalsFromEvents(objectStore, eventSearch);
        //collectPrincipalsFromStoragePolicies(objectStore, storagePolicySearch);
        //collectPrincipalsFromStorageAreas(objectStore, storageAreaSearch);
        collectPrincipalsFromSecurityPolicies(objectStore, securityPolicySearch);

		//currentPrincipals.showCurrentPrincipals();
		/*String jsonOutput = JsonExporter.exportToJson(currentPrincipals);
		if (jsonOutput != null) {
		    System.out.println(jsonOutput);
		}*/
		String resultsPath = configLoader.getProperty("resultsPath");
		logger.info("Total Principals: " + currentPrincipals.getPrincipals().size());
		currentPrincipals.getPrincipals().sort(new PrincipalComparator());
		ResultExporter.exportPrincipalCollectionToJsonfile(currentPrincipals.getPrincipals(), resultsPath);
		logger.info("Principal details at JSON file: " + resultsPath);
	}
}
