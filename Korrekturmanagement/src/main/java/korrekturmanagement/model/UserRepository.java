package korrekturmanagement.model;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@ApplicationScoped
public class UserRepository {

    private EntityManager em;

    public UserRepository() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KorrekturMgrPU");
        em = emf.createEntityManager();
    }

    public User findByUsername(String username) {
        System.out.println("Benutzersuche gestartet für: '" + username + "'");
        if (em == null) {
            System.out.println("⚠️ EntityManager ist NULL – Datenbankverbindung nicht verfügbar!");
            return null;
        }

        try {
            return em.createQuery(
                    "SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username)", User.class)
                    .setParameter("username", username.trim())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            System.out.println("❌ Fehler bei der Benutzersuche: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
