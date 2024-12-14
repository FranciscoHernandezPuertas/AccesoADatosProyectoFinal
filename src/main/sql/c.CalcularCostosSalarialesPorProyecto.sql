SELECT
    ep.project_id,
    SUM(er.salary / 1900) AS project_salary_costs
FROM
    employee_projects AS ep
        JOIN
    employees_realistic AS er
    ON
        ep.employee_id = er.employee_id
GROUP BY
    ep.project_id;
