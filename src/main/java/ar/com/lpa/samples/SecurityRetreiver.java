package ar.com.lpa.samples;

import ar.com.lpa.samples.model.FnDbTable;
import ar.com.lpa.samples.model.fnObjects.P8Realm;
import ar.com.lpa.samples.repository.FnDbTableRepo;
import ar.com.lpa.samples.repository.PrincipalRepo;
import ar.com.lpa.samples.util.*;
import org.apache.log4j.Logger;

import java.io.IOException;

public class SecurityRetreiver {
    private static final Logger logger = Logger.getLogger(SecurityRetreiver.class);
    private static final P8Realm p8realm = new P8Realm();
    private static final FnDbTableRepo fnDbTableRepo = new FnDbTableRepo();

    public static void main(String[] args) throws IOException {
        String configPath = "config.properties";
        ConfigLoader configLoader = new ConfigLoader(configPath);
        // Load attribute values from configuration file
        SecurityRetreiver.p8realm.setConnectionCeUri(configLoader.getProperty("ceURI"));
        SecurityRetreiver.p8realm.setConnectionUser(configLoader.getProperty("userName"));
        SecurityRetreiver.p8realm.setConnectionPswd(configLoader.getProperty("password"));
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
        String ownersJsonFile = configLoader.getProperty("OwnersJsonFile");

        /*String usersJson = ResultExporter.expUsersToJsonOnConsole(p8realm.getRealmUsers().getLdapUsers());
        if (usersJson != null) {
            System.out.println(usersJson);
        }*/
        ResultExporter.exportUsersToCsv(p8realm.getRealmUsers().getRealmUsers(), ldapUsersCsvFile);

        /*String groupsJson = ResultExporter.expGroupsToJsonOnConsole(p8realm.getRealmGroups().getLdapGroups());
        if (groupsJson != null) {
            System.out.println(groupsJson);
        }*/
        ResultExporter.exportGroupsToCsv(p8realm.getRealmGroups().getRealmGroups(), ldapGroupsCsvFile);
        if (dbType.equals("SQLServer")) {
            SQLServerOperations.retreiveSecurableObjects(dbHost, dbPort, databaseName, dbUserName, dbUserPswd, schemaName, tablesCsvFile, fnDbTableRepo);
        } else if (dbType.equals("Oracle")) {
            // TODO
        } else if (dbType.equals("DB2")) {
            // TODO
        }
        for (FnDbTable fnDbTable : fnDbTableRepo.getFnDbTables()) {
            switch (fnDbTable.getTableName()) {
                case "Annotation":
                    P8SecurityCollector.collectPrincipalsFromAnnotations(p8realm, objectStore, annotationSearch);
                    break;
                case "GlobalPropertyDef":
                    P8SecurityCollector.collectPrincipalsFromPropertyTemplates(p8realm, objectStore, propertyTemplateSearch);
                    break;
                case "ClassDefinition":
                    P8SecurityCollector.collectPrincipalsFromClassDefinitions(p8realm, objectStore, classSearch);
                    break;
                case "Container":
                    P8SecurityCollector.collectPrincipalsFromFolders(p8realm, objectStore, folderSearch);
                    break;
                case "DocVersion":
                    P8SecurityCollector.collectPrincipalsFromDocuments(p8realm, objectStore, documentSearch);
                    break;
                case "Cvl":
                    P8SecurityCollector.collectPrincipalsFromChoiceLists(p8realm, objectStore, choiceListSearch);
                    break;
                case "Generic":
                    P8SecurityCollector.collectPrincipalsFromCustomObjects(p8realm, objectStore, customObjectSearch);
                    break;
                case "StorageClass":
                    P8SecurityCollector.collectPrincipalsFromStoragePolicies(p8realm, objectStore, storagePolicySearch);
                    P8SecurityCollector.collectPrincipalsFromStorageAreas(p8realm, objectStore, storageAreaSearch);
                    break;
                case "SecurityPolicy": //Includes SecurityTemplate
                    P8SecurityCollector.collectPrincipalsFromSecurityPolicies(p8realm, objectStore, securityPolicySearch);
                    break;
                case "Event":
                    P8SecurityCollector.collectPrincipalsFromEvents(p8realm, objectStore, eventSearch);
                    break;
                case "Subscription":
                    P8SecurityCollector.collectPrincipalsFromSubscriptions(p8realm, objectStore, subscriptionSearch);
                    break;
                case "Sweep":
                    P8SecurityCollector.collectPrincipalsFromSweeps(p8realm, objectStore, sweepSearch);
                    break;
                case "SweepPolicy":
                    P8SecurityCollector.collectPrincipalsFromSweepPolicies(p8realm, objectStore, sweepPolicySearch);
                    break;
                case "TableDefinition":
                    P8SecurityCollector.collectPrincipalsFromTabledefinitions(p8realm, objectStore, tableDefinitionSearch);
                    break;
                case "UT_ClbDownloadRecord":
                    P8SecurityCollector.collectPrincipalsFromAbstractsPersistable(p8realm, objectStore, "ClbDownloadRecord");
                    break;
                case "UT_ClbSummaryData":
                    P8SecurityCollector.collectPrincipalsFromAbstractsPersistable(p8realm, objectStore, "ClbSummaryData");
                    break;
                case "UT_CmCustomRoleBase":
                    P8SecurityCollector.collectPrincipalsFromAbstractsPersistable(p8realm, objectStore, "CmCustomRoleBase");
                    break;
                default:
                    break;
            }
        }
        P8SecurityCollector.exportPrincipalsToFiles(principalsJsonFile, principalsCsvFile);
        P8SecurityCollector.exportOwnersToCsv(ownersJsonFile);

    }
}
