package ar.com.lpa.samples.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.filenet.api.collection.GroupSet;
import com.filenet.api.constants.PrincipalSearchAttribute;
import com.filenet.api.constants.PrincipalSearchSortType;
import com.filenet.api.constants.PrincipalSearchType;
import com.filenet.api.security.Group;
import com.filenet.api.security.Realm;

import ar.com.lpa.samples.util.Utilities;
import lombok.Getter;
import lombok.Setter;

public class RealmGroupsRepo {
	
	@Getter
	@Setter
	private Realm realm;
	private Collection<Group> realmGroups = new ArrayList<>();
	
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
    
    public Collection<Group> getRealmGroups()
    {
    	return this.realmGroups;
    }
    
    private Collection<Group> getGroupsCollectionByPattern(char initial) 
    {
        Collection<Group> GroupsCollection = new ArrayList<>();
        String pattern = String.valueOf(initial);
        GroupSet GroupSet = this.realm.findGroups(pattern, PrincipalSearchType.PREFIX_MATCH,PrincipalSearchAttribute.SHORT_NAME,PrincipalSearchSortType.NONE,Integer.valueOf("50"), null);    
        // Iteramos sobre los elementos del GroupSet y los agregamos
    	@SuppressWarnings("unchecked")
		Iterator<Group> it = GroupSet.iterator();
    	while (it.hasNext()) {
    		Group Group = (Group)it.next();
    		GroupsCollection.add(Group);
    	}
    	//System.out.println(String.valueOf(GroupsCollection.size()) + " Groups with \"" + pattern + "\"");
        return GroupsCollection;
    }
    
    public String getGroupSidFromDn(String dN)
    {
    	String GroupSiD = null;
    	for (Group Group : this.realmGroups)
    	{
    		if (Group.get_DistinguishedName().equals(dN))
    		{
    			GroupSiD = Group.get_Id();
    			break;
    		}
    	}
    	return GroupSiD;
    }
    
    public String getGroupSidFromShortName(String shortName)
    {
    	String GroupSiD = null;
    	for (Group Group : this.realmGroups)
    	{
    		if (Group.get_ShortName().equals(shortName))
    		{
    			GroupSiD = Group.get_Id();
    			break;
    		}
    	}
    	return GroupSiD;
    }
    
    public String getGroupDnFromShortName(String shortName)
    {
    	String dN = null;
    	for (Group Group : this.realmGroups)
    	{
    		if (Group.get_ShortName().equals(shortName))
    		{
    			dN = Group.get_DistinguishedName();
    			break;
    		}
    	}
    	return dN;
    }
    
    public String getGroupShortNameFromUpn(String uPN)
    {
    	String GroupShortName = null;
    	String name = Utilities.extractShortName(uPN);
    	for (Group Group : this.realmGroups)
    	{
    		if (Group.get_ShortName().equals(name))
    		{
    			GroupShortName = Group.get_ShortName();
    			break;
    		}
    	}
    	return GroupShortName;
    }
    
    public String getGroupShortNameFromDn(String dN)
    {
    	String GroupShortName = null;
    	for (Group Group : this.realmGroups)
    	{
    		if (Group.get_DistinguishedName().equals(dN))
    		{
    			GroupShortName = Group.get_ShortName();
    			break;
    		}
    	}
    	return GroupShortName;
    }
 }
