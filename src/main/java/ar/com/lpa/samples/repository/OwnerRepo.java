package ar.com.lpa.samples.repository;

import ar.com.lpa.samples.model.FnObjectType;
import ar.com.lpa.samples.model.FnOwner;
import com.filenet.api.admin.*;
import com.filenet.api.core.*;
import com.filenet.api.events.Event;
import com.filenet.api.events.Subscription;
import com.filenet.api.security.SecurityPolicy;
import com.filenet.api.sweep.CmSweep;
import com.filenet.api.sweep.CmSweepPolicy;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OwnerRepo {
    private static final Logger logger = Logger.getLogger(OwnerRepo.class);
    private final List<FnOwner> fnOwners = new ArrayList<>();

    public void addOwnerFromAnnotation(Annotation annotation){
        FnOwner fnOwner = new FnOwner(FnObjectType.ANNOTATION,annotation.get_Id().toString(), annotation.get_Owner());
        fnOwners.add(fnOwner);
    }

    public void addOwnerFromChoiceList(ChoiceList choiceList){
        FnOwner fnOwner = new FnOwner(FnObjectType.CHOICE_LIST, choiceList.get_Id().toString(), choiceList.get_Owner());
        fnOwners.add(fnOwner);
    }

    public void addOwnerFromClassDefinition(ClassDefinition classDefinition){
        FnOwner fnOwner = new FnOwner(FnObjectType.CLASS_DEFINITION, classDefinition.get_Id().toString(), classDefinition.get_Owner());
        fnOwners.add(fnOwner);
    }

    public void addOwnerFromCustomObject(CustomObject customObject){
        FnOwner fnOwner = new FnOwner(FnObjectType.CUSTOM_OBJECT, customObject.get_Id().toString(), customObject.get_Owner());
        fnOwners.add(fnOwner);
    }

    public void addOwnerFromDocument(Document document){
        FnOwner fnOwner = new FnOwner(FnObjectType.DOCUMENT, document.get_Id().toString(), document.get_Owner());
        fnOwners.add(fnOwner);
    }

    public void addOwnerFromEvent(Event event){
        FnOwner fnOwner = new FnOwner(FnObjectType.EVENT, event.get_Id().toString(), event.get_Owner());
        fnOwners.add(fnOwner);
    }

    public void addOwnerFromFolder(Folder folder){
        FnOwner fnOwner = new FnOwner(FnObjectType.FOLDER, folder.get_Id().toString(), folder.get_Owner());
        fnOwners.add(fnOwner);
    }

    public void addOwnerFromPropertyTemplate(PropertyTemplate propertyTemplate){
        FnOwner fnOwner = new FnOwner(FnObjectType.PROPERTY_TEMPLATE, propertyTemplate.get_Id().toString(), propertyTemplate.get_Owner());
        fnOwners.add(fnOwner);
    }

    public void addOwnerFromStoragePolicy(StoragePolicy storagePolicy){
        FnOwner fnOwner = new FnOwner(FnObjectType.STORAGE_POLICY, storagePolicy.get_Id().toString(), storagePolicy.get_Owner());
        fnOwners.add(fnOwner);
    }

    public void addOwnerFromStorageArea(StorageArea storageArea){
        FnOwner fnOwner = new FnOwner(FnObjectType.STORAGE_AREA, storageArea.get_Id().toString(), storageArea.get_Owner());
        fnOwners.add(fnOwner);
    }

    public void addOwnerFromSecurityPolicy(SecurityPolicy securityPolicy){
        FnOwner fnOwner = new FnOwner(FnObjectType.SECURITY_POLICY, securityPolicy.get_Id().toString(), securityPolicy.get_Owner());
        fnOwners.add(fnOwner);
    }

    public void addOwnerFromSubscription(Subscription subscription){
        FnOwner fnOwner = new FnOwner(FnObjectType.SUBSCRIPTION, subscription.get_Id().toString(), subscription.get_Owner());
        fnOwners.add(fnOwner);
    }

    public void addOwnerFromSweep(CmSweep sweep){
        FnOwner fnOwner = new FnOwner(FnObjectType.SWEEP, sweep.get_Id().toString(), sweep.get_Owner());
        fnOwners.add(fnOwner);
    }

    public void addOwnerFromSweepPolicy(CmSweepPolicy sweepPolicy){
        FnOwner fnOwner = new FnOwner(FnObjectType.SWEEP_POLICY, sweepPolicy.get_Id().toString(), sweepPolicy.get_Owner());
        fnOwners.add(fnOwner);
    }

    public void addOwnerFromTableDefinition(TableDefinition tableDefinition){
        FnOwner fnOwner = new FnOwner(FnObjectType.TABLE_DEFINITION, tableDefinition.get_Id().toString(), tableDefinition.get_Owner());
        fnOwners.add(fnOwner);
    }

    public void addOwnerFromAbstractPersistable(CmAbstractPersistable abstractPersistable, String abstractPersistableType){
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
        FnOwner fnOwner = new FnOwner(fnObjectType, abstractPersistable.get_Id().toString(), abstractPersistable.get_Owner());
        fnOwners.add(fnOwner);
    }

    public FnOwner findFnOwnerByObjectId(String objectId){
        for (FnOwner fnOwner : fnOwners) {
            if (fnOwner.getObjectId().equals(objectId)) {
                return fnOwner;
            }
        }
        return null;
    }

    public List<FnOwner> findFnOwnersByOwner(String owner) {
        return fnOwners.stream()
                .filter(fnOwner -> fnOwner.getSourceOwner().equals(owner))
                .collect(Collectors.toList());
    }

    public List<FnOwner> findFnOwnersByFnObjectType(FnObjectType fnObjectType) {
        return fnOwners.stream()
                .filter(fnOwner -> fnOwner.getFnObjectType().equals(fnObjectType))
                .collect(Collectors.toList());
    }

    public int ownersAmountByFnObjectType(FnObjectType fnObjectType){
        List<FnOwner> owners = this.findFnOwnersByFnObjectType(fnObjectType);
        return owners.size();
    }


}
