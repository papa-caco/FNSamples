package ar.com.lpa.samples.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import ar.com.lpa.samples.model.*;
import ar.com.lpa.samples.repository.SecurableObjectRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.filenet.api.security.Group;
import com.filenet.api.security.User;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;


public class ResultExporter {
    private static final Logger logger = Logger.getLogger(ResultExporter.class);
	
    public static String exportPrincipalCollectionToJsonOnConsole(Collection<Principal> principals) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(principals);
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }
    
    public static void exportPrincipalCollectionToJsonfile(List<Principal> principals, String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), principals);
        } catch (IOException e) {
            //e.printStackTrace();
        }
        logger.info(String.format("Exported %d Principals to %s", principals.size(), filePath));
    }

    public static void exportSelectFromTableToJsonFile(ResultSet resultSet, String filePath) throws SQLException, IOException {
        JSONArray jsonArray = new JSONArray();
        int count = 0;
        while (resultSet.next()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("table_name", resultSet.getString("table_name"));
            jsonObject.put("row_count", resultSet.getInt("row_count"));
            jsonObject.put("security_id_count", resultSet.getInt("security_id_count"));
            jsonArray.put(jsonObject);
            count++;
        }
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(jsonArray.toString(4)); // Formatea con indentación de 4 espacios
            logger.info(String.format("Exported %d Rows to %s", count, filePath));
        }
    }

    public static void exportSelectFromTableToCsv(ResultSet resultSet, String filePath) throws SQLException, IOException {
        try (FileWriter fileWriter = new FileWriter(filePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            int count = 0;
            // Escribir encabezados
            bufferedWriter.write("\"table_name\"|\"row_count\"|\"security_id_count\"");
            bufferedWriter.newLine();
            // Escribir filas
            while (resultSet.next()) {
                String tableName = resultSet.getString("table_name");
                int rowCount = resultSet.getInt("row_count");
                int securityIdCount = resultSet.getInt("security_id_count");
                String line = String.format("\"%s\"|\"%d\"|\"%d\"", tableName, rowCount, securityIdCount);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
                count++;
            }
            logger.info(String.format("Exported %d Rows to %s", count, filePath));
        }
    }

    public static void exportSelectToSecurableObjectRepo(ResultSet resultSet) throws SQLException {
        // Escribir filas
        while (resultSet.next()) {
            String tableName = resultSet.getString("table_name");
            int lineCount = resultSet.getInt("row_count");
            int securityIdCount = resultSet.getInt("security_id_count");
            SecurableObject securableObject = new SecurableObject(tableName, lineCount, securityIdCount);
            SecurableObjectRepo.getInstance().createSecurableObject(securableObject);
        }
    }

    public static void exportUsersToCsv(List<User> users, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            // Escribir encabezados
            bufferedWriter.write("\"userSID\"|\"name\"|\"distinguishedName\"|\"shortName\"|\"displayName\"|\"eMail\"");
            bufferedWriter.newLine();
            // Escribir filas
            for (User user : users) {
                String userSID = user.get_Id();
                String name = user.get_Name();
                String distiguishedName = user.get_DistinguishedName();
                String shortName = user.get_ShortName();
                String displayName = user.get_DisplayName();
                String eMail = user.get_Email();
                String line = String.format("\"%s\"|\"%s\"|\"%s\"|\"%s\"|\"%s\"|\"%s\"", userSID, name ,distiguishedName, shortName, displayName, eMail);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            logger.info(String.format("Exported %d LDAP Users to %s", users.size(), filePath));
        }
    }



    public static void exportGroupsToCsv(List<Group> groups, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            // Escribir encabezados
            bufferedWriter.write("\"groupSID\"|\"name\"|\"distinguishedName\"|\"shortName\"|\"displayName\"");
            bufferedWriter.newLine();
            // Escribir filas
            for (Group group : groups) {
                String groupSID = group.get_Id();
                String name = group.get_Name();
                String distiguishedName = group.get_DistinguishedName();
                String shortName = group.get_ShortName();
                String displayName = group.get_DisplayName();
                String line = String.format("\"%s\"|\"%s\"|\"%s\"|\"%s\"|\"%s\"", groupSID, name ,distiguishedName, shortName, displayName);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            logger.info(String.format("Exported %d LDAP Groups to %s", groups.size(), filePath));
        }
    }

    public static void exportPrincipalsToCsv(Collection<Principal> principals, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            // Escribir encabezados
            bufferedWriter.write("\"idPrincipal\"|\"name\"|\"sId\"|\"type\"|\"distinguishedName\"|\"samAccountName\"");
            bufferedWriter.newLine();
            // Escribir filas
            for (Principal principal : principals) {
                int idPrincipal = principal.getIdPrincipal();
                String name = principal.getName();
                String sId = principal.getSId();
                String type = principal.getPrincipalType().toString();
                String distiguishedName = principal.getDistinguishedName();
                String samAccountName = principal.getSamAccountName();
                String line = String.format("\"%d\"|\"%s\"|\"%s\"|\"%s\"|\"%s\"|\"%s\"", idPrincipal, name,sId, type ,distiguishedName, samAccountName);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            logger.info(String.format("Exported %d Principals to %s", principals.size(), filePath));
        }
    }

    public static void exportOwnersToCsv(List<FnOwner> fnOwners, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            // Escribir encabezados
            bufferedWriter.write("\"objectId\"|\"fnObjectType\"|\"owner\"|\"status\"");
            bufferedWriter.newLine();
            // Escribir filas
            for (FnOwner fnOwner : fnOwners) {
                String objectId = fnOwner.getObjectId() ;
                String fnObjectType = fnOwner.getFnObjectType().toString();
                String name = "null";
                if (fnOwner.getOwner() != null) {
                    name = fnOwner.getOwner().getName();
                }
                char status = fnOwner.getStatus();
                String line = String.format("\"%s\"|\"%s\"|\"%s\"|\"%s\"", objectId, fnObjectType, name, status);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            logger.info(String.format("Exported %d Owners to %s", fnOwners.size(), filePath));
        }
    }

    public static void exportPermissionsToCsv(List<FnAccessPermission> fnAccessPermissions, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            // Escribir encabezados
            bufferedWriter.write("\"objectId\"|\"fnObjectType\"|\"granteeName\"|\"principalType\"|\"permissionSource\"|\"accessMask\"|\"accessType\"|\"inheritableDepth\"|\"status");
            bufferedWriter.newLine();
            // Escribir filas
            for (FnAccessPermission fnAccessPermission: fnAccessPermissions) {
                String objectId = fnAccessPermission.getObjectId() ;
                String fnObjectType = fnAccessPermission.getFnObjectType().toString();
                String granteeName = "null";
                if (fnAccessPermission.getGranteeName() != null) {
                    granteeName = fnAccessPermission.getGranteeName().getName();
                }
                String principalType = fnAccessPermission.getPrincipalType().toString();
                String permissionSource = fnAccessPermission.getPermissionSource();
                int accessMask = fnAccessPermission.getAccessMask();
                String accessType = fnAccessPermission.getAccessType();
                int inheritableDepth = fnAccessPermission.getInheritableDepth();
                char status = fnAccessPermission.getStatus();
                String line = String.format("\"%s\"|\"%s\"|\"%s\"|\"%s\"|\"%s\"|\"%d\"|\"%s\"|\"%d\"|\"%s\"",
                        objectId, fnObjectType, granteeName, principalType, permissionSource, accessMask, accessType, inheritableDepth, status);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            logger.info(String.format("Exported %d Access Permissions to %s", fnAccessPermissions.size(), filePath));
        }
    }

}
