package korrekturmanagement;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
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

    // --- JTA EntityManager---
    @PersistenceContext(unitName = "KorrekturMgrPU")
    private EntityManager em;

    private SqlDataKorrekturmgm neueKorrektur = new SqlDataKorrekturmgm();
    private UploadedFile datei;

    // --- Getter / Setter ---
    public SqlDataKorrekturmgm getNeueKorrektur() { return neueKorrektur; }
    public void setNeueKorrektur(SqlDataKorrekturmgm neueKorrektur) { this.neueKorrektur = neueKorrektur; }
    public UploadedFile getDatei() { return datei; }
    public void setDatei(UploadedFile datei) { this.datei = datei; }

    // --- Alle Korrekturen aus der DB ---
    public List<SqlDataKorrekturmgm> getAlleKorrekturen() {
        return em.createQuery("SELECT k FROM SqlDataKorrekturmgm k", SqlDataKorrekturmgm.class)
                 .getResultList();
    }

    // --- Speichern einer neuen Korrektur ---
    @Transactional
    public String speichern() {
        try {
            if (datei != null) {
                neueKorrektur.setDateiName(datei.getFileName());
                neueKorrektur.setPdfDokument(datei.getContent());
            }

            em.persist(neueKorrektur);

            // Formular zurücksetzen
            neueKorrektur = new SqlDataKorrekturmgm();
            datei = null;

            return "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            return null; 
        }
    }

    // --- Löschen einer Korrektur ---
    @Transactional
    public void loeschen(Long id) {
        SqlDataKorrekturmgm k = em.find(SqlDataKorrekturmgm.class, id);
        if (k != null) {
            em.remove(k);
        }
    }

    // --- Datei herunterladen ---
    public void downloadDatei(Long id) {
        SqlDataKorrekturmgm k = em.find(SqlDataKorrekturmgm.class, id);
        if (k != null && k.getPdfDokument() != null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();

            String originalName = k.getDateiName();
            if (originalName == null || originalName.isEmpty()) {
                originalName = "korrektur_" + id + ".pdf";
            }

            // Content-Type ermitteln
            String contentType = "application/octet-stream";
            if (k.getPdfDokument().length > 4 && k.getPdfDokument()[0] == 0x25 && k.getPdfDokument()[1] == 0x50) {
                contentType = "application/pdf";
            } else {
                contentType = "image/jpeg";
            }

            try (OutputStream out = externalContext.getResponseOutputStream()) {
                externalContext.setResponseContentType(contentType);
                externalContext.setResponseHeader("Content-Disposition",
                        "attachment;filename=\"" + originalName + "\"");
                out.write(k.getPdfDokument());
                out.flush();
                facesContext.responseComplete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
