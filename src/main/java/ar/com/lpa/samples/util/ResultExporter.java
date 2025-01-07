package ar.com.lpa.samples.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ar.com.lpa.samples.model.FnDbTable;
import ar.com.lpa.samples.model.LdapGroup;
import ar.com.lpa.samples.model.LdapUser;
import ar.com.lpa.samples.repository.FnDbTableRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import ar.com.lpa.samples.model.Principal;
import com.filenet.api.security.Group;
import com.filenet.api.security.User;
import org.json.JSONArray;
import org.json.JSONObject;


public class ResultExporter {
	
    public static String exportPrincipalCollectionToJson(Collection<Principal> principals) {
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
    }

    public static void exportSelectFromTableToJson(ResultSet resultSet, String filePath) throws SQLException, IOException {
        JSONArray jsonArray = new JSONArray();

        while (resultSet.next()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("table_name", resultSet.getString("table_name"));
            jsonObject.put("row_count", resultSet.getInt("row_count"));
            jsonObject.put("security_id_count", resultSet.getInt("security_id_count"));
            jsonArray.put(jsonObject);
        }

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(jsonArray.toString(4)); // Formatea con indentación de 4 espacios
        }
    }

    public static void exportSelectFromTableToCsv(ResultSet resultSet, String filePath) throws SQLException, IOException {
        try (FileWriter fileWriter = new FileWriter(filePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

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
            }
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

    public static String expPrincipalsToJsonOnConsole(Collection<Principal> collection) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(collection);
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
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

}
