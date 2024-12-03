package ar.com.lpa.samples;

import java.util.Iterator;

import ar.com.lpa.samples.util.*;
import org.apache.log4j.Logger;

import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.filenet.api.core.CustomObject;
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
		collectPrincipalsFromDocuments(objectStore, documentSearch);
		collectPrincipalsFromFolders(objectStore, folderSearch);
        collectPrincipalsFromCustomObjects(objectStore, customObjectSearch);
		//currentPrincipals.showCurrentPrincipals();
		/*String jsonOutput = JsonExporter.exportToJson(currentPrincipals);
		if (jsonOutput != null) {
		    System.out.println(jsonOutput);
		}*/
		String resultsPath = configLoader.getProperty("resultsPath");
		logger.info("Total Principals: " + currentPrincipals.getPrincipals().size());
		currentPrincipals.getPrincipals().sort(new PrincipalComparator());
		JsonExporter.exportJsonToFile(currentPrincipals.getPrincipals(), resultsPath);
		logger.info("Principal details at JSON file: " + resultsPath);
	}
}
