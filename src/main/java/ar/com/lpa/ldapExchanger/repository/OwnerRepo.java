package ar.com.lpa.ldapExchanger.repository;

import ar.com.lpa.ldapExchanger.model.FnObjectType;
import ar.com.lpa.ldapExchanger.model.FnOwner;
import ar.com.lpa.ldapExchanger.model.Principal;
import com.filenet.api.core.*;
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

    public boolean addOwnerFromRepositoryObject(EngineObject repositoryObject, Principal owner, FnObjectType fnObjectType){
        return this.createFnOwner(new FnOwner(fnObjectType, repositoryObject.getProperties().getIdValue("Id").toString(), owner));
    }

    private boolean createFnOwner(FnOwner fnOwner){
        if (!existsOwner(fnOwner)){
            try {
                entityManager().getTransaction().begin();
                entityManager().persist(fnOwner);
                entityManager().getTransaction().commit();
                return true;
            } catch (PersistenceException e) {
                //e.printStackTrace();
                entityManager().getTransaction().rollback();
                throw new RuntimeException("An error has occurred persisting a new FnOwner, the operation cannot be completed", e);
            } finally {
                entityManager().close();
            }
        }
        return false;
    }

    private void deleteFnOwner(FnOwner fnOwner){
        if (existsOwner(fnOwner)) {
            try {
                entityManager().remove(fnOwner);
            } catch (PersistenceException e) {
                //e.printStackTrace();
                entityManager().getTransaction().rollback();
                throw new RuntimeException("An error has occurred removing FnOwner, the operation cannot be completed", e);
            } finally {
                entityManager().close();
            }
        }
    }

    private void updateFnOwner(FnOwner fnOwner){
        if (existsOwner(fnOwner)) {
            try {
                entityManager().getTransaction().begin();
                int id = entityManager().merge(fnOwner).getIdFnOwner();
                fnOwner.setIdFnOwner(id);
                entityManager().getTransaction().commit();
            } catch (PersistenceException e) {
                //e.printStackTrace();
                entityManager().getTransaction().rollback();
                throw new RuntimeException("An error has occurred updating FnOwner, the operation cannot be completed", e);
            } finally {
                entityManager().close();
            }
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

    private boolean existsOwner(FnOwner newOwner){
        String objectId = newOwner.getObjectId();
        for (FnOwner fnOwner : getFnOwners()) {
            if (objectId.equals(fnOwner.getObjectId())) {
                return true;
            }
        }
        return false;
    }


}
