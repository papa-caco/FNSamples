package ar.com.lpa.samples.model;


import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.PermissionSource;


import com.filenet.api.security.AccessPermission;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter(AccessLevel.PUBLIC)
@Entity(name = "FnAccessPermission")
@Table(name = "FnAccessPermission")
@RequiredArgsConstructor
@NoArgsConstructor
public class FnAccessPermission {
    @Id
    @GeneratedValue
    @Column(name = "idFnAccessPermission")
    private int idFnAccessPermission;

    @Column(name = "objectId")
    @NonNull
    private String objectId;

    @Column(name = "fnObjectType")
    @NonNull
    @Enumerated(EnumType.STRING)
    private FnObjectType fnObjectType;

    @JoinColumn(name ="idPrincipal")
    @ManyToOne(cascade = CascadeType.MERGE)
    private Principal granteeName;

    @Column(name = "principalType")
    @NonNull
    @Enumerated(EnumType.STRING)
    private PrincipalType principalType;

    @Column(name = "permissionSource")
    private String permissionSource;

    @Column(name = "accessMask")
    private int accessMask;

    @Column(name = "accessType")
    private String accessType;

    @Column(name = "inheritableDepth")
    private int inheritableDepth;

    @Column(name = "status")
    private char status;

    public FnAccessPermission(String objectId, FnObjectType fnObjectType, Principal granteeName, AccessPermission accessPermission)
    {
        this.objectId = objectId;
        this.fnObjectType = fnObjectType;
        this.granteeName = granteeName;
        this.principalType = PrincipalType.valueOf(accessPermission.get_GranteeType().toString());
        this.permissionSource = accessPermission.get_PermissionSource().toString();
        this.accessMask = accessPermission.get_AccessMask();
        this.accessType = accessPermission.get_AccessType().toString();
        this.inheritableDepth = accessPermission.get_InheritableDepth();
        this.status = 'N';
    }

}
