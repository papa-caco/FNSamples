package ar.com.lpa.samples.repository;

import ar.com.lpa.samples.model.FnAccessPermission;
import com.filenet.api.security.AccessPermission;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
@Getter
public class PermissionRepo {

    private final List<FnAccessPermission> FnAccessPermissions = new ArrayList<>();

}
