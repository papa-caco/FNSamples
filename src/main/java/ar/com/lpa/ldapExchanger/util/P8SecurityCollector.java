package ar.com.lpa.ldapExchanger.util;

import ar.com.lpa.ldapExchanger.model.*;
import ar.com.lpa.ldapExchanger.model.fnObjects.P8Realm;
import ar.com.lpa.ldapExchanger.repository.*;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.SecurityTemplateList;
import com.filenet.api.core.EngineObject;
import com.filenet.api.security.AccessPermission;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Getter
@Setter
public class P8SecurityCollector
{
	private static final Logger logger = Logger.getLogger(P8SecurityCollector.class);

    private static String fnAdmin = null;

    public static void setFnAdmin(String userName){
        fnAdmin = userName;
    }

    public static void collectSecurityFromDocuments(P8Realm p8realm, String osName, String documentSearch, FnBatch fnBatch) {
        if (documentSearch == null) {
            documentSearch = "SELECT * FROM Document where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, documentSearch, FnObjectType.DOCUMENT, fnBatch);
    }

    public static void collectSecurityFromCustomObjects(P8Realm p8realm, String osName, String customObjectSearch, FnBatch fnBatch) {
        if (customObjectSearch == null) {
            customObjectSearch = "SELECT * FROM CustomObject where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, customObjectSearch, FnObjectType.CUSTOM_OBJECT, fnBatch);
    }

    public static void collectSecurityFromFolders(P8Realm p8realm, String osName, String folderSearch, FnBatch fnBatch) {
        if (folderSearch == null) {
            folderSearch = "Select * FROM Folder where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, folderSearch, FnObjectType.FOLDER, fnBatch);
    }

    public static void collectSecurityFromClassDefinitions(P8Realm p8realm, String osName, String classSearch, FnBatch fnBatch) {
        if (classSearch == null) {
            classSearch = "select * FROM ClassDefinition where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm,osName, classSearch, FnObjectType.CLASS_DEFINITION, fnBatch);
    }

    public static void collectSecurityFromAnnotations(P8Realm p8realm,String osName, String annotationSearch, FnBatch fnBatch) {
        if (annotationSearch == null) {
            annotationSearch = "Select * FROM Annotation where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, annotationSearch, FnObjectType.ANNOTATION, fnBatch);
    }

    public static void collectSecurityFromPropertyTemplates(P8Realm p8realm,String osName, String propertyTemplateSearch, FnBatch fnBatch) {
        if (propertyTemplateSearch == null) {
            propertyTemplateSearch = "Select * FROM PropertyTemplate where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, propertyTemplateSearch, FnObjectType.PROPERTY_TEMPLATE, fnBatch);
    }

    public static void collectSecurityFromChoiceLists(P8Realm p8realm,String osName, String choiceListSearch, FnBatch fnBatch) {
        if (choiceListSearch == null) {
            choiceListSearch = "Select * FROM ChoiceList where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, choiceListSearch, FnObjectType.CHOICE_LIST, fnBatch);
    }

    public static void collectSecurityFromEvents(P8Realm p8realm,String osName, String eventSearch, FnBatch fnBatch) {
        if (eventSearch == null) {
            eventSearch = "Select * FROM Event where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, eventSearch, FnObjectType.EVENT, fnBatch);
    }

    public static void collectSecurityFromStoragePolicies(P8Realm p8realm,String osName, String storagePolicySearch, FnBatch fnBatch) {
        if (storagePolicySearch == null) {
            storagePolicySearch = "Select * FROM StoragePolicy where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, storagePolicySearch, FnObjectType.STORAGE_POLICY, fnBatch);
    }

    public static void collectSecurityFromStorageAreas(P8Realm p8realm,String osName, String storageAreaSearch, FnBatch fnBatch) {
        if (storageAreaSearch == null) {
            storageAreaSearch = "Select * FROM StorageArea where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, storageAreaSearch, FnObjectType.STORAGE_AREA, fnBatch);
    }

    public static void collectSecurityFromSecurityPolicies(P8Realm p8realm,String osName, String securityPolicySearch, FnBatch fnBatch) {
        if (securityPolicySearch == null) {
            securityPolicySearch = "Select * FROM SecurityPolicy where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, securityPolicySearch, FnObjectType.SECURITY_POLICY, fnBatch);
    }

    public static void collectSecurityFromClassSubscriptions(P8Realm p8realm,String osName, String subscriptionSearch, FnBatch fnBatch) {
        if (subscriptionSearch == null) {
            subscriptionSearch = "Select * FROM ClassSubscription where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, subscriptionSearch, FnObjectType.CLASS_SUBSCRIPTION, fnBatch);
    }

    public static void collectSecurityFromSweeps(P8Realm p8realm,String osName, String sweepSearch, FnBatch fnBatch) {
        if (sweepSearch == null) {
            sweepSearch = "Select * FROM CmSweep where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, sweepSearch, FnObjectType.SWEEP, fnBatch);
    }

    public static void collectSecurityFromSweepPolicies(P8Realm p8realm,String osName, String sweepPolicySearch, FnBatch fnBatch) {
        if (sweepPolicySearch == null) {
            sweepPolicySearch = "select * FROM CmSweepPolicy where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, sweepPolicySearch, FnObjectType.SWEEP_POLICY, fnBatch);
    }

    public static void collectSecurityFromTabledefinitions(P8Realm p8realm,String osName, String tableDefinitionSearch, FnBatch fnBatch) {
        if (tableDefinitionSearch == null) {
            tableDefinitionSearch = "Select * FROM TableDefinition where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, tableDefinitionSearch, FnObjectType.TABLE_DEFINITION, fnBatch);
    }

    public static void collectSecurityFromAbstractsPersistable(P8Realm p8realm,String osName, String abstractPersistableType, FnBatch fnBatch) {
        if (abstractPersistableType != null) {
            String abstractPersistableSearch = "select * from " + abstractPersistableType + " where Id IS NOT NULL";
            FnObjectType fnObjectType = null;
            switch (abstractPersistableType) {
                case "ClbDownloadRecord":
                    fnObjectType = FnObjectType.DOWNLOAD_RECORD;
                    break;
                case "ClbSummaryData":
                    fnObjectType = FnObjectType.SUMMARY_DATA;
                    break;
                case "CmCustomRoleBase":
                    fnObjectType = FnObjectType.CUSTOM_ROLE_BASE;
                    break;
                default:
                    fnObjectType = FnObjectType.ABSTRACT_PERSISTABLE;
                    break;
            }
            collectSecurityFromRepositoryObjects(p8realm, osName, abstractPersistableSearch, fnObjectType, fnBatch);
        }
    }

    public static void collectSecurityFromEventActions(P8Realm p8realm, String osName, FnBatch fnBatch) {
        String eventActionSearch = "Select * FROM EventAction where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, eventActionSearch, FnObjectType.EVENT_ACTION, fnBatch);
    }

    public static void collectSecurityFromChangePreprocessorAction(P8Realm p8realm, String osName, FnBatch fnBatch){
        String objectSearch = "Select * FROM CmChangePreprocessorAction where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.CHANGE_PREPROCESSOR_ACTION, fnBatch);
    }
    public static void collectSecurityFromContentConversionAction(P8Realm p8realm, String osName, FnBatch fnBatch){
        String objectSearch = "Select * FROM CmContentConversionAction where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.CONTENT_CONVERSION_ACTION, fnBatch);
    }
    public static void collectSecurityFromDocumentClassificationAction(P8Realm p8realm, String osName, FnBatch fnBatch){
        String objectSearch = "Select * FROM DocumentClassificationAction where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.DOCUMENT_CLASSIFICATION_ACTION, fnBatch);
    }
    public static void collectSecurityFromDocumentLifecycleAction(P8Realm p8realm, String osName, FnBatch fnBatch){
        String objectSearch = "Select * FROM DocumentLifecycleAction where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.DOCUMENT_LIFECYCLE_ACTION, fnBatch);
    }
    public static void collectSecurityFromDocumentLifecyclePolicy(P8Realm p8realm, String osName, FnBatch fnBatch){
        String objectSearch = "Select * FROM DocumentLifecyclePolicy where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.DOCUMENT_LIFECYCLE_POLICY, fnBatch);
    }
    public static void collectSecurityFromInstanceSubscription(P8Realm p8realm, String osName, FnBatch fnBatch){
        String objectSearch = "Select * FROM InstanceSubscription where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.INSTANCE_SUBSCRIPTION, fnBatch);
    }
    public static void collectSecurityFromRoleMembershipAction(P8Realm p8realm, String osName, FnBatch fnBatch){
        String objectSearch = "Select * FROM CmRoleMembershipAction where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.ROLE_MEMBERSHIP_ACTION, fnBatch);
    }
    public static void collectSecurityFromSearchFunctionDefinition(P8Realm p8realm, String osName, FnBatch fnBatch){
        String objectSearch = "Select * FROM CmSearchFunctionDefinition where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.SEARCH_FUNCTION_DEFINITION, fnBatch);
    }
    public static void collectSecurityFromSweepAction(P8Realm p8realm, String osName, FnBatch fnBatch){
        String objectSearch = "Select * FROM CmSweepAction where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.SWEEP_ACTION, fnBatch);
    }
    public static void collectSecurityFromTextIndexingPreprocessorAction(P8Realm p8realm, String osName, FnBatch fnBatch){
        String objectSearch = "Select * FROM CmTextIndexingPreprocessorAction where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.TEXT_INDEXING_PREPROCESSOR_ACTION, fnBatch);
    }

    private static void collectSecurityFromRepositoryObjects(P8Realm p8realm,String osName, String objectSearch, FnObjectType fnObjectType, FnBatch fnBatch){
        try{
            //logger.info(String.format("Collecting Security from %s - Object Store: %s",fnObjectType.toString() ,osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName, objectSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                int securityTemplatescount = 0;
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        EngineObject repositoryObject = (EngineObject) it.next();
                        String owner = repositoryObject.getProperties().getStringValue("Owner");
                        if (owner == null || !(RealmUsersRepo.getInstance().existsRealmUserByName(owner))){
                            owner = PrincipalRepo.getInstance().getPrincipalBySamAccountName(fnAdmin).getName();
                        } else {
                            PrincipalRepo.getInstance().addPrincipalFromObjectOwner(owner, p8realm);
                        }
                        Principal newOwner = PrincipalRepo.getInstance().getPrincipalByName(owner);
                        if (OwnerRepo.getInstance().addOwnerFromRepositoryObject(repositoryObject, newOwner, fnObjectType, fnBatch)) {
                            P8Logger.logRepositoryObjectProperties(logger, repositoryObject, fnObjectType, count);
                        }
                        String objectId = repositoryObject.getProperties().getIdValue("Id").toString();
                        if (!(repositoryObject.getProperties().getDependentObjectListValue("Permissions").isEmpty())) {
                            AccessPermissionList permissionList = (AccessPermissionList) repositoryObject.getProperties().getDependentObjectListValue("Permissions");
                            collectPermissionsFromRepositoryObjects(p8realm, fnObjectType, fnBatch,permissionList, objectId);
                        }
                        if (fnObjectType.equals(FnObjectType.CLASS_DEFINITION)){
                            if (!(repositoryObject.getProperties().getDependentObjectListValue("DefaultInstancePermissions").isEmpty())) {
                                AccessPermissionList defaultInstancePermissionList = (AccessPermissionList) repositoryObject.getProperties().getDependentObjectListValue("DefaultInstancePermissions");
                                collectPermissionsFromRepositoryObjects(p8realm, FnObjectType.CLASS_DEFINITION_DIP, fnBatch,defaultInstancePermissionList, objectId);
                            }
                        }
                        if (fnObjectType.equals(FnObjectType.SECURITY_POLICY)){
                            if(!(repositoryObject.getProperties().getDependentObjectListValue("SecurityTemplates").isEmpty())){
                                SecurityTemplateList securityTemplateList = (SecurityTemplateList) repositoryObject.getProperties().getDependentObjectListValue("SecurityTemplates");
                                securityTemplatescount += securityTemplateList.size();
                                collectPermissionsFromSecurityTemplateList(p8realm, fnBatch, securityTemplateList);
                            }
                        }
                    } while (it.hasNext());
                }
                int batchNumber = 0;
                if (fnObjectType == FnObjectType.DOCUMENT || fnObjectType == FnObjectType.FOLDER){
                    batchNumber = extractLockTimeoutValue(objectSearch);
                }
                updateSecurableObjectsAndBatchesStatus(fnObjectType, count, batchNumber ,securityTemplatescount);
            }
            else logger.info(String.format("No %s were found!", fnObjectType));
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }

    private static void collectPermissionsFromRepositoryObjects(P8Realm p8realm, FnObjectType fnObjectType, FnBatch fnBatch,AccessPermissionList permissionList, String objectId) {
        Iterator it1 = permissionList.iterator();
        if (it1.hasNext()) {
            do {
                AccessPermission permission = (AccessPermission) it1.next();
                PrincipalRepo.getInstance().addPrincipalFromPermission(permission, p8realm);
                Principal granteeName = PrincipalRepo.getInstance().getPrincipalByName(permission.get_GranteeName());
                if (granteeName != null) {
                    PermissionRepo.getInstance().addPermissionsFromFnObject(objectId, fnObjectType, fnBatch, granteeName, permission);
                }
            } while (it1.hasNext());
        }
    }

    private static void collectPermissionsFromSecurityTemplateList(P8Realm p8realm, FnBatch fnBatch,SecurityTemplateList securityTemplateList){
        Iterator it2 = securityTemplateList.iterator();
        int count2 = 0;
        if (it2.hasNext()) {
            do {
                count2++;
                EngineObject securityTemplate = (EngineObject) it2.next();
                String objectId = securityTemplate.getProperties().getIdValue("Id").toString();
                P8Logger.logRepositoryObjectProperties(logger, securityTemplate, FnObjectType.SECURITY_TEMPLATE, count2);
                if (!(securityTemplate.getProperties().getDependentObjectListValue("TemplatePermissions").isEmpty())) {
                    AccessPermissionList permissionList = (AccessPermissionList)  securityTemplate.getProperties().getDependentObjectListValue("TemplatePermissions");
                    collectPermissionsFromRepositoryObjects(p8realm,FnObjectType.SECURITY_TEMPLATE, fnBatch, permissionList,objectId);
                }
            } while(it2.hasNext());
        }
    }

    private static void updateSecurableObjectsAndBatchesStatus(FnObjectType fnObjectType, int objectCount, int batchCount, int securityTemplatescount){
        boolean lastBatch = false;
        if (fnObjectType == FnObjectType.DOCUMENT || fnObjectType == FnObjectType.FOLDER){
            FnBatch batch = BatchRepo.getInstance().getFnBatchByNumberAndType(fnObjectType, batchCount);
            batch.setObjectCount(objectCount);
            if (BatchRepo.getInstance().isLastBatchByStatus(batch)){
                lastBatch = true;
                objectCount = BatchRepo.getInstance().getTotalObjectsByBatchType(batch.getBatchType());
            }
            if (batch.getBatchSize() == batch.getObjectCount()){
                batch.setBatchStatus('R');
            } else {
                batch.setBatchStatus('E');
                logger.error(String.format("There were errors retrieving %s Batch# %d security", fnObjectType, batchCount));
            }
            BatchRepo.getInstance().updateFnBatch(batch);
        }
        if ((fnObjectType != FnObjectType.DOCUMENT && fnObjectType != FnObjectType.FOLDER) || lastBatch){
            SecurableObject securableObject = SecurableObjectRepo.getInstance().getSecurableObjectByFnObjectType(fnObjectType);
            securableObject.setObjectCount(objectCount);
            if (securableObject.getLineCount() == objectCount){
                securableObject.setProcessStatus('R');
            } else {
                securableObject.setProcessStatus('E');
            }
            securableObject.setProcessStatus('R');
            SecurableObjectRepo.getInstance().updateSecurableObject(securableObject);
            logger.info(String.format("Total %s: %d", fnObjectType, objectCount));
            if (fnObjectType.equals(FnObjectType.SECURITY_POLICY)) {
                SecurableObject securityTemplatesObject = SecurableObjectRepo.getInstance().getSecurableObjectByFnObjectType(FnObjectType.SECURITY_TEMPLATE);
                securityTemplatesObject.setFnObjectType(FnObjectType.SECURITY_TEMPLATE);
                securityTemplatesObject.setObjectCount(securityTemplatescount);
                if (securityTemplatesObject.getLineCount() == securityTemplatescount){
                    securityTemplatesObject.setProcessStatus('R');
                } else {
                    securityTemplatesObject.setProcessStatus('E');
                }
                securityTemplatesObject.setProcessStatus('R');
                SecurableObjectRepo.getInstance().updateSecurableObject(securityTemplatesObject);
            }
        }
    }

    public static void addFnAdminAsPrincipal(P8Realm p8realm){
        String fnAdminName = RealmUsersRepo.getInstance().getUserNameFromShortName(fnAdmin);
        PrincipalRepo.getInstance().addNewPrincipalFromShortName(fnAdminName,fnAdmin, PrincipalType.USER.toString(),p8realm);
    }

    public static void exportPrincipalsToFiles(String principalsJsonFile, String principalsCsvFile)  throws IOException {
        List<Principal> principals = new ArrayList<>(PrincipalRepo.getInstance().getPrincipals());
        principals.sort(new PrincipalComparator());
        ResultExporter.exportPrincipalCollectionToJsonfile(principals, principalsJsonFile);
        ResultExporter.exportPrincipalsToCsv(principals, principalsCsvFile);
    }

    public static void exportOwnersToCsv(String csvFile) throws IOException {
        ResultExporter.exportOwnersToCsv(OwnerRepo.getInstance().getFnOwners(), csvFile);
    }

    public static void exportPermissionsToCsv(String csvFile) throws IOException {
        ResultExporter.exportPermissionsToCsv(PermissionRepo.getInstance().getFnAccessPermissions(), csvFile);
    }

    private static int extractLockTimeoutValue(String query) {
        String pattern = "LockTimeout=(\\d+)";
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = regex.matcher(query);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            throw new IllegalArgumentException("LockTimeout value not present in query.");
        }
    }
}
