package ar.com.lpa.samples.repository;

import ar.com.lpa.samples.model.SecurableObject;
import lombok.Getter;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import javax.persistence.PersistenceException;
import java.util.List;

@Getter
public class SecurableObjectRepo implements WithGlobalEntityManager {

    private static SecurableObjectRepo instance = null;

    private SecurableObjectRepo(){
    }

    public static SecurableObjectRepo getInstance(){
        if (instance == null) {
            instance = new SecurableObjectRepo();
        }
        return instance;
    }

    public List<SecurableObject> getSecurableObjects() {
        return entityManager().createQuery("from SecurableObject").getResultList();
    }

    public void createSecurableObject(SecurableObject securableObject){
        if (!existsSecurableObject(securableObject.getTableName())) {
            try {
                entityManager().getTransaction().begin();
                entityManager().persist(securableObject);
                entityManager().getTransaction().commit();
            } catch (PersistenceException e) {
                //e.printStackTrace();
                entityManager().getTransaction().rollback();
                throw new RuntimeException("An error has occurred persisting a new SecurableObject, the operation cannot be completed", e);
            } finally {
                entityManager().close();
            }
        }
    }

    public void updateSecurableObject(SecurableObject securableObject){
        if (existsSecurableObject(securableObject.getTableName())) {
            try {
                entityManager().getTransaction().begin();
                int id = entityManager().merge(securableObject).getIdSecurableObject();
                securableObject.setIdSecurableObject(id);
                entityManager().getTransaction().commit();
            } catch (PersistenceException e) {
                //e.printStackTrace();
                entityManager().getTransaction().rollback();
                throw new RuntimeException("An error has occurred updating Securable, the operation cannot be completed", e);
            } finally {
                entityManager().close();
            }
        }
    }

    private void deleteSecurableObject(SecurableObject securableObject){
        if (existsSecurableObject(securableObject.getTableName())) {
            try {
                entityManager().remove(securableObject);
            } catch (PersistenceException e) {
                //e.printStackTrace();
                entityManager().getTransaction().rollback();
                throw new RuntimeException("An error has occurred removing SecurableObject, the operation cannot be completed", e);
            } finally {
                entityManager().close();
            }
        }
    }

    private boolean existsSecurableObject(String tableName){
        for (SecurableObject securableObject : getSecurableObjects()){
            if (tableName.equalsIgnoreCase(securableObject.getTableName())){
                return true;
            }
        }
        return false;
    }
}
