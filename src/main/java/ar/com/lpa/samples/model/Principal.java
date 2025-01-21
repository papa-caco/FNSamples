package ar.com.lpa.samples.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter(AccessLevel.PUBLIC)
@Entity(name = "Principal")
@Table(name = "Principal")
@RequiredArgsConstructor
@NoArgsConstructor
public class Principal
{
	@Id
	@GeneratedValue
	@Column(name = "idPrincipal")
	private int idPrincipal;

	@Column(name = "name")
	@NonNull
	private String name;

	@Column(name = "principalType")
	@NonNull
	@Enumerated(EnumType.STRING)
	private PrincipalType principalType;

	@Column(name = "samAccountName")
	private String samAccountName;

	@Column(name = "distinguishedName")
	private String distinguishedName;

	@Column(name = "sId")
	private String sId;
	
	public void setPrincipalTypeFromString(String type) 
	{
	    if (type == null || type.isEmpty()) {
	        throw new IllegalArgumentException("'type' cannot be NULL or Empty");
	    }

	    try {
	        this.principalType = PrincipalType.valueOf(type.toUpperCase());
	    } catch (IllegalArgumentException e) {
	        throw new IllegalArgumentException("'type' must be 'USER' or 'GROUP'; input value: " + type, e);
	    }
	}
}
