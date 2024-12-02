package ar.com.lpa.samples.repository;

import java.util.ArrayList;
import java.util.List;

import ar.com.lpa.samples.model.Principal;
import ar.com.lpa.samples.model.fnObjects.P8Realm;
import lombok.Getter;

@Getter
public class PrincipalRepo {
	private final List<Principal> principals = new ArrayList<>();

	public void addNewPrincipalFromDn(String dN, String type, P8Realm p8realm)
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
    
    public void addNewPrincipalFromShortName(String shortName, String type, P8Realm p8realm) 
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
