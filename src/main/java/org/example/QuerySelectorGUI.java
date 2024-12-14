package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.List;

public class QuerySelectorGUI {

    private static final String JDBC_URL = "jdbc:sqlite:company_database.db";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuerySelectorGUI().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Query Selector");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(32, 34, 37));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(47, 49, 54));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Seleccione el modo de consulta");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 20, 0));
        buttonPanel.setBackground(new Color(32, 34, 37));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JButton jdbcButton = new JButton("Usar JDBC");
        JButton hibernateButton = new JButton("Usar Hibernate");

        styleButton(jdbcButton);
        styleButton(hibernateButton);

        buttonPanel.add(jdbcButton);
        buttonPanel.add(hibernateButton);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(47, 49, 54));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel footerLabel = new JLabel("Desarrollado por QuerySelector Team");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(Color.LIGHT_GRAY);
        footerPanel.add(footerLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);

        jdbcButton.addActionListener(e -> showQueryPanel(frame, "JDBC"));
        hibernateButton.addActionListener(e -> showQueryPanel(frame, "Hibernate"));

        frame.setVisible(true);
    }

    private void showQueryPanel(JFrame frame, String mode) {
        frame.getContentPane().removeAll();

        JPanel queryPanel = new JPanel();
        queryPanel.setLayout(new GridLayout(5, 1, 10, 20));  // Mayor separación entre filas
        queryPanel.setBackground(new Color(32, 34, 37));
        queryPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(47, 49, 54), 2),
                BorderFactory.createEmptyBorder(20, 50, 20, 50)));

        JButton joinButton = new JButton("3.a Unir tablas");
        JButton salaryCostsButton = new JButton("3.b Costos salariales");
        JButton combineCostsButton = new JButton("3.c Combinar costos");
        JButton fractionButton = new JButton("3.d Fracción presupuesto");
        JButton backButton = new JButton("Volver Atrás");

        styleButton(joinButton);
        styleButton(salaryCostsButton);
        styleButton(combineCostsButton);
        styleButton(fractionButton);
        styleButton(backButton);

        queryPanel.add(joinButton);
        queryPanel.add(salaryCostsButton);
        queryPanel.add(combineCostsButton);
        queryPanel.add(fractionButton);
        queryPanel.add(backButton);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBackground(new Color(32, 34, 37));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(47, 49, 54), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        tablePanel.revalidate();
        tablePanel.repaint();

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable resultTable = new JTable(tableModel);
        resultTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resultTable.setForeground(Color.WHITE);
        resultTable.setBackground(new Color(47, 49, 54));  // Fondo de la tabla
        resultTable.setGridColor(new Color(88, 101, 242));
        resultTable.setRowHeight(50); // Aumento del tamaño de las filas

        // Cambiar fondo del JScrollPane para que coincida
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBackground(new Color(32, 34, 37));  // Fondo del scrollPane
        scrollPane.getViewport().setBackground(new Color(32, 34, 37)); // Fondo del área visible
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(queryPanel, BorderLayout.CENTER);
        frame.add(tablePanel, BorderLayout.SOUTH);

        joinButton.addActionListener(e -> executeQuery(mode, "join", tableModel));
        salaryCostsButton.addActionListener(e -> executeQuery(mode, "salaryCosts", tableModel));
        combineCostsButton.addActionListener(e -> executeQuery(mode, "combineCosts", tableModel));
        fractionButton.addActionListener(e -> executeQuery(mode, "fraction", tableModel));
        backButton.addActionListener(e -> {
            createAndShowGUI();
            frame.dispose();
        });

        frame.revalidate();
        frame.repaint();
    }



    private void executeQuery(String mode, String queryType, DefaultTableModel tableModel) {
        if (mode.equals("JDBC")) {
            executeJDBCQuery(queryType, tableModel);
        } else if (mode.equals("Hibernate")) {
            executeHibernateQuery(queryType, tableModel);
        }
    }

    private void executeJDBCQuery(String queryType, DefaultTableModel tableModel) {
        String query = switch (queryType) {
            case "join" -> "SELECT ep.project_id, ep.employee_id, er.salary FROM employee_projects AS ep JOIN employees_realistic AS er ON ep.employee_id = er.employee_id;";
            case "salaryCosts" -> "SELECT ep.project_id, SUM(er.salary / 1900) AS project_salary_costs FROM employee_projects AS ep JOIN employees_realistic AS er ON ep.employee_id = er.employee_id GROUP BY ep.project_id;";
            case "combineCosts" -> "SELECT p.project_id, p.budget, pc.project_salary_costs FROM projects AS p JOIN ( SELECT ep.project_id, SUM(er.salary / 1900) AS project_salary_costs FROM employee_projects AS ep JOIN employees_realistic AS er ON ep.employee_id = er.employee_id GROUP BY ep.project_id ) AS pc ON p.project_id = pc.project_id;";
            case "fraction" -> "SELECT p.project_id, p.budget, pc.project_salary_costs, (pc.project_salary_costs / p.budget) * 100 AS cost_fraction FROM projects AS p JOIN ( SELECT ep.project_id, SUM(er.salary / 1900) AS project_salary_costs FROM employee_projects AS ep JOIN employees_realistic AS er ON ep.employee_id = er.employee_id GROUP BY ep.project_id ) AS pc ON p.project_id = pc.project_id;";
            default -> "";
        };

        try (Connection conn = DriverManager.getConnection(JDBC_URL); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            updateTableModel(tableModel, rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeHibernateQuery(String queryType, DefaultTableModel tableModel) {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
             Session session = factory.openSession()) {
            HibernateSQLiteFullQueries queries = new HibernateSQLiteFullQueries();
            List<Object[]> results = switch (queryType) {
                case "join" -> queries.unirTablas();
                case "salaryCosts" -> queries.calcularCostos();
                case "combineCosts" -> queries.combinarCostosConPresupuesto();
                case "fraction" -> queries.calcularFraccionPresupuesto();
                default -> List.of();
            };

            updateTableModelHibernate(tableModel, results);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTableModel(DefaultTableModel tableModel, ResultSet rs) throws Exception {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        int columnCount = rs.getMetaData().getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            tableModel.addColumn(rs.getMetaData().getColumnName(i));
        }

        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            tableModel.addRow(row);
        }
    }

    private void updateTableModelHibernate(DefaultTableModel tableModel, List<Object[]> results) {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        if (!results.isEmpty()) {
            Object[] firstRow = results.get(0);
            for (int i = 0; i < firstRow.length; i++) {
                tableModel.addColumn("Column " + (i + 1));
            }

            for (Object[] row : results) {
                tableModel.addRow(row);
            }
        }
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Tamaño de fuente más grande
        button.setBackground(new Color(88, 101, 242));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30)); // Márgenes más grandes
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(47, 49, 54), 2, true), // Borde redondeado
                BorderFactory.createEmptyBorder(15, 30, 15, 30)
        ));
    }
}
