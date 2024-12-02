package ar.com.lpa.samples.model.fnObjects;

import com.filenet.api.security.Realm;

import org.apache.log4j.Logger;

import ar.com.lpa.samples.repository.RealmGroupsRepo;
import ar.com.lpa.samples.repository.RealmUsersRepo;

import lombok.Getter;

@Getter
public class P8Realm {
	private P8Domain p8domain = new P8Domain();
	private Realm realm = null;
    private RealmUsersRepo realmUsers = new RealmUsersRepo();
    private RealmGroupsRepo realmGroups = new RealmGroupsRepo();
	
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
				this.setUserGroupRepos();
				logger.info("Found " +String.valueOf(this.getRealmUsers().getRealmUsers().size()) 
				+ " Users & " + String.valueOf(this.getRealmGroups().getRealmGroups().size()) 
				+ " Groups at domain: " + this.getRealm().get_Name());
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
