package ar.com.lpa.samples.util;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.log4j.Logger;

@Getter
@Setter
public class SQLServerOperations {

    private static String dbHost = null;
    private static String dbPort = null;
    private static String dbName = null;
    private static String schemaName = null;
    private static String dbUserName = null;
    private static String dbUserPswd = null;

    public static void executeDatabaseOperations(Logger logger, String csvFilePath, String jsonFilePath) {
        String connectionString = "jdbc:sqlserver://" + dbHost + ":" + dbPort + ";databaseName="
                + dbName + ";encrypt=false;user=" + dbUserName + ";password=" + dbUserPswd;
        Connection connection = null;

        try {
            // Conexión a la base de datos
            connection = DriverManager.getConnection(connectionString);
            logger.info(String.format("Conexión exitosa a la base  SQL Server %s.", dbName));
            // Paso 1: Crear la tabla temporal
            createTemporaryTable(connection, logger);
            // Paso 2: Ejecutar el cursor
            executeCursor(connection, logger);
            // Paso 3: Consultar la tabla temporal y mostrar resultados
            queryTemporaryTableToCsv(connection, csvFilePath, logger);
            queryTemporaryTableToJson(connection, jsonFilePath, logger);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    logger.info("Conexión cerrada.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void createTemporaryTable(Connection connection, Logger logger) throws Exception {
        String createTableSQL = "IF OBJECT_ID('tempdb..#TablesResults') IS NOT NULL DROP TABLE #TablesResults;\n" +
                                "CREATE TABLE #TablesResults (table_name NVARCHAR(128), row_count INT, security_id_count INT);";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            logger.info("Tabla temporal creada.");
        }
    }

    private static void executeCursor(Connection connection, Logger logger) throws Exception {
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
            logger.info("Cursor ejecutado con éxito.");
        }
    }

    private static void queryTemporaryTableToCsv(Connection connection, String resultsFilePath, Logger logger) throws Exception {
        String querySQL = "SELECT * FROM #TablesResults ORDER BY table_name;";

        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(querySQL)) {
            ResultExporter.exportSelectFromTableToCsv(resultSet, resultsFilePath);
            logger.info(String.format("Exporting results from temporary table to %s", resultsFilePath));
        }
    }

    private static void queryTemporaryTableToJson(Connection connection, String resultsFilePath, Logger logger) throws Exception {
        String querySQL = "SELECT * FROM #TablesResults ORDER BY table_name;";

        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(querySQL)) {
            ResultExporter.exportSelectFromTableToJson(resultSet, resultsFilePath);
            logger.info(String.format("Exporting results from temporary table to %s", resultsFilePath));
        }
    }

    // Ejemplo de uso
    public static void main(String[] args) {
        dbPort = "1433";
        dbHost = "172.16.16.97";
        dbName = "OBJST1";
        schemaName = "dbo";
        dbUserName = "sa";
        dbUserPswd = "Lpa1234$";
        String csvFilePath = "C:/Logs/results.csv";
        String jsonFilePath = "C:/Logs/results.json";
        Logger logger = Logger.getLogger(SQLServerOperations.class);
        executeDatabaseOperations(logger, csvFilePath, jsonFilePath);
    }

}
