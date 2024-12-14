package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLiteJDBCFullQueries {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:company_database.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Conexión establecida con SQLite.");
            Statement stmt = conn.createStatement();

            // 3.a Unir tablas
            String joinQuery = """
                    SELECT ep.project_id, ep.employee_id, er.salary
                    FROM employee_projects AS ep
                    JOIN employees_realistic AS er
                    ON ep.employee_id = er.employee_id;
                    """;

            ResultSet rsJoin = stmt.executeQuery(joinQuery);
            System.out.println("3.a Resultados de unir tablas:");
            while (rsJoin.next()) {
                System.out.printf("Project ID: %d, Employee ID: %d, Salary: %.2f%n",
                        rsJoin.getInt("project_id"), rsJoin.getInt("employee_id"), rsJoin.getDouble("salary"));
            }

            // 3.b Calcular costos salariales por proyecto
            String salaryCostsQuery = """
                    SELECT ep.project_id, SUM(er.salary / 1900) AS project_salary_costs
                    FROM employee_projects AS ep
                    JOIN employees_realistic AS er
                    ON ep.employee_id = er.employee_id
                    GROUP BY ep.project_id;
                    """;

            ResultSet rsCosts = stmt.executeQuery(salaryCostsQuery);
            System.out.println("3.b Resultados de costos salariales:");
            while (rsCosts.next()) {
                System.out.printf("Project ID: %d, Salary Costs: %.2f%n",
                        rsCosts.getInt("project_id"), rsCosts.getDouble("project_salary_costs"));
            }

            // 3.c Combinar costos con presupuesto
            String combineQuery = """
                    SELECT p.project_id, p.budget, pc.project_salary_costs
                    FROM projects AS p
                    JOIN (
                        SELECT ep.project_id, SUM(er.salary / 1900) AS project_salary_costs
                        FROM employee_projects AS ep
                        JOIN employees_realistic AS er
                        ON ep.employee_id = er.employee_id
                        GROUP BY ep.project_id
                    ) AS pc
                    ON p.project_id = pc.project_id;
                    """;

            ResultSet rsCombine = stmt.executeQuery(combineQuery);
            System.out.println("3.c Resultados de costos con presupuesto:");
            while (rsCombine.next()) {
                System.out.printf("Project ID: %d, Budget: %.2f, Salary Costs: %.2f%n",
                        rsCombine.getInt("project_id"), rsCombine.getDouble("budget"), rsCombine.getDouble("project_salary_costs"));
            }

            // 3.d Calcular fracción del presupuesto
            String fractionQuery = """
                    SELECT p.project_id, p.budget, pc.project_salary_costs,
                           (pc.project_salary_costs / p.budget) * 100 AS cost_fraction
                    FROM projects AS p
                    JOIN (
                        SELECT ep.project_id, SUM(er.salary / 1900) AS project_salary_costs
                        FROM employee_projects AS ep
                        JOIN employees_realistic AS er
                        ON ep.employee_id = er.employee_id
                        GROUP BY ep.project_id
                    ) AS pc
                    ON p.project_id = pc.project_id;
                    """;

            ResultSet rsFraction = stmt.executeQuery(fractionQuery);
            System.out.println("3.d Resultados de fracción del presupuesto:");
            while (rsFraction.next()) {
                System.out.printf("Project ID: %d, Budget: %.2f, Salary Costs: %.2f, Fraction: %.2f%%%n",
                        rsFraction.getInt("project_id"), rsFraction.getDouble("budget"),
                        rsFraction.getDouble("project_salary_costs"), rsFraction.getDouble("cost_fraction"));
            }
        } catch (Exception e) {
            System.err.println("Error conectándose a la base de datos SQLite.");
            e.printStackTrace();
        }
    }
}
