package ar.com.lpa.samples.model;


import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.AccessLevel;
import com.filenet.api.constants.PermissionSource;


import com.filenet.api.security.AccessPermission;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FnAccessPermission {
    private String objectId;
    private FnObjectType fnObjectType;
    private String granteeName;
    private PrincipalType principalType;
    private int permissionSource;
    private int accessMask;
    private int accessType;
    private int inheritableDepth;
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
