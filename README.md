# NOTA: El archivo Readme.md ha sido generado por un script python que convierte los archivos pdf a markdown. Pueden presentarse errores en la visualización del mismo.

# Página 1

https://github.com/FranciscoHernandezPuertas/AccesoADatosProyectoFinal 
Funcionalidad: 
0. Asumimos que tenemos instalado Python (y está añadido a PATH) y un IDE de Java 
como IntelliJ IDEA o NetBeans 
 
1. Ejecutamos el archivo “a.ImportarCSVaSQLite.py” ubicado en src/main/csv para crear 
la base de datos SQLite (company_database.db, se creará en la ruta del proyecto) e 
importar los archivos .csv a la base de datos. En caso de que no estén todos los 
archivos ("customers.csv", "departments.csv", "employees_realistic.csv", 
"employee_projects.csv", "orders.csv", "order_items.csv", "projects.csv") la 
importación no se efectuará. 
 
 


![Imagen 1](./Documentos/image_page1_0.jpeg)

![Imagen 2](./Documentos/image_page1_1.png)

![Imagen 3](./Documentos/image_page1_2.png)

# Página 2

https://github.com/FranciscoHernandezPuertas/AccesoADatosProyectoFinal 
En caso de fallo porque Python no funcione, podemos ejecutar el archivo 
“ImportCSVtoSQLite.java” y ocurrirá lo mismo. 
2. Las consultas pedidas se han realizado en archivos .sql 
(“b.ConsultarInformacionProyecto.sql”, “CalcularCostosSalarialesPorProyecto.sql”, 
“CombinarCostosConPresupuesto.sql” y 
“e.CalcularFraccionPresupuestoCostosSalariales.sql”). 
3. En el archivo “SQLiteJDBCFullQueries.java” se encuentran las consultas mencionadas 
previamente que se ejecutan utilizando JBDC. 
Lo ejecutamos y obtenemos lo siguiente: 
ResultadoSQLiteJDBCFullQueries.java.txt
 
4. En el archivo “HibernateSQLiteFullQueries.java” se encuentran las consultas 
mencionadas previamente que se ejecutan utilizando Hibernate. 
Lo ejecutamos y obtenemos lo siguiente: 
ResultadoHibernateSQLiteFullQueries.java.txt
 
5. Para mejorar la visualización de los datos, contamos con la clase 
“QuerySelectorGUI.java”, que, al ejecutarla, nos ofrece una GUI de Java Swing donde: 
a. Podemos seleccionar si usar JDBC o Hibernate para la consulta a realizar: 
 


![Imagen 1](./Documentos/image_page2_0.png)

![Imagen 2](./Documentos/image_page2_1.png)

![Imagen 3](./Documentos/image_page2_2.png)

# Página 3

https://github.com/FranciscoHernandezPuertas/AccesoADatosProyectoFinal 
b. Independientemente de cuál seleccionemos, nos mostrará la siguiente lista de 
consultas disponibles: 
 
c. Seleccionaremos la 3.d: Primero en JDBC, y luego en Hibernate 
En JDBC se ven los nombres de las columnas: 
 
 
 
 


![Imagen 1](./Documentos/image_page3_0.png)

![Imagen 2](./Documentos/image_page3_1.png)

# Página 4

https://github.com/FranciscoHernandezPuertas/AccesoADatosProyectoFinal 
En Hibernate no se ven los nombres de las columnas, puesto que hibernate 
trabaja con objetos y no tiene un método de acceder al nombre de las 
columnas:  
 
Documentación del código 
Importación de Datos 
a. ImportarCSVaSQLite.py 
Este script en Python utiliza pandas para leer archivos CSV y sqlite3 para insertar los datos en 
una base de datos SQLite. 
 


![Imagen 1](./Documentos/image_page4_0.png)

![Imagen 2](./Documentos/image_page4_1.jpeg)

# Página 5

https://github.com/FranciscoHernandezPuertas/AccesoADatosProyectoFinal 
b. ImportCSVtoSQLite.java 
Esta clase Java lee archivos CSV desde un directorio especíﬁco y los inserta en la base de datos 
SQLite utilizando JDBC. 
Captura del main: 
 
Captura de la sentencia SQL para inserción: 
 
 
 


![Imagen 1](./Documentos/image_page5_0.jpeg)

![Imagen 2](./Documentos/image_page5_1.jpeg)

# Página 6

https://github.com/FranciscoHernandezPuertas/AccesoADatosProyectoFinal 
Conﬁguración de Maven 
pom.xml 
Este archivo contiene las conﬁguraciones y dependencias necesarias para el proyecto, 
incluyendo Hibernate y SQLite JDBC Driver. 
Captura del bloque <dependencies> de pom.xml: 
 
Captura del bloque <repositories> de pom.xml: 
 
Conﬁguración de Hibernate: 
hibernate.cfg.xml 
Este archivo XML conﬁgura Hibernate para conectar con la base de datos SQLite y mapear las 
clases en entidades/objetos. 
Captura del bloque <session-factory> de hibernate.cfg.xml: y captura de las etiquetas 
<mapping class="..."/>: 
 


![Imagen 1](./Documentos/image_page6_0.png)

![Imagen 2](./Documentos/image_page6_1.png)

![Imagen 3](./Documentos/image_page6_2.jpeg)

# Página 7

https://github.com/FranciscoHernandezPuertas/AccesoADatosProyectoFinal 
Clases de Hibernate 
SessionFactoryProvider.java 
Esta clase proporciona una instancia única de SessionFactory. 
Captura del método provideSessionFactory() de SessionFactoryProvider.java: 
 
Consultas con JDBC y Hibernate 
SQLiteJDBCFullQueries.java 
Este script Java contiene consultas SQL directas a la base de datos SQLite para unir tablas, 
calcular costos salariales, combinar costos con presupuesto y calcular fracciones del 
presupuesto. 
Captura del bloque try donde se ejecutan las consultas (de una consulta especíﬁca joinQuery): 
 
 


![Imagen 1](./Documentos/image_page7_0.png)

![Imagen 2](./Documentos/image_page7_1.jpeg)

# Página 8

https://github.com/FranciscoHernandezPuertas/AccesoADatosProyectoFinal 
HibernateSQLiteFullQueries.java 
Esta clase Java utiliza Hibernate para realizar consultas similares a las realizadas con JDBC. 
Captura de la deﬁnición de las entidades (EmployeeProject, EmployeeRealistic, Project): 
 
  
Captura del método unirTablas(), que realiza la primera consulta (3.a) utilizando Hibernate: 
 
 
 


![Imagen 1](./Documentos/image_page8_0.png)

![Imagen 2](./Documentos/image_page8_1.jpeg)

![Imagen 3](./Documentos/image_page8_2.jpeg)

![Imagen 4](./Documentos/image_page8_3.jpeg)

# Página 9

https://github.com/FranciscoHernandezPuertas/AccesoADatosProyectoFinal 
Interfaz Gráﬁca (GUI) 
QuerySelectorGUI.java 
Esta clase Java crea una interfaz gráﬁca utilizando Java Swing que permite al usuario 
seleccionar el modo de consulta (JDBC o Hibernate) y ejecutar diferentes consultas. 
Captura del método createAndShowGUI(): 
 
 
 
 
 
 
 


![Imagen 1](./Documentos/image_page9_0.jpeg)

# Página 10

https://github.com/FranciscoHernandezPuertas/AccesoADatosProyectoFinal 
Captura del método showQueryPanel(JFrame frame, String mode): 
 


![Imagen 1](./Documentos/image_page10_0.jpeg)

# Página 11

https://github.com/FranciscoHernandezPuertas/AccesoADatosProyectoFinal 
Captura de los métodos para ejecutar Query (con JDBC o Hibernate): 
 


![Imagen 1](./Documentos/image_page11_0.png)

