package ar.com.lpa.ldapExchanger.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter(AccessLevel.PUBLIC)
@Entity(name = "FnBatch")
@Table(name = "FnBatch")
@RequiredArgsConstructor
@NoArgsConstructor
public class FnBatch {

    @Id
    @GeneratedValue
    @Column(name = "idFnBatch")
    private int idFnBatch;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "batchType")
    private FnObjectType batchType;

    @NonNull
    @Column(name = "batchNumber")
    private int batchNumber;

    @NonNull
    @Column(name = "batchSize")
    private int batchSize;

    @Column(name = "objectCount")
    private int objectCount;

    @Column(name = "batchStatus")
    private char batchStatus;

    public FnBatch(FnObjectType batchType, int batchNumber, int batchSize, int objectCount, char batchStatus){
        this.batchNumber = batchNumber;
        this.batchType = batchType;
        this.batchSize = batchSize;
        this.objectCount = objectCount;
        this.batchStatus = batchStatus;
    }

}
