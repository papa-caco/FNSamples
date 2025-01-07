package ar.com.lpa.samples.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ar.com.lpa.samples.model.LdapGroup;
import com.filenet.api.collection.GroupSet;
import com.filenet.api.constants.PrincipalSearchAttribute;
import com.filenet.api.constants.PrincipalSearchSortType;
import com.filenet.api.constants.PrincipalSearchType;
import com.filenet.api.security.Group;
import com.filenet.api.security.Realm;

import ar.com.lpa.samples.util.Utilities;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RealmGroupsRepo {

	private Realm realm;
	private final Collection<LdapGroup> realmGroups = new ArrayList<>();
	
    public void setRealmGroups()
    {
    	for (char c = 'A'; c <= 'Z'; c++) 
        {
    		realmGroups.addAll(getGroupsCollectionByPattern(c));
        }    
        for (char c = '0'; c <= '9'; c++) 
        {
        	realmGroups.addAll(getGroupsCollectionByPattern(c));
        } 	
    }

	private Collection<LdapGroup> getGroupsCollectionByPattern(char initial)
    {
        Collection<LdapGroup> GroupsCollection = new ArrayList<>();
        String pattern = String.valueOf(initial);
        GroupSet GroupSet = this.realm.findGroups(pattern, PrincipalSearchType.PREFIX_MATCH,PrincipalSearchAttribute.SHORT_NAME,PrincipalSearchSortType.NONE,Integer.valueOf("50"), null);    
        // Iteramos sobre los elementos del GroupSet y los agregamos
		Iterator<Group> it = GroupSet.iterator();
    	while (it.hasNext()) {
    		Group group = (Group)it.next();
			LdapGroup ldapGroup = LdapGroup.instanceFromGroup(group);
    		GroupsCollection.add(ldapGroup);
    	}
    	//System.out.println(String.valueOf(GroupsCollection.size()) + " Groups with \"" + pattern + "\"");
        return GroupsCollection;
    }
    
    public String getGroupSidFromDn(String dN)
    {
    	String groupSiD = null;
    	for (LdapGroup ldapGroup : this.realmGroups)
    	{
    		if (ldapGroup.getDistinguishedName().equals(dN))
    		{
    			groupSiD = ldapGroup.getGroupId();
    			break;
    		}
    	}
    	return groupSiD;
    }
    
    public String getGroupSidFromShortName(String shortName)
    {
    	String groupSid = null;
    	for (LdapGroup ldapGroup : this.realmGroups)
    	{
    		if (ldapGroup.getShortName().equals(shortName))
    		{
    			groupSid = ldapGroup.getGroupId();
    			break;
    		}
    	}
    	return groupSid;
    }
    
    public String getGroupDnFromShortName(String shortName)
    {
    	String dN = null;
    	for (LdapGroup ldapGroup : this.realmGroups)
    	{
    		if (ldapGroup.getShortName().equals(shortName))
    		{
    			dN = ldapGroup.getDistinguishedName();
    			break;
    		}
    	}
    	return dN;
    }
    
    public String getGroupShortNameFromUpn(String uPN)
    {
    	String groupShortName = null;
    	String name = Utilities.extractShortName(uPN);
    	for (LdapGroup ldapGroup : this.realmGroups)
    	{
    		if (ldapGroup.getShortName().equals(name))
    		{
    			groupShortName = ldapGroup.getShortName();
    			break;
    		}
    	}
    	return groupShortName;
    }
    
    public String getGroupShortNameFromDn(String dN)
    {
    	String groupShortName = null;
    	for (LdapGroup ldapGroup : this.realmGroups)
    	{
    		if (ldapGroup.getDistinguishedName().equals(dN))
    		{
    			groupShortName = ldapGroup.getShortName();
    			break;
    		}
    	}
    	return groupShortName;
    }
 }
