package ar.com.lpa.ldapExchanger;

import ar.com.lpa.ldapExchanger.model.FnBatch;
import ar.com.lpa.ldapExchanger.model.FnObjectType;
import ar.com.lpa.ldapExchanger.model.SecurableObject;
import ar.com.lpa.ldapExchanger.model.fnObjects.P8Realm;
import ar.com.lpa.ldapExchanger.repository.*;
import ar.com.lpa.ldapExchanger.util.*;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import  ar.com.lpa.ldapExchanger.util.P8SecurityCollector;

public class SecurityRetriever {
    private static final Logger logger = Logger.getLogger(SecurityRetriever.class);
    private static final P8Realm p8realm = new P8Realm();
    private static final String configPath = "config.properties";

    private static final int THREAD_POOL_SIZE = 1;//Runtime.getRuntime().availableProcessors(); // Auto Adjust according to CPU resources (8 in this computer)

    private static ConfigLoader configLoader(){
        return new ConfigLoader(configPath);
    }

    private static void setRealmConnection() throws IOException {
        System.out.println("Thread Pool Size: " + THREAD_POOL_SIZE);
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
        P8SecurityCollector.setFnAdmin(configLoader().getProperty("fnAdmin"));
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
    }

    private static void retrieveOwnersAndPermissionsFromFoldersAndDocuments(String objectStore){
        /*BatchRepo.getInstance().getFnBatchesByBatchStatus('N').parallelStream().forEach(fnBatch -> {
            if (!BatchRepo.getInstance().getFnBatchesByBatchStatus('R').isEmpty()){
                PermissionRepo.getInstance().deleteFnAccessPermissionsByBatchNumber(fnBatch.getBatchNumber());
                OwnerRepo.getInstance().deleteOwnersByBatchNumber(fnBatch.getBatchNumber());
            }
            switch (fnBatch.getBatchType()) {
                case FOLDER:
                    P8SecurityCollector.collectSecurityFromFolders(p8realm, objectStore, "Select * FROM Folder WHERE LockTimeout=" + fnBatch.getBatchNumber(),fnBatch);
                    break;
                case DOCUMENT:
                    P8SecurityCollector.collectSecurityFromDocuments(p8realm, objectStore, "Select * FROM Document WHERE LockTimeout=" + fnBatch.getBatchNumber(), fnBatch);
                    break;
            }
        });*/
        List<FnBatch> fnBatches = BatchRepo.getInstance().getFnBatchesByBatchStatus('N');
        ExecutorService executor = Executors.newFixedThreadPool(1);

        for (FnBatch fnBatch : fnBatches) {
            executor.submit(() -> {
                if (!BatchRepo.getInstance().getFnBatchesByBatchStatus('R').isEmpty()){
                    PermissionRepo.getInstance().deleteFnAccessPermissionsByBatchNumber(fnBatch.getBatchNumber());
                    OwnerRepo.getInstance().deleteOwnersByBatchNumber(fnBatch.getBatchNumber());
                }
                switch (fnBatch.getBatchType()) {
                    case FOLDER:
                        String folderSearch = "Select * FROM Folder WHERE LockTimeout=" + fnBatch.getBatchNumber();
                        P8SecurityCollector.collectSecurityFromFolders(p8realm, objectStore, folderSearch, fnBatch);
                        break;
                    case DOCUMENT:
                        String documentSearch = "Select * FROM Document WHERE LockTimeout=" + fnBatch.getBatchNumber();
                        P8SecurityCollector.collectSecurityFromDocuments(p8realm, objectStore, documentSearch, fnBatch);
                        break;
                    default:
                        break;
                }
            });
        }
        executor.shutdown(); // No acepta más tareas después de este punto
    }

    private static void exportPrincipalsOwnersAndPermissions() throws IOException {
        P8SecurityCollector.exportPrincipalsToFiles(configLoader().getProperty("PrincipalsJsonFile"),configLoader().getProperty("PrincipalsCsvFile"));
        P8SecurityCollector.exportOwnersToCsv(configLoader().getProperty("OwnersCsvFile"));
        P8SecurityCollector.exportPermissionsToCsv(configLoader().getProperty("PermissionsCsvFile"));
    }

    public static void main(String[] args) throws IOException {
        setRealmConnection();
        obtainSecurableObjectsFromObjectStoreDB();
        retrieveOwnersAndPermissionsFromEngineObjects(configLoader().getProperty("objectStore"));
        retrieveOwnersAndPermissionsFromFoldersAndDocuments(configLoader().getProperty("objectStore"));
        exportPrincipalsOwnersAndPermissions();
    }
}
