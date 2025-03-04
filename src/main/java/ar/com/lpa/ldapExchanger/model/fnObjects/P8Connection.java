package ar.com.lpa.ldapExchanger.model.fnObjects;

import javax.security.auth.Subject;

import org.apache.log4j.Logger;

import com.filenet.api.core.Factory;
import com.filenet.api.core.Connection;
import com.filenet.api.util.UserContext;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P8Connection 
{
	private Connection connection = null;
    private String ceURI = null;
    private String userName = null;
    private String password = null;
    private static final Logger logger = Logger.getLogger(P8Connection.class);
	
    public void setConnection()
    {
        try {
            if(this.connection == null){
            	this.connection = Factory.Connection.getConnection(ceURI);
            	Subject subject = UserContext.createSubject(this.connection, userName, password, null);
            	UserContext uc = UserContext.get();
            	uc.pushSubject(subject);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        logger.info("Establishing connection to: " + this.connection.getURI());  

    }
}
