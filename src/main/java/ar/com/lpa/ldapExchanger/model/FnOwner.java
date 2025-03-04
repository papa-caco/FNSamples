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

    @Column(name = "status")
    private char status;

    public FnOwner(FnObjectType type, String objectId, Principal owner) {
        this.fnObjectType = type;
        this.objectId = objectId;
        this.owner = owner;
        this.status = 'N';
    }
}
