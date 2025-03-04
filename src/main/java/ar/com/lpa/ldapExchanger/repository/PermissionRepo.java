package ar.com.lpa.ldapExchanger.repository;

import ar.com.lpa.ldapExchanger.model.FnAccessPermission;
import ar.com.lpa.ldapExchanger.model.FnObjectType;
import ar.com.lpa.ldapExchanger.model.Principal;
import com.filenet.api.security.AccessPermission;

import java.util.List;

import lombok.Getter;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import javax.persistence.PersistenceException;

@Getter
public class PermissionRepo  implements WithGlobalEntityManager {

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

    public void addPermissionsFromFnObject(String objectId, FnObjectType fnObjectType, Principal granteeName, AccessPermission permission){
        FnAccessPermission fnAccessPermission = new FnAccessPermission(objectId, fnObjectType, granteeName, permission);
        this.createFnAccessPermission(fnAccessPermission);
    }

    private void createFnAccessPermission(FnAccessPermission fnAccessPermission){
           if (!existsFnAccessPermission(fnAccessPermission)) {
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
    }

    private void updateFnAccessPermission(FnAccessPermission fnAccessPermission){
        if (existsFnAccessPermission(fnAccessPermission)) {
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
        if (existsFnAccessPermission(fnAccessPermission)) {
            try {
                entityManager().remove(fnAccessPermission);
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
    private boolean existsFnAccessPermission(FnAccessPermission fnAccessPermission) {
        String objectId = fnAccessPermission.getObjectId();
        String principalName = fnAccessPermission.getGranteeName().getName();
        int inheritableDepth = fnAccessPermission.getInheritableDepth();
        for (FnAccessPermission accessPermission : getFnAccessPermissions()) {
            if (objectId.equals(accessPermission.getObjectId())) {
                if (fnAccessPermission.getFnObjectType().equals(accessPermission.getFnObjectType())){
                    if (principalName.equals(accessPermission.getGranteeName().getName())) {
                        if (inheritableDepth == accessPermission.getInheritableDepth()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
