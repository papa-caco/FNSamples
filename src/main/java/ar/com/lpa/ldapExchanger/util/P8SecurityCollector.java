package ar.com.lpa.ldapExchanger.util;

import ar.com.lpa.ldapExchanger.model.FnObjectType;
import ar.com.lpa.ldapExchanger.model.Principal;
import ar.com.lpa.ldapExchanger.model.PrincipalType;
import ar.com.lpa.ldapExchanger.model.SecurableObject;
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

    public static void collectSecurityFromDocuments(P8Realm p8realm, String osName, String documentSearch) {
        if (documentSearch == null) {
            documentSearch = "SELECT * FROM Document where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, documentSearch, FnObjectType.DOCUMENT);
    }

    public static void collectSecurityFromCustomObjects(P8Realm p8realm, String osName, String customObjectSearch) {
        if (customObjectSearch == null) {
            customObjectSearch = "SELECT * FROM CustomObject where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, customObjectSearch, FnObjectType.CUSTOM_OBJECT);
    }

    public static void collectSecurityFromFolders(P8Realm p8realm, String osName, String folderSearch) {
        if (folderSearch == null) {
            folderSearch = "Select * FROM Folder where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, folderSearch, FnObjectType.FOLDER);
    }

    public static void collectSecurityFromClassDefinitions(P8Realm p8realm, String osName, String classSearch) {
        if (classSearch == null) {
            classSearch = "select * FROM ClassDefinition where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm,osName, classSearch, FnObjectType.CLASS_DEFINITION);
    }

    public static void collectSecurityFromAnnotations(P8Realm p8realm,String osName, String annotationSearch) {
        if (annotationSearch == null) {
            annotationSearch = "Select * FROM Annotation where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, annotationSearch, FnObjectType.ANNOTATION);
    }

    public static void collectSecurityFromPropertyTemplates(P8Realm p8realm,String osName, String propertyTemplateSearch) {
        if (propertyTemplateSearch == null) {
            propertyTemplateSearch = "Select * FROM PropertyTemplate where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, propertyTemplateSearch, FnObjectType.PROPERTY_TEMPLATE);
    }

    public static void collectSecurityFromChoiceLists(P8Realm p8realm,String osName, String choiceListSearch) {
        if (choiceListSearch == null) {
            choiceListSearch = "Select * FROM ChoiceList where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, choiceListSearch, FnObjectType.CHOICE_LIST);
    }

    public static void collectSecurityFromEvents(P8Realm p8realm,String osName, String eventSearch) {
        if (eventSearch == null) {
            eventSearch = "Select * FROM Event where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, eventSearch, FnObjectType.EVENT);
    }

    public static void collectSecurityFromStoragePolicies(P8Realm p8realm,String osName, String storagePolicySearch) {
        if (storagePolicySearch == null) {
            storagePolicySearch = "Select * FROM StoragePolicy where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, storagePolicySearch, FnObjectType.STORAGE_POLICY);
    }

    public static void collectSecurityFromStorageAreas(P8Realm p8realm,String osName, String storageAreaSearch) {
        if (storageAreaSearch == null) {
            storageAreaSearch = "Select * FROM StorageArea where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, storageAreaSearch, FnObjectType.STORAGE_AREA);
    }

    public static void collectSecurityFromSecurityPolicies(P8Realm p8realm,String osName, String securityPolicySearch) {
        if (securityPolicySearch == null) {
            securityPolicySearch = "Select * FROM SecurityPolicy where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, securityPolicySearch, FnObjectType.SECURITY_POLICY);
    }

    public static void collectSecurityFromClassSubscriptions(P8Realm p8realm,String osName, String subscriptionSearch) {
        if (subscriptionSearch == null) {
            subscriptionSearch = "Select * FROM ClassSubscription where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, subscriptionSearch, FnObjectType.CLASS_SUBSCRIPTION);
    }

    public static void collectSecurityFromSweeps(P8Realm p8realm,String osName, String sweepSearch) {
        if (sweepSearch == null) {
            sweepSearch = "Select * FROM CmSweep where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, sweepSearch, FnObjectType.SWEEP);
    }

    public static void collectSecurityFromSweepPolicies(P8Realm p8realm,String osName, String sweepPolicySearch) {
        if (sweepPolicySearch == null) {
            sweepPolicySearch = "select * FROM CmSweepPolicy where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, sweepPolicySearch, FnObjectType.SWEEP_POLICY);
    }

    public static void collectSecurityFromTabledefinitions(P8Realm p8realm,String osName, String tableDefinitionSearch) {
        if (tableDefinitionSearch == null) {
            tableDefinitionSearch = "Select * FROM TableDefinition where Id IS NOT NULL";
        }
        collectSecurityFromRepositoryObjects(p8realm, osName, tableDefinitionSearch, FnObjectType.TABLE_DEFINITION);
    }

    public static void collectSecurityFromAbstractsPersistable(P8Realm p8realm,String osName, String abstractPersistableType) {
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
            collectSecurityFromRepositoryObjects(p8realm, osName, abstractPersistableSearch, fnObjectType);
        }
    }

    public static void collectSecurityFromEventActions(P8Realm p8realm, String osName) {
        String eventActionSearch = "Select * FROM EventAction where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, eventActionSearch, FnObjectType.EVENT_ACTION);
    }

    public static void collectSecurityFromChangePreprocessorAction(P8Realm p8realm, String osName){
        String objectSearch = "Select * FROM CmChangePreprocessorAction where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.CHANGE_PREPROCESSOR_ACTION);
    }
    public static void collectSecurityFromContentConversionAction(P8Realm p8realm, String osName){
        String objectSearch = "Select * FROM CmContentConversionAction where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.CONTENT_CONVERSION_ACTION);
    }
    public static void collectSecurityFromDocumentClassificationAction(P8Realm p8realm, String osName){
        String objectSearch = "Select * FROM DocumentClassificationAction where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.DOCUMENT_CLASSIFICATION_ACTION);
    }
    public static void collectSecurityFromDocumentLifecycleAction(P8Realm p8realm, String osName){
        String objectSearch = "Select * FROM DocumentLifecycleAction where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.DOCUMENT_LIFECYCLE_ACTION);
    }
    public static void collectSecurityFromDocumentLifecyclePolicy(P8Realm p8realm, String osName){
        String objectSearch = "Select * FROM DocumentLifecyclePolicy where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.DOCUMENT_LIFECYCLE_POLICY);
    }
    public static void collectSecurityFromInstanceSubscription(P8Realm p8realm, String osName){
        String objectSearch = "Select * FROM InstanceSubscription where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.INSTANCE_SUBSCRIPTION);
    }
    public static void collectSecurityFromRoleMembershipAction(P8Realm p8realm, String osName){
        String objectSearch = "Select * FROM CmRoleMembershipAction where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.ROLE_MEMBERSHIP_ACTION);
    }
    public static void collectSecurityFromSearchFunctionDefinition(P8Realm p8realm, String osName){
        String objectSearch = "Select * FROM CmSearchFunctionDefinition where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.SEARCH_FUNCTION_DEFINITION);
    }
    public static void collectSecurityFromSweepAction(P8Realm p8realm, String osName){
        String objectSearch = "Select * FROM CmSweepAction where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.SWEEP_ACTION);
    }
    public static void collectSecurityFromTextIndexingPreprocessorAction(P8Realm p8realm, String osName){
        String objectSearch = "Select * FROM CmTextIndexingPreprocessorAction where Id IS NOT NULL";
        collectSecurityFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.TEXT_INDEXING_PREPROCESSOR_ACTION);
    }

    private static void collectSecurityFromRepositoryObjects(P8Realm p8realm,String osName, String objectSearch, FnObjectType fnObjectType){
        try{
            logger.info(String.format("Collecting Principals from %s - Object Store: %s",fnObjectType.toString() ,osName));
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
                        if (owner == null){
                            owner = PrincipalRepo.getInstance().getPrincipalBySamAccountName(fnAdmin).getName();
                        } else {
                            PrincipalRepo.getInstance().addPrincipalFromObjectOwner(owner, p8realm);
                        }
                        Principal newOwner = PrincipalRepo.getInstance().getPrincipalByName(owner);
                        if (OwnerRepo.getInstance().addOwnerFromRepositoryObject(repositoryObject, newOwner, fnObjectType)) {
                            P8Logger.logRepositoryObjectProperties(logger, repositoryObject, fnObjectType, count);
                        }
                        String objectId = repositoryObject.getProperties().getIdValue("Id").toString();
                        if (!(repositoryObject.getProperties().getDependentObjectListValue("Permissions").isEmpty())) {
                            AccessPermissionList permissionList = (AccessPermissionList) repositoryObject.getProperties().getDependentObjectListValue("Permissions");
                            collectPermissionsFromRepositoryObjects(p8realm, fnObjectType, permissionList, objectId);
                        }
                        if (fnObjectType.equals(FnObjectType.CLASS_DEFINITION)){
                            if (!(repositoryObject.getProperties().getDependentObjectListValue("DefaultInstancePermissions").isEmpty())) {
                                AccessPermissionList defaultInstancePermissionList = (AccessPermissionList) repositoryObject.getProperties().getDependentObjectListValue("DefaultInstancePermissions");
                                collectPermissionsFromRepositoryObjects(p8realm, FnObjectType.CLASS_DEFINITION_DI, defaultInstancePermissionList, objectId);
                            }
                        }
                        if (fnObjectType.equals(FnObjectType.SECURITY_POLICY)){
                            if(!(repositoryObject.getProperties().getDependentObjectListValue("SecurityTemplates").isEmpty())){
                                SecurityTemplateList securityTemplateList = (SecurityTemplateList) repositoryObject.getProperties().getDependentObjectListValue("SecurityTemplates");
                                securityTemplatescount += securityTemplateList.size();
                                collectPermissionsFromSecurityTemplateList(p8realm, securityTemplateList);
                            }
                        }
                    } while (it.hasNext());
                }
                SecurableObject securableObject = SecurableObjectRepo.getInstance().getSecurableObjectByFnObjectType(fnObjectType);
                securableObject.setObjectCount(count);
                securableObject.setProcessStatus('R');
                SecurableObjectRepo.getInstance().updateSecurableObject(securableObject);
                logger.info(String.format("Total %s: %d", fnObjectType, count));
                if (fnObjectType.equals(FnObjectType.SECURITY_POLICY)){
                    SecurableObject securityTemplatesObject = SecurableObjectRepo.getInstance().getSecurableObjectByFnObjectType(FnObjectType.SECURITY_TEMPLATE);
                    securityTemplatesObject.setFnObjectType(FnObjectType.SECURITY_TEMPLATE);
                    securityTemplatesObject.setObjectCount(securityTemplatescount);
                    securityTemplatesObject.setProcessStatus('R');
                    SecurableObjectRepo.getInstance().updateSecurableObject(securityTemplatesObject);
                }
            }
            else logger.info(String.format("No %s were found!", fnObjectType));
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }

    private static void collectPermissionsFromRepositoryObjects(P8Realm p8realm, FnObjectType fnObjectType, AccessPermissionList permissionList, String objectId) {
        Iterator it1 = permissionList.iterator();
        if (it1.hasNext()) {
            do {
                AccessPermission permission = (AccessPermission) it1.next();
                PrincipalRepo.getInstance().addPrincipalFromPermission(permission, p8realm);
                Principal granteeName = PrincipalRepo.getInstance().getPrincipalByName(permission.get_GranteeName());
                if (granteeName != null) {
                    PermissionRepo.getInstance().addPermissionsFromFnObject(objectId, fnObjectType, granteeName, permission);
                }
            } while (it1.hasNext());
        }
    }

    private static void collectPermissionsFromSecurityTemplateList(P8Realm p8realm, SecurityTemplateList securityTemplateList){
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
                    collectPermissionsFromRepositoryObjects(p8realm,FnObjectType.SECURITY_TEMPLATE,permissionList,objectId);
                }
            } while(it2.hasNext());
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
}
