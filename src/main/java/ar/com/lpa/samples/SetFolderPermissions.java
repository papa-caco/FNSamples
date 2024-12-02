package ar.com.lpa.samples;

import java.io.IOException;
import java.util.Iterator;

import javax.security.auth.Subject;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.util.UserContext;

public class SetFolderPermissions {
	
    private static Connection conn = null;

    public static Connection getCEConn()
    {
        try {
            String ceURI =    "https://w2019p8.lab.grupolpa.com:9443/wsi/FNCEWS40MTOM/";
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
        System.out.println("CE Connection"+conn);
        return conn;
    }
    


	public static void getFolderPermissions(String osName, String folder) throws IOException
    {
        try {
			Connection conn = getCEConn();
			Domain domain = Factory.Domain.fetchInstance(conn, null, null);
			ObjectStore objStore = Factory.ObjectStore.fetchInstance(domain, osName, null);
			com.filenet.api.core.Folder folderOj = Factory.Folder.fetchInstance(objStore, folder, null);

			System.out.println("   P8 Domain :: " + domain.get_Name());
			System.out.println("Object Store :: " + objStore.get_Name());
			System.out.println(" Folder name :: " + folderOj.get_FolderName());
			System.out.println("   Folder Id :: " + folderOj.get_Id().toString());
			System.out.println("       Owner :: " + folderOj.get_Owner());
			AccessPermissionList permissions= folderOj.get_Permissions();
			@SuppressWarnings("unchecked")
			Iterator<AccessPermission> it1 = permissions.iterator();
			            
              while (it1.hasNext())
              {
              	AccessPermission permission = (AccessPermission)it1.next();
              	System.out.println("\n	GranteeName = "+ permission.get_GranteeName());
              	System.out.println("	GranteeType = " + permission.get_GranteeType().toString());
              	System.out.println("	PermissionSource = " +permission.get_PermissionSource().toString());
              	System.out.println("	Accesslevel = " + permission.get_AccessMask().toString());
              	System.out.println("	Accesstype = " +permission.get_AccessType().toString());
              	System.out.println("	Inheritabledepth = " +permission.get_InheritableDepth());
              }
			System.out.println("Done");
        	}
            catch(Exception e){
                e.printStackTrace();
            }
    }
	
	public static void setFolderPermission(String osName, String folder, String user) throws IOException
    {
        try {
			Connection conn = getCEConn();
			Domain domain = Factory.Domain.fetchInstance(conn, null, null);
			ObjectStore objStore = Factory.ObjectStore.fetchInstance(domain, osName, null);

			com.filenet.api.core.Folder folderOj = Factory.Folder.fetchInstance(objStore, folder, null);
			System.out.println("   P8 Domain :: " + domain.get_Name());
			System.out.println("Object Store :: " + objStore.get_Name());
			System.out.println("      Folder :: " + folderOj.get_Name());
			
			folderOj.set_Owner(user);

			AccessPermission permission = Factory.AccessPermission.createInstance();
			permission.set_GranteeName(user);
			permission.set_AccessType(AccessType.ALLOW);
			permission.set_InheritableDepth(0);
			//.set_InheritableDepth(new Integer(0));
			permission.set_AccessMask(999415);
			//permission.set_AccessMask(new Integer(AccessLevel.READ_AS_INT));//131073
			//permission.set_AccessMask(new Integer(AccessLevel.VIEW_AS_INT));//131201
			//permission.set_AccessMask(new Integer(AccessLevel.LINK_FOLDER_AS_INT));//131121
			//permission.set_AccessMask(new Integer(AccessLevel.WRITE_FOLDER_AS_INT));//135155
			//permission.set_AccessMask(new Integer(AccessLevel.FULL_CONTROL_FOLDER_AS_INT));//999415

			AccessPermissionList permissions = folderOj.get_Permissions();
			permissions.clear();
			permissions.add(permission);
			folderOj.set_Permissions(permissions);
			folderOj.save(RefreshMode.REFRESH);
			System.out.println("Done");
        	}
            catch(Exception e){
                e.printStackTrace();
            }
    }

	public static void main(String[] args) throws IOException 
	{
		getFolderPermissions("OS1_FVG", "/sarasa");
		setFolderPermission("OS1_FVG", "/sarasa", "administrator");

	}
}
