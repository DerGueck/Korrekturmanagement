package korrekturmanagement;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import korrekturmanagement.model.User;

@Named("userCreationBean")
@RequestScoped
public class UserCreationBean {

    private String newUsername;
    private String newPassword;

    @PersistenceContext(unitName = "KorrekturMgrPU")
    private EntityManager em; // Container-managed JTA EntityManager

    @Transactional
    public String createUser() {
        try {
            String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            User user = new User();
            user.setUsername(newUsername);
            user.setPasswordHash(hashed);

            em.persist(user); // keine manuelle Transaktion nötig

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Testnutzer angelegt",
                    "Benutzer '" + newUsername + "' wurde gespeichert."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Fehler beim Speichern",
                    e.getMessage()));
        }
        return null;
    }

    // Getter & Setter
    public String getNewUsername() {
        return newUsername;
    }
    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
