package ar.com.lpa.ldapExchanger;

import ar.com.lpa.ldapExchanger.model.FnBatch;
import ar.com.lpa.ldapExchanger.model.FnObjectType;
import ar.com.lpa.ldapExchanger.model.SecurableObject;
import ar.com.lpa.ldapExchanger.model.fnObjects.P8Realm;
import ar.com.lpa.ldapExchanger.repository.*;
import ar.com.lpa.ldapExchanger.util.*;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import  ar.com.lpa.ldapExchanger.util.P8SecurityCollector;

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
        int batchSize = Integer.parseInt(configLoader().getProperty("batchSize"));
        int folderBatchNumber = Integer.parseInt(configLoader().getProperty("folderBatchNumber"));
        int documentBatchNumber = Integer.parseInt(configLoader().getProperty("documentBatchNumber"));
        int maxDocumentBatchNumber = BatchRepo.getInstance().getMaxBatchNumber(FnObjectType.DOCUMENT);
        int maxFolderBatchNumber = BatchRepo.getInstance().getMaxBatchNumber(FnObjectType.FOLDER);
        if (maxFolderBatchNumber > 0){
            folderBatchNumber = maxFolderBatchNumber + 1;
        }
        if (maxDocumentBatchNumber > 0){
            documentBatchNumber = maxDocumentBatchNumber + 1;
        }
        logger.info("------------------ Starting Security Retrieve Process ------------------");
        switch (dbType) {
            case "SQLServer":
                SQLServerOperations.retreiveSecurableObjects(dbHost, dbPort, databaseName, dbUserName, dbUserPasswd, schemaName, tablesCsvFile);
                SQLServerOperations.createFolderBatches(dbHost,dbPort, databaseName, dbUserName, dbUserPasswd, schemaName, folderBatchNumber, batchSize, logger);
                SQLServerOperations.createDocumentBatches(dbHost,dbPort, databaseName, dbUserName, dbUserPasswd, schemaName, documentBatchNumber, batchSize, logger);
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
        if (P8SecurityCollector.getFnAdmin() == null){
            P8SecurityCollector.setFnAdmin(configLoader().getProperty("fnAdmin"));
        }
        P8SecurityCollector.addFnAdminAsPrincipal(p8realm);
        for (SecurableObject securableObject : SecurableObjectRepo.getInstance().getSecurableObjectsByProcessStatus('N')) {
            switch (securableObject.getTableName().toLowerCase()) {
                case "annotation":
                    P8SecurityCollector.collectSecurityFromAnnotations(p8realm, objectStore, configLoader().getProperty("annotationSearch"), null);
                    break;
                case "globalpropertydef":
                    P8SecurityCollector.collectSecurityFromPropertyTemplates(p8realm, objectStore, configLoader().getProperty("propertyTemplateSearch"), null);
                    break;
                case "classdefinition":
                    P8SecurityCollector.collectSecurityFromClassDefinitions(p8realm, objectStore, configLoader().getProperty("classSearch"), null);
                    break;
                case "cvl":
                    P8SecurityCollector.collectSecurityFromChoiceLists(p8realm, objectStore, configLoader().getProperty("choiceListSearch"), null);
                    break;
                case "generic":
                    P8SecurityCollector.collectSecurityFromCustomObjects(p8realm, objectStore, configLoader().getProperty("customObjectSearch"), null);
                    break;
                case "storageclass":
                    P8SecurityCollector.collectSecurityFromStoragePolicies(p8realm, objectStore, configLoader().getProperty("storagePolicySearch"), null);
                    P8SecurityCollector.collectSecurityFromStorageAreas(p8realm, objectStore, configLoader().getProperty("storageAreaSearch"), null);
                    break;
                case "securitypolicy": //Include Security Templates
                    P8SecurityCollector.collectSecurityFromSecurityPolicies(p8realm, objectStore, configLoader().getProperty("securityPolicySearch"), null);
                    break;
                case "event":
                    P8SecurityCollector.collectSecurityFromEvents(p8realm, objectStore, configLoader().getProperty("eventSearch"), null);
                    break;
                case "subscription":
                    P8SecurityCollector.collectSecurityFromClassSubscriptions(p8realm, objectStore, configLoader().getProperty("subscriptionSearch"), null);
                    P8SecurityCollector.collectSecurityFromEventActions(p8realm, objectStore, null);
                    P8SecurityCollector.collectSecurityFromChangePreprocessorAction(p8realm, objectStore, null);
                    P8SecurityCollector.collectSecurityFromContentConversionAction(p8realm, objectStore, null);
                    P8SecurityCollector.collectSecurityFromDocumentClassificationAction(p8realm, objectStore, null);
                    P8SecurityCollector.collectSecurityFromDocumentLifecycleAction(p8realm, objectStore, null);
                    P8SecurityCollector.collectSecurityFromDocumentLifecyclePolicy(p8realm, objectStore, null);
                    P8SecurityCollector.collectSecurityFromInstanceSubscription(p8realm, objectStore, null);
                    P8SecurityCollector.collectSecurityFromRoleMembershipAction(p8realm, objectStore, null);
                    P8SecurityCollector.collectSecurityFromSearchFunctionDefinition(p8realm, objectStore, null);
                    P8SecurityCollector.collectSecurityFromSweepAction(p8realm, objectStore, null);
                    P8SecurityCollector.collectSecurityFromTextIndexingPreprocessorAction(p8realm, objectStore, null);
                    break;
                case "sweep":
                    P8SecurityCollector.collectSecurityFromSweeps(p8realm, objectStore, configLoader().getProperty("sweepSearch"),null);
                    break;
                case "sweeppolicy":
                    P8SecurityCollector.collectSecurityFromSweepPolicies(p8realm, objectStore, configLoader().getProperty("sweepPolicySearch"),null);
                    break;
                case "tabledefinition":
                    P8SecurityCollector.collectSecurityFromTabledefinitions(p8realm, objectStore, configLoader().getProperty("tableDefinitionSearch"),null);
                    break;
                case "roleobject":
                    P8SecurityCollector.collectSecurityFromRoles(p8realm, objectStore, configLoader().getProperty("clbRoleSearch"),null);
                    break;
                case "ut_clbdownloadrecord":
                    P8SecurityCollector.collectSecurityFromAbstractsPersistable(p8realm, objectStore, "ClbDownloadRecord",null);
                    break;
                case "ut_clbsummarydata":
                    P8SecurityCollector.collectSecurityFromAbstractsPersistable(p8realm, objectStore, "ClbSummaryData",null);
                    break;
                case "ut_cmcustomrolebase":
                    P8SecurityCollector.collectSecurityFromAbstractsPersistable(p8realm, objectStore, "CmCustomRoleBase",null);
                    break;
                default:
                    break;
            }
        }
        logger.info("All Owners & Permissions retrieved from Engine Objects");
    }

    private static void retrieveOwnersAndPermissionsFromFoldersAndDocuments(String objectStore, FnObjectType batchType, int initialBatch, int finalBatch){
        if (P8SecurityCollector.getFnAdmin() == null){
            P8SecurityCollector.setFnAdmin(configLoader().getProperty("fnAdmin"));
        }
        List<FnBatch> fnBatches = new ArrayList<>();
        String objectSearch = null;
        if (batchType == FnObjectType.FOLDER) {
            fnBatches = BatchRepo.getInstance().getBatchesByTypeAndBatchNumber(FnObjectType.FOLDER, initialBatch, finalBatch);
            objectSearch = "Select * FROM Folder WHERE LockTimeout=";
            int count = 0;
            for (FnBatch fnBatch : fnBatches) {
                if (fnBatch.getBatchStatus() == 'N'){
                    PermissionRepo.getInstance().deleteFnAccessPermissionsByBatchNumber(fnBatch.getBatchNumber());
                    OwnerRepo.getInstance().deleteOwnersByBatchNumber(fnBatch.getBatchNumber());
                    String search = objectSearch + fnBatch.getBatchNumber();
                    P8SecurityCollector.collectSecurityFromFolders(p8realm, objectStore, search, fnBatch);
                    logger.info(String.format("Owners & Permissions retrieved from %s Batch #%d", batchType, fnBatch.getBatchNumber()));
                }
            }
        } else if (batchType == FnObjectType.DOCUMENT) {
            fnBatches = BatchRepo.getInstance().getBatchesByTypeAndBatchNumber(FnObjectType.DOCUMENT, initialBatch, finalBatch);
            objectSearch = "Select * FROM Document WHERE LockTimeout=";
            for (FnBatch fnBatch : fnBatches) {
                if (fnBatch.getBatchStatus() == 'N'){
                    PermissionRepo.getInstance().deleteFnAccessPermissionsByBatchNumber(fnBatch.getBatchNumber());
                    OwnerRepo.getInstance().deleteOwnersByBatchNumber(fnBatch.getBatchNumber());
                    String search = objectSearch + fnBatch.getBatchNumber();
                    P8SecurityCollector.collectSecurityFromDocuments(p8realm, objectStore, search, fnBatch);
                    logger.info(String.format("Owners & Permissions retrieved from %s Batch #%d", batchType, fnBatch.getBatchNumber()));
                }
            }
        }
    }

    private static void exportPrincipalsOwnersAndPermissions() throws IOException {
        P8SecurityCollector.exportPrincipalsToFiles(configLoader().getProperty("PrincipalsJsonFile"),configLoader().getProperty("PrincipalsCsvFile"));
        P8SecurityCollector.exportOwnersToCsv(configLoader().getProperty("OwnersCsvFile"));
        P8SecurityCollector.exportPermissionsToCsv(configLoader().getProperty("PermissionsCsvFile"));
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            logger.error("Incorrect Use. Enter at least: <objectStore> <retrieveType>");
            return;
        }
        String objectStore = args[0];;
        char retrieveType = args[1].charAt(0);
        Integer initialBatch = null;
        Integer finalBatch = null;
        if (retrieveType == 'D' || retrieveType == 'F') {
            if (args.length < 3) {
                initialBatch = 1;
                finalBatch = 10000000;
            } else if (args.length < 4){
                finalBatch = 10000000;
                try {
                    initialBatch = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    logger.error("Error: <finalBatch> must be Integer values.");
                    return;
                }
            } else {
                try {
                    initialBatch = Integer.parseInt(args[2]);
                    finalBatch = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    logger.error("Error: <initialBatch> & <finalBatch> must be Integer values.");
                    return;
                }
            }
        } else if (retrieveType != 'E') {
            logger.error("Error: retrieveType must be 'D', 'F' or 'E'.");
            return;
        }
        setRealmConnection();
        obtainSecurableObjectsFromObjectStoreDB();
        switch (retrieveType){
            case 'E':
                retrieveOwnersAndPermissionsFromEngineObjects(objectStore);
                break;
            case 'F':
                retrieveOwnersAndPermissionsFromFoldersAndDocuments(objectStore, FnObjectType.FOLDER , initialBatch, finalBatch);
                break;
            case 'D':
                retrieveOwnersAndPermissionsFromFoldersAndDocuments(objectStore, FnObjectType.DOCUMENT , initialBatch, finalBatch);
                break;
        }
        exportPrincipalsOwnersAndPermissions();
    }

}
