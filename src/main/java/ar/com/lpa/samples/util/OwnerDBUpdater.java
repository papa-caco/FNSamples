package ar.com.lpa.samples.util;

import ar.com.lpa.samples.model.PrincipalType;
import ar.com.lpa.samples.repository.PrincipalRepo;
import ar.com.lpa.samples.model.Principal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class OwnerDBUpdater {

    private static final Logger logger = Logger.getLogger(OwnerDBUpdater.class);


    /*public static void main(String[] args) throws IOException, SQLException {
        Principal principal = new Principal("CN=P85CEAdmin,CN=Users,DC=LAB,DC=GRUPOLPA,DC=COM",PrincipalType.USER);
        principal.setDistinguishedName("CN=P85CEAdmin,CN=Users,DC=LAB,DC=GRUPOLPA,DC=COM");
        principal.setSamAccountName("P85CEAdmin");
        principal.setSId("S-1-5-21-2592625401-1541527055-4017201578-1122");
        PrincipalRepo.getInstance().createPrincipal(principal);

        String configPath = "config.properties";
        ConfigLoader configLoader = new ConfigLoader(configPath);
        String dbHost = configLoader.getProperty("dbHost");
        String dbPort = configLoader.getProperty("dbPort");
        String databaseName = configLoader.getProperty("databaseName");
        String schemaName = configLoader.getProperty("schemaName");
        String dbUserName = configLoader.getProperty("dbUserName");
        String dbUserPswd = configLoader.getProperty("dbUserPswd");

        String userName = configLoader.getProperty("userName");
        System.out.println(userName);
        String documentId = "A288286D-8B4F-C0CF-86A1-8DCC28800000";
        String sId = PrincipalRepo.getInstance().getPrincipalBySamAccountName(userName).getSId();


        Connection dbConnection = SQLServerOperations.openDbConnection(dbHost, dbPort, databaseName, dbUserName, dbUserPswd);
        String securityId = SQLServerOperations.getMostUsedSecurityId(dbConnection, schemaName);
        SQLServerOperations.updateMostUsedNtSecurityDescriptor(dbConnection,securityId,schemaName,sId);
        SQLServerOperations.closeDbConnection(dbConnection);
    }*/
}


