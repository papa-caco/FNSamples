package ar.com.lpa.samples.repository;

import ar.com.lpa.samples.model.FnObjectType;
import ar.com.lpa.samples.model.FnOwner;
import ar.com.lpa.samples.model.Principal;
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

    public void addOwnerFromAnnotation(Annotation annotation, Principal owner){
        this.createFnOwner(new FnOwner(FnObjectType.ANNOTATION,annotation.get_Id().toString(), owner));
    }

    public void addOwnerFromChoiceList(ChoiceList choiceList, Principal owner){
        this.createFnOwner(new FnOwner(FnObjectType.CHOICE_LIST, choiceList.get_Id().toString(), owner));
    }

    public void addOwnerFromClassDefinition(ClassDefinition classDefinition, Principal owner){
        this.createFnOwner(new FnOwner(FnObjectType.CLASS_DEFINITION, classDefinition.get_Id().toString(), owner));
    }

    public void addOwnerFromCustomObject(CustomObject customObject, Principal owner){
        this.createFnOwner(new FnOwner(FnObjectType.CUSTOM_OBJECT, customObject.get_Id().toString(), owner));
    }

    public void addOwnerFromDocument(Document document, Principal owner){
        this.createFnOwner(new FnOwner(FnObjectType.DOCUMENT, document.get_Id().toString(), owner));
    }

    public void addOwnerFromEvent(Event event, Principal owner){
        this.createFnOwner(new FnOwner(FnObjectType.EVENT, event.get_Id().toString(), owner));
    }

    public void addOwnerFromFolder(Folder folder, Principal owner){
        this.createFnOwner(new FnOwner(FnObjectType.FOLDER, folder.get_Id().toString(), owner));
    }

    public void addOwnerFromPropertyTemplate(PropertyTemplate propertyTemplate, Principal owner){
        this.createFnOwner(new FnOwner(FnObjectType.PROPERTY_TEMPLATE, propertyTemplate.get_Id().toString(), owner));
    }

    public void addOwnerFromStoragePolicy(StoragePolicy storagePolicy, Principal owner){
        this.createFnOwner(new FnOwner(FnObjectType.STORAGE_POLICY, storagePolicy.get_Id().toString(), owner));
    }

    public void addOwnerFromStorageArea(StorageArea storageArea, Principal owner){
        this.createFnOwner(new FnOwner(FnObjectType.STORAGE_AREA, storageArea.get_Id().toString(), owner));
    }

    public void addOwnerFromSecurityPolicy(SecurityPolicy securityPolicy, Principal owner){
        this.createFnOwner(new FnOwner(FnObjectType.SECURITY_POLICY, securityPolicy.get_Id().toString(), owner));
    }

    public void addOwnerFromSubscription(Subscription subscription, Principal owner){
        this.createFnOwner(new FnOwner(FnObjectType.SUBSCRIPTION, subscription.get_Id().toString(), owner));
    }

    public void addOwnerFromSweep(CmSweep sweep, Principal owner){
        this.createFnOwner(new FnOwner(FnObjectType.SWEEP, sweep.get_Id().toString(), owner));
    }

    public void addOwnerFromSweepPolicy(CmSweepPolicy sweepPolicy, Principal owner){
        this.createFnOwner(new FnOwner(FnObjectType.SWEEP_POLICY, sweepPolicy.get_Id().toString(), owner));
    }

    public void addOwnerFromTableDefinition(TableDefinition tableDefinition, Principal owner){
        this.createFnOwner(new FnOwner(FnObjectType.TABLE_DEFINITION, tableDefinition.get_Id().toString(), owner));
    }

    public void addOwnerFromAbstractPersistable(CmAbstractPersistable abstractPersistable, String abstractPersistableType, Principal owner){
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
        this.createFnOwner(new FnOwner(fnObjectType, abstractPersistable.get_Id().toString(), owner));
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
                .filter(fnOwner -> fnOwner.getOwner().equals(owner))
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
