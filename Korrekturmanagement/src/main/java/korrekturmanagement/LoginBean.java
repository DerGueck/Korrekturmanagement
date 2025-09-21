package korrekturmanagement;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import korrekturmanagement.model.User;


@Named
@SessionScoped
public class LoginBean implements Serializable {

    private String username;
    private String password;
    private User loggedInUser;

    @Inject
    private LoginService loginService;

    public String login() {    	
        
        loggedInUser = loginService.authenticate(username, password);
        if (loggedInUser != null) {
            return "backend.xhtml?faces-redirect=true";
        } else {        	
        	
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login fehlgeschlagen", "Benutzername oder Passwort ist ungültig"));
            return null;
        }         
        

    }
    

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index.xhtml?faces-redirect=true";
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }    

    // Getter & Setter
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public User getLoggedInUser() {
        return loggedInUser;
    }
}
