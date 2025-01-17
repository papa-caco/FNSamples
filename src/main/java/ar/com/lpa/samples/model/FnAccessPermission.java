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

    @Column(name = "granteeName")
    @NonNull
    private String granteeName;

    @Column(name = "principalType")
    @NonNull
    @Enumerated(EnumType.STRING)
    private PrincipalType principalType;

    @Column(name = "permissionSource")
    private int permissionSource;

    @Column(name = "accessMask")
    private int accessMask;

    @Column(name = "accessType")
    private int accessType;

    @Column(name = "inheritableDepth")
    private int inheritableDepth;

    @Column(name = "status")
    private char status;

    public FnAccessPermission(String objectId, FnObjectType fnObjectType, AccessPermission accessPermission)
    {
        this.setObjectId(objectId);
        this.setFnObjectType(fnObjectType);
        this.setGranteeName(accessPermission.get_GranteeName());
        this.setPrincipalType(PrincipalType.valueOf(accessPermission.get_GranteeType().toString()));
        this.setPermissionSource(accessPermission.get_PermissionSource().getValue());
        this.setAccessMask(accessPermission.get_AccessMask());
        this.setAccessType(accessPermission.get_AccessType().getValue());
        this.setInheritableDepth(accessPermission.get_InheritableDepth());
        this.setStatus('N');
    }

}
