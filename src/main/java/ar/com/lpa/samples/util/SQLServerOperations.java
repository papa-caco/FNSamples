package ar.com.lpa.samples.util;

import lombok.Getter;
import lombok.Setter;

import java.sql.*;

import org.apache.log4j.Logger;

@Getter
@Setter
public class SQLServerOperations {

    private static final Logger logger = Logger.getLogger(SQLServerOperations.class);

    public static void retreiveSecurableObjects(String dbHost, String dbPort, String dbName, String dbUserName, String dbUserPswd, String schemaName, String csvFilePath) {
        Connection dbConnection = openDbConnection(dbHost,dbPort, dbName,dbUserName, dbUserPswd);

        try {
            // Conexión a la base de datos
            logger.info(String.format("Successfully connected to database %s.", dbName));
            // Paso 1: Crear la tabla temporal
            createTemporaryTable(dbConnection);
            // Paso 2: Ejecutar el cursor
            executeCursor(dbConnection, schemaName);
            // Paso 3: Consultar la tabla temporal y guardar resultados
            queryTemporaryTableToCsv(dbConnection, csvFilePath);
            queryTempToSecurableObjectRepo(dbConnection);


        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            closeDbConnection(dbConnection);
        }
    }

    private static void createTemporaryTable(Connection connection) throws Exception {
        String createTableSQL = "IF OBJECT_ID('tempdb..#TablesResults') IS NOT NULL DROP TABLE #TablesResults;\n" +
                                "CREATE TABLE #TablesResults (table_name NVARCHAR(128), row_count INT, security_id_count INT);";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            logger.info("Temporary table created.");
        }
    }

    private static void executeCursor(Connection connection, String schemaName) throws Exception {
        String cursorSQL = "DECLARE @sql NVARCHAR(MAX);\n" +
                           "DECLARE @tableName NVARCHAR(128);\n" +
                           "DECLARE table_cursor CURSOR FOR\n" +
                           "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE COLUMN_NAME = N'security_id' AND TABLE_NAME != 'SecurityDesc';\n" +
                           "OPEN table_cursor;\n" +
                           "FETCH NEXT FROM table_cursor INTO @tableName;\n" +
                           "WHILE @@FETCH_STATUS = 0\n" +
                           "BEGIN\n" +
                           "    -- Insertar row_count\n" +
                           "    SET @sql = 'INSERT INTO #TablesResults (table_name, row_count)\n" +
                           "                SELECT ''' + @tableName + ''', COUNT(*)\n" +
                           "                FROM " + schemaName + ".' + @tableName + ';';\n" +
                           "    EXEC sp_executesql @sql;\n" +
                           "    -- Actualizar security_id_count\n" +
                           "    SET @sql = 'UPDATE #TablesResults SET security_id_count =\n" +
                           "                (SELECT COUNT(DISTINCT(t.security_id)) \n" +
                           "                FROM " + schemaName + ".' + @tableName + ' t JOIN " + schemaName + ".SecurityDesc s \n" +
                           "                            ON t.security_id = s.security_id) WHERE table_name = ''' + @tableName + ''';';\n" +
                           "                EXEC sp_executesql @sql;\n" +
                           "                FETCH NEXT FROM table_cursor INTO @tableName;\n" +
                           "            END;\n" +
                           "            CLOSE table_cursor;\n" +
                           "            DEALLOCATE table_cursor;\n" +
                           "            DELETE FROM #TablesResults WHERE security_id_count = 0;";
                try (Statement stmt = connection.createStatement()) {
            stmt.execute(cursorSQL);
            logger.info("Cursor executed.");
        }
    }

    private static void queryTemporaryTableToCsv(Connection connection, String resultsFilePath) throws Exception {
        if (resultsFilePath != null) {
            String querySQL = "SELECT * FROM #TablesResults ORDER BY table_name;";

            try (Statement stmt = connection.createStatement();
                 ResultSet resultSet = stmt.executeQuery(querySQL)) {
                ResultExporter.exportSelectFromTableToCsv(resultSet, resultsFilePath);
            }
        }
    }

    private static void queryTemporaryTableToJson(Connection connection, String resultsFilePath, Logger logger) throws Exception {
        String querySQL = "SELECT * FROM #TablesResults ORDER BY table_name;";

        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(querySQL)) {
            ResultExporter.exportSelectFromTableToJsonFile(resultSet, resultsFilePath);
            logger.info(String.format("Exporting results from temporary table to %s", resultsFilePath));
        }
    }

    private static void queryTempToSecurableObjectRepo(Connection connection) throws Exception {
        String querySQL = "SELECT * FROM #TablesResults ORDER BY table_name;";
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(querySQL)) {
            ResultExporter.exportSelectToSecurableObjectRepo(resultSet);
        }
    }

    public static Connection openDbConnection(String dbHost, String dbPort, String dbName, String dbUserName, String dbUserPswd) {
        Connection connection = null;
        String connectionString = "jdbc:sqlserver://" + dbHost + ":" + dbPort + ";databaseName="
                + dbName + ";encrypt=false;user=" + dbUserName + ";password=" + dbUserPswd;

        try {
            // Conexión a la base de datos
            connection = DriverManager.getConnection(connectionString);
            logger.info(String.format("Successfully connected to database %s.", dbName));
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return connection;
    }

    public static void closeDbConnection(Connection connection){
        if (connection != null){
            try {
                connection.close();
                logger.info("Connection closed.");
            } catch (Exception e) {
            }
        }
    }

    public static void updateMostUsedNtSecurityDescriptor(Connection dbConnection, String securityId, String dbSchema, String stringSid) throws SQLException {
        if (dbConnection != null) {
            String hexSId = SIDConverters.convertStringSIdToHexSId(stringSid);
            String ntSecurityDescriptor = "0x04000000001C0000000000000000000000000000000000" + hexSId + "00000000";
            String updateDml = "UPDATE " + dbSchema + ".SecurityDesc SET nt_security_descriptor = " + ntSecurityDescriptor + " WHERE security_id = '"+ securityId +"'";
            try (PreparedStatement statement = dbConnection.prepareStatement(updateDml)) {
                int rowsAffected = statement.executeUpdate();
                logger.debug(String.format("SecurityDesc - %d Rows Updated", rowsAffected));
            }
        }
    }

    public static String getMostUsedSecurityId(Connection connection, String dbSchema) throws SQLException {
        String mostUsedSecurityId = null;
        String query = "SELECT TOP(1) COUNT(*), d.security_id " +
                "FROM " + dbSchema + ".DocVersion d " +
                "JOIN " + dbSchema + ".SecurityDesc s ON d.security_id = s.security_id " +
                "GROUP BY d.security_id " +
                "ORDER BY 1 DESC";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                mostUsedSecurityId = resultSet.getString("security_id");
            }
        }
        return mostUsedSecurityId;
    }

}
