package ar.com.lpa.samples;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.IndependentObjectSet;

import ar.com.lpa.samples.model.fnObjects.P8Realm;
import ar.com.lpa.samples.repository.PrincipalRepo;
import ar.com.lpa.samples.util.ConfigLoader;
import ar.com.lpa.samples.util.JsonExporter;
import ar.com.lpa.samples.util.P8Logger;
import ar.com.lpa.samples.util.PrincipalComparator;
import ar.com.lpa.samples.util.Utilities;

public class PrincipalCollectorFromObjects 
{
	private static final Logger logger = Logger.getLogger(PrincipalCollectorFromObjects.class);

    private static P8Realm p8realm = new P8Realm();
    private static PrincipalRepo currentPrincipals = new PrincipalRepo();
 
    public static void collectPrincipalsFromDocuments(String osName, String documentSearch) throws IOException
    {   
        try{
        	p8realm.setRealm(logger);         
            ObjectStore objStore = Factory.ObjectStore.fetchInstance(p8realm.getP8domain().getDomain(), osName,null);
            SearchScope searchScope = new SearchScope(objStore);
            int count=0;
            SearchSQL searchSQL = new SearchSQL(documentSearch);
            logger.info("P8 Domain: " + p8realm.getP8domain().getDomain().get_Name() + " - Object Store: " + objStore.get_SymbolicName());
            logger.info("Document Search: "+ documentSearch);
            @SuppressWarnings("removal")
			IndependentObjectSet independentObjectSet = searchScope.fetchObjects(searchSQL, new Integer(10), null, new Boolean(true));
            if(!(independentObjectSet.isEmpty())){
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
                  
                    while (it1.hasNext())
                    {
                    	AccessPermission permission = (AccessPermission)it1.next();
                    	String granteeName = permission.get_GranteeName();
                    	String principalType = permission.get_GranteeType().toString();
                    	if (principalType.equals("USER") || principalType.equals("GROUP"))
                    	{
                    		if (granteeName.contains("@")) {
                    			String shortName = Utilities.extractShortName(granteeName);
                    			currentPrincipals.addNewPrincipalFromShortName(shortName, principalType, p8realm);
                    			                   			
                            }
                    		else {
                    			currentPrincipals.addNewPrincipalFromDn(granteeName,principalType, p8realm);
                    		}
                    	}
                    	P8Logger.logPermisionValues(logger, permission);
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
    
    public static void collectPrincipalsFromFolders(String osName, String folderSearch) throws IOException
    {   
        try{
        	p8realm.setRealm(logger);         
            ObjectStore objStore = Factory.ObjectStore.fetchInstance(p8realm.getP8domain().getDomain(), osName,null);
            SearchScope searchScope = new SearchScope(objStore);
            int count=0;
            SearchSQL searchSQL = new SearchSQL(folderSearch);
            logger.info("P8 Domain: " + p8realm.getP8domain().getDomain().get_Name() + " - Object Store: " + objStore.get_SymbolicName());
            logger.info("Document Search: "+ folderSearch);
            @SuppressWarnings("removal")
			IndependentObjectSet independentObjectSet = searchScope.fetchObjects(searchSQL, new Integer(10), null, new Boolean(true));
            if(!(independentObjectSet.isEmpty())){
                @SuppressWarnings("rawtypes")
				Iterator it=independentObjectSet.iterator();            
                while(it.hasNext())    {
                    count++;             	
                    Folder folder =(Folder)it.next();
                    String folderOwner = folder.get_Owner();
                    P8Logger.logFolderProperties(logger, folder, count);
                    if (folderOwner != null && folderOwner.startsWith("CN=")) {
                    	currentPrincipals.addNewPrincipalFromDn(folderOwner, "USER", p8realm);
                    }
                    else if (folderOwner != null && folderOwner.contains("@")) {
                    	String ownerShortName = Utilities.extractShortName(folderOwner);
                    	currentPrincipals.addNewPrincipalFromShortName(ownerShortName, "USER", p8realm);	
                    }
                    AccessPermissionList permissions = folder.get_Permissions();
                    @SuppressWarnings("rawtypes")
					Iterator it1 = permissions.iterator();
                  
                    while (it1.hasNext())
                    {
                    	AccessPermission permission = (AccessPermission)it1.next();
                    	String granteeName = permission.get_GranteeName();
                    	String principalType = permission.get_GranteeType().toString();
                    	if (principalType.equals("USER") || principalType.equals("GROUP"))
                    	{
                    		if (granteeName.contains("@")) {
                    			String shortName = Utilities.extractShortName(granteeName);
                    			currentPrincipals.addNewPrincipalFromShortName(shortName, principalType, p8realm);
                    			                   			
                            }
                    		else {
                    			currentPrincipals.addNewPrincipalFromDn(granteeName,principalType, p8realm);
                    		}
                    	}
                    	P8Logger.logPermisionValues(logger, permission);
                    }
                }
                	logger.info("Total Folders: " + count);
                } else {
                	logger.info("No documents were found!");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
    }
    
	public static void main(String[] args) throws IOException 
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
		Collections.sort(currentPrincipals.getPrincipals(), new PrincipalComparator());
		JsonExporter.exportJsonToFile(currentPrincipals.getPrincipals(), resultsPath);
		logger.info("Principal details at JSON file: " + resultsPath);
	}
}
