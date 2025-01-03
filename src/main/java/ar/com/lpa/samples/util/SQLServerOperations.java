package ar.com.lpa.samples.util;

import ar.com.lpa.samples.repository.FnDbTableRepo;
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


    private static final FnDbTableRepo fnDbTableRepo = new FnDbTableRepo();
    private static final Logger logger = Logger.getLogger(SQLServerOperations.class);

    public static void retreiveSecurableObjects(String dbHost, String dbPort, String dbName, String dbUserName, String dbUserPswd, String schemaName, String csvFilePath) {
        String connectionString = "jdbc:sqlserver://" + dbHost + ":" + dbPort + ";databaseName="
                + dbName + ";encrypt=false;user=" + dbUserName + ";password=" + dbUserPswd;
        Connection connection = null;

        try {
            // Conexión a la base de datos
            connection = DriverManager.getConnection(connectionString);
            logger.info(String.format("Successfully connected to database %s.", dbName));
            // Paso 1: Crear la tabla temporal
            createTemporaryTable(connection);
            // Paso 2: Ejecutar el cursor
            executeCursor(connection, schemaName);
            // Paso 3: Consultar la tabla temporal y guardar resultados
            queryTemporaryTableToCsv(connection, csvFilePath);
            queryTempToFnDbTableRepo(connection);

        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    logger.info("Connection closed.");
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
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
                logger.info(String.format("Exporting results from temporary table to %s", resultsFilePath));
            }
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

    private static void queryTempToFnDbTableRepo(Connection connection) throws Exception {
        String querySQL = "SELECT * FROM #TablesResults ORDER BY table_name;";

        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(querySQL)) {
            ResultExporter.exportSelectToFnDbTables(resultSet, fnDbTableRepo);
            logger.info(String.format("Exporting results from temporary table to FnDbTableRepo - # Records: %d",fnDbTableRepo.getFnDbTables().size()));
        }
    }

    // Ejemplo de uso
    public static void main(String[] args) {
        retreiveSecurableObjects("172.16.16.113","1433","testing",
                "sa","Lpa23291","dbo",null);
    }

}
