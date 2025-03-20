package ar.com.lpa.ldapExchanger;

import ar.com.lpa.ldapExchanger.model.SecurableObject;
import ar.com.lpa.ldapExchanger.model.fnObjects.P8Realm;
import ar.com.lpa.ldapExchanger.repository.RealmGroupsRepo;
import ar.com.lpa.ldapExchanger.repository.RealmUsersRepo;
import ar.com.lpa.ldapExchanger.repository.SecurableObjectRepo;
import ar.com.lpa.ldapExchanger.repository.PrincipalRepo;
import ar.com.lpa.ldapExchanger.util.*;
import org.apache.log4j.Logger;

import java.io.IOException;

public class PrincipalRetriever {
    private static final Logger logger = Logger.getLogger(PrincipalRetriever.class);
    private static final P8Realm p8realm = new P8Realm();

    public static void main(String[] args) throws IOException {
        String configPath = "config.properties";
        ConfigLoader configLoader = new ConfigLoader(configPath);
        // Load attribute values from configuration file
        PrincipalRetriever.p8realm.setConnectionCeUri(configLoader.getProperty("ceURI"));
        PrincipalRetriever.p8realm.setConnectionUser(configLoader.getProperty("userName"));
        PrincipalRetriever.p8realm.setConnectionPswd(configLoader.getProperty("password"));
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

        ResultExporter.exportUsersToCsv(RealmUsersRepo.getInstance().getRealmUsers(), ldapUsersCsvFile);
        ResultExporter.exportGroupsToCsv(RealmGroupsRepo.getInstance().getRealmGroups(),ldapGroupsCsvFile );

        if(dbType.equals("SQLServer")){
            SQLServerOperations.retreiveSecurableObjects(dbHost, dbPort, databaseName,dbUserName,dbUserPswd,schemaName,tablesCsvFile);
        } else if (dbType.equals("Oracle")){
            // TODO
        } else if (dbType.equals("DB2")) {
            // TODO
        }
        for (SecurableObject securableObject : SecurableObjectRepo.getInstance().getSecurableObjects()) {
            switch (securableObject.getTableName()) {
                case "Annotation":
                    P8PrincipalCollector.collectPrincipalsFromAnnotations(p8realm, objectStore, annotationSearch);
                    break;
                case "GlobalPropertyDef":
                    P8PrincipalCollector.collectPrincipalsFromPropertyTemplates(p8realm, objectStore, propertyTemplateSearch);
                    break;
                case "ClassDefinition":
                    P8PrincipalCollector.collectPrincipalsFromClassDefinitions(p8realm, objectStore, classSearch);
                    break;
                case "Container":
                    P8PrincipalCollector.collectPrincipalsFromFolders(p8realm, objectStore, folderSearch);
                    break;
                case "DocVersion":
                    P8PrincipalCollector.collectPrincipalsFromDocuments(p8realm, objectStore, documentSearch);
                    break;
                case "Cvl":
                    P8PrincipalCollector.collectPrincipalsFromChoiceLists(p8realm, objectStore, choiceListSearch);
                    break;
                case "Generic":
                    P8PrincipalCollector.collectPrincipalsFromCustomObjects(p8realm, objectStore, customObjectSearch);
                    break;
                case "StorageClass":
                    P8PrincipalCollector.collectPrincipalsFromStoragePolicies(p8realm, objectStore, storagePolicySearch);
                    P8PrincipalCollector.collectPrincipalsFromStorageAreas(p8realm, objectStore, storageAreaSearch);
                    break;
                case "SecurityPolicy": //Includes SecurityTemplate
                    P8PrincipalCollector.collectPrincipalsFromSecurityPolicies(p8realm, objectStore, securityPolicySearch);
                    break;
                case "Event":
                    P8PrincipalCollector.collectPrincipalsFromEvents(p8realm, objectStore, eventSearch);
                    break;
                case "Subscription":
                    P8PrincipalCollector.collectPrincipalsFromSubscriptions(p8realm, objectStore, subscriptionSearch);
                    P8PrincipalCollector.collectPrincipalsFromEventAction(p8realm, objectStore);
                    P8PrincipalCollector.collectPrincipalsFromChangePreprocessorAction(p8realm, objectStore);
                    P8PrincipalCollector.collectPrincipalsFromContentConversionAction(p8realm, objectStore);
                    P8PrincipalCollector.collectPrincipalsFromDocumentClassificationAction(p8realm, objectStore);
                    P8PrincipalCollector.collectPrincipalsFromDocumentLifecycleAction(p8realm, objectStore);
                    P8PrincipalCollector.collectPrincipalsFromDocumentLifecyclePolicy(p8realm, objectStore);
                    P8PrincipalCollector.collectPrincipalsFromInstanceSubscription(p8realm, objectStore);
                    P8PrincipalCollector.collectPrincipalsFromRoleMembershipAction(p8realm, objectStore);
                    P8PrincipalCollector.collectPrincipalsFromSearchFunctionDefinition(p8realm, objectStore);
                    P8PrincipalCollector.collectPrincipalsFromSweepAction(p8realm, objectStore);
                    P8PrincipalCollector.collectPrincipalsFromTextIndexingPreprocessorAction(p8realm, objectStore);
                    break;
                case "Sweep":
                    P8PrincipalCollector.collectPrincipalsFromSweeps(p8realm, objectStore, sweepSearch);
                    break;
                case "SweepPolicy":
                    P8PrincipalCollector.collectPrincipalsFromSweepPolicies(p8realm, objectStore, sweepPolicySearch);
                    break;
                case "TableDefinition":
                    P8PrincipalCollector.collectPrincipalsFromTabledefinitions(p8realm, objectStore,tableDefinitionSearch);
                    break;
                case "UT_ClbDownloadRecord":
                    P8PrincipalCollector.collectPrincipalsFromAbstractsPersistable(p8realm, objectStore, "ClbDownloadRecord");
                    break;
                case "UT_ClbSummaryData":
                    P8PrincipalCollector.collectPrincipalsFromAbstractsPersistable(p8realm, objectStore, "ClbSummaryData");
                    break;
                case "UT_CmCustomRoleBase":
                    P8PrincipalCollector.collectPrincipalsFromAbstractsPersistable(p8realm, objectStore, "CmCustomRoleBase");
                    break;
                default:
                    break;
            }
        }

        PrincipalRepo.getInstance().getPrincipals().sort(new PrincipalComparator());
        ResultExporter.exportPrincipalCollectionToJsonfile(PrincipalRepo.getInstance().getPrincipals(), principalsJsonFile);
        ResultExporter.exportPrincipalsToCsv(PrincipalRepo.getInstance().getPrincipals(), principalsCsvFile);
    }
}
