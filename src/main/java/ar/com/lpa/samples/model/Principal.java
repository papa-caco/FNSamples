package ar.com.lpa.samples.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Principal
{
	private PrincipalType principalType;
	private String samAccountName;
	private String distinguishedName;
	private String sId;
	
	public void setPrincipalTypeFromString(String type) 
	{
	    if (type == null || type.isEmpty()) {
	        throw new IllegalArgumentException("'type' cannot be NULL or Empty");
	    }

	    try {
	        this.principalType = PrincipalType.valueOf(type.toUpperCase());
	    } catch (IllegalArgumentException e) {
	        throw new IllegalArgumentException("'type' must be 'USER' or 'GROUP'; input vaue: " + type, e);
	    }
	}
}
