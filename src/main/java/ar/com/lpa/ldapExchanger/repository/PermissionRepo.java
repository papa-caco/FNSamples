package ar.com.lpa.ldapExchanger.repository;

import ar.com.lpa.ldapExchanger.model.FnAccessPermission;
import ar.com.lpa.ldapExchanger.model.FnBatch;
import ar.com.lpa.ldapExchanger.model.FnObjectType;
import ar.com.lpa.ldapExchanger.model.Principal;
import com.filenet.api.security.AccessPermission;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import org.apache.log4j.Logger;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

@Getter
public class PermissionRepo  implements WithGlobalEntityManager {

    private static final Logger logger = Logger.getLogger(PermissionRepo.class);

    private static PermissionRepo instance = null;

    private PermissionRepo(){
    }

    public static PermissionRepo getInstance(){
        if (instance == null) {
            instance = new PermissionRepo();
        }
        return instance;
    }

    public List<FnAccessPermission> getFnAccessPermissions(){
        return entityManager().createQuery("from FnAccessPermission").getResultList();
    }

    private List<FnAccessPermission> getAccessPermissionsWithBatch(){
        return this.getFnAccessPermissions().stream().filter(p -> p.getFnBatch() != null).collect(Collectors.toList());
    }

    public void addPermissionsFromFnObject(String objectId, FnObjectType fnObjectType, FnBatch fnBatch, Principal granteeName, AccessPermission permission){
        if (fnObjectType != FnObjectType.DOCUMENT && fnObjectType != FnObjectType.FOLDER){
            if (!PermissionRepo.getInstance().existsFnAccessPermission(fnObjectType, objectId, granteeName.getName(), permission.get_InheritableDepth())){
                FnAccessPermission fnAccessPermission = new FnAccessPermission(objectId, fnObjectType,  fnBatch, granteeName, permission);
                this.createFnAccessPermission(fnAccessPermission);
            }
        } else {
            FnAccessPermission fnAccessPermission = new FnAccessPermission(objectId, fnObjectType, fnBatch, granteeName, permission);
            this.createFnAccessPermission(fnAccessPermission);
        }
    }

    public void deleteFnAccessPermissionsByBatchNumber(int batchNumber){
        List<FnAccessPermission> permissions = this.getAccessPermissionsWithBatch().stream().filter(p -> p.getFnBatch().getBatchNumber() == batchNumber).collect(Collectors.toList());
        if (permissions.isEmpty()) {
            return;
        }
        EntityManager em = entityManager();
        int count = 0;
        try {
            em.getTransaction().begin(); // Iniciar una sola transacción
            for (FnAccessPermission permission : permissions) {
                FnAccessPermission managedEntity = em.find(FnAccessPermission.class, permission.getIdFnAccessPermission());
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
        logger.debug(String.format("%d Access Permissions deleted from uncompleted Batch# %d", count, batchNumber));
    }

    private void createFnAccessPermission(FnAccessPermission fnAccessPermission){
        try {
            entityManager().getTransaction().begin();
            entityManager().persist(fnAccessPermission);
            entityManager().getTransaction().commit();
        } catch (PersistenceException e) {
            entityManager().getTransaction().rollback();
            throw new RuntimeException("An error has occurred persisting a new FnAccessPermission, the operation cannot be completed", e);
        } finally {
            entityManager().close();
        }
    }

    private void updateFnAccessPermission(FnAccessPermission fnAccessPermission){
        if (existsFnAccessPermission(fnAccessPermission.getFnObjectType(), fnAccessPermission.getObjectId(),
                fnAccessPermission.getGranteeName().getName(), fnAccessPermission.getInheritableDepth())) {
            try {
                entityManager().getTransaction().begin();
                int id = entityManager().merge(fnAccessPermission).getIdFnAccessPermission();
                fnAccessPermission.setIdFnAccessPermission(id);
                entityManager().getTransaction().commit();
            } catch (PersistenceException e) {
                //e.printStackTrace();
                entityManager().getTransaction().rollback();
                throw new RuntimeException("An error has occurred updating FnAccessPermission, the operation cannot be completed", e);
            } finally {
                entityManager().close();
            }
        }
    }

    private void deleteFnAccessPermission(FnAccessPermission fnAccessPermission){
        if (existsFnAccessPermission(fnAccessPermission.getFnObjectType(), fnAccessPermission.getObjectId(),
                fnAccessPermission.getGranteeName().getName(), fnAccessPermission.getInheritableDepth())) {
            try {
                EntityManager em = entityManager();
                em.getTransaction().begin(); // to ensure an active transaction
                FnAccessPermission managedEntity = em.find(FnAccessPermission.class, fnAccessPermission.getIdFnAccessPermission()); // Entity load
                if (managedEntity != null) {
                    em.remove(managedEntity);
                }
                em.getTransaction().commit();
            } catch (PersistenceException e) {
                //e.printStackTrace();
                entityManager().getTransaction().rollback();
                throw new RuntimeException("An error has occurred removing FnAccessPermission, the operation cannot be completed", e);
            } finally {
                entityManager().close();
            }
        }
    }

    //TODO
    private boolean existsFnAccessPermission(FnObjectType objectType, String objectId, String principalName, int inheritableDepth) {
        for (FnAccessPermission accessPermission : getFnAccessPermissions()) {
            if (accessPermission.getFnObjectType().equals(objectType)){
                if (accessPermission.getObjectId().equals(objectId)) {
                    if (accessPermission.getGranteeName().getName().equals(principalName)) {
                        if (accessPermission.getInheritableDepth() == inheritableDepth) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
