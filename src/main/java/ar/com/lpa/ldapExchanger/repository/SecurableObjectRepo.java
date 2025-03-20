package ar.com.lpa.ldapExchanger.repository;

import ar.com.lpa.ldapExchanger.model.FnObjectType;
import ar.com.lpa.ldapExchanger.model.SecurableObject;
import lombok.Getter;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

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

    public SecurableObject getSecurableObjectByTableName(String tableName){
        SecurableObject securableObject = null;
        if (existsSecurableObject(tableName)){
            securableObject = this.getSecurableObjects().stream().filter(s -> s.getTableName().equals(tableName)).collect(Collectors.toList()).get(0);
        }
        return securableObject;
    }

    public boolean securableObjectHasDifferences(String tableName, int lineCount){
        boolean result = false;
        if (existsSecurableObject(tableName)){
            int actualCount = this.getSecurableObjects().stream().filter(s -> s.getTableName().equalsIgnoreCase(tableName)).collect(Collectors.toList()).get(0).getLineCount();
            if (actualCount != lineCount) result = true;
        }
        return result;
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

    public List<SecurableObject> getSecurableObjectsByProcessStatus(char status){
        return this.getSecurableObjects().stream().filter(s -> s.getProcessStatus() == status).collect(Collectors.toList());
    }

    public SecurableObject getSecurableObjectByFnObjectType(FnObjectType fnObjectType){
        SecurableObject result = null;
        String tableName = this.getTableNameByFnObjectType(fnObjectType);
        for (SecurableObject securableObject: this.getSecurableObjects()){
            if (securableObject.getTableName().equalsIgnoreCase(tableName)) {
                if (securableObject.getFnObjectType() == null) {
                    securableObject.setFnObjectType(fnObjectType);
                    updateSecurableObject(securableObject);
                    result = securableObject;
                } else if (securableObject.getFnObjectType() == fnObjectType) {
                    result = securableObject;
                } else {
                    SecurableObject newObject = this.duplicateSecurableObject(securableObject, fnObjectType);
                    createSecurableObject(newObject);
                    result = newObject;
                }
            }
        }
        return result;
    }

    public boolean existsSecurableObject(String tableName){
        for (SecurableObject securableObject : getSecurableObjects()){
            if (tableName.equalsIgnoreCase(securableObject.getTableName())){
                return true;
            }
        }
        return false;
    }

    private SecurableObject duplicateSecurableObject(SecurableObject securableObject, FnObjectType fnObjectType){
        SecurableObject newObject = new SecurableObject();
        newObject.setTableName(securableObject.getTableName());
        newObject.setFnObjectType(fnObjectType);
        newObject.setObjectCount(0);
        newObject.setLineCount(securableObject.getLineCount());
        newObject.setSecurityIdCount(securableObject.getSecurityIdCount());
        newObject.setProcessStatus('N');
        return newObject;
    }

    private String getTableNameByFnObjectType(FnObjectType fnObjectType){
        String tableName = null;
        switch (fnObjectType){
            case ANNOTATION:
                tableName = "annotation";
                break;
            case DOCUMENT:
                tableName = "docversion";
                break;
            case CLASS_DEFINITION:
            case CLASS_DEFINITION_DIP:
                tableName = "classdefinition";
                break;
            case PROPERTY_TEMPLATE:
                tableName = "globalpropertydef";
                break;
            case FOLDER:
                tableName = "container";
                break;
            case CHOICE_LIST:
                tableName = "cvl";
                break;
            case EVENT:
                tableName = "event";
                break;
            case CUSTOM_OBJECT:
                tableName = "generic";
                break;
            case STORAGE_AREA:
            case STORAGE_POLICY:
                tableName = "storageclass";
                break;
            case SECURITY_POLICY:
                tableName = "securitypolicy";
                break;
            case SECURITY_TEMPLATE:
                tableName = "securitytemplate";
                break;
            case SWEEP:
                tableName = "sweep";
                break;
            case SWEEP_POLICY:
                tableName = "sweeppolicy";
                break;
            case TABLE_DEFINITION:
                tableName = "tabledefinition";
                break;
            case DOWNLOAD_RECORD:
                tableName = "ut_clbdownloadrecord";
                break;
            case SUMMARY_DATA:
                tableName = "ut_clbsummarydata";
                break;
            case CUSTOM_ROLE_BASE:
                tableName = "ut_cmcustomrolebase";
                break;
            case ROLE:
                tableName = "roleObject";
                break;
            case CLASS_SUBSCRIPTION:
            case EVENT_ACTION:
            case CHANGE_PREPROCESSOR_ACTION:
            case CONTENT_CONVERSION_ACTION:
            case DOCUMENT_CLASSIFICATION_ACTION:
            case DOCUMENT_LIFECYCLE_ACTION:
            case DOCUMENT_LIFECYCLE_POLICY:
            case INSTANCE_SUBSCRIPTION:
            case ROLE_MEMBERSHIP_ACTION:
            case SEARCH_FUNCTION_DEFINITION:
            case SWEEP_ACTION:
            case TEXT_INDEXING_PREPROCESSOR_ACTION:
                tableName = "Subscription";
                break;
            default:
                break;
        }
        return tableName;
    }
}
