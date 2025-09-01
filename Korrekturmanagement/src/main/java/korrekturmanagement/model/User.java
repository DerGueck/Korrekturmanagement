package korrekturmanagement.model;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    

	@Column(unique = true, nullable = false)
    private String username;

    @Column(name="password_hash", nullable = false)
    private String passwordHash;

   

 // Getter & Setter

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}	
   
}
