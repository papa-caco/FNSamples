package ar.com.lpa.samples.model.fnObjects;

import com.filenet.api.security.Realm;

import org.apache.log4j.Logger;

import ar.com.lpa.samples.repository.LdapGroupsRepo;
import ar.com.lpa.samples.repository.LdapUsersRepo;

import lombok.Getter;

@Getter
public class P8Realm {
	private final P8Domain p8domain = new P8Domain();
	private Realm realm = null;
    private final LdapUsersRepo realmUsers = new LdapUsersRepo();
    private final LdapGroupsRepo realmGroups = new LdapGroupsRepo();
	
	public void setP8Domain() 
	{
		this.p8domain.setP8Connection();
		this.p8domain.setEntireNetwork();
		this.p8domain.setDomain();
	}
	
	public void setRealm(Logger logger) 
	{
		try {
			this.setP8Domain();
			if (realm == null) {
				this.realm = this.p8domain.getEntireNetwork().get_MyRealm();
				logger.info(String.format("P8 Domain: %s", p8domain.getDomain().get_Name()));
				this.setUserGroupRepos();
				logger.info(String.format("Found %s Users & %s Groups at domain: %s",
						this.getRealmUsers().getLdapUsers().size(),
						this.getRealmGroups().getLdapGroups().size(), this.getRealm().get_Name()));
			}
		} catch(Exception e){
	   		 e.printStackTrace();
		}
	}
	
	private void setUserGroupRepos() 
	{
		this.realmUsers.setLdapUsers(this.realm);
		this.realmGroups.setLdapGroups(this.realm);
	}
	
	public void setConnectionUser(String user) {
		this.getP8domain().getP8connection().setUserName(user);
	}
	
	public void setConnectionPswd(String pswd) {
		this.getP8domain().getP8connection().setPassword(pswd);
	}
	
	public void setConnectionCeUri(String ceUri) {
		this.getP8domain().getP8connection().setCeURI(ceUri);
	}
}
