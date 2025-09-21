package korrekturmanagement;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import korrekturmanagement.model.SqlDataKorrekturmgm;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class KorrekturTabelleBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "KorrekturMgrPU")
    private EntityManager em;

    private List<SqlDataKorrekturmgm> alleKorrekturen;

    // --- Getter für die Tabelle ---
    public List<SqlDataKorrekturmgm> getAlleKorrekturen() {
        if (alleKorrekturen == null) {
            alleKorrekturen = em.createQuery("SELECT k FROM SqlDataKorrekturmgm k", SqlDataKorrekturmgm.class)
                                .getResultList();
        }
        return alleKorrekturen;
    }

    // --- Update Status ---
    @Transactional
    public void updateStatus(SqlDataKorrekturmgm korrektur) {
        SqlDataKorrekturmgm managed = em.find(SqlDataKorrekturmgm.class, korrektur.getId());
        if (managed != null) {
            managed.setBearbeitungsStatus(korrektur.getBearbeitungsStatus());
            em.merge(managed);
        }
        alleKorrekturen = null; // Tabelle neu laden
    }

    // --- Löschen eines Eintrags ---
    @Transactional
    public void deleteKorrektur(SqlDataKorrekturmgm korrektur) {
        SqlDataKorrekturmgm managed = em.find(SqlDataKorrekturmgm.class, korrektur.getId());
        if (managed != null) {
            em.remove(managed);
        }
        alleKorrekturen = null; // Tabelle neu laden
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
