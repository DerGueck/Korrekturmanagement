package korrekturmanagement;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import korrekturmanagement.model.SqlDataKorrekturmgm;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class KorrekturTabelleBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("KorrekturMgrPU");

    private List<SqlDataKorrekturmgm> alleKorrekturen;

    // --- Getter für die Tabelle ---
    public List<SqlDataKorrekturmgm> getAlleKorrekturen() {
        if (alleKorrekturen == null) {
            EntityManager em = emf.createEntityManager();
            try {
                alleKorrekturen = em.createQuery("SELECT k FROM SqlDataKorrekturmgm k", SqlDataKorrekturmgm.class)
                        .getResultList();
            } finally {
                em.close();
            }
        }
        return alleKorrekturen;
    }

    // --- Update Status ---
    public void updateStatus(SqlDataKorrekturmgm korrektur) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            SqlDataKorrekturmgm managed = em.find(SqlDataKorrekturmgm.class, korrektur.getId());
            managed.setBearbeitungsStatus(korrektur.getBearbeitungsStatus());
            em.merge(managed);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        // Tabelle neu laden
        alleKorrekturen = null;
    }

    // --- Löschen eines Eintrags ---
    public void deleteKorrektur(SqlDataKorrekturmgm korrektur) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            SqlDataKorrekturmgm managed = em.find(SqlDataKorrekturmgm.class, korrektur.getId());
            if (managed != null) {
                em.remove(managed);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        // Tabelle neu laden
        alleKorrekturen = null;
    }

    // --- Virtuelle Sortierwerte ---
    public String getErrorTextForSort(SqlDataKorrekturmgm k) {
        return k.getErrorText() == null ? "" : k.getErrorText();
    }

    public String getKategorieForSort(SqlDataKorrekturmgm k) {
        return k.getKategorie() == null ? "" : k.getKategorie();
    }

    public String getPriorisierungForSort(SqlDataKorrekturmgm k) {
        return k.getPriorisierung() == null ? "" : k.getPriorisierung();
    }

    public String getMedienbezugForSort(SqlDataKorrekturmgm k) {
        return k.getMedienbezug() == null ? "" : k.getMedienbezug();
    }

    public String getBearbeitungsStatusForSort(SqlDataKorrekturmgm k) {
        return k.getBearbeitungsStatus() == null ? "" : k.getBearbeitungsStatus();
    }
}
