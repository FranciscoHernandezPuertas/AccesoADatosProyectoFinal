SELECT
    ep.project_id,
    ep.employee_id,
    er.salary
FROM
    employee_projects AS ep
        JOIN
    employees_realistic AS er
    ON
        ep.employee_id = er.employee_id;
