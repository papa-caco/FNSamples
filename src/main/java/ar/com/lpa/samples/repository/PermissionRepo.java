package ar.com.lpa.samples.repository;

import ar.com.lpa.samples.model.FnAccessPermission;
import ar.com.lpa.samples.model.FnObjectType;
import com.filenet.api.security.AccessPermission;

import java.util.ArrayList;
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

    public void addPermissionsFromFnObject(String objectId, FnObjectType fnObjectType,AccessPermission permission){
        this.createFnAccessPermission(new FnAccessPermission(objectId, fnObjectType, permission));
    }

    private void createFnAccessPermission(FnAccessPermission fnAccessPermission){
        try {
            entityManager().getTransaction().begin();
            entityManager().persist(fnAccessPermission);
            entityManager().getTransaction().commit();
        } catch (PersistenceException e) {
            //e.printStackTrace();
            entityManager().getTransaction().rollback();
            throw new RuntimeException("An error has occurred persisting a new FnAccessPermission, the operation cannot be completed", e);
        } finally {
            entityManager().close();
        }
    }

    private void updateFnAccessPermission(FnAccessPermission fnAccessPermission){
        try {
            entityManager().getTransaction().begin();
            int id = entityManager().merge(fnAccessPermission).getIdFnAccessPermission();
            fnAccessPermission.setIdFnAccessPermission(id);
            entityManager().getTransaction().commit();
        }
        catch (PersistenceException e) {
            //e.printStackTrace();
            entityManager().getTransaction().rollback();
            throw new RuntimeException("An error has occurred updating FnAccessPermission, the operation cannot be completed", e);
        }
        finally {
            entityManager().close();
        }
    }

    private void deleteFnAccessPermission(FnAccessPermission fnAccessPermission){
        try {
            entityManager().remove(fnAccessPermission);
        } catch (PersistenceException e) {
            //e.printStackTrace();
            entityManager().getTransaction().rollback();
            throw new RuntimeException("An error has occurred removing FnAccessPermission, the operation cannot be completed", e);
        }
        finally {
            entityManager().close();
        }
    }

}
