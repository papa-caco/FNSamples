package ar.com.lpa.samples.testing;

import java.io.IOException;
import java.util.Iterator;

import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import org.apache.log4j.Logger;



import ar.com.lpa.samples.model.fnObjects.P8Realm;
import ar.com.lpa.samples.util.P8ObjectSearch;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.events.Event;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.RefreshMode;

import com.filenet.api.security.AccessPermission;


public class SetFolderPermissions {
	
    private static final P8Realm realm = new P8Realm();
	private static final Logger logger = Logger.getLogger(SetFolderPermissions.class);


    


	public static void getObjectDetailsAndPermissions(String osName, String sqlSearch) throws IOException
    {
        try {
			IndependentObjectSet result = P8ObjectSearch.getFnObjectsFromSearch(realm,logger,osName,sqlSearch);
			if (!(result.isEmpty())){
				Iterator<Event> it= result.iterator();
				Event object = (Event) it.next();
				System.out.println(" Object name :: " + object.get_Name());
				System.out.println("   Object Id :: " + object.get_Id().toString());
				System.out.println("Object Class :: " + object.getClassName());
				System.out.println("       Owner :: " + object.get_Owner());
				AccessPermissionList permissions= object.get_Permissions();
				Iterator<AccessPermission> it1 = permissions.iterator();

                if (it1.hasNext()) {
                    do {
                        AccessPermission permission = (AccessPermission) it1.next();
                        System.out.println("\n	GranteeName = " + permission.get_GranteeName());
                        System.out.println("	GranteeType = " + permission.get_GranteeType().toString());
                        System.out.println("	PermissionSource = " + permission.get_PermissionSource().toString());
                        System.out.println("	Accesslevel = " + permission.get_AccessMask().toString());
                        System.out.println("	Accesstype = " + permission.get_AccessType().toString());
                        System.out.println("	Inheritabledepth = " + permission.get_InheritableDepth());
                    } while (it1.hasNext());
                }

			} else System.out.println("No objects found...");

			System.out.println("Done");
        	}
            catch(Exception e){
                e.printStackTrace();
            }
    }
	
	public static void setFolderPermission(String osName, String folderPath, String user) throws IOException
    {
        try {

			Folder folder = Factory.Folder.fetchInstance(Factory.ObjectStore.fetchInstance(realm.getP8domain().getDomain(), osName, null),
					folderPath, null);
			System.out.println("      Folder :: " + folder.get_Name());
			
			folder.set_Owner(user);

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

			AccessPermissionList permissions = folder.get_Permissions();
			permissions.clear();
			permissions.add(permission);
			folder.set_Permissions(permissions);
			folder.save(RefreshMode.REFRESH);
			System.out.println("Done");
        	}
            catch(Exception e){
                e.printStackTrace();
            }
    }

	public static void main(String[] args) throws IOException 
	{
		realm.setConnectionCeUri("https://w2019p8.lab.grupolpa.com:9443/wsi/FNCEWS40MTOM/");
		realm.setConnectionUser("p85ceadmin");
		realm.setConnectionPswd("Filenet01");
		realm.setRealm(logger);
		getObjectDetailsAndPermissions("objst1","SELECT * FROM Event");
		//setFolderPermission("OS1_FVG", "/sarasa", "administrator");

	}
}
