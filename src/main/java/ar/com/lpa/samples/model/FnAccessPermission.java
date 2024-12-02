package ar.com.lpa.samples.model;


import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.AccessLevel;
import com.filenet.api.constants.PermissionSource;


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
    private AccessLevel accessLevel;
    private AccessType accessType;
    private Integer inheritableDepth;




}
