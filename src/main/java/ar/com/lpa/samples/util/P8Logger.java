package ar.com.lpa.samples.util;

import ar.com.lpa.samples.model.FnObjectType;
import com.filenet.api.core.EngineObject;
import com.filenet.api.security.AccessPermission;
import org.apache.log4j.Logger;

import lombok.Getter;

@Getter
public class P8Logger {

    public static void logRepositoryObjectProperties(Logger logger, EngineObject repositoryObject, FnObjectType fnObjectType, int count) {
        String nameProperty = "Name";
        switch (fnObjectType) {
            case STORAGE_AREA:
            case SECURITY_POLICY:
            case SWEEP_POLICY:
            case INSTANCE_SUBSCRIPTION:
            case DOCUMENT_LIFECYCLE_POLICY:
            case SECURITY_TEMPLATE:
                nameProperty = "DisplayName";
                break;
            default:
                break;
        }
        try {
            // Loggear los valores obtenidos
            logger.debug(String.format("%s#: %d", fnObjectType.toString(), count));
            logger.debug("        Id: " + repositoryObject.getProperties().getIdValue("Id").toString());
            if (fnObjectType != FnObjectType.SWEEP) {
                logger.debug("      Name: " + repositoryObject.getProperties().getStringValue(nameProperty));
            }
            logger.debug("     Class: " + repositoryObject.getClassName());
            if (fnObjectType != FnObjectType.SECURITY_TEMPLATE){
                logger.debug("     Owner: " + repositoryObject.getProperties().getStringValue("Owner"));
                logger.debug("Permissions #: " + repositoryObject.getProperties().getDependentObjectListValue("Permissions").size());
            } else {
                logger.debug("Template Permissions #: " + repositoryObject.getProperties().getDependentObjectListValue("TemplatePermissions").size());
            }
            if (fnObjectType.equals(FnObjectType.CLASS_DEFINITION)){
                logger.debug("D.I. Permissions #: " + repositoryObject.getProperties().getDependentObjectListValue("DefaultInstancePermissions").size());
            }
        } catch (Exception e) {
            logger.error("Error loggeando entidad: " + repositoryObject, e);
        }
    }

    public static void logPermisionValues(Logger logger, AccessPermission permission)
    {
    	 try {
         		logger.trace("	     GranteeName : " + permission.get_GranteeName());
         		logger.trace("	     GranteeType : " + permission.get_GranteeType().toString());
         		logger.trace("	PermissionSource : " + permission.get_PermissionSource().toString());
         		logger.trace("	     Accesslevel : " + permission.get_AccessMask().toString());
         		logger.trace("	      Accesstype : " + permission.get_AccessType().toString());
         		logger.trace("	Inheritabledepth : " + permission.get_InheritableDepth());
         		logger.trace("        -----------------");
         	} 
    	 catch (Exception e) {
             logger.error("Error loggeando entidad: " + permission, e);
         }
    }

}
