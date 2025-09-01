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
            System.out.println("Benutzer gefunden: " + user.getUsername());
            System.out.println("Gespeicherter Passwort-Hash: " + user.getPasswordHash());
            System.out.println("Eingegebenes Passwort: " + password);

            boolean match = BCrypt.checkpw(password, user.getPasswordHash());
            System.out.println("Passwortprüfung erfolgreich? " + match);

            if (match) {
                return user;
            }
        } else {
            System.out.println("Kein Benutzer mit diesem Namen gefunden.");
        }

        return null;
    }

    
}
