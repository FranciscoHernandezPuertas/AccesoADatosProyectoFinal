# Python script para importar datos CSV a SQLite
import sqlite3
import pandas as pd
import os

# Ruta relativa para la base de datos
db_path = os.path.join("..", "..", "..", "..", "company_database.db")

# Verificar si los archivos CSV existen en el directorio actual
csv_files = ["customers.csv", "departments.csv", "employees_realistic.csv", "employee_projects.csv", "orders.csv", "order_items.csv", "projects.csv"]
missing_files = [f for f in csv_files if not os.path.exists(f)]

if missing_files:
    print(f"Faltan los siguientes archivos CSV: {', '.join(missing_files)}")
    exit(1)

# Crear la base de datos SQLite en la ruta relativa especificada
connection = sqlite3.connect(db_path)
cursor = connection.cursor()

# Diccionario de archivos y nombres de tablas
tables = {
    "customers.csv": "customers",
    "departments.csv": "departments",
    "employees_realistic.csv": "employees_realistic",
    "employee_projects.csv": "employee_projects",
    "orders.csv": "orders",
    "order_items.csv": "order_items",
    "projects.csv": "projects",
}

# Importar datos a SQLite
for file, table in tables.items():
    print(f"Importando datos de {file} en la tabla {table}...")
    df = pd.read_csv(file)
    df.to_sql(table, connection, if_exists="replace", index=False)

print("Importación completada correctamente.")
connection.close()
