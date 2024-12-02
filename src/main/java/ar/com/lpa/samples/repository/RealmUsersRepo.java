package ar.com.lpa.samples.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.filenet.api.collection.UserSet;
import com.filenet.api.constants.PrincipalSearchAttribute;
import com.filenet.api.constants.PrincipalSearchSortType;
import com.filenet.api.constants.PrincipalSearchType;
import com.filenet.api.security.Realm;
import com.filenet.api.security.User;

import ar.com.lpa.samples.util.Utilities;
import lombok.Getter;
import lombok.Setter;

public class RealmUsersRepo {
	
	@Getter
	@Setter
	private Realm realm;
	private Collection<User> realmUsers = new ArrayList<>();
	
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
    
    public Collection<User> getRealmUsers()
    {
    	return this.realmUsers;
    }
    
    private Collection<User> getUsersCollectionByPattern(char initial) 
    {
        Collection<User> usersCollection = new ArrayList<>();
        String pattern = String.valueOf(initial);
        UserSet userSet = this.realm.findUsers(pattern, PrincipalSearchType.PREFIX_MATCH,PrincipalSearchAttribute.SHORT_NAME,PrincipalSearchSortType.NONE,Integer.valueOf("50"), null);    
        // Iteramos sobre los elementos del UserSet y los agregamos
    	@SuppressWarnings("unchecked")
		Iterator<User> it = userSet.iterator();
    	while (it.hasNext()) {
    		User user = (User)it.next();
    		usersCollection.add(user);
    	}
    	//System.out.println(String.valueOf(usersCollection.size()) + " users with \"" + pattern + "\"");
        return usersCollection;
    }
    
    public String getUserSidFromDn(String dN)
    {
    	String userSiD = null;
    	for (User user : this.realmUsers)
    	{
    		if (user.get_DistinguishedName().equals(dN))
    		{
    			userSiD = user.get_Id();
    			break;
    		}
    	}
    	return userSiD;
    }
    
    public String getUserSidFromShortName(String shortName)
    {
    	String userSiD = null;
    	for (User user : this.realmUsers)
    	{
    		if (user.get_ShortName().equals(shortName))
    		{
    			userSiD = user.get_Id();
    			break;
    		}
    	}
    	return userSiD;
    }
    
    public String getUserDnFromShortName(String shortName)
    {
    	String dN = null;
    	for (User user : this.realmUsers)
    	{
    		if (user.get_ShortName().equals(shortName))
    		{
    			dN = user.get_DistinguishedName();
    			break;
    		}
    	}
    	return dN;
    }
    
    public String getUserShortNameFromUpn(String uPN)
    {
    	String userShortName = null;
    	String name = Utilities.extractShortName(uPN);
    	for (User user : this.realmUsers)
    	{
    		if (user.get_ShortName().equals(name))
    		{
    			userShortName = user.get_ShortName();
    			break;
    		}
    	}
    	return userShortName;
    }
    
    public String getUserShortNameFromDn(String dN)
    {
    	String userShortName = null;
    	for (User user : this.realmUsers)
    	{
    		if (user.get_DistinguishedName().equals(dN))
    		{
    			userShortName = user.get_ShortName();
    			break;
    		}
    	}
    	return userShortName;
    }
 }
