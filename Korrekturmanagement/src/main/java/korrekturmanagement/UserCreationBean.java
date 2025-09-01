package korrekturmanagement;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.mindrot.jbcrypt.BCrypt;
import korrekturmanagement.model.User;

@Named("userCreationBean")
@RequestScoped
public class UserCreationBean {

    private String newUsername;
    private String newPassword;

    private EntityManager em;

    public UserCreationBean() {
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("KorrekturMgrPU");;
        em = emf.createEntityManager();
    }

    public String createUser() {
        try {
            em.getTransaction().begin();

            String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            User user = new User();
            user.setUsername(newUsername);
            user.setPasswordHash(hashed);

            em.persist(user);
            em.getTransaction().commit();

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Testnutzer angelegt", "Benutzer '" + newUsername + "' wurde gespeichert."));
        } catch (Exception e) {
            em.getTransaction().rollback();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler beim Speichern", e.getMessage()));
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
