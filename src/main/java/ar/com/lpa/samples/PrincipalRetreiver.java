package ar.com.lpa.samples;

import ar.com.lpa.samples.model.fnObjects.P8Realm;
import ar.com.lpa.samples.repository.PrincipalRepo;
import ar.com.lpa.samples.util.ConfigLoader;
import ar.com.lpa.samples.util.P8PrincipalCollector;
import ar.com.lpa.samples.util.PrincipalComparator;
import ar.com.lpa.samples.util.ResultExporter;
import org.apache.log4j.Logger;

public class PrincipalRetreiver {
    private static final Logger logger = Logger.getLogger(PrincipalRetreiver.class);
    private static final P8Realm p8realm = new P8Realm();
    private static final PrincipalRepo currentPrincipals = new PrincipalRepo();

    public static void main(String[] args)
    {
        String configPath = "config.properties";
        ConfigLoader configLoader = new ConfigLoader(configPath);
        // Load attribute values from configuration file
        PrincipalRetreiver.p8realm.setConnectionCeUri(configLoader.getProperty("ceURI"));
        PrincipalRetreiver.p8realm.setConnectionUser(configLoader.getProperty("userName"));
        PrincipalRetreiver.p8realm.setConnectionPswd(configLoader.getProperty("password"));
        p8realm.setRealm(logger);

        String dbPort = configLoader.getProperty("dbPort");
        String dbHost = configLoader.getProperty("dbHost");
        String databaseName = configLoader.getProperty("databaseName");
        String schemaName = configLoader.getProperty("schemaName");
        String dbUserName = configLoader.getProperty("dbUserName");
        String dbUserPswd = configLoader.getProperty("dbUserPswd");

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
        String subscriptionSearch = configLoader.getProperty("subscriptionSearch");
        String sweepSearch = configLoader.getProperty("sweepSearch");
        String sweepPolicySearch = configLoader.getProperty("sweepPolicySearch");
        String tableDefinitionSearch = configLoader.getProperty("tableDefinitionSearch");
        String downloadRecordSearch = configLoader.getProperty("downloadRecordSearch");
        String summaryDataSearch = configLoader.getProperty("summaryDataSearch");
        String customRoleBaseSearch = configLoader.getProperty("customRoleBaseSearch");



        P8PrincipalCollector.collectPrincipalsFromDocuments(currentPrincipals, p8realm, objectStore, documentSearch);
        P8PrincipalCollector.collectPrincipalsFromFolders(currentPrincipals, p8realm, objectStore, folderSearch);
        P8PrincipalCollector.collectPrincipalsFromCustomObjects(currentPrincipals, p8realm, objectStore, customObjectSearch);
        P8PrincipalCollector.collectPrincipalsFromClassDefinitions(currentPrincipals, p8realm, objectStore, classSearch);
        P8PrincipalCollector.collectPrincipalsFromAnnotations(currentPrincipals, p8realm, objectStore, annotationSearch);
        P8PrincipalCollector.collectPrincipalsFromChoiceLists(currentPrincipals, p8realm, objectStore, choiceListSearch);
        P8PrincipalCollector.collectPrincipalsFromEvents(currentPrincipals, p8realm, objectStore, eventSearch);
        P8PrincipalCollector.collectPrincipalsFromStoragePolicies(currentPrincipals, p8realm, objectStore, storagePolicySearch);
        P8PrincipalCollector.collectPrincipalsFromStorageAreas(currentPrincipals, p8realm, objectStore, storageAreaSearch);
        P8PrincipalCollector.collectPrincipalsFromSecurityPolicies(currentPrincipals, p8realm, objectStore, securityPolicySearch);
        P8PrincipalCollector.collectPrincipalsFromSubscriptions(currentPrincipals, p8realm, objectStore, subscriptionSearch);
        P8PrincipalCollector.collectPrincipalsFromSweeps(currentPrincipals, p8realm, objectStore, sweepSearch);
        P8PrincipalCollector.collectPrincipalsFromSweepPolicies(currentPrincipals, p8realm, objectStore, sweepPolicySearch);
        P8PrincipalCollector.collectPrincipalsFromTabledefinitions(currentPrincipals, p8realm, objectStore,tableDefinitionSearch);
        P8PrincipalCollector.collectPrincipalsFromAbstractsPersistable(currentPrincipals, p8realm, objectStore, downloadRecordSearch);
        P8PrincipalCollector.collectPrincipalsFromAbstractsPersistable(currentPrincipals, p8realm, objectStore, summaryDataSearch);
        P8PrincipalCollector.collectPrincipalsFromAbstractsPersistable(currentPrincipals, p8realm, objectStore, customRoleBaseSearch);

        currentPrincipals.showCurrentPrincipals();
		String jsonOutput = JsonExporter.exportToJson(currentPrincipals);
		if (jsonOutput != null) {
		    System.out.println(jsonOutput);
		}
        String resultsPath = configLoader.getProperty("resultsPath");
        logger.info("Total Principals: " + currentPrincipals.getPrincipals().size());
        currentPrincipals.getPrincipals().sort(new PrincipalComparator());
        ResultExporter.exportPrincipalCollectionToJsonfile(currentPrincipals.getPrincipals(), resultsPath);
        logger.info("Principal details at JSON file: " + resultsPath);
    }
}
