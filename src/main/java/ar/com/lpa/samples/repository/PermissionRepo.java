package ar.com.lpa.samples.repository;

import ar.com.lpa.samples.model.FnAccessPermission;
import ar.com.lpa.samples.model.FnObjectType;
import com.filenet.api.security.AccessPermission;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import org.apache.log4j.Logger;

@Getter
public class PermissionRepo {

    private final List<FnAccessPermission> fnAccessPermissions = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(PrincipalRepo.class);

    public void addPermissionsFromFnObject(String objectId, FnObjectType fnObjectType,AccessPermission permission){
        fnAccessPermissions.add(new FnAccessPermission(objectId, fnObjectType, permission));
    }

}
