package ar.com.lpa.samples.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ar.com.lpa.samples.model.Principal;
import ar.com.lpa.samples.model.fnObjects.P8Realm;
import ar.com.lpa.samples.util.Utilities;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.security.AccessPermission;
import lombok.Getter;

@Getter
public class PrincipalRepo {
	private final List<Principal> principals = new ArrayList<>();

	private void addNewPrincipalFromDn(String dN, String type, P8Realm p8realm)
    {
    	if (!existsPrincipalDn(dN)) {
    		Principal principal = new Principal();
    		principal.setDistinguishedName(dN);
    		principal.setPrincipalTypeFromString(type);
    		if (type.equals("USER")) {
    			principal.setSamAccountName(p8realm.getRealmUsers().getUserShortNameFromDn(dN));
    			principal.setSId(p8realm.getRealmUsers().getUserSidFromDn(dN));
    		} else {
    			principal.setSamAccountName(p8realm.getRealmGroups().getGroupShortNameFromDn(dN));
    			principal.setSId(p8realm.getRealmGroups().getGroupSidFromDn(dN));
    		}
    		principals.add(principal);
    	}
    }
    
    private void addNewPrincipalFromShortName(String shortName, String type, P8Realm p8realm)
    {
    	if (!existsPrincipalShortName(shortName)) {
    		Principal principal = new Principal();
    		principal.setSamAccountName(shortName);
    		principal.setPrincipalTypeFromString(type);
    		if (type.equals("USER")) {
    			principal.setDistinguishedName(p8realm.getRealmUsers().getUserDnFromShortName(shortName));
    			principal.setSId(p8realm.getRealmUsers().getUserSidFromShortName(shortName));
    		} else {
    			principal.setDistinguishedName(p8realm.getRealmGroups().getGroupDnFromShortName(shortName));
    			principal.setSId(p8realm.getRealmGroups().getGroupSidFromShortName(shortName));
    		}
    		principals.add(principal);
    	}
    }

	public void addPrincipalFromObjectOwner(String objectOwner, P8Realm p8realm){
		if (objectOwner != null && objectOwner.startsWith("CN=")) {
			this.addNewPrincipalFromDn(objectOwner, "USER", p8realm);
		} else if (objectOwner != null && objectOwner.contains("@")) {
			String ownerShortName = Utilities.extractShortName(objectOwner);
			this.addNewPrincipalFromShortName(ownerShortName, "USER", p8realm);
		}
	}

	public void addPrincipalsFromPermissions(AccessPermissionList permissions, P8Realm p8realm)
	{
		@SuppressWarnings("rawtypes")
		Iterator it1 = permissions.iterator();
		if (it1.hasNext()) {
			do {
				AccessPermission permission = (AccessPermission) it1.next();
				String granteeName = permission.get_GranteeName();
				String principalType = permission.get_GranteeType().toString();
				if (principalType.equals("USER") || principalType.equals("GROUP")) {
					if (granteeName.contains("@")) {
						String shortName = Utilities.extractShortName(granteeName);
						this.addNewPrincipalFromShortName(shortName, principalType, p8realm);

					} else {
						this.addNewPrincipalFromDn(granteeName, principalType, p8realm);
					}
				}
				//P8Logger.logPermisionValues(logger, permission);
			} while (it1.hasNext());
		}
	}
    
    private boolean existsPrincipalDn(String dN) 
    {
        for (Principal principal : principals) {
            if (dN.equals(principal.getDistinguishedName())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean existsPrincipalShortName(String shortName) 
    {
        for (Principal principal : principals) {
            if (shortName.equals(principal.getSamAccountName())) {
                return true;
            }
        }
        return false;
    }    
    

}
