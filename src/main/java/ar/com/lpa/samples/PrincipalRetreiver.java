package ar.com.lpa.samples;

import ar.com.lpa.samples.model.FnDbTable;
import ar.com.lpa.samples.model.fnObjects.P8Realm;
import ar.com.lpa.samples.repository.FnDbTableRepo;
import ar.com.lpa.samples.repository.PrincipalRepo;
import ar.com.lpa.samples.util.*;
import org.apache.log4j.Logger;

import java.io.IOException;

public class PrincipalRetreiver {
    private static final Logger logger = Logger.getLogger(PrincipalRetreiver.class);
    private static final P8Realm p8realm = new P8Realm();

    private static final FnDbTableRepo fnDbTableRepo = new FnDbTableRepo();

    public static void main(String[] args) throws IOException {
        String configPath = "config.properties";
        ConfigLoader configLoader = new ConfigLoader(configPath);
        // Load attribute values from configuration file
        PrincipalRetreiver.p8realm.setConnectionCeUri(configLoader.getProperty("ceURI"));
        PrincipalRetreiver.p8realm.setConnectionUser(configLoader.getProperty("userName"));
        PrincipalRetreiver.p8realm.setConnectionPswd(configLoader.getProperty("password"));
        p8realm.setRealm(logger);

        String dbType = configLoader.getProperty("dbType");
        String dbHost = configLoader.getProperty("dbHost");
        String dbPort = configLoader.getProperty("dbPort");
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
        String propertyTemplateSearch = configLoader.getProperty("propertyTemplateSearch");
        String subscriptionSearch = configLoader.getProperty("subscriptionSearch");
        String sweepSearch = configLoader.getProperty("sweepSearch");
        String sweepPolicySearch = configLoader.getProperty("sweepPolicySearch");
        String tableDefinitionSearch = configLoader.getProperty("tableDefinitionSearch");
        String tablesCsvFile = configLoader.getProperty("tablesCsvFile");
        String ldapUsersCsvFile = configLoader.getProperty("LdapUsersCsvFile");
        String ldapGroupsCsvFile = configLoader.getProperty("LdapGroupsCsvFile");
        String principalsCsvFile = configLoader.getProperty("PrincipalsCsvFile");
        String principalsJsonFile = configLoader.getProperty("PrincipalsJsonFile");

        /*String usersJson = ResultExporter.expUsersToJsonOnConsole(p8realm.getRealmUsers().getLdapUsers());
        if (usersJson != null) {
            System.out.println(usersJson);
        }*/
        ResultExporter.exportUsersToCsv(p8realm.getRealmUsers().getRealmUsers(), ldapUsersCsvFile);

        /*String groupsJson = ResultExporter.expGroupsToJsonOnConsole(p8realm.getRealmGroups().getLdapGroups());
        if (groupsJson != null) {
            System.out.println(groupsJson);
        }*/
        ResultExporter.exportGroupsToCsv(p8realm.getRealmGroups().getRealmGroups(),ldapGroupsCsvFile );
        if(dbType.equals("SQLServer")){
            SQLServerOperations.retreiveSecurableObjects(dbHost, dbPort, databaseName,dbUserName,dbUserPswd,schemaName,tablesCsvFile, fnDbTableRepo);
        } else if (dbType.equals("Oracle")){
            // TODO
        } else if (dbType.equals("DB2")) {
            // TODO
        }
        for (FnDbTable fnDbTable : fnDbTableRepo.getFnDbTables()) {
            switch (fnDbTable.getTableName()) {
                case "Annotation":
                    P8PrincipalCollector.collectPrincipalsFromAnnotations(PrincipalRepo.getInstance(), p8realm, objectStore, annotationSearch);
                    break;
                case "GlobalPropertyDef":
                    P8PrincipalCollector.collectPrincipalsFromPropertyTemplates(PrincipalRepo.getInstance(), p8realm, objectStore, propertyTemplateSearch);
                    break;
                case "ClassDefinition":
                    P8PrincipalCollector.collectPrincipalsFromClassDefinitions(PrincipalRepo.getInstance(), p8realm, objectStore, classSearch);
                    break;
                case "Container":
                    P8PrincipalCollector.collectPrincipalsFromFolders(PrincipalRepo.getInstance(), p8realm, objectStore, folderSearch);
                    break;
                case "DocVersion":
                    P8PrincipalCollector.collectPrincipalsFromDocuments(PrincipalRepo.getInstance(), p8realm, objectStore, documentSearch);
                    break;
                case "Cvl":
                    P8PrincipalCollector.collectPrincipalsFromChoiceLists(PrincipalRepo.getInstance(), p8realm, objectStore, choiceListSearch);
                    break;
                case "Generic":
                    P8PrincipalCollector.collectPrincipalsFromCustomObjects(PrincipalRepo.getInstance(), p8realm, objectStore, customObjectSearch);
                    break;
                case "StorageClass":
                    P8PrincipalCollector.collectPrincipalsFromStoragePolicies(PrincipalRepo.getInstance(), p8realm, objectStore, storagePolicySearch);
                    P8PrincipalCollector.collectPrincipalsFromStorageAreas(PrincipalRepo.getInstance(), p8realm, objectStore, storageAreaSearch);
                    break;
                case "SecurityPolicy": //Includes SecurityTemplate
                    P8PrincipalCollector.collectPrincipalsFromSecurityPolicies(PrincipalRepo.getInstance(), p8realm, objectStore, securityPolicySearch);
                    break;
                case "Event":
                    P8PrincipalCollector.collectPrincipalsFromEvents(PrincipalRepo.getInstance(), p8realm, objectStore, eventSearch);
                    break;
                case "Subscription":
                    P8PrincipalCollector.collectPrincipalsFromSubscriptions(PrincipalRepo.getInstance(), p8realm, objectStore, subscriptionSearch);
                    break;
                case "Sweep":
                    P8PrincipalCollector.collectPrincipalsFromSweeps(PrincipalRepo.getInstance(), p8realm, objectStore, sweepSearch);
                    break;
                case "SweepPolicy":
                    P8PrincipalCollector.collectPrincipalsFromSweepPolicies(PrincipalRepo.getInstance(), p8realm, objectStore, sweepPolicySearch);
                    break;
                case "TableDefinition":
                    P8PrincipalCollector.collectPrincipalsFromTabledefinitions(PrincipalRepo.getInstance(), p8realm, objectStore,tableDefinitionSearch);
                    break;
                case "UT_ClbDownloadRecord":
                    P8PrincipalCollector.collectPrincipalsFromAbstractsPersistable(PrincipalRepo.getInstance(), p8realm, objectStore, "ClbDownloadRecord");
                    break;
                case "UT_ClbSummaryData":
                    P8PrincipalCollector.collectPrincipalsFromAbstractsPersistable(PrincipalRepo.getInstance(), p8realm, objectStore, "ClbSummaryData");
                    break;
                case "UT_CmCustomRoleBase":
                    P8PrincipalCollector.collectPrincipalsFromAbstractsPersistable(PrincipalRepo.getInstance(), p8realm, objectStore, "CmCustomRoleBase");
                    break;
                default:
                    break;
            }
        }

        PrincipalRepo.getInstance().getPrincipals().sort(new PrincipalComparator());
        ResultExporter.exportPrincipalCollectionToJsonfile(PrincipalRepo.getInstance().getPrincipals(), principalsJsonFile);
        ResultExporter.exportPrincipalsToCsv(PrincipalRepo.getInstance().getPrincipals(), principalsCsvFile);
/*
        String principalsJson = ResultExporter.exportPrincipalCollectionToJsonOnConsole(currentPrincipals.getPrincipals());
        if (principalsJson != null) {
            System.out.println(principalsJson);
        }
 */
    }
}
