package ar.com.lpa.samples.testing;
import java.io.IOException;
import java.util.Iterator;
import javax.security.auth.Subject;
import com.filenet.api.collection.GroupSet;
import com.filenet.api.collection.UserSet;
import com.filenet.api.constants.PrincipalSearchAttribute;
import com.filenet.api.constants.PrincipalSearchSortType;
import com.filenet.api.constants.PrincipalSearchType;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Factory;
import com.filenet.api.util.UserContext;

public class GetRealmUsrGrp {
	private static Connection conn = null;
	
	public static Connection getCEConn()
	    {
	        try {
	            String ceURI = "https://w2019p8.lab.grupolpa.com:9443/wsi/FNCEWS40MTOM/"; 
	            //"http://p8sql2012.lab.grupolpa.com:9080/wsi/FNCEWS40MTOM/";  
	            //"https://w2019p8.lab.grupolpa.com:9443/wsi/FNCEWS40MTOM/";
	            String userName = "p85ceadmin";//"sarasa";
	            String password = "Filenet01";//"Lpa23291$";
	            if(conn==null){
	            	conn = Factory.Connection.getConnection(ceURI);
	            	Subject subject = UserContext.createSubject(conn, userName, password, null);
	            	UserContext uc = UserContext.get();
	            	uc.pushSubject(subject);
	            }

	        } catch (Exception e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	        }
	        System.out.println("CE Connection "+conn+"\n");
	        return conn;		
	}
	
    public static void getRealmUsrGrp(String osName) throws IOException{
        
        try{
            Connection conn = getCEConn();
            com.filenet.api.core.EntireNetwork entireNetwork= Factory.EntireNetwork.fetchInstance(conn, null);
            com.filenet.api.security.Realm realm= entireNetwork.get_MyRealm();
            String realmName = realm.get_Name();
            System.out.println("Retrieved Realm: "+realmName);
            
            getRealmUsersByPattern("pere", realm);
           
            for (char c = 'A'; c <= 'Z'; c++) 
            {
            	getRealmUsersByPattern(c, realm);
            }
            
            for (char c = '0'; c <= '9'; c++) 
            {
            	getRealmUsersByPattern(c, realm);
            }
            for (char c = 'A'; c <= 'Z'; c++) 
            {
            	getRealmGroupsByPattern(c, realm);
            }
            for (char c = '0'; c <= '9'; c++) 
            {
            	getRealmGroupsByPattern(c, realm);
            }
           
            System.out.println("Done");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //retrieve user names by pattern
    private static void getRealmUsersByPattern(char initial, com.filenet.api.security.Realm realm) {
    	String pattern = String.valueOf(initial);
    	UserSet users = realm.findUsers(pattern, PrincipalSearchType.PREFIX_MATCH,PrincipalSearchAttribute.SHORT_NAME,PrincipalSearchSortType.NONE,Integer.valueOf("50"), null);
    	com.filenet.api.security.User user;
    	@SuppressWarnings("unchecked")
    	Iterator<com.filenet.api.security.User> it = users.iterator();
    	while (it.hasNext())
    	{
    		user = (com.filenet.api.security.User)it.next();
    		System.out.println("\nDistinguished Name = " + user.get_DistinguishedName());
    		System.out.println("        Short Name = " + user.get_ShortName());
    		System.out.println("                CN = " + extractCN(user.get_DistinguishedName()));
    		// System.out.println("             name = " + user.get_Name());  -->> igual a user.get_DistinguishedName()
    		System.out.println("               Sid = " + user.get_Id());
    		// System.out.println("      displayname = " + user.get_DisplayName()); -->> igual a user.get_ShortName()
    	}
    }
    
    private static void getRealmUsersByPattern(String pattern, com.filenet.api.security.Realm realm) 
    {
    	UserSet users = realm.findUsers(pattern, PrincipalSearchType.PREFIX_MATCH,PrincipalSearchAttribute.SHORT_NAME,PrincipalSearchSortType.NONE,Integer.valueOf("50"), null);
    	com.filenet.api.security.User user;
    	@SuppressWarnings("unchecked")
    	Iterator<com.filenet.api.security.User> it = users.iterator();
    	while (it.hasNext())
    	{
    		user = (com.filenet.api.security.User)it.next();
    		System.out.println("\nDistinguished Name = " + user.get_DistinguishedName());
    		System.out.println("        Short Name = " + user.get_ShortName());
    		System.out.println("                CN = " + extractCN(user.get_DistinguishedName()));
    		// System.out.println("             name = " + user.get_Name());  -->> igual a user.get_DistinguishedName()
    		System.out.println("               Sid = " + user.get_Id());
    		// System.out.println("      displayname = " + user.get_DisplayName()); -->> igual a user.get_ShortName()
    	}
    }

    //retrieve group names by pattern
    private static void getRealmGroupsByPattern(char initial, com.filenet.api.security.Realm realm) 
    {
    	String pattern = String.valueOf(initial);
    	GroupSet groups = realm.findGroups(pattern,PrincipalSearchType.PREFIX_MATCH,PrincipalSearchAttribute.SHORT_NAME,PrincipalSearchSortType.NONE,Integer.valueOf("50"), null);
    	@SuppressWarnings("unchecked")
    	Iterator<com.filenet.api.security.Group> groupIt= groups.iterator();
    	while (groupIt.hasNext())
    	{
    		com.filenet.api.security.Group group = groupIt.next();
    		System.out.println("\nDistinguished Name = " + group.get_DistinguishedName());
    		System.out.println("        Short Name = " + group.get_ShortName());
    		System.out.println("                CN = " + extractCN(group.get_DistinguishedName()));
    		System.out.println("               Sid = " + group.get_Id());
    		//System.out.println("Users =" + group.get_Users());
    	}
    }
    
    private static String extractCN(String dN)
    {
        String[] parts = dN.split(",");
        for (String part : parts) {
            if (part.startsWith("CN=")) {
                return part.split("=")[1];
            }
        }
        return null; 
    }

	public static void main(String[] args) throws IOException {
		getRealmUsrGrp("OS1_FVG");

	}

}
