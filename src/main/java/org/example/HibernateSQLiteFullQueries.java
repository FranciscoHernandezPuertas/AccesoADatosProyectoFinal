package org.example;

import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utilidades.SessionFactoryProvider;

import java.util.List;

@Entity
@Table(name = "employee_projects")
class EmployeeProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "project_id", insertable = false, updatable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id", insertable = false, updatable = false)
    private EmployeeRealistic employeeRealistic;

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public EmployeeRealistic getEmployeeRealistic() {
        return employeeRealistic;
    }

    public void setEmployeeRealistic(EmployeeRealistic employeeRealistic) {
        this.employeeRealistic = employeeRealistic;
    }
}

@Entity
@Table(name = "employees_realistic")
class EmployeeRealistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "employee_id")
    private int employeeId;

    @Column(name = "salary")
    private double salary;

    @OneToMany(mappedBy = "employeeRealistic")
    private List<EmployeeProject> employeeProjects;

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public List<EmployeeProject> getEmployeeProjects() {
        return employeeProjects;
    }

    public void setEmployeeProjects(List<EmployeeProject> employeeProjects) {
        this.employeeProjects = employeeProjects;
    }
}

@Entity
@Table(name = "projects")
class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private int projectId;

    @Column(name = "budget")
    private double budget;

    @OneToMany(mappedBy = "project")
    private List<EmployeeProject> employeeProjects;

    // Getters y setters
    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public List<EmployeeProject> getEmployeeProjects() {
        return employeeProjects;
    }

    public void setEmployeeProjects(List<EmployeeProject> employeeProjects) {
        this.employeeProjects = employeeProjects;
    }
}

public class HibernateSQLiteFullQueries {

    private final SessionFactory factory;

    public HibernateSQLiteFullQueries() {
        this.factory = new SessionFactoryProvider().provideSessionFactory();
    }

    public List<Object[]> unirTablas() {
        try (Session session = factory.openSession()) {
            String hql = """
                    SELECT ep.project.projectId, er.salary
                    FROM EmployeeProject ep
                    JOIN ep.employeeRealistic er
                    """;
            return session.createQuery(hql, Object[].class).getResultList();
        }
    }

    public List<Object[]> calcularCostos() {
        try (Session session = factory.openSession()) {
            String hql = """
                    SELECT ep.project.projectId, SUM(er.salary / 1900)
                    FROM EmployeeProject ep
                    JOIN ep.employeeRealistic er
                    GROUP BY ep.project.projectId
                    """;
            return session.createQuery(hql, Object[].class).getResultList();
        }
    }

    public List<Object[]> combinarCostosConPresupuesto() {
        try (Session session = factory.openSession()) {
            String hql = """
                    SELECT p.projectId, p.budget, SUM(er.salary / 1900)
                    FROM Project p
                    JOIN p.employeeProjects ep
                    JOIN ep.employeeRealistic er
                    GROUP BY p.projectId, p.budget
                    """;
            return session.createQuery(hql, Object[].class).getResultList();
        }
    }

    public List<Object[]> calcularFraccionPresupuesto() {
        try (Session session = factory.openSession()) {
            String hql = """
                    SELECT p.projectId, p.budget, SUM(er.salary / 1900),
                           (SUM(er.salary / 1900) / p.budget) * 100
                    FROM Project p
                    JOIN p.employeeProjects ep
                    JOIN ep.employeeRealistic er
                    GROUP BY p.projectId, p.budget
                    """;
            return session.createQuery(hql, Object[].class).getResultList();
        }
    }

    public static void main(String[] args) {
        HibernateSQLiteFullQueries queries = new HibernateSQLiteFullQueries();

        System.out.println("3.a Resultados de unir tablas:");
        for (Object[] row : queries.unirTablas()) {
            System.out.printf("Project ID: %d, Salary: %.2f%n", row[0], row[1]);
        }

        System.out.println("3.b Resultados de costos salariales:");
        for (Object[] row : queries.calcularCostos()) {
            System.out.printf("Project ID: %d, Salary Costs: %.2f%n", row[0], row[1]);
        }

        System.out.println("3.c Resultados de costos con presupuesto:");
        for (Object[] row : queries.combinarCostosConPresupuesto()) {
            System.out.printf("Project ID: %d, Budget: %.2f, Salary Costs: %.2f%n", row[0], row[1], row[2]);
        }

        System.out.println("3.d Resultados de fracción del presupuesto:");
        for (Object[] row : queries.calcularFraccionPresupuesto()) {
            System.out.printf("Project ID: %d, Budget: %.2f, Salary Costs: %.2f, Fraction: %.2f%%%n",
                    row[0], row[1], row[2], row[3]);
        }
    }
}
