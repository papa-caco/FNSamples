package ar.com.lpa.samples.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.filenet.api.collection.GroupSet;
import com.filenet.api.security.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LdapUser {
    private String userId;
    private String name;
    private String distinguishedName;
    private String shortName;
    private String displayName;
    private String eMail;
    @JsonIgnore
    private GroupSet memberOfGroups;

    public static LdapUser instanceFromUser(User user) {
        LdapUser ldapUser = new LdapUser();
        ldapUser.setUserId(user.get_Id());
        ldapUser.setName(user.get_Name());
        ldapUser.setDistinguishedName(user.get_DistinguishedName());
        ldapUser.setShortName(user.get_ShortName());
        ldapUser.setDisplayName(user.get_DisplayName());
        ldapUser.setEMail(user.get_Email());
        ldapUser.setMemberOfGroups(user.get_MemberOfGroups());
        return  ldapUser;
    }

}
