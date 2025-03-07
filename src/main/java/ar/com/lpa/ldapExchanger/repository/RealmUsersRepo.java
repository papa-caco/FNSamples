package ar.com.lpa.ldapExchanger.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.filenet.api.collection.UserSet;
import com.filenet.api.constants.PrincipalSearchAttribute;
import com.filenet.api.constants.PrincipalSearchSortType;
import com.filenet.api.constants.PrincipalSearchType;
import com.filenet.api.security.Realm;
import com.filenet.api.security.User;

import ar.com.lpa.ldapExchanger.util.Utilities;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealmUsersRepo {

	private static RealmUsersRepo instance = null;

	private RealmUsersRepo(){
	}

	public static RealmUsersRepo getInstance(){
		if(instance == null){
			instance = new RealmUsersRepo();
		}
		return instance;
	}

	private final List<User> realmUsers = new ArrayList<>();
	
    public void setRealmUsers(Realm realm)
    {
    	for (char c = 'A'; c <= 'Z'; c++) 
        {
    		realmUsers.addAll(getUsersCollectionByPattern(c, realm));
        }    
        for (char c = '0'; c <= '9'; c++) 
        {
        	realmUsers.addAll(getUsersCollectionByPattern(c, realm));
        } 	
    }

	private List<User> getUsersCollectionByPattern(char initial, Realm realm)
    {
        List<User> usersCollection = new ArrayList<>();
        String pattern = String.valueOf(initial);
        UserSet userSet = realm.findUsers(pattern, PrincipalSearchType.PREFIX_MATCH,PrincipalSearchAttribute.SHORT_NAME,PrincipalSearchSortType.NONE,Integer.valueOf("50"), null);
        // Iteramos sobre los elementos del UserSet y los agregamos
		//int count = 0;
    	Iterator<User> it = userSet.iterator();
    	while (it.hasNext()) {
    		User user = (User)it.next();
			//count++;
			//System.out.println(count + " - " + user.get_Name());
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
    		if (user.get_DistinguishedName().equalsIgnoreCase(dN))
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
    		if (user.get_ShortName().equalsIgnoreCase(shortName))
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
    	for (User ldapUser : this.realmUsers)
    	{
    		if (ldapUser.get_ShortName().equalsIgnoreCase(shortName))
    		{
    			dN = ldapUser.get_DistinguishedName();
    			break;
    		}
    	}
    	return dN;
    }
    
    public String getUserShortNameFromUpn(String uPN)
    {
    	String userShortName = null;
    	String name = Utilities.extractShortName(uPN);
    	for (User ldapUser : this.realmUsers)
    	{
    		if (ldapUser.get_ShortName().equalsIgnoreCase(name))
    		{
    			userShortName = ldapUser.get_ShortName();
    			break;
    		}
    	}
    	return userShortName;
    }
    
    public String getUserShortNameFromDn(String dN)
    {
    	String userShortName = null;
    	for (User ldapUser : this.realmUsers)
    	{
    		if (ldapUser.get_DistinguishedName().equalsIgnoreCase(dN))
    		{
    			userShortName = ldapUser.get_ShortName();
    			break;
    		}
    	}
    	return userShortName;
    }

	public String getUserNameFromShortName(String ShortName)
	{
		String name = null;
		for (User ldapUser : this.realmUsers)
		{
			if (ldapUser.get_ShortName().equalsIgnoreCase(ShortName))
			{
				name = ldapUser.get_Name();
				break;
			}
		}
		return name;
	}

	public boolean existsRealmUserByName(String name){
		return this.realmUsers.stream().filter(u -> u.get_Name().equalsIgnoreCase(name)).count() == 1;
	}
 }


