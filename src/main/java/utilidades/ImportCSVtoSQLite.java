package utilidades;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class ImportCSVtoSQLite {
    public static void main(String[] args) {
        // Usar rutas relativas
        String baseDir = "src/main"; // Ruta base para el proyecto (relativa al directorio principal)
        String csvDir = "csv"; // Subdirectorio donde están los archivos CSV
        String dbDir = "ProyectoFinal"; // Carpeta donde se encuentra la base de datos

        // Ruta relativa de la base de datos
        Path dbPath = Paths.get(baseDir, dbDir, "company_database.db");

        // Archivos CSV y sus tablas correspondientes
        Map<String, String> csvFiles = new HashMap<>();
        csvFiles.put("customers.csv", "customers");
        csvFiles.put("departments.csv", "departments");
        csvFiles.put("employees_realistic.csv", "employees_realistic");
        csvFiles.put("employee_projects.csv", "employee_projects");
        csvFiles.put("orders.csv", "orders");
        csvFiles.put("order_items.csv", "order_items");
        csvFiles.put("projects.csv", "projects");

        // Conexión a la base de datos SQLite
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath.toString())) {
            Statement stmt = conn.createStatement();

            // Iterar sobre los archivos CSV
            for (Map.Entry<String, String> entry : csvFiles.entrySet()) {
                String csvFile = entry.getKey();
                String tableName = entry.getValue();
                System.out.println("Importando datos de " + csvFile + " en la tabla " + tableName + "...");

                // Leer el archivo CSV desde la ruta relativa
                Path csvPath = Paths.get(baseDir, csvDir, csvFile);
                try (BufferedReader br = Files.newBufferedReader(csvPath)) {
                    String line;
                    String[] headers = br.readLine().split(","); // Leer encabezados (primera línea)

                    // Preparar sentencia SQL para insertar datos
                    StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
                    for (String header : headers) {
                        sql.append(header).append(",");
                    }
                    sql.deleteCharAt(sql.length() - 1); // Eliminar la última coma
                    sql.append(") VALUES (");
                    for (int i = 0; i < headers.length; i++) {
                        sql.append("?,");
                    }
                    sql.deleteCharAt(sql.length() - 1); // Eliminar la última coma
                    sql.append(");");

                    try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
                        // Leer cada línea de datos y preparar para inserción
                        while ((line = br.readLine()) != null) {
                            String[] values = line.split(",");
                            for (int i = 0; i < values.length; i++) {
                                pstmt.setString(i + 1, values[i]);
                            }
                            pstmt.executeUpdate(); // Ejecutar la inserción
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error leyendo el archivo CSV " + csvFile + ": " + e.getMessage());
                }
            }
            System.out.println("Importación completada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error con la base de datos: " + e.getMessage());
        }
    }
}
