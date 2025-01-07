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
public class RealmUsersRepo {

	private Realm realm;
	private final List<LdapUser> realmUsers = new ArrayList<>();
	
    public void setRealmUsers()
    {
    	for (char c = 'A'; c <= 'Z'; c++) 
        {
    		realmUsers.addAll(getUsersCollectionByPattern(c));
        }    
        for (char c = '0'; c <= '9'; c++) 
        {
        	realmUsers.addAll(getUsersCollectionByPattern(c));
        } 	
    }

	private Collection<LdapUser> getUsersCollectionByPattern(char initial)
    {
        Collection<LdapUser> usersCollection = new ArrayList<>();
        String pattern = String.valueOf(initial);
        UserSet userSet = this.realm.findUsers(pattern, PrincipalSearchType.PREFIX_MATCH,PrincipalSearchAttribute.SHORT_NAME,PrincipalSearchSortType.NONE,Integer.valueOf("50"), null);    
        // Iteramos sobre los elementos del UserSet y los agregamos
    	Iterator<User> it = userSet.iterator();
    	while (it.hasNext()) {
    		User user = (User)it.next();
			LdapUser ldapUser = LdapUser.instanceFromUser(user);
    		usersCollection.add(ldapUser);
    	}
    	//System.out.println(String.valueOf(usersCollection.size()) + " users with \"" + pattern + "\"");
        return usersCollection;
    }
    
    public String getUserSidFromDn(String dN)
    {
    	String userSiD = null;
    	for (LdapUser ldapUser : this.realmUsers)
    	{
    		if (ldapUser.getDistinguishedName().equals(dN))
    		{
    			userSiD = ldapUser.getUserId();
    			break;
    		}
    	}
    	return userSiD;
    }
    
    public String getUserSidFromShortName(String shortName)
    {
    	String userSiD = null;
    	for (LdapUser ldapUser : this.realmUsers)
    	{
    		if (ldapUser.getShortName().equals(shortName))
    		{
    			userSiD = ldapUser.getUserId();
    			break;
    		}
    	}
    	return userSiD;
    }
    
    public String getUserDnFromShortName(String shortName)
    {
    	String dN = null;
    	for (LdapUser ldapUser : this.realmUsers)
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
    	for (LdapUser ldapUser : this.realmUsers)
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
    	for (LdapUser ldapUser : this.realmUsers)
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
