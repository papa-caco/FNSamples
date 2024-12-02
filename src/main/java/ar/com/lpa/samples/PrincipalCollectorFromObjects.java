package ar.com.lpa.samples;

import java.util.Iterator;

import ar.com.lpa.samples.util.*;
import org.apache.log4j.Logger;

import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.filenet.api.security.AccessPermission;
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
                while(it.hasNext())    {
                    count++;           	
                    Document document =(Document)it.next();
                    String documentOwner = document.get_Owner();
                    if (documentOwner != null && documentOwner.startsWith("CN=")) {
                    	currentPrincipals.addNewPrincipalFromDn(documentOwner, "USER", p8realm);
                    }
                    else if (documentOwner != null && documentOwner.contains("@")) {
                    	String ownerShortName = Utilities.extractShortName(documentOwner);
                    	currentPrincipals.addNewPrincipalFromShortName(ownerShortName, "USER", p8realm);	
                    }
                    P8Logger.logDocumentProperties(logger, document, count);
                    AccessPermissionList permissions = document.get_Permissions();
                    @SuppressWarnings("rawtypes")
					Iterator it1 = permissions.iterator();

                    if (it1.hasNext()) {
                        do {
                            AccessPermission permission = (AccessPermission) it1.next();
                            String granteeName = permission.get_GranteeName();
                            String principalType = permission.get_GranteeType().toString();
                            if (principalType.equals("USER") || principalType.equals("GROUP")) {
                                if (granteeName.contains("@")) {
                                    String shortName = Utilities.extractShortName(granteeName);
                                    currentPrincipals.addNewPrincipalFromShortName(shortName, principalType, p8realm);

                                } else {
                                    currentPrincipals.addNewPrincipalFromDn(granteeName, principalType, p8realm);
                                }
                            }
                            P8Logger.logPermisionValues(logger, permission);
                        } while (it1.hasNext());
                    }
                }
                	logger.info("Total Documents: " + count);
                } else {
                	logger.info("No documents were found!");
                }
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
                        P8Logger.logFolderProperties(logger, folder, count);
                        if (folderOwner != null && folderOwner.startsWith("CN=")) {
                            currentPrincipals.addNewPrincipalFromDn(folderOwner, "USER", p8realm);
                        } else if (folderOwner != null && folderOwner.contains("@")) {
                            String ownerShortName = Utilities.extractShortName(folderOwner);
                            currentPrincipals.addNewPrincipalFromShortName(ownerShortName, "USER", p8realm);
                        }
                        AccessPermissionList permissions = folder.get_Permissions();
                        @SuppressWarnings("rawtypes")
                        Iterator it1 = permissions.iterator();

                        if (it1.hasNext()) {
                            do {
                                AccessPermission permission = (AccessPermission) it1.next();
                                String granteeName = permission.get_GranteeName();
                                String principalType = permission.get_GranteeType().toString();
                                if (principalType.equals("USER") || principalType.equals("GROUP")) {
                                    if (granteeName.contains("@")) {
                                        String shortName = Utilities.extractShortName(granteeName);
                                        currentPrincipals.addNewPrincipalFromShortName(shortName, principalType, p8realm);

                                    } else {
                                        currentPrincipals.addNewPrincipalFromDn(granteeName, principalType, p8realm);
                                    }
                                }
                                P8Logger.logPermisionValues(logger, permission);
                            } while (it1.hasNext());
                        }
                    } else {
                        break;
                    }
                }
                	logger.info("Total Folders: " + count);
                } else logger.info("No documents were found!");
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
		
		String objectStore = configLoader.getProperty("objectStore");
        String documentSearch = configLoader.getProperty("documentSearch");
        String folderSearch = configLoader.getProperty("folderSearch");
		collectPrincipalsFromDocuments(objectStore, documentSearch);
		collectPrincipalsFromFolders(objectStore, folderSearch);
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
