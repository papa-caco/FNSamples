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
    private PermissionSource permissionSource;
    private int accessMask;
    private int accessType;
    private int inheritableDepth;
    private char status;

    public FnAccessPermission constructFromAccessPermission(String objectId, FnObjectType fnObjectType, AccessPermission accessPermission)
    {
        FnAccessPermission fnAccessPermission = new FnAccessPermission();
        fnAccessPermission.setObjectId(objectId);
        fnAccessPermission.setFnObjectType(fnObjectType);
        fnAccessPermission.setGranteeName(accessPermission.get_GranteeName());
        fnAccessPermission.setPrincipalType(PrincipalType.valueOf(accessPermission.get_GranteeType().toString()));
        fnAccessPermission.setPermissionSource(accessPermission.get_PermissionSource());
        fnAccessPermission.setAccessMask(accessPermission.get_AccessMask());
        fnAccessPermission.setAccessType(accessPermission.get_AccessType().getValue());
        fnAccessPermission.setInheritableDepth(accessPermission.get_InheritableDepth());
        fnAccessPermission.setStatus('N');
        return fnAccessPermission;
    }

}
