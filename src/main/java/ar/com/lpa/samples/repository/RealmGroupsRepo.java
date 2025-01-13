package ar.com.lpa.samples.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

	private final List<Group> realmGroups = new ArrayList<>();
	
    public void setRealmGroups(Realm realm)
    {
    	for (char c = 'A'; c <= 'Z'; c++) 
        {
    		realmGroups.addAll(getGroupsCollectionByPattern(c, realm));
        }    
        for (char c = '0'; c <= '9'; c++) 
        {
        	realmGroups.addAll(getGroupsCollectionByPattern(c, realm));
        } 	
    }

	private List<Group> getGroupsCollectionByPattern(char initial, Realm realm)
    {
        List<Group> GroupsCollection = new ArrayList<>();
        String pattern = String.valueOf(initial);
        GroupSet GroupSet = realm.findGroups(pattern, PrincipalSearchType.PREFIX_MATCH,PrincipalSearchAttribute.SHORT_NAME,PrincipalSearchSortType.NONE,Integer.valueOf("50"), null);
        // Iteramos sobre los elementos del GroupSet y los agregamos
		Iterator<Group> it = GroupSet.iterator();
    	while (it.hasNext()) {
    		Group group = (Group)it.next();
    		GroupsCollection.add(group);
    	}
    	//System.out.println(String.valueOf(GroupsCollection.size()) + " Groups with \"" + pattern + "\"");
        return GroupsCollection;
    }
    
    public String getGroupSidFromDn(String dN)
    {
    	String groupSiD = null;
    	for (Group group : this.realmGroups)
    	{
    		if (group.get_DistinguishedName().equals(dN))
    		{
    			groupSiD = group.get_Id();
    			break;
    		}
    	}
    	return groupSiD;
    }
    
    public String getGroupSidFromShortName(String shortName)
    {
    	String groupSid = null;
    	for (Group group : this.realmGroups)
    	{
    		if (group.get_ShortName().equals(shortName))
    		{
    			groupSid = group.get_Id();
    			break;
    		}
    	}
    	return groupSid;
    }
    
    public String getGroupDnFromShortName(String shortName)
    {
    	String dN = null;
    	for (Group group : this.realmGroups)
    	{
    		if (group.get_ShortName().equals(shortName))
    		{
    			dN = group.get_DistinguishedName();
    			break;
    		}
    	}
    	return dN;
    }
    
    public String getGroupShortNameFromUpn(String uPN)
    {
    	String groupShortName = null;
    	String name = Utilities.extractShortName(uPN);
    	for (Group group : this.realmGroups)
    	{
    		if (group.get_ShortName().equals(name))
    		{
    			groupShortName = group.get_ShortName();
    			break;
    		}
    	}
    	return groupShortName;
    }
    
    public String getGroupShortNameFromDn(String dN)
    {
    	String groupShortName = null;
    	for (Group group : this.realmGroups)
    	{
    		if (group.get_DistinguishedName().equals(dN))
    		{
    			groupShortName = group.get_ShortName();
    			break;
    		}
    	}
    	return groupShortName;
    }
 }
