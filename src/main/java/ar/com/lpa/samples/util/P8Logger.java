package ar.com.lpa.samples.util;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.core.CustomObject;
import org.apache.log4j.Logger;

import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.filenet.api.security.AccessPermission;

import lombok.Getter;

@Getter
public class P8Logger {
		
    public static void logFolderProperties(Logger logger, Folder folder, int count, int permissionsCount) {
        try {
            // Loggear los valores obtenidos
            logger.debug(" Folder#: " + count);
            logger.debug("        Id: " + folder.get_Id().toString());
            logger.debug("      Name: " + folder.get_Name());
            logger.debug("     Class: " + folder.getClassName());
            logger.debug("     Owner: " + folder.get_Owner());
            logger.debug("Permissions #: " + permissionsCount);

        } catch (Exception e) {
            logger.error("Error loggeando entidad: " + folder, e);
        }
    }
    
    public static void logDocumentProperties(Logger logger, Document document, int count, int permissionsCount) {
        try {
            // Loggear los valores obtenidos
            logger.debug("Document#: " + count);
            logger.debug("           Id: " + document.get_Id().toString());
            logger.debug("         Name: " + document.get_Name());
            logger.debug("        Class: " + document.getClassName());
            logger.debug("        Owner: " + document.get_Owner());
            logger.debug("Permissions #: " + permissionsCount);

        } catch (Exception e) {
            logger.error("Error loggeando entidad: " + document, e);
        }
    }

    public static void logCustomObjectProperties(Logger logger, CustomObject customObject, int count, int permissionsCount) {
        try {
            // Loggear los valores obtenidos
            logger.debug("Custom Object#: " + count);
            logger.debug("            Id: " + customObject.get_Id().toString());
            logger.debug("          Name: " + customObject.get_Name());
            logger.debug("         Class: " + customObject.getClassName());
            logger.debug("         Owner: " + customObject.get_Owner());
            logger.debug(" Permissions #: " + permissionsCount);

        } catch (Exception e) {
            logger.error("Error loggeando entidad: " + customObject, e);
        }
    }

    public static void logClassProperties(Logger logger, ClassDefinition classDefinition, int count, int permissionsCount) {
        try {
            // Loggear los valores obtenidos
            logger.debug(" Folder#: " + count);
            logger.debug("        Id: " + classDefinition.get_Id().toString());
            logger.debug("      Name: " + classDefinition.get_Name());
            logger.debug("     Class: " + classDefinition.getClassName());
            logger.debug("     Owner: " + classDefinition.get_Owner());
            logger.debug("Permissions #: " + permissionsCount);

        } catch (Exception e) {
            logger.error("Error loggeando entidad: " + classDefinition, e);
        }
    }
    
    public static void logPermisionValues(Logger logger, AccessPermission permission) 
    {
    	 try {
         		logger.debug("	     GranteeName : " + permission.get_GranteeName());
         		logger.debug("	     GranteeType : " + permission.get_GranteeType().toString());
         		logger.debug("	PermissionSource : " + permission.get_PermissionSource().toString());
         		logger.debug("	     Accesslevel : " + permission.get_AccessMask().toString());
         		logger.debug("	      Accesstype : " + permission.get_AccessType().toString());
         		logger.debug("	Inheritabledepth : " + permission.get_InheritableDepth());
         		logger.debug("        -----------------");
         	} 
    	 catch (Exception e) {
             logger.error("Error loggeando entidad: " + permission, e);
         }
    }

}
