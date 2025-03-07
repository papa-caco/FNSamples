package ar.com.lpa.ldapExchanger.repository;

import ar.com.lpa.ldapExchanger.model.FnBatch;
import ar.com.lpa.ldapExchanger.model.FnObjectType;
import ar.com.lpa.ldapExchanger.model.SecurableObject;
import org.apache.log4j.Logger;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import javax.persistence.PersistenceException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BatchRepo implements WithGlobalEntityManager {

    private static BatchRepo instance = null;

    private BatchRepo(){
    }

    public static BatchRepo getInstance(){
        if (instance == null){
            instance = new BatchRepo();
        }
        return instance;
    }

    private static final Logger logger = Logger.getLogger(BatchRepo.class);

    public List<FnBatch> getBatches(){
        return entityManager().createQuery("from FnBatch").getResultList();
    }

    public void createFnBatch(FnObjectType batchType, int batchNumber, int batchSize){
        if (!existsBatchTypeAndNumber(batchType, batchNumber)){
            FnBatch fnBatch = new FnBatch(batchType,batchNumber,batchSize, 0,'N');
            try {
                entityManager().getTransaction().begin();
                entityManager().persist(fnBatch);
                entityManager().getTransaction().commit();
            } catch (PersistenceException e) {
                //e.printStackTrace();
                entityManager().getTransaction().rollback();
                throw new RuntimeException("An error has occurred persisting FnBatch, the operation cannot be completed", e);
            } finally {
                entityManager().close();
            }
        }
    }

    public void updateFnBatch(FnBatch fnBatch){
        if (existsBatchTypeAndNumber(fnBatch.getBatchType(), fnBatch.getBatchNumber())) {
            try {
                entityManager().getTransaction().begin();
                int id = entityManager().merge(fnBatch).getIdFnBatch();
                fnBatch.setIdFnBatch(id);
                entityManager().getTransaction().commit();
            } catch (PersistenceException e) {
                //e.printStackTrace();
                entityManager().getTransaction().rollback();
                throw new RuntimeException("An error has occurred updating FnBatch, the operation cannot be completed", e);
            } finally {
                entityManager().close();
            }
        }
    }

    public void deleteFnBatch(FnBatch fnBatch){
        if (existsBatchTypeAndNumber(fnBatch.getBatchType(), fnBatch.getBatchNumber())) {
            try {
                entityManager().remove(fnBatch);
            } catch (PersistenceException e) {
                //e.printStackTrace();
                entityManager().getTransaction().rollback();
                throw new RuntimeException("An error has occurred removing FnBatch, the operation cannot be completed", e);
            } finally {
                entityManager().close();
            }
        }
    }

    public List<FnBatch> getFnBatchesByBatchStatus(char status){
        return this.getBatches().stream().filter(b -> b.getBatchStatus() == status).collect(Collectors.toList());
    }

    public FnBatch getFnBatchByNumberAndType(FnObjectType batchType, int batchNumber){
        FnBatch batch = null;
        for (FnBatch fnBatch : getBatches()){
            if (fnBatch.getBatchType() == batchType){
                if (fnBatch.getBatchNumber() == batchNumber){
                    batch = fnBatch;
                }
            }
        }
        return batch;
    }

    private boolean existsBatchTypeAndNumber(FnObjectType batchType, int batchNumber){
        for (FnBatch fnBatch : getBatches()){
            if (fnBatch.getBatchType() == batchType){
                if (fnBatch.getBatchNumber() == batchNumber){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isLastBatchByStatus(FnBatch fnBatch){
        List<FnBatch> batches = this.getBatches().stream().filter(b -> b.getBatchType() == fnBatch.getBatchType() && b.getBatchStatus() == fnBatch.getBatchStatus()).collect(Collectors.toList());
        return batches.size() == 1;
    }

    public int getTotalObjectsByBatchType(FnObjectType batchType){
        int totalObjects = 0;
        List <FnBatch> batches = this.getBatches().stream().filter(b -> b.getBatchType() == batchType).collect(Collectors.toList());
        for (FnBatch batch : batches){
            totalObjects += batch.getBatchSize();
        }
        return totalObjects;
    }

    public int getMaxBatchNumber(FnObjectType batchType){
        return this.getBatches().stream().filter(b -> b.getBatchType() == batchType)
                .map(FnBatch::getBatchNumber).max(Comparator.naturalOrder()).orElse(0);
    }

}
