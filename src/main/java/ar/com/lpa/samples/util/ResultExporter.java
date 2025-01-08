package ar.com.lpa.samples.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import ar.com.lpa.samples.model.FnDbTable;
import ar.com.lpa.samples.model.LdapGroup;
import ar.com.lpa.samples.model.LdapUser;
import ar.com.lpa.samples.repository.FnDbTableRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import ar.com.lpa.samples.model.Principal;
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
            e.printStackTrace();
            return null;
        }
    }
    
    public static void exportPrincipalCollectionToJsonfile(Collection<Principal> principals, String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), principals);
        } catch (IOException e) {
            e.printStackTrace();
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

    public static void exportSelectToFnDbTables(ResultSet resultSet, FnDbTableRepo fnDbTableRepo) throws SQLException, IOException {
        // Escribir filas
        while (resultSet.next()) {
            String tableName = resultSet.getString("table_name");
            int rowCount = resultSet.getInt("row_count");
            int securityIdCount = resultSet.getInt("security_id_count");
            FnDbTable fnDbTable = new FnDbTable();
            fnDbTable.setTableName(tableName);
            fnDbTable.setRowCount(rowCount);
            fnDbTable.setSecurityIdCount(securityIdCount);
            fnDbTableRepo.getFnDbTables().add(fnDbTable);
        }
    }

    public static String expUsersToJsonOnConsole(List<LdapUser> collection) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(collection);
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

    public static String expGroupsToJsonOnConsole(Collection<LdapGroup> collection) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(collection);
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

    public static void exportLdapUsersToCsv(Collection<LdapUser> ldapUsers, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            // Escribir encabezados
            bufferedWriter.write("\"userSID\"|\"name\"|\"distinguishedName\"|\"shortName\"|\"displayName\"|\"eMail\"");
            bufferedWriter.newLine();
            // Escribir filas
            for (LdapUser ldapUser : ldapUsers) {
                String userSID = ldapUser.getUserSID();
                String name = ldapUser.getName();
                String distiguishedName = ldapUser.getDistinguishedName();
                String shortName = ldapUser.getShortName();
                String displayName = ldapUser.getDisplayName();
                String eMail = ldapUser.getEMail();
                String line = String.format("\"%s\"|\"%s\"|\"%s\"|\"%s\"|\"%s\"|\"%s\"", userSID, name ,distiguishedName, shortName, displayName, eMail);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            logger.info(String.format("Exported %d LDAP Users to %s", ldapUsers.size(), filePath));
        }
    }

    public static void exportLdapGroupsToCsv(Collection<LdapGroup> ldapGroups, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            // Escribir encabezados
            bufferedWriter.write("\"groupSID\"|\"name\"|\"distinguishedName\"|\"shortName\"|\"displayName\"");
            bufferedWriter.newLine();
            // Escribir filas
            for (LdapGroup ldapGroup : ldapGroups) {
                String groupSID = ldapGroup.getGroupSID();
                String name = ldapGroup.getName();
                String distiguishedName = ldapGroup.getDistinguishedName();
                String shortName = ldapGroup.getShortName();
                String displayName = ldapGroup.getDisplayName();
                String line = String.format("\"%s\"|\"%s\"|\"%s\"|\"%s\"|\"%s\"", groupSID, name ,distiguishedName, shortName, displayName);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            logger.info(String.format("Exported %d LDAP Groups to %s", ldapGroups.size(), filePath));
        }
    }

    public static void exportPrincipalsToCsv(Collection<Principal> principals, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            // Escribir encabezados
            bufferedWriter.write("\"sId\"|\"type\"|\"distinguishedName\"|\"samAccountName\"");
            bufferedWriter.newLine();
            // Escribir filas
            for (Principal principal : principals) {
                String sId = principal.getSId();
                String type = principal.getPrincipalType().toString();
                String distiguishedName = principal.getDistinguishedName();
                String samAccountName = principal.getSamAccountName();
                String line = String.format("\"%s\"|\"%s\"|\"%s\"|\"%s\"", sId, type ,distiguishedName, samAccountName);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            logger.info(String.format("Exported %d Principals to %s", principals.size(), filePath));
        }
    }

}
