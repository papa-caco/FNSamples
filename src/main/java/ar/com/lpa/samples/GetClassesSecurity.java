package ar.com.lpa.samples;

import java.io.IOException;
import java.util.Iterator;
import javax.security.auth.Subject;
import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.ClassDefinitionSet;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.util.UserContext;

public class GetClassesSecurity {
    private static Connection conn = null;
    static String className=null;
   
    static String disGranteeName =null;
    static String disGranteeType =null;
    static String disPermissionSource =null;
    static String disaccessType =null;
    static String disInheritableDepth =null;
    static String disAccessMask=null;
   
    static String GranteeName =null;
    static String GranteeType =null;
    static String PermissionSource =null;
    static String accessType =null;
    static String InheritableDepth =null;
    static String AccessMask=null;
    public static Connection getCEConn()
    {
        try {
            String ceURI =    "https://w2019p8.lab.grupolpa.com:9443/wsi/FNCEWS40MTOM/";
            String userName ="p85ceadmin";
            String password ="Filenet01";
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

   
    public static void getClassPermissions(String osName) throws IOException{
       
        try{
           
           
            Connection conn = getCEConn();
            Domain domain = Factory.Domain.fetchInstance(conn,null, null);
            ObjectStore objStore = Factory.ObjectStore.fetchInstance(domain, osName,null);
           
            System.out.println("ObjectStoreDisplayName :: "+objStore.get_DisplayName());
            System.out.println("Class Name");
            System.out.println("Grantee Name");
            System.out.println("Grantee Type");
            System.out.println("Permission Source");
            System.out.println("Access Type");
            System.out.println("Inheritabled Depth");
            System.out.println("Access Mask(Object Access Rights)");
          
           
            ClassDefinitionSet cds= objStore.get_RootClassDefinitions();
            ClassDefinition cd;
            Iterator it = cds.iterator();
            while (it.hasNext())
            {
                      System.out.println("-----------------------------");
                cd = (ClassDefinition)it.next();
                className=cd.get_DisplayName();
                System.out.println("Class name = " + className);
                System.out.println("ClassDisplayName :: "+cd.get_DisplayName());
                System.out.println("ClassName :: "+cd.get_Name());
                System.out.println("ClassSymbolicName :: "+cd.get_SymbolicName());
                System.out.println("ClassCreator :: "+cd.get_Creator());
                System.out.println("ClassDefaultInstanceOwner :: "+cd.get_DefaultInstanceOwner());
                System.out.println("ClassLastModifier :: "+cd.get_LastModifier());
                System.out.println("ClassOwner :: "+cd.get_Owner());
                System.out.println("ClassDateCreated :: "+cd.get_DateCreated());
                System.out.println("ClassID :: "+cd.get_Id());
                System.out.println("ClassDateLastModified :: "+cd.get_DateLastModified());
                System.out.println("ClassIsHidden :: "+cd.get_IsHidden());
                System.out.println("ClassIsCurrent :: "+cd.isCurrent());
                System.out.println("ClassIsPersistent :: "+cd.get_IsPersistent());
                System.out.println("ClassIsSystemOwned :: "+cd.get_IsSystemOwned());
                System.out.println("ClassIsCBREnabled :: "+cd.get_IsCBREnabled());

               
               
                System.out.println("------Default Instance Security------");

                AccessPermissionList dis = cd.get_DefaultInstancePermissions();
           
                Iterator itdis = dis.iterator();
                while (itdis.hasNext())
                {
                AccessPermission permission = (AccessPermission)itdis.next();
               
                disGranteeName=permission.get_GranteeName();
                disGranteeType=permission.get_GranteeType().toString();
                disPermissionSource=permission.get_PermissionSource().toString();
                disaccessType=permission.get_AccessType().toString();
                disInheritableDepth=permission.get_InheritableDepth().toString();       
                disAccessMask=permission.get_AccessMask().toString();
               
                System.out.println("GranteeName="+ disGranteeName);
                System.out.println("GranteeType=" + disGranteeType);
                System.out.println("PermissionSource=" +disPermissionSource);
               
                System.out.println("Accesstype =" +disaccessType);
                System.out.println("Inheritabledepth=" +disInheritableDepth);
                System.out.println(className);
                 System.out.println(disGranteeName);
                 System.out.println(disGranteeType);
                 System.out.println(disPermissionSource);
                 System.out.println(disaccessType);
                 System.out.println(disInheritableDepth);
                 System.out.println(disAccessMask);
               
               
                }
               
               
                System.out.println("------Direct Security------");
               
                 AccessPermissionList s = cd.get_Permissions();
               
                Iterator itdis1 = s.iterator();
                while (itdis1.hasNext())
                {
                AccessPermission permission1 = (AccessPermission)itdis1.next();
               
                GranteeName=permission1.get_GranteeName();
                GranteeType=permission1.get_GranteeType().toString();
                PermissionSource=permission1.get_PermissionSource().toString();
                accessType=permission1.get_AccessType().toString();
                InheritableDepth=permission1.get_InheritableDepth().toString();       
                AccessMask=permission1.get_AccessMask().toString();
                System.out.println("GranteeName="+ GranteeName);
                System.out.println("GranteeType=" + GranteeType);
                System.out.println("PermissionSource=" +PermissionSource);
               
                System.out.println("Accesstype =" +accessType);
                System.out.println("Inheritabledepth=" +InheritableDepth);
                System.out.println(className);
                 System.out.println(GranteeName);
                 System.out.println(GranteeType);
                 System.out.println(PermissionSource);
                 System.out.println(accessType);
                 System.out.println(InheritableDepth);
                 System.out.println(AccessMask);
               
               
                }
               
                ClassDefinitionSet cds1= cd.get_ImmediateSubclassDefinitions();
                  if(!cds1.isEmpty())
                  {
                      System.out.println("--------Children Class of "+className +"----------");
                       
                      childClass(cds1);
                      System.out.println("Not Empty");
                  }
            }
           
            System.out.println("-------------End Object Store--------------------------");
           
            System.out.println("Done");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private static void childClass(ClassDefinitionSet cds1) throws IOException {
        // TODO Auto-generated method stub
        ClassDefinition cd;
        Iterator it = cds1.iterator();
        while (it.hasNext())
        {
                  System.out.println("-----------------------------");
               
               
            cd = (ClassDefinition)it.next();
           
            System.out.println("Class name = " + cd.get_DisplayName());
            System.out.println("ClassDisplayName :: "+cd.get_DisplayName());       
            System.out.println("ClassName :: "+cd.get_Name());
            System.out.println("ClassSymbolicName :: "+cd.get_SymbolicName());
            System.out.println("ClassCreator :: "+cd.get_Creator());
            System.out.println("ClassDefaultInstanceOwner :: "+cd.get_DefaultInstanceOwner());
            System.out.println("ClassLastModifier :: "+cd.get_LastModifier());
            System.out.println("ClassOwner :: "+cd.get_Owner());
            System.out.println("ClassDateCreated :: "+cd.get_DateCreated());
            System.out.println("ClassID :: "+cd.get_Id());
            System.out.println("ClassDateLastModified :: "+cd.get_DateLastModified());
            System.out.println("ClassIsHidden :: "+cd.get_IsHidden());
            System.out.println("ClassIsCurrent :: "+cd.isCurrent());
            System.out.println("ClassIsPersistent :: "+cd.get_IsPersistent());
            System.out.println("ClassIsSystemOwned :: "+cd.get_IsSystemOwned());
            System.out.println("ClassIsCBREnabled :: "+cd.get_IsCBREnabled());
           
            System.out.println("------Default Instance Security------");
           
            AccessPermissionList dis = cd.get_DefaultInstancePermissions();
            Iterator itdis = dis.iterator();
            while (itdis.hasNext())
            {
            AccessPermission permission = (AccessPermission)itdis.next();
           
            disGranteeName=permission.get_GranteeName();
            disGranteeType=permission.get_GranteeType().toString();
            disPermissionSource=permission.get_PermissionSource().toString();
            disaccessType=permission.get_AccessType().toString();
            disInheritableDepth=permission.get_InheritableDepth().toString();       
            disAccessMask=permission.get_AccessMask().toString();
            System.out.println("GranteeName="+ disGranteeName);
            System.out.println("GranteeType=" + disGranteeType);
            System.out.println("PermissionSource=" +disPermissionSource);

            System.out.println("Accesstype =" +disaccessType);
            System.out.println("Inheritabledepth=" +disInheritableDepth);
           
           
           
            }
           
           
            System.out.println("------Direct Security------");
           
             AccessPermissionList s = cd.get_Permissions();
            System.out.println("------------");
             System.out.println("------------");
             System.out.println("------------");
             System.out.println("------------");
             System.out.println("------------");
             System.out.println("------------");
           
            Iterator itdis1 = s.iterator();
            while (itdis1.hasNext())
            {
            AccessPermission permission1 = (AccessPermission)itdis1.next();
           
            GranteeName=permission1.get_GranteeName();
            GranteeType=permission1.get_GranteeType().toString();
            PermissionSource=permission1.get_PermissionSource().toString();
            accessType=permission1.get_AccessType().toString();
            InheritableDepth=permission1.get_InheritableDepth().toString();       
            AccessMask=permission1.get_AccessMask().toString();
           
            System.out.println(className);
             System.out.println(GranteeName);
             System.out.println(GranteeType);
             System.out.println(PermissionSource);
             System.out.println(accessType);
             System.out.println(InheritableDepth);
             System.out.println(AccessMask);
           
           
            }
           
            ClassDefinitionSet cds2= cd.get_ImmediateSubclassDefinitions();
              if(!cds2.isEmpty())
              {
                  System.out.println("--------Children Class of "+className +"----------");
                 
                  childClass(cds2);
                  System.out.println("Not Empty");
              }
        }

    }
    public static void main(String[] args) throws IOException {
   
            getClassPermissions("objst1");
        }
       
       
        //    FetchOSList();
/*
         InheritableDepth
         0 - No inheritance (this object only).
        1 - This object and immediate children only.
        -1 - This object and all children (infinite levels deep).
        -2 - All children (infinite levels deep) but not this object.
        -3 - Immediate children only; not this object.
 
 */
   
}
