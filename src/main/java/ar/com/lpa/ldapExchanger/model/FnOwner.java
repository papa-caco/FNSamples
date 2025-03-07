package ar.com.lpa.ldapExchanger.model;

import lombok.*;

import javax.persistence.*;


@Getter
@Setter(AccessLevel.PUBLIC)
@Entity (name = "FnOwner")
@Table(name = "FnOwner")
@RequiredArgsConstructor
@NoArgsConstructor
public class FnOwner {
    @Id
    @GeneratedValue
    @Column(name = "idFnOwner")
    private int idFnOwner;

    @Column(name = "objectId")
    @NonNull
    private String objectId;

    @Column(name = "fnObjectType")
    @NonNull
    @Enumerated(EnumType.STRING)
    private FnObjectType fnObjectType;

    @JoinColumn(name ="idPrincipal")
    @ManyToOne(cascade = CascadeType.MERGE)
    private Principal owner;

    @JoinColumn(name ="idFnBatch")
    @ManyToOne(cascade = CascadeType.MERGE)
    private FnBatch fnBatch;

    @Column(name = "status")
    private char status;

    public FnOwner(FnObjectType type, String objectId, FnBatch fnBatch, Principal owner) {
        this.fnObjectType = type;
        this.objectId = objectId;
        this.fnBatch = fnBatch;
        this.owner = owner;
        this.status = 'N';
    }
}
