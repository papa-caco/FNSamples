package ar.com.lpa.ldapExchanger;

import ar.com.lpa.ldapExchanger.model.SecurableObject;
import ar.com.lpa.ldapExchanger.model.fnObjects.P8Realm;
import ar.com.lpa.ldapExchanger.repository.RealmGroupsRepo;
import ar.com.lpa.ldapExchanger.repository.RealmUsersRepo;
import ar.com.lpa.ldapExchanger.repository.SecurableObjectRepo;
import ar.com.lpa.ldapExchanger.util.*;
import org.apache.log4j.Logger;
import java.io.IOException;

import static ar.com.lpa.ldapExchanger.util.P8SecurityCollector.*;

public class SecurityRetriever {
    private static final Logger logger = Logger.getLogger(SecurityRetriever.class);
    private static final P8Realm p8realm = new P8Realm();
    private static final String configPath = "config.properties";

    private static ConfigLoader configLoader(){
        return new ConfigLoader(configPath);
    }

    private static void setRealmConnection() throws IOException {
        String fnAdmin = configLoader().getProperty("fnAdmin");
        p8realm.setConnectionCeUri(configLoader().getProperty("ceURI"));
        p8realm.setConnectionUser(fnAdmin);
        p8realm.setConnectionPswd(configLoader().getProperty("password"));
        p8realm.setRealm(logger);
        String ldapUsersCsvFile = configLoader().getProperty("LdapUsersCsvFile");
        String ldapGroupsCsvFile = configLoader().getProperty("LdapGroupsCsvFile");
        ResultExporter.exportUsersToCsv(RealmUsersRepo.getInstance().getRealmUsers(), ldapUsersCsvFile);
        ResultExporter.exportGroupsToCsv(RealmGroupsRepo.getInstance().getRealmGroups(), ldapGroupsCsvFile);
    }

    private static void obtainSecurableObjectsFromObjectStoreDB(){
        String dbType = configLoader().getProperty("dbType");
        String dbHost = configLoader().getProperty("dbHost");
        String dbPort = configLoader().getProperty("dbPort");
        String databaseName = configLoader().getProperty("databaseName");
        String schemaName = configLoader().getProperty("schemaName");
        String dbUserName = configLoader().getProperty("dbUserName");
        String dbUserPasswd = configLoader().getProperty("dbUserPswd");
        String tablesCsvFile = configLoader().getProperty("tablesCsvFile");

        switch (dbType) {
            case "SQLServer":
                SQLServerOperations.retreiveSecurableObjects(dbHost, dbPort, databaseName, dbUserName, dbUserPasswd, schemaName, tablesCsvFile);
                break;
            case "Oracle":
                // TODO
                break;
            case "DB2":
                // TODO
                break;
        }
    }

    private static void retrieveOwnersAndPermissionsFromEngineObjects(String objectStore){
        P8SecurityCollector.setFnAdmin(configLoader().getProperty("fnAdmin"));
        P8SecurityCollector.addFnAdminAsPrincipal(p8realm);
        for (SecurableObject securableObject : SecurableObjectRepo.getInstance().getSecurableObjectsByProcessStatus('N')) {
            switch (securableObject.getTableName().toLowerCase()) {
                case "annotation":
                    collectSecurityFromAnnotations(p8realm, objectStore, configLoader().getProperty("annotationSearch"));
                    break;
                case "globalpropertydef":
                    collectSecurityFromPropertyTemplates(p8realm, objectStore, configLoader().getProperty("propertyTemplateSearch"));
                    break;
                case "classdefinition":
                    collectSecurityFromClassDefinitions(p8realm, objectStore, configLoader().getProperty("classSearch"));
                    break;
                case "cvl":
                    collectSecurityFromChoiceLists(p8realm, objectStore, configLoader().getProperty("choiceListSearch"));
                    break;
                case "generic":
                    collectSecurityFromCustomObjects(p8realm, objectStore, configLoader().getProperty("customObjectSearch"));
                    break;
                case "storageclass":
                    collectSecurityFromStoragePolicies(p8realm, objectStore, configLoader().getProperty("storagePolicySearch"));
                    collectSecurityFromStorageAreas(p8realm, objectStore, configLoader().getProperty("storageAreaSearch"));
                    break;
                case "securitypolicy": //Include Security Templates
                    collectSecurityFromSecurityPolicies(p8realm, objectStore, configLoader().getProperty("securityPolicySearch"));
                    break;
                case "event":
                    collectSecurityFromEvents(p8realm, objectStore, configLoader().getProperty("eventSearch"));
                    break;
                case "subscription":
                    collectSecurityFromClassSubscriptions(p8realm, objectStore, configLoader().getProperty("subscriptionSearch"));
                    collectSecurityFromEventActions(p8realm, objectStore);
                    collectSecurityFromChangePreprocessorAction(p8realm, objectStore);
                    collectSecurityFromContentConversionAction(p8realm, objectStore);
                    collectSecurityFromDocumentClassificationAction(p8realm, objectStore);
                    collectSecurityFromDocumentLifecycleAction(p8realm, objectStore);
                    collectSecurityFromDocumentLifecyclePolicy(p8realm, objectStore);
                    collectSecurityFromInstanceSubscription(p8realm, objectStore);
                    collectSecurityFromRoleMembershipAction(p8realm, objectStore);
                    collectSecurityFromSearchFunctionDefinition(p8realm, objectStore);
                    collectSecurityFromSweepAction(p8realm, objectStore);
                    collectSecurityFromTextIndexingPreprocessorAction(p8realm, objectStore);
                    break;
                case "sweep":
                    collectSecurityFromSweeps(p8realm, objectStore, configLoader().getProperty("sweepSearch"));
                    break;
                case "sweeppolicy":
                    collectSecurityFromSweepPolicies(p8realm, objectStore, configLoader().getProperty("sweepPolicySearch"));
                    break;
                case "tabledefinition":
                    collectSecurityFromTabledefinitions(p8realm, objectStore, configLoader().getProperty("tableDefinitionSearch"));
                    break;
                case "ut_clbdownloadrecord":
                    collectSecurityFromAbstractsPersistable(p8realm, objectStore, "ClbDownloadRecord");
                    break;
                case "ut_clbsummarydata":
                    collectSecurityFromAbstractsPersistable(p8realm, objectStore, "ClbSummaryData");
                    break;
                case "ut_cmcustomrolebase":
                    collectSecurityFromAbstractsPersistable(p8realm, objectStore, "CmCustomRoleBase");
                    break;
                default:
                    break;
            }
        }
    }

    private static void retrieveOwnersAndPermissionsFromFoldersAndDocuments(String objectStore){
        for (SecurableObject securableObject : SecurableObjectRepo.getInstance().getSecurableObjectsByProcessStatus('N')) {
            switch (securableObject.getTableName().toLowerCase()) {
                case "container":
                    collectSecurityFromFolders(p8realm, objectStore, configLoader().getProperty("folderSearch"));
                    break;
                case "docversion":
                    collectSecurityFromDocuments(p8realm, objectStore, configLoader().getProperty("documentSearch"));
                    break;
                default:
                    break;
            }
        }

    }

    private static void exportPrincipalsOwnersAndPermissions() throws IOException {
        exportPrincipalsToFiles(configLoader().getProperty("PrincipalsJsonFile"),configLoader().getProperty("PrincipalsCsvFile"));
        exportOwnersToCsv(configLoader().getProperty("OwnersCsvFile"));
        exportPermissionsToCsv(configLoader().getProperty("PermissionsCsvFile"));
    }

    public static void main(String[] args) throws IOException {
        setRealmConnection();
        obtainSecurableObjectsFromObjectStoreDB();
        retrieveOwnersAndPermissionsFromEngineObjects(configLoader().getProperty("objectStore"));
        retrieveOwnersAndPermissionsFromFoldersAndDocuments(configLoader().getProperty("objectStore"));
        exportPrincipalsOwnersAndPermissions();
    }
}
