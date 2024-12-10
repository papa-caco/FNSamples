package ar.com.lpa.samples.util;

import com.filenet.api.admin.ChoiceList;
import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.StorageArea;
import com.filenet.api.admin.StoragePolicy;
import com.filenet.api.core.Annotation;
import com.filenet.api.core.CustomObject;
import com.filenet.api.events.Event;
import com.filenet.api.events.Subscription;
import com.filenet.api.security.SecurityPolicy;
import com.filenet.api.security.SecurityTemplate;
import com.filenet.api.sweep.CmSweep;
import com.filenet.api.sweep.CmSweepPolicy;
import org.apache.log4j.Logger;

import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.filenet.api.security.AccessPermission;

import lombok.Getter;

@Getter
public class P8Logger {
		
    public static void logFolderProperties(Logger logger, Folder folder, int count) {
        try {
            // Loggear los valores obtenidos
            logger.debug(" Folder#: " + count);
            logger.debug("        Id: " + folder.get_Id().toString());
            logger.debug("      Name: " + folder.get_Name());
            logger.debug("     Class: " + folder.getClassName());
            logger.debug("     Owner: " + folder.get_Owner());
            logger.debug("Permissions #: " + folder.get_Permissions().size());

        } catch (Exception e) {
            logger.error("Error loggeando entidad: " + folder, e);
        }
    }
    
    public static void logDocumentProperties(Logger logger, Document document, int count) {
        try {
            // Loggear los valores obtenidos
            logger.debug("Document#: " + count);
            logger.debug("           Id: " + document.get_Id().toString());
            logger.debug("         Name: " + document.get_Name());
            logger.debug("        Class: " + document.getClassName());
            logger.debug("        Owner: " + document.get_Owner());
            logger.debug("Permissions #: " + document.get_Permissions().size());

        } catch (Exception e) {
            logger.error("Error loggeando entidad: " + document, e);
        }
    }

    public static void logCustomObjectProperties(Logger logger, CustomObject customObject, int count) {
        try {
            // Loggear los valores obtenidos
            logger.debug("Custom Object#: " + count);
            logger.debug("            Id: " + customObject.get_Id().toString());
            logger.debug("          Name: " + customObject.get_Name());
            logger.debug("         Class: " + customObject.getClassName());
            logger.debug("         Owner: " + customObject.get_Owner());
            logger.debug(" Permissions #: " + customObject.get_Permissions().size());

        } catch (Exception e) {
            logger.error("Error loggeando entidad: " + customObject, e);
        }
    }

    public static void logClassProperties(Logger logger, ClassDefinition classDefinition, int count) {
        try {
            // Loggear los valores obtenidos
            logger.debug(" Class Definition#: " + count);
            logger.debug("        Id: " + classDefinition.get_Id().toString());
            logger.debug("      Name: " + classDefinition.get_Name());
            logger.debug("     Class: " + classDefinition.getClassName());
            logger.debug("     Owner: " + classDefinition.get_Owner());
            logger.debug("Permissions #: " + classDefinition.get_Permissions().size());

        } catch (Exception e) {
            logger.error("Error loggeando entidad: " + classDefinition, e);
        }
    }

    public static void logAnnotationProperties(Logger logger, Annotation annotation, int count) {
        try {
            // Loggear los valores obtenidos
            logger.debug(" Annotation#: " + count);
            logger.debug("        Id: " + annotation.get_Id().toString());
            logger.debug("      Name: " + annotation.get_Name());
            logger.debug("     Class: " + annotation.getClassName());
            logger.debug("     Owner: " + annotation.get_Owner());
            logger.debug("Permissions #: " + annotation.get_Permissions().size());

        } catch (Exception e) {
            logger.error("Error loggeando entidad: " + annotation, e);
        }
    }

    public static void logChoiceListProperties(Logger logger, ChoiceList choiceList, int count) {
        try {
            // Loggear los valores obtenidos
            logger.debug("Choice List#: " + count);
            logger.debug("         Id: " + choiceList.get_Id().toString());
            logger.debug("       Name: " + choiceList.get_Name());
            logger.debug("      Class: " + choiceList.getClassName());
            logger.debug("      Owner: " + choiceList.get_Owner());
            logger.debug("Permissions #: " + choiceList.get_Permissions().size());

        } catch (Exception e) {
            logger.error("Error loggeando entidad: " + choiceList, e);
        }
    }

    public static void logEventProperties(Logger logger, Event event, int count) {
        try {
            // Loggear los valores obtenidos
            logger.debug("Event#: " + count);
            logger.debug("         Id: " + event.get_Id().toString());
            logger.debug("       Name: " + event.get_Name());
            logger.debug("      Class: " + event.getClassName());
            logger.debug("      Owner: " + event.get_Owner());
            logger.debug("Permissions #: " + event.get_Permissions().size());

        } catch (Exception e) {
            logger.error("Error loggeando entidad: " + event, e);
        }
    }

    public static void logStorageAreaProperties(Logger logger, StorageArea storageArea, int count) {
        try {
            // Loggear los valores obtenidos
            logger.debug("Storage Area#: " + count);
            logger.debug("         Id: " + storageArea.get_Id().toString());
            logger.debug("       Name: " + storageArea.get_DisplayName());
            logger.debug("      Class: " + storageArea.getClassName());
            logger.debug("      Owner: " + storageArea.get_Owner());
            logger.debug("Permissions #: " + storageArea.get_Permissions().size());

        } catch (Exception e) {
            logger.error("Error loggeando entidad: " + storageArea, e);
        }
    }

    public static void logStoragePolicyProperties(Logger logger, StoragePolicy storagePolicy, int count) {
        try {
            // Loggear los valores obtenidos
            logger.debug("Storage Policy#: " + count);
            logger.debug("         Id: " + storagePolicy.get_Id().toString());
            logger.debug("       Name: " + storagePolicy.get_Name());
            logger.debug("      Class: " + storagePolicy.getClassName());
            logger.debug("      Owner: " + storagePolicy.get_Owner());
            logger.debug("Permissions #: " + storagePolicy.get_Permissions().size());

        } catch (Exception e) {
            logger.error("Error loggeando entidad: " + storagePolicy, e);
        }
    }

    public static void logSecurityPolicyProperties(Logger logger, SecurityPolicy securityPolicy, int count) {
        try {
            // Loggear los valores obtenidos
            logger.debug("Security Policy#: " + count);
            logger.debug("             Id: " + securityPolicy.get_Id().toString());
            logger.debug("           Name: " + securityPolicy.get_Name());
            logger.debug("          Class: " + securityPolicy.getClassName());
            logger.debug("          Owner: " + securityPolicy.get_Owner());
            logger.debug("  Permissions #: " + securityPolicy.get_Permissions().size());
            logger.debug("Sec Templates #: " + securityPolicy.get_SecurityTemplates().size());

        } catch (Exception e) {
            logger.error("Error loggeando entidad: " + securityPolicy, e);
        }
    }

    public static void logSecurityTemplateProperties(Logger logger, int count, SecurityTemplate securityTemplate) {
        try {
            // Loggear los valores obtenidos
            logger.debug("- - - - - - - - - - ");
            logger.debug("Security Template#: " + count);
            logger.debug("                Id: " + securityTemplate.get_Id().toString());
            logger.debug("      Display Name: " + securityTemplate.get_DisplayName());
            logger.debug("             Class: " + securityTemplate.getClassName());
            logger.debug("     Permissions #: " + securityTemplate.get_TemplatePermissions().size());
        } catch (Exception e) {
            logger.error("Error loggeando entidad: " + securityTemplate, e);
        }
    }

    public static void logSubscriptionProperties(Logger logger, Subscription subscription, int count) {
        try {
            // Loggear los valores obtenidos
            logger.debug("Subscription#: " + count);
            logger.debug("         Id: " + subscription.get_Id().toString());
            logger.debug("       Name: " + subscription.get_DisplayName());
            logger.debug("      Class: " + subscription.getClassName());
            logger.debug("      Owner: " + subscription.get_Owner());
            logger.debug("Permissions #: " + subscription.get_Permissions().size());

        } catch (Exception e) {
            logger.error("Error loggeando entidad: " + subscription, e);
        }
    }

    public static void logSweepProperties(Logger logger, CmSweep sweep, int count) {
        try {
            // Loggear los valores obtenidos
            logger.debug("       Sweep#: " + count);
            logger.debug("           Id: " + sweep.get_Id().toString());
            logger.debug("        Class: " + sweep.getClassName());
            logger.debug("        Owner: " + sweep.get_Owner());
            logger.debug("Permissions #: " + sweep.get_Permissions().size());

        } catch (Exception e) {
            logger.error("Error loggeando entidad: " + sweep, e);
        }
    }

    public static void logSweepPolicyProperties(Logger logger, CmSweepPolicy sweepPolicy, int count) {
        try {
            // Loggear los valores obtenidos
            logger.debug("Sweep Policy#: " + count);
            logger.debug("           Id: " + sweepPolicy.get_Id().toString());
            logger.debug("         Name: " + sweepPolicy.get_DisplayName());
            logger.debug("        Class: " + sweepPolicy.getClassName());
            logger.debug("        Owner: " + sweepPolicy.get_Owner());
            logger.debug("Permissions #: " + sweepPolicy.get_Permissions().size());

        } catch (Exception e) {
            logger.error("Error loggeando entidad: " + sweepPolicy, e);
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
