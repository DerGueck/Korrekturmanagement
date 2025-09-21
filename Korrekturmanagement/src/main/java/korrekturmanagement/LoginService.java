package korrekturmanagement;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import korrekturmanagement.model.User;
import korrekturmanagement.model.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class LoginService {

    @Inject
    private UserRepository userRepository;

    public User authenticate(String username, String password) {
        System.out.println("Authentifizierung gestartet für Benutzer: " + username);

        User user = userRepository.findByUsername(username);
        if (user != null) { 
        	boolean match = BCrypt.checkpw(password, user.getPasswordHash());            

            if (match) {
                return user;
            }
        } 

        return null;
    }

    
}
