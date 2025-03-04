package ar.com.lpa.ldapExchanger.model.fnObjects;


import com.filenet.api.core.Domain;
import com.filenet.api.core.EntireNetwork;
import com.filenet.api.core.Factory;

import lombok.Getter;


@Getter
public class P8Domain {
	private Domain domain = null;
	private EntireNetwork entireNetwork = null;
	private P8Connection p8connection = new P8Connection();
	
	public void setP8Connection() 
	{
		this.p8connection.setConnection();
	}
	
	public void setDomain() 
	{
		if (this.domain == null) {
			this.domain = Factory.Domain.fetchInstance(this.p8connection.getConnection(),null, null);
		}
	}
	
	public void setEntireNetwork() 
	{
		if (this.entireNetwork == null) {
			this.entireNetwork = Factory.EntireNetwork.fetchInstance(this.p8connection.getConnection(), null);
		}
	}
	

}
