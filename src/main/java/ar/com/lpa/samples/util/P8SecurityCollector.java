package ar.com.lpa.samples.util;

import ar.com.lpa.samples.model.FnObjectType;
import ar.com.lpa.samples.model.fnObjects.P8Realm;
import ar.com.lpa.samples.repository.OwnerRepo;
import ar.com.lpa.samples.repository.PermissionRepo;
import ar.com.lpa.samples.repository.PrincipalRepo;
import com.filenet.api.admin.*;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.core.*;
import com.filenet.api.events.Event;
import com.filenet.api.events.Subscription;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.security.SecurityPolicy;
import com.filenet.api.security.SecurityTemplate;
import com.filenet.api.sweep.CmSweep;
import com.filenet.api.sweep.CmSweepPolicy;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Iterator;

public class P8SecurityCollector
{
	private static final Logger logger = Logger.getLogger(P8SecurityCollector.class);
    private static final PrincipalRepo principalRepo = new PrincipalRepo();


    public static void collectSecurityFromDocuments(P8Realm p8realm, String osName, String documentSearch) {
        if (documentSearch == null){
            documentSearch = "SELECT * FROM Document where Id IS NOT NULL";
        }
        try{
            logger.info(String.format("Collecting Principals from Documents - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,documentSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
				Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        Document document = (Document) it.next();
                        principalRepo.addPrincipalFromObjectOwner(document.get_Owner(), p8realm);
                        OwnerRepo.getInstance().addOwnerFromDocument(document);
                        P8Logger.logDocumentProperties(logger, document, count);
                        if (!(document.get_Permissions().isEmpty())) {
                            Iterator it1 = document.get_Permissions().iterator();
                            if (it1.hasNext()) {
                                do {
                                    AccessPermission permission = (AccessPermission) it1.next();
                                    principalRepo.addPrincipalFromPermission(permission,p8realm);
                                    PermissionRepo.getInstance().addPermissionsFromFnObject(document.get_Id().toString(), FnObjectType.DOCUMENT, permission);
                                } while (it1.hasNext());
                            }
                        }
                    } while (it.hasNext()) ;
                }
                logger.info("Total Documents: " + count);
            }
            else logger.info("No documents were found!");
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }

    public static void collectSecurityFromCustomObjects(P8Realm p8realm, String osName, String customObjectSearch) {
        if (customObjectSearch == null){
            customObjectSearch = "SELECT * FROM CustomObject where Id IS NOT NULL";
        }
        try{
            logger.info(String.format("Collecting Principals from Custom Objects - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm, logger, osName, customObjectSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        CustomObject customObject = (CustomObject) it.next();
                        principalRepo.addPrincipalFromObjectOwner(customObject.get_Owner(), p8realm);
                        OwnerRepo.getInstance().addOwnerFromCustomObject(customObject);
                        P8Logger.logCustomObjectProperties(logger, customObject, count);
                        if (!(customObject.get_Permissions().isEmpty())) {
                            Iterator it1 = customObject.get_Permissions().iterator();
                            if (it1.hasNext()) {
                                do {
                                    AccessPermission permission = (AccessPermission) it1.next();
                                    principalRepo.addPrincipalFromPermission(permission,p8realm);
                                    PermissionRepo.getInstance().addPermissionsFromFnObject(customObject.get_Id().toString(), FnObjectType.CUSTOM_OBJECT, permission);
                                } while (it1.hasNext());
                            }
                        }
                    }  while (it.hasNext()) ;
                }
                logger.info("Total Custom Objects: " + count);
            }
            else logger.info("No custom objects were found!");
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }
    
    public static void collectSecurityFromFolders(P8Realm p8realm, String osName, String folderSearch) {
        if (folderSearch == null){
            folderSearch = "Select * FROM Folder where Id IS NOT NULL";
        }
        try{
            logger.info(String.format("Collecting Principals from Folders - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,folderSearch);
            if(!(independentObjectSet.isEmpty())) {
                int count = 0;
                Iterator it = independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        Folder folder = (Folder) it.next();
                        principalRepo.addPrincipalFromObjectOwner(folder.get_Owner(), p8realm);
                        OwnerRepo.getInstance().addOwnerFromFolder(folder);
                        P8Logger.logFolderProperties(logger, folder, count);
                        if (!(folder.get_Permissions().isEmpty())) {
                            Iterator it1 = folder.get_Permissions().iterator();
                            if (it1.hasNext()) {
                                do {
                                    AccessPermission permission = (AccessPermission) it1.next();
                                    principalRepo.addPrincipalFromPermission(permission,p8realm);
                                    PermissionRepo.getInstance().addPermissionsFromFnObject(folder.get_Id().toString(), FnObjectType.FOLDER, permission);
                                } while (it1.hasNext());
                            }
                        }
                    } while (it.hasNext());
                }
                logger.info("Total Folders: " + count);
            }
            else logger.info("No folders were found!");
        }
        catch(Exception e) {
                //e.printStackTrace();
        }
    }

    public static void collectSecurityFromClassDefinitions(P8Realm p8realm, String osName, String classSearch) {
        if (classSearch == null){
            classSearch = "select * FROM ClassDefinition where Id IS NOT NULL";
        }
        try{
            logger.info(String.format("Collecting Principals from Class Definitions - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        ClassDefinition classDefinition = (ClassDefinition) it.next();
                        principalRepo.addPrincipalFromObjectOwner(classDefinition.get_Owner(), p8realm);
                        OwnerRepo.getInstance().addOwnerFromClassDefinition(classDefinition);
                        P8Logger.logClassProperties(logger, classDefinition, count);
                        if (!(classDefinition.get_Permissions().isEmpty())) {
                            Iterator it1 = classDefinition.get_Permissions().iterator();
                            if (it1.hasNext()) {
                                do {
                                    AccessPermission permission = (AccessPermission) it1.next();
                                    principalRepo.addPrincipalFromPermission(permission,p8realm);
                                    PermissionRepo.getInstance().addPermissionsFromFnObject(classDefinition.get_Id().toString(), FnObjectType.CLASS_DEFINITION, permission);
                                } while (it1.hasNext());
                            }
                        }
                        if (!(classDefinition.get_DefaultInstancePermissions().isEmpty())) {
                            Iterator it2 = classDefinition.get_DefaultInstancePermissions().iterator();
                            if (it2.hasNext()) {
                                do {
                                    AccessPermission permission = (AccessPermission) it2.next();
                                    principalRepo.addPrincipalFromPermission(permission,p8realm);
                                    PermissionRepo.getInstance().addPermissionsFromFnObject(classDefinition.get_Id().toString(), FnObjectType.CLASS_DEFINITION_DI, permission);
                                } while (it2.hasNext());
                            }
                        }
                    } while (it.hasNext());
                }
                logger.info("Total Class Definitions: " + count);
            }
            else logger.info("No Class Definitions were found!");
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }

    public static void collectSecurityFromAnnotations(P8Realm p8realm,String osName, String annotationSearch) {
        if (annotationSearch == null){
            annotationSearch = "Select * FROM Annotation where Id IS NOT NULL";
        }
        try{
            logger.info(String.format("Collecting Principals from Annotations - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,annotationSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()){
                    do {
                        count++;
                        Annotation annotation = (Annotation) it.next();
                        principalRepo.addPrincipalFromObjectOwner(annotation.get_Owner(), p8realm);
                        OwnerRepo.getInstance().addOwnerFromAnnotation(annotation);
                        P8Logger.logAnnotationProperties(logger, annotation, count);
                        if (!(annotation.get_Permissions().isEmpty())) {
                            Iterator it1 = annotation.get_Permissions().iterator();
                            if (it1.hasNext()) {
                                do {
                                    AccessPermission permission = (AccessPermission) it1.next();
                                    principalRepo.addPrincipalFromPermission(permission,p8realm);
                                    PermissionRepo.getInstance().addPermissionsFromFnObject(annotation.get_Id().toString(), FnObjectType.ANNOTATION, permission);
                                } while (it1.hasNext());
                            }
                        }
                     } while (it.hasNext());
                }
                logger.info("Total Annotations: " + count);
            }
            else logger.info("No Annotations were found!");
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }

    public static void collectSecurityFromPropertyTemplates(P8Realm p8realm,String osName, String propertyTemplateSearch) {
        if (propertyTemplateSearch == null){
            propertyTemplateSearch = "Select * FROM PropertyTemplate where Id IS NOT NULL";
        }
        try{
            logger.info(String.format("Collecting Principals from Property Templates - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,propertyTemplateSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()){
                    do {
                        count++;
                        PropertyTemplate propertyTemplate= (PropertyTemplate) it.next();
                        principalRepo.addPrincipalFromObjectOwner(propertyTemplate.get_Owner(), p8realm);
                        OwnerRepo.getInstance().addOwnerFromPropertyTemplate(propertyTemplate);
                        P8Logger.logPropertyTemplatesProperties(logger, propertyTemplate, count);
                        if (!(propertyTemplate.get_Permissions().isEmpty())) {
                            Iterator it1 = propertyTemplate.get_Permissions().iterator();
                            if (it1.hasNext()) {
                                do {
                                    AccessPermission permission = (AccessPermission) it1.next();
                                    principalRepo.addPrincipalFromPermission(permission,p8realm);
                                    PermissionRepo.getInstance().addPermissionsFromFnObject(propertyTemplate.get_Id().toString(), FnObjectType.PROPERTY_TEMPLATE, permission);
                                } while (it1.hasNext());
                            }
                        }
                    } while (it.hasNext());
                }
                logger.info("Total Property Templates: " + count);
            }
            else logger.info("No Property Templates were found!");
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }

    public static void collectSecurityFromChoiceLists(P8Realm p8realm,String osName, String choiceListSearch) {
        if (choiceListSearch == null){
            choiceListSearch = "Select * FROM ChoiceList where Id IS NOT NULL";
        }
        try{
            logger.info(String.format("Collecting Principals from Choice Lists - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,choiceListSearch);
            if(!(independentObjectSet.isEmpty())) {
                int count = 0;
                Iterator it = independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        ChoiceList choiceList = (ChoiceList) it.next();
                        principalRepo.addPrincipalFromObjectOwner(choiceList.get_Owner(), p8realm);
                        OwnerRepo.getInstance().addOwnerFromChoiceList(choiceList);
                        P8Logger.logChoiceListProperties(logger, choiceList, count);
                        if (!(choiceList.get_Permissions().isEmpty())) {
                            Iterator it1 = choiceList.get_Permissions().iterator();
                            if (it1.hasNext()) {
                                do {
                                    AccessPermission permission = (AccessPermission) it1.next();
                                    principalRepo.addPrincipalFromPermission(permission,p8realm);
                                    PermissionRepo.getInstance().addPermissionsFromFnObject(choiceList.get_Id().toString(), FnObjectType.CHOICE_LIST, permission);
                                } while (it1.hasNext());
                            }
                        }
                    } while (it.hasNext());
                }
                logger.info("Total Choice Lists: " + count);
            }
            else logger.info("No Choice Lists were found!");
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }

    public static void collectSecurityFromEvents(P8Realm p8realm,String osName, String eventSearch) {
        if (eventSearch == null){
            eventSearch = "Select * FROM Event where Id IS NOT NULL";
        }
        try{
            logger.info(String.format("Collecting Principals from Events - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,eventSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do{
                        count++;
                        Event event = (Event) it.next();
                        principalRepo.addPrincipalFromObjectOwner(event.get_Owner(), p8realm);
                        OwnerRepo.getInstance().addOwnerFromEvent(event);
                        P8Logger.logEventProperties(logger, event, count);
                        if (!(event.get_Permissions().isEmpty())) {
                            Iterator it1 = event.get_Permissions().iterator();
                            if (it1.hasNext()) {
                                do {
                                    AccessPermission permission = (AccessPermission) it1.next();
                                    principalRepo.addPrincipalFromPermission(permission,p8realm);
                                    PermissionRepo.getInstance().addPermissionsFromFnObject(event.get_Id().toString(), FnObjectType.EVENT, permission);
                                } while (it1.hasNext());
                            }
                        }
                    } while(it.hasNext());
                }
                logger.info("Total Events: " + count);
            }
            else logger.info("No Events were found!");
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }

    public static void collectSecurityFromStoragePolicies(P8Realm p8realm,String osName, String storagePolicySearch) {
        if (storagePolicySearch == null) {
            storagePolicySearch = "Select * FROM StoragePolicy where Id IS NOT NULL";
        }
        try{
            logger.info(String.format("Collecting Principals from Storage Policies - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,storagePolicySearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        StoragePolicy storagePolicy = (StoragePolicy) it.next();
                        principalRepo.addPrincipalFromObjectOwner(storagePolicy.get_Owner(), p8realm);
                        OwnerRepo.getInstance().addOwnerFromStoragePolicy(storagePolicy);
                        P8Logger.logStoragePolicyProperties(logger, storagePolicy, count);
                        if (!(storagePolicy.get_Permissions().isEmpty())) {
                            Iterator it1 = storagePolicy.get_Permissions().iterator();
                            if (it1.hasNext()) {
                                do {
                                    AccessPermission permission = (AccessPermission) it1.next();
                                    principalRepo.addPrincipalFromPermission(permission,p8realm);
                                    PermissionRepo.getInstance().addPermissionsFromFnObject(storagePolicy.get_Id().toString(), FnObjectType.STORAGE_POLICY, permission);
                                } while (it1.hasNext());
                            }
                        }
                        } while (it.hasNext());
                }
                logger.info("Total Storage Policies: " + count);
            }
            else logger.info("No Storage Policies were found!");
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }

    public static void collectSecurityFromStorageAreas(P8Realm p8realm,String osName, String storageAreaSearch) {
        if (storageAreaSearch == null) {
            storageAreaSearch = "Select * FROM StorageArea where Id IS NOT NULL";
        }
        try{
            logger.info(String.format("Collecting Principals from Storage Areas - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName, storageAreaSearch);
            if(!(independentObjectSet.isEmpty())) {
                int count = 0;
                Iterator it = independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        StorageArea storageArea = (StorageArea) it.next();
                        principalRepo.addPrincipalFromObjectOwner(storageArea.get_Owner(), p8realm);
                        OwnerRepo.getInstance().addOwnerFromStorageArea(storageArea);
                        P8Logger.logStorageAreaProperties(logger, storageArea, count);
                        if (!(storageArea.get_Permissions().isEmpty())) {
                            Iterator it1 = storageArea.get_Permissions().iterator();
                            if (it1.hasNext()) {
                                do {
                                    AccessPermission permission = (AccessPermission) it1.next();
                                    principalRepo.addPrincipalFromPermission(permission,p8realm);
                                    PermissionRepo.getInstance().addPermissionsFromFnObject(storageArea.get_Id().toString(), FnObjectType.STORAGE_AREA, permission);
                                } while (it1.hasNext());
                            }
                        }
                    } while (it.hasNext());
                }
                logger.info("Total Storage Areas: " + count);
            }
            else logger.info("No Storage Areas were found!");
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }

    public static void collectSecurityFromSecurityPolicies(P8Realm p8realm,String osName, String securityPolicySearch) {
        if (securityPolicySearch == null) {
            securityPolicySearch = "Select * FROM SecurityPolicy where Id IS NOT NULL";
        }
        try{
            logger.info(String.format("Collecting Principals from Security Policies - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,securityPolicySearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        SecurityPolicy securityPolicy = (SecurityPolicy) it.next();
                        principalRepo.addPrincipalFromObjectOwner(securityPolicy.get_Owner(), p8realm);
                        OwnerRepo.getInstance().addOwnerFromSecurityPolicy(securityPolicy);
                        P8Logger.logSecurityPolicyProperties(logger, securityPolicy, count);
                        if (!(securityPolicy.get_Permissions().isEmpty())) {
                            Iterator it1 = securityPolicy.get_Permissions().iterator();
                            if (it1.hasNext()) {
                                do {
                                    AccessPermission permission = (AccessPermission) it1.next();
                                    principalRepo.addPrincipalFromPermission(permission,p8realm);
                                    PermissionRepo.getInstance().addPermissionsFromFnObject(securityPolicy.get_Id().toString(), FnObjectType.SECURITY_POLICY, permission);
                                } while (it1.hasNext());
                            }
                        }
                        if (!(securityPolicy.get_SecurityTemplates().isEmpty())){
                            Iterator it2 = securityPolicy.get_SecurityTemplates().iterator();
                            int count2 = 0;
                            if (it2.hasNext()) {
                                do {
                                    count2++;
                                    SecurityTemplate securityTemplate = (SecurityTemplate) it2.next();
                                    P8Logger.logSecurityTemplateProperties(logger, count2, securityTemplate);
                                    if (!(securityTemplate.get_TemplatePermissions().isEmpty())) {
                                        Iterator it3 = securityTemplate.get_TemplatePermissions().iterator();
                                        if (it3.hasNext()) {
                                            do {
                                                AccessPermission permission = (AccessPermission) it3.next();
                                                principalRepo.addPrincipalFromPermission(permission,p8realm);
                                                PermissionRepo.getInstance().addPermissionsFromFnObject(securityTemplate.get_Id().toString(), FnObjectType.SECURITY_TEMPLATE, permission);
                                            } while (it3.hasNext());
                                        }
                                    }
                                } while(it2.hasNext());
                            }
                        }
                    } while(it.hasNext());
                }
                logger.info("Total Security Policies: " + count);
            }
            else logger.info("No Security Policies were found!");
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }

    public static void collectSecurityFromSubscriptions(P8Realm p8realm,String osName, String subscriptionSearch) {
        if (subscriptionSearch == null) {
        }
        subscriptionSearch = "Select * FROM Subscription where Id IS NOT NULL";
        try{
            logger.info(String.format("Collecting Principals from Subscriptions - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName, subscriptionSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        Subscription subscription = (Subscription) it.next();
                        principalRepo.addPrincipalFromObjectOwner(subscription.get_Owner(), p8realm);
                        OwnerRepo.getInstance().addOwnerFromSubscription(subscription);
                        P8Logger.logSubscriptionProperties(logger, subscription, count);
                        if (!(subscription.get_Permissions().isEmpty())) {
                            Iterator it1 = subscription.get_Permissions().iterator();
                            if (it1.hasNext()) {
                                do {
                                    AccessPermission permission = (AccessPermission) it1.next();
                                    principalRepo.addPrincipalFromPermission(permission,p8realm);
                                    PermissionRepo.getInstance().addPermissionsFromFnObject(subscription.get_Id().toString(), FnObjectType.SUBSCRIPTION, permission);
                                } while (it1.hasNext());
                            }
                        }
                    } while (it.hasNext());
                }
                logger.info("Total Subscriptions: " + count);
            }
            else logger.info("No Subscriptions were found!");
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }

    public static void collectSecurityFromSweeps(P8Realm p8realm,String osName, String sweepSearch) {
        if (sweepSearch == null) {
            sweepSearch = "Select * FROM CmSweep where Id IS NOT NULL";
        }
        try{
            logger.info(String.format("Collecting Principals from Sweeps - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,sweepSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        CmSweep sweep = (CmSweep) it.next();
                        principalRepo.addPrincipalFromObjectOwner(sweep.get_Owner(), p8realm);
                        OwnerRepo.getInstance().addOwnerFromSweep(sweep);
                        P8Logger.logSweepProperties(logger, sweep, count);
                        if (!(sweep.get_Permissions().isEmpty())) {
                            Iterator it1 = sweep.get_Permissions().iterator();
                            if (it1.hasNext()) {
                                do {
                                    AccessPermission permission = (AccessPermission) it1.next();
                                    principalRepo.addPrincipalFromPermission(permission,p8realm);
                                    PermissionRepo.getInstance().addPermissionsFromFnObject(sweep.get_Id().toString(), FnObjectType.SWEEP, permission);
                                } while (it1.hasNext());
                            }
                        }
                    } while (it.hasNext());
                }
                logger.info("Total Sweeps: " + count);
            }
            else logger.info("No Sweeps were found!");
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }

    public static void collectSecurityFromSweepPolicies(P8Realm p8realm,String osName, String sweepPolicySearch) {
        if (sweepPolicySearch == null) {
            sweepPolicySearch = "select * FROM CmSweepPolicy where Id IS NOT NULL";
        }
        try{
            logger.info(String.format("Collecting Principals from Sweep Policies - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,sweepPolicySearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        CmSweepPolicy sweepPolicy = (CmSweepPolicy) it.next();
                        principalRepo.addPrincipalFromObjectOwner(sweepPolicy.get_Owner(), p8realm);
                        OwnerRepo.getInstance().addOwnerFromSweepPolicy(sweepPolicy);
                        P8Logger.logSweepPolicyProperties(logger, sweepPolicy, count);
                        if (!(sweepPolicy.get_Permissions().isEmpty())) {
                            Iterator it1 = sweepPolicy.get_Permissions().iterator();
                            if (it1.hasNext()) {
                                do {
                                    AccessPermission permission = (AccessPermission) it1.next();
                                    principalRepo.addPrincipalFromPermission(permission,p8realm);
                                    PermissionRepo.getInstance().addPermissionsFromFnObject(sweepPolicy.get_Id().toString(), FnObjectType.SWEEP_POLICY, permission);
                                } while (it1.hasNext());
                            }
                        }
                    } while (it.hasNext());
                }
                logger.info("Total Sweep Policies: " + count);
            }  else logger.info("No Sweep Policies were found!");
        }  catch(Exception e){
            //e.printStackTrace();
        }
    }

    public static void collectSecurityFromTabledefinitions(P8Realm p8realm,String osName, String tableDefinitionSearch) {
        if (tableDefinitionSearch == null) {
            tableDefinitionSearch = "Select * FROM TableDefinition where Id IS NOT NULL";
        }
        try{
            logger.info(String.format("Collecting Principals from Table Definitions - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName, tableDefinitionSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        TableDefinition tableDefinition = (TableDefinition) it.next();
                        principalRepo.addPrincipalFromObjectOwner(tableDefinition.get_Owner(), p8realm);
                        OwnerRepo.getInstance().addOwnerFromTableDefinition(tableDefinition);
                        P8Logger.logTableDefinitionProperties(logger, tableDefinition, count);
                        if (!(tableDefinition.get_Permissions().isEmpty())) {
                            Iterator it1 = tableDefinition.get_Permissions().iterator();
                            if (it1.hasNext()) {
                                do {
                                    AccessPermission permission = (AccessPermission) it1.next();
                                    principalRepo.addPrincipalFromPermission(permission,p8realm);
                                    PermissionRepo.getInstance().addPermissionsFromFnObject(tableDefinition.get_Id().toString(), FnObjectType.TABLE_DEFINITION, permission);
                                } while (it1.hasNext());
                            }
                        }
                    } while (it.hasNext());
                }
                logger.info("Total Table Definitions: " + count);
            }
            else logger.info("No Table Definitions were found!");
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }

    public static void collectSecurityFromAbstractsPersistable(P8Realm p8realm,String osName, String abstractPersistableType) {
        if (abstractPersistableType != null) {
            String abstractPersistableSearch = "select * from " + abstractPersistableType + " where Id IS NOT NULL";
            try{
                logger.info(String.format("Collecting Principals from %s - Object Store: %s", abstractPersistableType, osName));
                IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName, abstractPersistableSearch);
                if(!(independentObjectSet.isEmpty())){
                    int count=0;
                    Iterator it=independentObjectSet.iterator();
                    if (it.hasNext()) {
                        do {
                            count++;
                            CmAbstractPersistable cmAbstractPersistable = (CmAbstractPersistable) it.next();
                            principalRepo.addPrincipalFromObjectOwner(cmAbstractPersistable.get_Owner(), p8realm);
                            OwnerRepo.getInstance().addOwnerFromAbstractPersistable(cmAbstractPersistable, abstractPersistableType);
                            P8Logger.logAbstractPersistableProperties(logger, cmAbstractPersistable, count);
                            if (!(cmAbstractPersistable.get_Permissions().isEmpty())) {
                                Iterator it1 = cmAbstractPersistable.get_Permissions().iterator();
                                if (it1.hasNext()) {
                                    do {
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
                                        AccessPermission permission = (AccessPermission) it1.next();
                                        principalRepo.addPrincipalFromPermission(permission,p8realm);
                                        PermissionRepo.getInstance().addPermissionsFromFnObject(cmAbstractPersistable.get_Id().toString(), fnObjectType, permission);
                                    } while (it1.hasNext());
                                }
                            }
                        } while (it.hasNext());
                    }
                    logger.info(String.format("Total %s: %d", abstractPersistableType, count));
                }
                else logger.info(String.format("No %s were found!", abstractPersistableType));
            }
            catch(Exception e){
                //e.printStackTrace();
            }
        }
    }

    public static void exportPrincipalsToFiles(String principalsJsonFile, String principalsCsvFile)  throws IOException {
            principalRepo.getPrincipals().sort(new PrincipalComparator());
        ResultExporter.exportPrincipalCollectionToJsonfile(principalRepo.getPrincipals(), principalsJsonFile);
        ResultExporter.exportPrincipalsToCsv(principalRepo.getPrincipals(), principalsCsvFile);
    }

    public static void exportOwnersToCsv(String csvFile) throws IOException {
        ResultExporter.exportOwnersToCsv(OwnerRepo.getInstance().getFnOwners(), csvFile);
    }

    public static void exportPermissionsToCsv(String csvFile) throws IOException {
        ResultExporter.exportPermissionsToCsv(PermissionRepo.getInstance().getFnAccessPermissions(), csvFile);
    }
}
