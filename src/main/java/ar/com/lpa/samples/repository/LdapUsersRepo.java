package ar.com.lpa.samples.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ar.com.lpa.samples.model.LdapUser;
import com.filenet.api.collection.UserSet;
import com.filenet.api.constants.PrincipalSearchAttribute;
import com.filenet.api.constants.PrincipalSearchSortType;
import com.filenet.api.constants.PrincipalSearchType;
import com.filenet.api.security.Realm;
import com.filenet.api.security.User;

import ar.com.lpa.samples.util.Utilities;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LdapUsersRepo {

	private final List<LdapUser> ldapUsers = new ArrayList<>();
	
    public void setLdapUsers(Realm realm)
    {
    	for (char c = 'A'; c <= 'Z'; c++) 
        {
    		ldapUsers.addAll(getUsersCollectionByPattern(c, realm));
        }    
        for (char c = '0'; c <= '9'; c++) 
        {
        	ldapUsers.addAll(getUsersCollectionByPattern(c, realm));
        } 	
    }

	private Collection<LdapUser> getUsersCollectionByPattern(char initial, Realm realm)
    {
        Collection<LdapUser> usersCollection = new ArrayList<>();
        String pattern = String.valueOf(initial);
        UserSet userSet = realm.findUsers(pattern, PrincipalSearchType.PREFIX_MATCH,PrincipalSearchAttribute.SHORT_NAME,PrincipalSearchSortType.NONE,Integer.valueOf("50"), null);
        // Iteramos sobre los elementos del UserSet y los agregamos
		int count = 0;
    	Iterator<User> it = userSet.iterator();
    	while (it.hasNext()) {
    		User user = (User)it.next();
			count++;
			System.out.println(count + " - " + user.get_Name());
			LdapUser ldapUser = LdapUser.instanceFromUser(user);
    		usersCollection.add(ldapUser);
    	}
    	//System.out.println(String.valueOf(usersCollection.size()) + " users with \"" + pattern + "\"");
        return usersCollection;
    }
    
    public String getUserSidFromDn(String dN)
    {
    	String userSiD = null;
    	for (LdapUser ldapUser : this.ldapUsers)
    	{
    		if (ldapUser.getDistinguishedName().equals(dN))
    		{
    			userSiD = ldapUser.getUserSID();
    			break;
    		}
    	}
    	return userSiD;
    }
    
    public String getUserSidFromShortName(String shortName)
    {
    	String userSiD = null;
    	for (LdapUser ldapUser : this.ldapUsers)
    	{
    		if (ldapUser.getShortName().equals(shortName))
    		{
    			userSiD = ldapUser.getUserSID();
    			break;
    		}
    	}
    	return userSiD;
    }
    
    public String getUserDnFromShortName(String shortName)
    {
    	String dN = null;
    	for (LdapUser ldapUser : this.ldapUsers)
    	{
    		if (ldapUser.getShortName().equals(shortName))
    		{
    			dN = ldapUser.getDistinguishedName();
    			break;
    		}
    	}
    	return dN;
    }
    
    public String getUserShortNameFromUpn(String uPN)
    {
    	String userShortName = null;
    	String name = Utilities.extractShortName(uPN);
    	for (LdapUser ldapUser : this.ldapUsers)
    	{
    		if (ldapUser.getShortName().equals(name))
    		{
    			userShortName = ldapUser.getShortName();
    			break;
    		}
    	}
    	return userShortName;
    }
    
    public String getUserShortNameFromDn(String dN)
    {
    	String userShortName = null;
    	for (LdapUser ldapUser : this.ldapUsers)
    	{
    		if (ldapUser.getDistinguishedName().equals(dN))
    		{
    			userShortName = ldapUser.getShortName();
    			break;
    		}
    	}
    	return userShortName;
    }
 }
