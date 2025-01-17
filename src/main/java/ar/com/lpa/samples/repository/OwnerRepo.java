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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import javax.persistence.PersistenceException;

@Getter
@Setter
public class OwnerRepo implements WithGlobalEntityManager {

    private static OwnerRepo instance = null;

    private OwnerRepo(){
    }

    public static OwnerRepo getInstance() {
        if (instance == null) {
            instance = new OwnerRepo();
        }
        return instance;
    }

    public void addOwnerFromAnnotation(Annotation annotation){
        this.createFnOwner(new FnOwner(FnObjectType.ANNOTATION,annotation.get_Id().toString(), annotation.get_Owner()));
    }

    public void addOwnerFromChoiceList(ChoiceList choiceList){
        this.createFnOwner(new FnOwner(FnObjectType.CHOICE_LIST, choiceList.get_Id().toString(), choiceList.get_Owner()));
    }

    public void addOwnerFromClassDefinition(ClassDefinition classDefinition){
        this.createFnOwner(new FnOwner(FnObjectType.CLASS_DEFINITION, classDefinition.get_Id().toString(), classDefinition.get_Owner()));
    }

    public void addOwnerFromCustomObject(CustomObject customObject){
        this.createFnOwner(new FnOwner(FnObjectType.CUSTOM_OBJECT, customObject.get_Id().toString(), customObject.get_Owner()));
    }

    public void addOwnerFromDocument(Document document){
        this.createFnOwner(new FnOwner(FnObjectType.DOCUMENT, document.get_Id().toString(), document.get_Owner()));
    }

    public void addOwnerFromEvent(Event event){
        this.createFnOwner(new FnOwner(FnObjectType.EVENT, event.get_Id().toString(), event.get_Owner()));
    }

    public void addOwnerFromFolder(Folder folder){
        this.createFnOwner(new FnOwner(FnObjectType.FOLDER, folder.get_Id().toString(), folder.get_Owner()));
    }

    public void addOwnerFromPropertyTemplate(PropertyTemplate propertyTemplate){
        this.createFnOwner(new FnOwner(FnObjectType.PROPERTY_TEMPLATE, propertyTemplate.get_Id().toString(), propertyTemplate.get_Owner()));
    }

    public void addOwnerFromStoragePolicy(StoragePolicy storagePolicy){
        this.createFnOwner(new FnOwner(FnObjectType.STORAGE_POLICY, storagePolicy.get_Id().toString(), storagePolicy.get_Owner()));
    }

    public void addOwnerFromStorageArea(StorageArea storageArea){
        this.createFnOwner(new FnOwner(FnObjectType.STORAGE_AREA, storageArea.get_Id().toString(), storageArea.get_Owner()));
    }

    public void addOwnerFromSecurityPolicy(SecurityPolicy securityPolicy){
        this.createFnOwner(new FnOwner(FnObjectType.SECURITY_POLICY, securityPolicy.get_Id().toString(), securityPolicy.get_Owner()));
    }

    public void addOwnerFromSubscription(Subscription subscription){
        this.createFnOwner(new FnOwner(FnObjectType.SUBSCRIPTION, subscription.get_Id().toString(), subscription.get_Owner()));
    }

    public void addOwnerFromSweep(CmSweep sweep){
        this.createFnOwner(new FnOwner(FnObjectType.SWEEP, sweep.get_Id().toString(), sweep.get_Owner()));
    }

    public void addOwnerFromSweepPolicy(CmSweepPolicy sweepPolicy){
        this.createFnOwner(new FnOwner(FnObjectType.SWEEP_POLICY, sweepPolicy.get_Id().toString(), sweepPolicy.get_Owner()));
    }

    public void addOwnerFromTableDefinition(TableDefinition tableDefinition){
        this.createFnOwner(new FnOwner(FnObjectType.TABLE_DEFINITION, tableDefinition.get_Id().toString(), tableDefinition.get_Owner()));
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
        this.createFnOwner(new FnOwner(fnObjectType, abstractPersistable.get_Id().toString(), abstractPersistable.get_Owner()));
    }

    private void createFnOwner(FnOwner fnOwner){
        try {
            entityManager().getTransaction().begin();
            entityManager().persist(fnOwner);
            entityManager().getTransaction().commit();
        } catch (PersistenceException e) {
            //e.printStackTrace();
            entityManager().getTransaction().rollback();
            throw new RuntimeException("An error has occurred persisting a new FnOwner, the operation cannot be completed", e);
        } finally {
            entityManager().close();
        }
    }

    private void deleteFnOwner(FnOwner fnOwner){
        try {
            entityManager().remove(fnOwner);
        } catch (PersistenceException e) {
            //e.printStackTrace();
            entityManager().getTransaction().rollback();
            throw new RuntimeException("An error has occurred removing FnOwner, the operation cannot be completed", e);
        }
        finally {
            entityManager().close();
        }
    }

    private void updateFnOwner(FnOwner fnOwner){
        try {
            entityManager().getTransaction().begin();
            int id = entityManager().merge(fnOwner).getIdFnOwner();
            fnOwner.setIdFnOwner(id);
            entityManager().getTransaction().commit();
        }
        catch (PersistenceException e) {
            //e.printStackTrace();
            entityManager().getTransaction().rollback();
            throw new RuntimeException("An error has occurred updating FnOwner, the operation cannot be completed", e);
        }
        finally {
            entityManager().close();
        }
    }

    public List<FnOwner> getFnOwners() {
        return entityManager().createQuery("from FnOwner").getResultList();
    }

    public FnOwner findFnOwnerByObjectId(String objectId){
        Optional<FnOwner> fetchedFnOwner = this.getFnOwners().stream().filter(fno -> fno.getObjectId().equals(objectId)).findFirst();
        return fetchedFnOwner.orElse(null);
    }

    public List<FnOwner> findFnOwnersByOwner(String owner) {
        return this.getFnOwners().stream()
                .filter(fnOwner -> fnOwner.getSourceOwner().equals(owner))
                .collect(Collectors.toList());
    }

    public List<FnOwner> findFnOwnersByFnObjectType(FnObjectType fnObjectType) {
        return this.getFnOwners().stream()
                .filter(fnOwner -> fnOwner.getFnObjectType().equals(fnObjectType))
                .collect(Collectors.toList());
    }

    public int ownersAmountByFnObjectType(FnObjectType fnObjectType){
        return this.findFnOwnersByFnObjectType(fnObjectType).size();
    }


}
