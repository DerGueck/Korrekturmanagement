package korrekturmanagement;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ExternalContext;
import korrekturmanagement.model.SqlDataKorrekturmgm;
import org.primefaces.model.file.UploadedFile;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class KorrekturBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("KorrekturMgrPU");

    private SqlDataKorrekturmgm neueKorrektur = new SqlDataKorrekturmgm();
    private UploadedFile datei;

    // --- Getter / Setter ---
    public SqlDataKorrekturmgm getNeueKorrektur() { return neueKorrektur; }
    public void setNeueKorrektur(SqlDataKorrekturmgm neueKorrektur) { this.neueKorrektur = neueKorrektur; }
    public UploadedFile getDatei() { return datei; }
    public void setDatei(UploadedFile datei) { this.datei = datei; }

    // --- Alle Korrekturen aus DB ---
    public List<SqlDataKorrekturmgm> getAlleKorrekturen() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT k FROM SqlDataKorrekturmgm k", SqlDataKorrekturmgm.class)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    // --- Speichern einer neuen Korrektur ---
    public String speichern() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            if (datei != null) {
                String name = datei.getFileName();
                neueKorrektur.setDateiName(name);

                // Direkt in byte[]
                neueKorrektur.setPdfDokument(datei.getContent());
            }

            em.persist(neueKorrektur);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        // Formular zurücksetzen
        neueKorrektur = new SqlDataKorrekturmgm();
        datei = null;

        return "index?faces-redirect=true";
    }

    // --- Löschen ---
    public void loeschen(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            SqlDataKorrekturmgm k = em.find(SqlDataKorrekturmgm.class, id);
            if (k != null) em.remove(k);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // --- Download ---
    public void downloadDatei(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            SqlDataKorrekturmgm k = em.find(SqlDataKorrekturmgm.class, id);
            if (k != null && k.getPdfDokument() != null) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext externalContext = facesContext.getExternalContext();

                String originalName = k.getDateiName();
                if (originalName == null || originalName.isEmpty()) {
                    originalName = "korrektur_" + id + ".pdf";
                }

                String contentType = "application/octet-stream";
                if (k.getPdfDokument().length > 4 && k.getPdfDokument()[0] == 0x25 && k.getPdfDokument()[1] == 0x50) {
                    contentType = "application/pdf";
                } else {
                    contentType = "image/jpeg";
                }

                externalContext.setResponseContentType(contentType);
                externalContext.setResponseHeader("Content-Disposition",
                        "attachment;filename=\"" + originalName + "\"");

                OutputStream out = externalContext.getResponseOutputStream();
                out.write(k.getPdfDokument());
                out.flush();
                facesContext.responseComplete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
