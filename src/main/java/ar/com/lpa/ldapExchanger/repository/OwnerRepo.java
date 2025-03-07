package ar.com.lpa.ldapExchanger.repository;

import ar.com.lpa.ldapExchanger.model.*;
import com.filenet.api.core.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

@Getter
@Setter
public class OwnerRepo implements WithGlobalEntityManager {

    private static final Logger logger = Logger.getLogger(OwnerRepo.class);

    private static OwnerRepo instance = null;

    private OwnerRepo(){
    }

    public static OwnerRepo getInstance() {
        if (instance == null) {
            instance = new OwnerRepo();
        }
        return instance;
    }

    public boolean addOwnerFromRepositoryObject(EngineObject repositoryObject, Principal owner, FnObjectType fnObjectType, FnBatch fnBatch){
        boolean result;
        if (fnObjectType == FnObjectType.DOCUMENT || fnObjectType == FnObjectType.FOLDER){
            createFnOwner(new FnOwner(fnObjectType, repositoryObject.getProperties().getIdValue("Id").toString(), fnBatch, owner));
            result = true;
        } else {
            if (!existsOwner(repositoryObject.getProperties().getIdValue("Id").toString(), fnObjectType)){
                createFnOwner(new FnOwner(fnObjectType, repositoryObject.getProperties().getIdValue("Id").toString(), fnBatch, owner));
                result = true;
            } else {
                result = false;
            }
        }
        return result;
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
        if (existsOwner(fnOwner.getObjectId(), fnOwner.getFnObjectType())) {
            try {
                EntityManager em = entityManager();
                em.getTransaction().begin(); // to ensure an active transaction
                FnOwner managedEntity = em.find(FnOwner.class, fnOwner.getIdFnOwner()); // Entity load
                if (managedEntity != null) {
                    em.remove(managedEntity);
                }
                em.getTransaction().commit();
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
        if (existsOwner(fnOwner.getObjectId(), fnOwner.getFnObjectType())) {
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

    private List<FnOwner> getOwnersWithBatch(){
        return this.getFnOwners().stream().filter(o -> o.getFnBatch() != null).collect(Collectors.toList());
    }

    public List<FnOwner> findFnOwnersByPrincipalName(String owner) {
        return this.getFnOwners().stream()
                .filter(fnOwner -> fnOwner.getOwner().getName().equals(owner))
                .collect(Collectors.toList());
    }

    public List<FnOwner> findFnOwnersByFnObjectType(FnObjectType fnObjectType) {
        return this.getFnOwners().stream()
                .filter(fnOwner -> fnOwner.getFnObjectType().equals(fnObjectType))
                .collect(Collectors.toList());
    }

    public void deleteOwnersByBatchNumber(int batchNumber){
        List<FnOwner> owners = this.getOwnersWithBatch().stream().filter(o -> o.getFnBatch().getBatchNumber() == batchNumber).collect(Collectors.toList());
        if (owners.isEmpty()) {
            return;
        }
        EntityManager em = entityManager();
        int count = 0;
        try {
            em.getTransaction().begin(); // Iniciar una sola transacción

            for (FnOwner fnOwner : owners) {
                FnOwner managedEntity = em.find(FnOwner.class, fnOwner.getIdFnOwner());
                if (managedEntity != null) {
                    em.remove(managedEntity);
                    count++;
                }
            }
            em.getTransaction().commit(); // Confirmar la transacción solo una vez
        } catch (PersistenceException e) {
            em.getTransaction().rollback(); // Revertir si hay un error
            throw new RuntimeException("Error deleting batch permissions", e);
        } finally {
            em.close(); // Cerrar el EntityManager
        }
        logger.debug(String.format("%d Owners deleted from uncompleted Batch# %d", count, batchNumber));
    }

    public int ownersAmountByFnObjectType(FnObjectType fnObjectType){
        return this.findFnOwnersByFnObjectType(fnObjectType).size();
    }

    private boolean existsOwner(String objectId, FnObjectType fnObjectType){
        for (FnOwner fnOwner : getFnOwners()) {
            if (fnOwner.getFnObjectType() == fnObjectType) {
                if(fnOwner.getObjectId().equals(objectId)){
                    return true;
                }
            }
        }
        return false;
    }

}
