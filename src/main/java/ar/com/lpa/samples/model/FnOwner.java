package ar.com.lpa.samples.model;

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

    @Column(name = "sourceOwner")
    private String sourceOwner;

    @Column(name = "destOwner")
    private String destinationOwner;

    @Column(name = "status")
    private char status;

    public FnOwner(FnObjectType type, String objectId, String owner) {
        this.fnObjectType = type;
        this.objectId = objectId;
        this.sourceOwner = owner;
        this.status = 'N';
    }
}
