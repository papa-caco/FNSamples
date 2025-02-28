package ar.com.lpa.samples;

import ar.com.lpa.samples.model.SecurableObject;
import ar.com.lpa.samples.model.fnObjects.P8Realm;
import ar.com.lpa.samples.repository.SecurableObjectRepo;
import ar.com.lpa.samples.util.*;
import org.apache.log4j.Logger;

import java.io.IOException;

public class SecurityRetriever {
    private static final Logger logger = Logger.getLogger(SecurityRetriever.class);
    private static final P8Realm p8realm = new P8Realm();

    public static void main(String[] args) throws IOException {
        //EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("db");
        String configPath = "config.properties";
        ConfigLoader configLoader = new ConfigLoader(configPath);
        // Load attribute values from configuration file
        p8realm.setConnectionCeUri(configLoader.getProperty("ceURI"));
        p8realm.setConnectionUser(configLoader.getProperty("userName"));
        p8realm.setConnectionPswd(configLoader.getProperty("password"));
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
        String ownersCsvFile = configLoader.getProperty("OwnersCsvFile");
        String permissionsCsvFile = configLoader.getProperty("PermissionsCsvFile");

        ResultExporter.exportUsersToCsv(p8realm.getRealmUsers().getRealmUsers(), ldapUsersCsvFile);
        ResultExporter.exportGroupsToCsv(p8realm.getRealmGroups().getRealmGroups(), ldapGroupsCsvFile);
        switch (dbType) {
            case "SQLServer":
                SQLServerOperations.retreiveSecurableObjects(dbHost, dbPort, databaseName, dbUserName, dbUserPswd, schemaName, tablesCsvFile);
                break;
            case "Oracle":
                // TODO
                break;
            case "DB2":
                // TODO
                break;
        }
        for (SecurableObject securableObject : SecurableObjectRepo.getInstance().getSecurableObjects()) {
            switch (securableObject.getTableName().toLowerCase()) {
                case "annotation":
                    P8SecurityCollector.collectSecurityFromAnnotations(p8realm, objectStore, annotationSearch);
                    break;
                case "globalpropertydef":
                    P8SecurityCollector.collectSecurityFromPropertyTemplates(p8realm, objectStore, propertyTemplateSearch);
                    break;
                case "classdefinition":
                    P8SecurityCollector.collectSecurityFromClassDefinitions(p8realm, objectStore, classSearch);
                    break;
                case "container":
                    P8SecurityCollector.collectSecurityFromFolders(p8realm, objectStore, folderSearch);
                    break;
                case "docversion":
                    P8SecurityCollector.collectSecurityFromDocuments(p8realm, objectStore, documentSearch);
                    break;
                case "cvl":
                    P8SecurityCollector.collectSecurityFromChoiceLists(p8realm, objectStore, choiceListSearch);
                    break;
                case "generic":
                    P8SecurityCollector.collectSecurityFromCustomObjects(p8realm, objectStore, customObjectSearch);
                    break;
                case "storageclass":
                    P8SecurityCollector.collectSecurityFromStoragePolicies(p8realm, objectStore, storagePolicySearch);
                    P8SecurityCollector.collectSecurityFromStorageAreas(p8realm, objectStore, storageAreaSearch);
                    break;
                case "securitypolicy": //Include Security Templates
                    P8SecurityCollector.collectSecurityFromSecurityPolicies(p8realm, objectStore, securityPolicySearch);
                    break;
                case "event":
                    P8SecurityCollector.collectSecurityFromEvents(p8realm, objectStore, eventSearch);
                    break;
                case "subscription":
                    P8SecurityCollector.collectSecurityFromClassSubscriptions(p8realm, objectStore, subscriptionSearch);
                    P8SecurityCollector.collectSecurityFromEventActions(p8realm, objectStore);
                    P8SecurityCollector.collectSecurityFromChangePreprocessorAction(p8realm, objectStore);
                    P8SecurityCollector.collectSecurityFromContentConversionAction(p8realm, objectStore);
                    P8SecurityCollector.collectSecurityFromDocumentClassificationAction(p8realm, objectStore);
                    P8SecurityCollector.collectSecurityFromDocumentLifecycleAction(p8realm, objectStore);
                    P8SecurityCollector.collectSecurityFromDocumentLifecyclePolicy(p8realm, objectStore);
                    P8SecurityCollector.collectSecurityFromInstanceSubscription(p8realm, objectStore);
                    P8SecurityCollector.collectSecurityFromRoleMembershipAction(p8realm, objectStore);
                    P8SecurityCollector.collectSecurityFromSearchFunctionDefinition(p8realm, objectStore);
                    P8SecurityCollector.collectSecurityFromSweepAction(p8realm, objectStore);
                    P8SecurityCollector.collectSecurityFromTextIndexingPreprocessorAction(p8realm, objectStore);
                    break;
                case "sweep":
                    P8SecurityCollector.collectSecurityFromSweeps(p8realm, objectStore, sweepSearch);
                    break;
                case "sweeppolicy":
                    P8SecurityCollector.collectSecurityFromSweepPolicies(p8realm, objectStore, sweepPolicySearch);
                    break;
                case "tabledefinition":
                    P8SecurityCollector.collectSecurityFromTabledefinitions(p8realm, objectStore, tableDefinitionSearch);
                    break;
                case "ut_clbdownloadrecord":
                    P8SecurityCollector.collectSecurityFromAbstractsPersistable(p8realm, objectStore, "ClbDownloadRecord");
                    break;
                case "ut_clbsummarydata":
                    P8SecurityCollector.collectSecurityFromAbstractsPersistable(p8realm, objectStore, "ClbSummaryData");
                    break;
                case "ut_cmcustomrolebase":
                    P8SecurityCollector.collectSecurityFromAbstractsPersistable(p8realm, objectStore, "CmCustomRoleBase");
                    break;
                default:
                    break;
            }
        }
        P8SecurityCollector.exportPrincipalsToFiles(principalsJsonFile,principalsCsvFile);
        P8SecurityCollector.exportOwnersToCsv(ownersCsvFile);
        P8SecurityCollector.exportPermissionsToCsv(permissionsCsvFile);
    }
}


