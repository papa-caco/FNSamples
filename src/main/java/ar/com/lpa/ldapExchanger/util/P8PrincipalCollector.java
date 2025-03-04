package ar.com.lpa.ldapExchanger.util;

import ar.com.lpa.ldapExchanger.model.FnObjectType;
import ar.com.lpa.ldapExchanger.model.fnObjects.P8Realm;
import ar.com.lpa.ldapExchanger.repository.PrincipalRepo;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.SecurityTemplateList;
import com.filenet.api.core.EngineObject;
import com.filenet.api.security.AccessPermission;
import org.apache.log4j.Logger;

import java.util.Iterator;

public class P8PrincipalCollector
{
	private static final Logger logger = Logger.getLogger(P8PrincipalCollector.class);

    public static void collectPrincipalsFromDocuments(P8Realm p8realm, String osName, String documentSearch) {
        if (documentSearch == null){
            documentSearch = "SELECT * FROM Document where Id IS NOT NULL";
        }
		collectPrincipalsFromRepositoryObjects(p8realm, osName, documentSearch, FnObjectType.DOCUMENT);
    }

    public static void collectPrincipalsFromCustomObjects(P8Realm p8realm, String osName, String customObjectSearch) {
        if (customObjectSearch == null){
            customObjectSearch = "SELECT * FROM CustomObject where Id IS NOT NULL";
        }
		collectPrincipalsFromRepositoryObjects(p8realm, osName, customObjectSearch, FnObjectType.CUSTOM_OBJECT);
    }
    
    public static void collectPrincipalsFromFolders(P8Realm p8realm, String osName, String folderSearch) {
        if (folderSearch == null){
            folderSearch = "Select * FROM Folder where Id IS NOT NULL";
        }
		collectPrincipalsFromRepositoryObjects(p8realm, osName, folderSearch, FnObjectType.FOLDER);
    }

    public static void collectPrincipalsFromClassDefinitions(P8Realm p8realm, String osName, String classSearch) {
        if (classSearch == null){
            classSearch = "select * FROM ClassDefinition where Id IS NOT NULL";
        }
		collectPrincipalsFromRepositoryObjects(p8realm, osName, classSearch, FnObjectType.CLASS_DEFINITION);
    }

    public static void collectPrincipalsFromAnnotations(P8Realm p8realm, String osName, String annotationSearch) {
        if (annotationSearch == null){
            annotationSearch = "Select * FROM Annotation where Id IS NOT NULL";
        }
		collectPrincipalsFromRepositoryObjects(p8realm, osName, annotationSearch, FnObjectType.ANNOTATION);
    }

    public static void collectPrincipalsFromPropertyTemplates(P8Realm p8realm,String osName, String propertyTemplateSearch) {
        if (propertyTemplateSearch == null){
            propertyTemplateSearch = "Select * FROM PropertyTemplate where Id IS NOT NULL";
        }
		collectPrincipalsFromRepositoryObjects(p8realm, osName, propertyTemplateSearch, FnObjectType.PROPERTY_TEMPLATE);
    }

    public static void collectPrincipalsFromChoiceLists(P8Realm p8realm,String osName, String choiceListSearch) {
        if (choiceListSearch == null){
            choiceListSearch = "Select * FROM ChoiceList where Id IS NOT NULL";
        }
		collectPrincipalsFromRepositoryObjects(p8realm, osName, choiceListSearch, FnObjectType.CHOICE_LIST);
    }

    public static void collectPrincipalsFromEvents(P8Realm p8realm,String osName, String eventSearch) {
        if (eventSearch == null){
            eventSearch = "Select * FROM Event where Id IS NOT NULL";
        }
		collectPrincipalsFromRepositoryObjects(p8realm, osName, eventSearch, FnObjectType.EVENT);
    }

    public static void collectPrincipalsFromStoragePolicies(P8Realm p8realm,String osName, String storagePolicySearch) {
        if (storagePolicySearch == null) {
            storagePolicySearch = "Select * FROM StoragePolicy where Id IS NOT NULL";
        }
		collectPrincipalsFromRepositoryObjects(p8realm, osName, storagePolicySearch, FnObjectType.STORAGE_POLICY);
    }

    public static void collectPrincipalsFromStorageAreas(P8Realm p8realm,String osName, String storageAreaSearch) {
        if (storageAreaSearch == null) {
            storageAreaSearch = "Select * FROM StorageArea where Id IS NOT NULL";
        }
		collectPrincipalsFromRepositoryObjects(p8realm, osName, storageAreaSearch, FnObjectType.STORAGE_AREA);
    }

    public static void collectPrincipalsFromSecurityPolicies(P8Realm p8realm,String osName, String securityPolicySearch) {
        if (securityPolicySearch == null) {
            securityPolicySearch = "Select * FROM SecurityPolicy where Id IS NOT NULL";
        }
		collectPrincipalsFromRepositoryObjects(p8realm, osName, securityPolicySearch, FnObjectType.SECURITY_POLICY);

    }

    public static void collectPrincipalsFromSubscriptions(P8Realm p8realm,String osName, String subscriptionSearch) {
        if (subscriptionSearch == null) {
            subscriptionSearch = "Select * FROM ClassSubscription where Id IS NOT NULL";
        }
		collectPrincipalsFromRepositoryObjects(p8realm, osName, subscriptionSearch, FnObjectType.CLASS_SUBSCRIPTION);
    }

    public static void collectPrincipalsFromSweeps(P8Realm p8realm,String osName, String sweepSearch) {
        if (sweepSearch == null) {
            sweepSearch = "Select * FROM CmSweep where Id IS NOT NULL";
        }
		collectPrincipalsFromRepositoryObjects(p8realm, osName, sweepSearch,FnObjectType.SWEEP);
    }

    public static void collectPrincipalsFromSweepPolicies(P8Realm p8realm,String osName, String sweepPolicySearch) {
        if (sweepPolicySearch == null) {
            sweepPolicySearch = "select * FROM CmSweepPolicy where Id IS NOT NULL";
        }
		collectPrincipalsFromRepositoryObjects(p8realm, osName, sweepPolicySearch, FnObjectType.SWEEP_POLICY);
    }

    public static void collectPrincipalsFromChangePreprocessorAction(P8Realm p8realm, String osName){
        String objectSearch = "select * FROM CmChangePreprocessorAction where Id IS NOT NULL";
        collectPrincipalsFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.CHANGE_PREPROCESSOR_ACTION);

    }
    public static void collectPrincipalsFromContentConversionAction(P8Realm p8realm, String osName){
        String objectSearch = "select * FROM CmContentConversionAction where Id IS NOT NULL";
        collectPrincipalsFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.CONTENT_CONVERSION_ACTION);
    }
    public static void collectPrincipalsFromDocumentClassificationAction(P8Realm p8realm, String osName){
        String objectSearch = "select * FROM DocumentClassificationAction where Id IS NOT NULL";
        collectPrincipalsFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.DOCUMENT_CLASSIFICATION_ACTION);
    }
    public static void collectPrincipalsFromDocumentLifecycleAction(P8Realm p8realm, String osName){
        String objectSearch = "select * FROM DocumentLifecycleAction where Id IS NOT NULL";
        collectPrincipalsFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.DOCUMENT_LIFECYCLE_ACTION);
    }
    public static void collectPrincipalsFromDocumentLifecyclePolicy(P8Realm p8realm, String osName){
        String objectSearch = "select * FROM DocumentLifecyclePolicy where Id IS NOT NULL";
        collectPrincipalsFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.DOCUMENT_LIFECYCLE_POLICY);
    }

    public static void collectPrincipalsFromEventAction(P8Realm p8realm, String osName){
        String objectSearch = "select * FROM EventAction where Id IS NOT NULL";
        collectPrincipalsFromRepositoryObjects(p8realm, osName, objectSearch,FnObjectType.EVENT_ACTION);
    }
    public static void collectPrincipalsFromInstanceSubscription(P8Realm p8realm, String osName){
        String objectSearch = "select * FROM InstanceSubscription where Id IS NOT NULL";
        collectPrincipalsFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.INSTANCE_SUBSCRIPTION);
    }
    public static void collectPrincipalsFromRoleMembershipAction(P8Realm p8realm, String osName){
        String objectSearch = "select * FROM CmRoleMembershipAction where Id IS NOT NULL";
        collectPrincipalsFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.ROLE_MEMBERSHIP_ACTION);
    }
    public static void collectPrincipalsFromSearchFunctionDefinition(P8Realm p8realm, String osName){
        String objectSearch = "select * FROM CmSearchFunctionDefinition where Id IS NOT NULL";
        collectPrincipalsFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.SEARCH_FUNCTION_DEFINITION);
    }
    public static void collectPrincipalsFromSweepAction(P8Realm p8realm, String osName){
        String objectSearch = "select * FROM CmSweepAction where Id IS NOT NULL";
        collectPrincipalsFromRepositoryObjects(p8realm, osName, objectSearch, FnObjectType.SWEEP_ACTION);
    }
    public static void collectPrincipalsFromTextIndexingPreprocessorAction(P8Realm p8realm, String osName){
        String objectSearch = "select * FROM CmTextIndexingPreprocessorAction where Id IS NOT NULL";
        collectPrincipalsFromRepositoryObjects(p8realm, osName, objectSearch,FnObjectType.TEXT_INDEXING_PREPROCESSOR_ACTION);
    }

    public static void collectPrincipalsFromAbstractsPersistable(P8Realm p8realm,String osName, String abstractPersistableType) {
        if (abstractPersistableType != null) {
            String abstractPersistableSearch = "select * from " + abstractPersistableType + " where Id IS NOT NULL";
            FnObjectType fnObjectType;
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
            collectPrincipalsFromRepositoryObjects(p8realm, osName, abstractPersistableSearch, fnObjectType);
        }
    }

    public static void collectPrincipalsFromTabledefinitions(P8Realm p8realm,String osName, String tableDefinitionSearch) {
		if (tableDefinitionSearch == null) {
			tableDefinitionSearch = "Select * FROM TableDefinition where Id IS NOT NULL";
		}
		collectPrincipalsFromRepositoryObjects(p8realm, osName, tableDefinitionSearch, FnObjectType.TABLE_DEFINITION);
	}

	private static void collectPrincipalsFromRepositoryObjects(P8Realm p8realm, String osName, String objectSearch, FnObjectType fnObjectType){
	try{
		logger.info(String.format("Collecting Principals from %s - Object Store: %s",fnObjectType.toString() ,osName));
		IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName, objectSearch);
		if(!(independentObjectSet.isEmpty())){
			int count=0;
			Iterator it=independentObjectSet.iterator();
			if (it.hasNext()) {
				do {
					count++;
					EngineObject repositoryObject = (EngineObject) it.next();
					String owner = repositoryObject.getProperties().getStringValue("Owner");
					PrincipalRepo.getInstance().addPrincipalFromObjectOwner(owner, p8realm);
					P8Logger.logRepositoryObjectProperties(logger, repositoryObject, fnObjectType, count);
					if (!(repositoryObject.getProperties().getDependentObjectListValue("Permissions").isEmpty())) {
						AccessPermissionList permissionList = (AccessPermissionList) repositoryObject.getProperties().getDependentObjectListValue("Permissions");
						collectPrincipalsFromRepositoryObjects(p8realm, permissionList);
					}
					if (fnObjectType.equals(FnObjectType.CLASS_DEFINITION)){
						if (!(repositoryObject.getProperties().getDependentObjectListValue("DefaultInstancePermissions").isEmpty())) {
							AccessPermissionList defaultInstancePermissionList = (AccessPermissionList) repositoryObject.getProperties().getDependentObjectListValue("DefaultInstancePermissions");
							collectPrincipalsFromRepositoryObjects(p8realm, defaultInstancePermissionList);
						}
					}
					if (fnObjectType.equals(FnObjectType.SECURITY_POLICY)){
						if(!(repositoryObject.getProperties().getDependentObjectListValue("SecurityTemplates").isEmpty())){
							SecurityTemplateList securityTemplateList = (SecurityTemplateList) repositoryObject.getProperties().getDependentObjectListValue("SecurityTemplates");
							collectPrincipalsFromSecurityTemplateList(p8realm, securityTemplateList);
						}
					}
				} while (it.hasNext());
			}
			logger.info(String.format("Total %s: %d", fnObjectType, count));
		}
		else logger.info(String.format("No %s were found!", fnObjectType));
	}
	catch(Exception e){
		//e.printStackTrace();
	}
}

	private static void collectPrincipalsFromRepositoryObjects(P8Realm p8realm, AccessPermissionList permissionList) {
		Iterator it1 = permissionList.iterator();
		if (it1.hasNext()) {
			do {
				AccessPermission permission = (AccessPermission) it1.next();
				PrincipalRepo.getInstance().addPrincipalFromPermission(permission, p8realm);
			} while (it1.hasNext());
		}
	}

	private static void collectPrincipalsFromSecurityTemplateList(P8Realm p8realm, SecurityTemplateList securityTemplateList){
		Iterator it2 = securityTemplateList.iterator();
		int count2 = 0;
		if (it2.hasNext()) {
			do {
				count2++;
				EngineObject securityTemplate = (EngineObject) it2.next();
				P8Logger.logRepositoryObjectProperties(logger, securityTemplate, FnObjectType.SECURITY_TEMPLATE, count2);
				if (!(securityTemplate.getProperties().getDependentObjectListValue("TemplatePermissions").isEmpty())) {
					AccessPermissionList permissionList = (AccessPermissionList)  securityTemplate.getProperties().getDependentObjectListValue("TemplatePermissions");
					collectPrincipalsFromRepositoryObjects(p8realm, permissionList);
				}
			} while(it2.hasNext());
		}
	}
}
