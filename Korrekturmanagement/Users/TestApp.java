package korrekturmanagement;

import jakarta.persistence.*;
import korrekturmanagement.model.SqlDataKorrekturmgm;

public class TestApp {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KorrekturMgrPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        SqlDataKorrekturmgm korrektur = new SqlDataKorrekturmgm("Testtext", "Max Mustermann");
        em.persist(korrektur);
        em.getTransaction().commit();

        System.out.println("Gespeicherte ID: " + korrektur.getId());

        em.close();
        emf.close();
    }
}
