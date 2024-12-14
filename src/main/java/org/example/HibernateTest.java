package org.example;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateTest {
    public static void main(String[] args) {
        // Configuración básica de Hibernate
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // Busca hibernate.cfg.xml en el classpath
                .build();
        try (SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory()) {
            System.out.println("¡Hibernate configurado correctamente!");
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            e.printStackTrace();
        }
    }
}
