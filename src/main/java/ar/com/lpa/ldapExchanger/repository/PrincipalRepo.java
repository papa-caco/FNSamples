package ar.com.lpa.ldapExchanger.repository;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import ar.com.lpa.ldapExchanger.model.Principal;
import ar.com.lpa.ldapExchanger.model.fnObjects.P8Realm;
import ar.com.lpa.ldapExchanger.util.P8Logger;
import ar.com.lpa.ldapExchanger.util.Utilities;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.security.AccessPermission;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import javax.persistence.PersistenceException;

@Getter
public class PrincipalRepo implements WithGlobalEntityManager {

	private static PrincipalRepo instance = null;

	private PrincipalRepo(){
	}

	public static PrincipalRepo getInstance() {
		if (instance == null) {
			instance = new PrincipalRepo();
		}
		return instance;
	}

	private static final Logger logger = Logger.getLogger(PrincipalRepo.class);

	public List<Principal> getPrincipals(){
		return entityManager().createQuery("from Principal").getResultList();
	}
	public void createPrincipal(Principal principal){
		// Duplicated Principals are being checked in previous "create" methods
			try {
				entityManager().getTransaction().begin();
				entityManager().persist(principal);
				entityManager().getTransaction().commit();
			} catch (PersistenceException e) {
				//e.printStackTrace();
				entityManager().getTransaction().rollback();
				throw new RuntimeException("An error has occurred persisting a new Principal, the operation cannot be completed", e);
			} finally {
				entityManager().close();
			}
	}

	public void updatePrincipal(Principal principal){
		if (existsPrincipalShortName(principal.getSamAccountName())) {
			try {
				entityManager().getTransaction().begin();
				int id = entityManager().merge(principal).getIdPrincipal();
				principal.setIdPrincipal(id);
				entityManager().getTransaction().commit();
			} catch (PersistenceException e) {
				//e.printStackTrace();
				entityManager().getTransaction().rollback();
				throw new RuntimeException("An error has occurred updating Principal, the operation cannot be completed", e);
			} finally {
				entityManager().close();
			}
		}
	}

	public void deletePrincipal(Principal principal){
		if (existsPrincipalShortName(principal.getSamAccountName())) {
			try {
				entityManager().remove(principal);
			} catch (PersistenceException e) {
				//e.printStackTrace();
				entityManager().getTransaction().rollback();
				throw new RuntimeException("An error has occurred removing Principal, the operation cannot be completed", e);
			} finally {
				entityManager().close();
			}
		}
	}

	public void addPrincipalFromObjectOwner(String objectOwner, P8Realm p8realm){
		if (objectOwner != null && objectOwner.startsWith("CN=")) {
			this.addNewPrincipalFromDn(objectOwner, "USER", p8realm);
		} else if (objectOwner != null && objectOwner.contains("@")) {
			String ownerShortName = Utilities.extractShortName(objectOwner);
			this.addNewPrincipalFromShortName(objectOwner, ownerShortName, "USER", p8realm);
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
						this.addNewPrincipalFromShortName(granteeName, shortName, principalType, p8realm);
	
					} else {
						this.addNewPrincipalFromDn(granteeName, principalType, p8realm);
					}
				}
				P8Logger.logPermisionValues(logger, permission);
			} while (it1.hasNext());
		}
	}

	public void addPrincipalFromPermission(AccessPermission permission, P8Realm p8realm) {
		String principalType = permission.get_GranteeType().toString();
		if (principalType.equals("USER") || principalType.equals("GROUP")) {
			if (permission.get_GranteeName().contains("@")) {
				String shortName = Utilities.extractShortName(permission.get_GranteeName());
				this.addNewPrincipalFromShortName(permission.get_GranteeName(), shortName, principalType, p8realm);

			} else if (permission.get_GranteeName().startsWith("CN") || permission.get_GranteeName().startsWith("#")) {
				this.addNewPrincipalFromDn(permission.get_GranteeName(), principalType, p8realm);
			} else {
				logger.debug("Unrecognized user: " + permission.get_GranteeName() + " type: " + permission.get_GranteeType().toString());
			}
		}
		P8Logger.logPermisionValues(logger, permission);
	}

	public void addNewPrincipalFromDn(String dN, String type, P8Realm p8realm)
    {
    	if (!existsPrincipalDn(dN)) {
    		Principal principal = new Principal();
			principal.setName(dN);
    		principal.setDistinguishedName(dN);
    		principal.setPrincipalTypeFromString(type);
    		if (type.equals("USER")) {
    			principal.setSamAccountName(RealmUsersRepo.getInstance().getUserShortNameFromDn(dN));
    			principal.setSId(RealmUsersRepo.getInstance().getUserSidFromDn(dN));
    		} else {
    			principal.setSamAccountName(RealmGroupsRepo.getInstance().getGroupShortNameFromDn(dN));
    			principal.setSId(RealmGroupsRepo.getInstance().getGroupSidFromDn(dN));
    		}
    		this.createPrincipal(principal);
    	}
    }
    
    public void addNewPrincipalFromShortName(String name, String shortName, String type, P8Realm p8realm) {
    	if (!existsPrincipalShortName(shortName)) {
			Principal principal = new Principal();
			principal.setName(name);
    		principal.setSamAccountName(shortName);
    		principal.setPrincipalTypeFromString(type);
    		if (type.equals("USER")) {
    			principal.setDistinguishedName(RealmUsersRepo.getInstance().getUserDnFromShortName(shortName));
    			principal.setSId(RealmUsersRepo.getInstance().getUserSidFromShortName(shortName));
    		} else {
    			principal.setDistinguishedName(RealmGroupsRepo.getInstance().getGroupDnFromShortName(shortName));
    			principal.setSId(RealmGroupsRepo.getInstance().getGroupSidFromShortName(shortName));
    		}
    		this.createPrincipal(principal);
    	}
    }

	public Principal getPrincipalByName(String principalName){
		if (getPrincipals() == null || principalName == null) {
			return null; // null arguments
		}
		List<Principal> principals = getPrincipals().stream()
				.filter(principal -> principalName.equalsIgnoreCase(principal.getName())).collect(Collectors.toList());
		if (!(principals.isEmpty())){
			return principals.get(0);
		} else {
			return null;
		}
	}

	public Principal getPrincipalBySamAccountName(String samAccountName){
		if (getPrincipals() == null || samAccountName == null) {
			return null; // null arguments
		}
		List<Principal> principals = getPrincipals().stream()
				.filter(principal -> samAccountName.equalsIgnoreCase(principal.getSamAccountName())).collect(Collectors.toList());
		if (!(principals.isEmpty())){
			return principals.get(0);
		} else {
			return null;
		}
	}

	public Principal getPrincipalById(int idPrincipal){
		if (getPrincipals() == null) {
			return null;
		}
		return getPrincipals().stream()
				.filter(principal -> idPrincipal == principal.getIdPrincipal()).collect(Collectors.toList()).get(0);
	}


	private boolean existsPrincipalDn(String dN) 
    {
        for (Principal principal : getPrincipals()) {
            if (dN.equalsIgnoreCase(principal.getDistinguishedName())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean existsPrincipalShortName(String shortName) 
    {
        for (Principal principal : getPrincipals()) {
            if (shortName.equalsIgnoreCase(principal.getSamAccountName())) {
                return true;
            }
        }
        return false;
    }    
    

}
