package ar.com.lpa.samples.util;

import java.util.Iterator;

import com.filenet.api.admin.*;
import com.filenet.api.core.*;
import com.filenet.api.events.Event;
import com.filenet.api.events.Subscription;
import com.filenet.api.security.SecurityPolicy;
import com.filenet.api.security.SecurityTemplate;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.sweep.CmSweep;
import com.filenet.api.sweep.CmSweepPolicy;
import org.apache.log4j.Logger;

import ar.com.lpa.samples.model.fnObjects.P8Realm;
import ar.com.lpa.samples.repository.PrincipalRepo;

public class P8PrincipalCollector
{
	private static final Logger logger = Logger.getLogger(P8PrincipalCollector.class);

    public static void collectPrincipalsFromDocuments(PrincipalRepo principalRepo, P8Realm p8realm, String osName, String documentSearch) {
        try{
            logger.info(String.format("Collecting Principals from Documents - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,documentSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
				Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        Document document = (Document) it.next();
                        principalRepo.addPrincipalFromObjectOwner(document.get_Owner(), p8realm);
                        P8Logger.logDocumentProperties(logger, document, count);
                        if (!(document.get_Permissions().isEmpty())) {
                            principalRepo.addPrincipalsFromPermissions(document.get_Permissions(), p8realm);
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

    public static void collectPrincipalsFromCustomObjects(PrincipalRepo principalRepo, P8Realm p8realm, String osName, String customObjectSearch) {
        try{
            logger.info(String.format("Collecting Principals from Custom Objects - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm, logger, osName, customObjectSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        CustomObject customObject = (CustomObject) it.next();
                        principalRepo.addPrincipalFromObjectOwner(customObject.get_Owner(), p8realm);
                        P8Logger.logCustomObjectProperties(logger, customObject, count);
                        if (!(customObject.get_Permissions().isEmpty()))
                            principalRepo.addPrincipalsFromPermissions(customObject.get_Permissions(), p8realm);
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
    
    public static void collectPrincipalsFromFolders(PrincipalRepo principalRepo, P8Realm p8realm, String osName, String folderSearch) {
        try{
            logger.info(String.format("Collecting Principals from Folders - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,folderSearch);
            if(!(independentObjectSet.isEmpty())) {
                int count = 0;
                @SuppressWarnings("rawtypes")
                Iterator it = independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        Folder folder = (Folder) it.next();
                        principalRepo.addPrincipalFromObjectOwner(folder.get_Owner(), p8realm);
                        P8Logger.logFolderProperties(logger, folder, count);
                        if (!(folder.get_Permissions().isEmpty()))
                            principalRepo.addPrincipalsFromPermissions(folder.get_Permissions(), p8realm);
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

    public static void collectPrincipalsFromClassDefinitions(PrincipalRepo principalRepo, P8Realm p8realm, String osName, String classSearch) {
        try{
            logger.info(String.format("Collecting Principals from Class Definitions - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        ClassDefinition classDefinition = (ClassDefinition) it.next();
                        principalRepo.addPrincipalFromObjectOwner(classDefinition.get_Owner(), p8realm);
                        P8Logger.logClassProperties(logger, classDefinition, count);
                        if (!(classDefinition.get_Permissions().isEmpty()))
                            principalRepo.addPrincipalsFromPermissions(classDefinition.get_Permissions(), p8realm);
                        if (!(classDefinition.get_DefaultInstancePermissions().isEmpty()))
                            principalRepo.addPrincipalsFromPermissions(classDefinition.get_DefaultInstancePermissions(), p8realm);
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

    public static void collectPrincipalsFromAnnotations(PrincipalRepo principalRepo, P8Realm p8realm,String osName, String classSearch) {
        try{
            logger.info(String.format("Collecting Principals from Annotations - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()){
                    do {
                        count++;
                        Annotation annotation = (Annotation) it.next();
                        principalRepo.addPrincipalFromObjectOwner(annotation.get_Owner(), p8realm);
                        P8Logger.logAnnotationProperties(logger, annotation, count);
                        if (!(annotation.get_Permissions().isEmpty()))
                            principalRepo.addPrincipalsFromPermissions(annotation.get_Permissions(), p8realm);
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

    public static void collectPrincipalsFromChoiceLists(PrincipalRepo principalRepo, P8Realm p8realm,String osName, String classSearch) {
        try{
            logger.info(String.format("Collecting Principals from Choice Lists - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
            if(!(independentObjectSet.isEmpty())) {
                int count = 0;
                @SuppressWarnings("rawtypes")
                Iterator it = independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        ChoiceList choiceList = (ChoiceList) it.next();
                        principalRepo.addPrincipalFromObjectOwner(choiceList.get_Owner(), p8realm);
                        P8Logger.logChoiceListProperties(logger, choiceList, count);
                        if (!(choiceList.get_Permissions().isEmpty()))
                            principalRepo.addPrincipalsFromPermissions(choiceList.get_Permissions(), p8realm);
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

    public static void collectPrincipalsFromEvents(PrincipalRepo principalRepo, P8Realm p8realm,String osName, String classSearch) {
        try{
            logger.info(String.format("Collecting Principals from Events - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do{
                        count++;
                        Event event = (Event) it.next();
                        principalRepo.addPrincipalFromObjectOwner(event.get_Owner(), p8realm);
                        P8Logger.logEventProperties(logger, event, count);
                        if (!(event.get_Permissions().isEmpty()))
                            principalRepo.addPrincipalsFromPermissions(event.get_Permissions(), p8realm);
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

    public static void collectPrincipalsFromStoragePolicies(PrincipalRepo principalRepo, P8Realm p8realm,String osName, String classSearch) {
        try{
            logger.info(String.format("Collecting Principals from Storage Policies - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        StoragePolicy storagePolicy = (StoragePolicy) it.next();
                        principalRepo.addPrincipalFromObjectOwner(storagePolicy.get_Owner(), p8realm);
                        P8Logger.logStoragePolicyProperties(logger, storagePolicy, count);
                        if (!(storagePolicy.get_Permissions().isEmpty()))
                            principalRepo.addPrincipalsFromPermissions(storagePolicy.get_Permissions(), p8realm);
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

    public static void collectPrincipalsFromStorageAreas(PrincipalRepo principalRepo, P8Realm p8realm,String osName, String classSearch) {
        try{
            logger.info(String.format("Collecting Principals from Storage Areas - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
            if(!(independentObjectSet.isEmpty())) {
                int count = 0;
                @SuppressWarnings("rawtypes")
                Iterator it = independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        StorageArea storageArea = (StorageArea) it.next();
                        principalRepo.addPrincipalFromObjectOwner(storageArea.get_Owner(), p8realm);
                        P8Logger.logStorageAreaProperties(logger, storageArea, count);
                        if (!(storageArea.get_Permissions().isEmpty()))
                            principalRepo.addPrincipalsFromPermissions(storageArea.get_Permissions(), p8realm);
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

    public static void collectPrincipalsFromSecurityPolicies(PrincipalRepo principalRepo, P8Realm p8realm,String osName, String classSearch) {
        try{
            logger.info(String.format("Collecting Principals from Security Policies - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        SecurityPolicy securityPolicy = (SecurityPolicy) it.next();
                        principalRepo.addPrincipalFromObjectOwner(securityPolicy.get_Owner(), p8realm);
                        P8Logger.logSecurityPolicyProperties(logger, securityPolicy, count);
                        if (!(securityPolicy.get_Permissions().isEmpty()))
                            principalRepo.addPrincipalsFromPermissions(securityPolicy.get_Permissions(), p8realm);
                        if (!(securityPolicy.get_SecurityTemplates().isEmpty())){
                            Iterator it1 = securityPolicy.get_SecurityTemplates().iterator();
                            int count2 = 0;
                            if (it1.hasNext()) {
                                do {
                                    count2++;
                                    SecurityTemplate securityTemplate = (SecurityTemplate) it1.next();
                                    P8Logger.logSecurityTemplateProperties(logger, count2, securityTemplate);
                                    if (!(securityTemplate.get_TemplatePermissions().isEmpty()))
                                        principalRepo.addPrincipalsFromPermissions(securityTemplate.get_TemplatePermissions(), p8realm);
                                    } while(it1.hasNext());
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

    public static void collectPrincipalsFromSubscriptions(PrincipalRepo principalRepo, P8Realm p8realm,String osName, String classSearch) {
        try{
            logger.info(String.format("Collecting Principals from Subscriptions - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        Subscription subscription = (Subscription) it.next();
                        principalRepo.addPrincipalFromObjectOwner(subscription.get_Owner(), p8realm);
                        P8Logger.logSubscriptionProperties(logger, subscription, count);
                        if (!(subscription.get_Permissions().isEmpty()))
                            principalRepo.addPrincipalsFromPermissions(subscription.get_Permissions(), p8realm);
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

    public static void collectPrincipalsFromSweeps(PrincipalRepo principalRepo, P8Realm p8realm,String osName, String classSearch) {
        try{
            logger.info(String.format("Collecting Principals from Sweeps - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        CmSweep sweep = (CmSweep) it.next();
                        principalRepo.addPrincipalFromObjectOwner(sweep.get_Owner(), p8realm);
                        P8Logger.logSweepProperties(logger, sweep, count);
                        if (!(sweep.get_Permissions().isEmpty()))
                            principalRepo.addPrincipalsFromPermissions(sweep.get_Permissions(), p8realm);
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

    public static void collectPrincipalsFromSweepPolicies(PrincipalRepo principalRepo, P8Realm p8realm,String osName, String classSearch) {
    try{
        logger.info(String.format("Collecting Principals from Sweep Policies - Object Store: %s", osName));
        IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
        if(!(independentObjectSet.isEmpty())){
            int count=0;
            @SuppressWarnings("rawtypes")
            Iterator it=independentObjectSet.iterator();
            if (it.hasNext()) {
                do {
                    count++;
                    CmSweepPolicy sweepPolicy = (CmSweepPolicy) it.next();
                    principalRepo.addPrincipalFromObjectOwner(sweepPolicy.get_Owner(), p8realm);
                    P8Logger.logSweepPolicyProperties(logger, sweepPolicy, count);
                    if (!(sweepPolicy.get_Permissions().isEmpty())) {
                        principalRepo.addPrincipalsFromPermissions(sweepPolicy.get_Permissions(), p8realm);
                    }
                } while (it.hasNext());
            }
            logger.info("Total Sweep Policies: " + count);
        }
            else logger.info("No Sweep Policies were found!");
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }

    public static void collectPrincipalsFromTabledefinitions(PrincipalRepo principalRepo, P8Realm p8realm,String osName, String classSearch) {
        try{
            logger.info(String.format("Collecting Principals from Table Definitions - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        TableDefinition tableDefinition = (TableDefinition) it.next();
                        principalRepo.addPrincipalFromObjectOwner(tableDefinition.get_Owner(), p8realm);
                        P8Logger.logTableDefinitionProperties(logger, tableDefinition, count);
                        if (!(tableDefinition.get_Permissions().isEmpty())) {
                            principalRepo.addPrincipalsFromPermissions(tableDefinition.get_Permissions(), p8realm);
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

    public static void collectPrincipalsFromAbstractsPersistable(PrincipalRepo principalRepo, P8Realm p8realm,String osName, String classSearch) {
        try{
            logger.info(String.format("Collecting Principals from Abstract Persistables - Object Store: %s", osName));
            IndependentObjectSet independentObjectSet = P8ObjectSearch.getFnObjectsFromSearch(p8realm,logger,osName,classSearch);
            if(!(independentObjectSet.isEmpty())){
                int count=0;
                @SuppressWarnings("rawtypes")
                Iterator it=independentObjectSet.iterator();
                if (it.hasNext()) {
                    do {
                        count++;
                        CmAbstractPersistable cmAbstractPersistable = (CmAbstractPersistable) it.next();
                        principalRepo.addPrincipalFromObjectOwner(cmAbstractPersistable.get_Owner(), p8realm);
                        P8Logger.logAbstractPersistableProperties(logger, cmAbstractPersistable, count);
                        if (!(cmAbstractPersistable.get_Permissions().isEmpty())) {
                            principalRepo.addPrincipalsFromPermissions(cmAbstractPersistable.get_Permissions(), p8realm);
                        }
                    } while (it.hasNext());
                }
                logger.info("Total Abstracts Persistable: " + count);
            }
            else logger.info("No Abstracts Persistable were found!");
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }
}
