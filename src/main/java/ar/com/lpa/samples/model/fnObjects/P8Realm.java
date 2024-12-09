package ar.com.lpa.samples.model.fnObjects;

import com.filenet.api.security.Realm;

import org.apache.log4j.Logger;

import ar.com.lpa.samples.repository.RealmGroupsRepo;
import ar.com.lpa.samples.repository.RealmUsersRepo;

import lombok.Getter;

@Getter
public class P8Realm {
	private final P8Domain p8domain = new P8Domain();
	private Realm realm = null;
    private final RealmUsersRepo realmUsers = new RealmUsersRepo();
    private final RealmGroupsRepo realmGroups = new RealmGroupsRepo();
	
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
						this.getRealmUsers().getRealmUsers().size(),
						this.getRealmGroups().getRealmGroups().size(), this.getRealm().get_Name()));
			}
		} catch(Exception e){
	   		 e.printStackTrace();
		}
	}
	
	private void setUserGroupRepos() 
	{
		this.realmUsers.setRealm(this.realm);
		this.realmGroups.setRealm(this.realm);
		this.realmUsers.setRealmUsers();
		this.realmGroups.setRealmGroups();
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
