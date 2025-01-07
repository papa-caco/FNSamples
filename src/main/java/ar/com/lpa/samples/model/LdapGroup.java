package ar.com.lpa.samples.model;

import com.filenet.api.collection.GroupSet;
import com.filenet.api.collection.UserSet;
import com.filenet.api.security.Group;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
public class LdapGroup {
    private String groupId;
    private String name;
    private String distinguishedName;
    private String shortName;
    private String displayName;
    @JsonIgnore
    private UserSet users;
    @JsonIgnore
    private GroupSet groups;
    @JsonIgnore
    private GroupSet memberOfGroups;

    public static LdapGroup instanceFromGroup(Group group){
        LdapGroup ldapGroup = new LdapGroup();
        ldapGroup.setGroupId(group.get_Id());
        ldapGroup.setName(group.get_Name());
        ldapGroup.setDistinguishedName(group.get_DistinguishedName());
        ldapGroup.setShortName(group.get_ShortName());
        ldapGroup.setDisplayName(group.get_DisplayName());
        ldapGroup.setUsers(group.get_Users());
        ldapGroup.setGroups(group.get_Groups());
        ldapGroup.setMemberOfGroups(group.get_MemberOfGroups());
        return ldapGroup;
    }
}
