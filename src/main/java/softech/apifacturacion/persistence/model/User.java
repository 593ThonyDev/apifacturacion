package softech.apifacturacion.persistence.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.*;
import lombok.*;
import softech.apifacturacion.persistence.enums.Role;
import softech.apifacturacion.persistence.enums.UserStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "iduser")
    Integer idUser;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    @JoinColumn(name = "fkemisor")
    Emisor fkEmisor;

    @Basic
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "username", nullable = false)
    String username;

    @Column(name = "password")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String password;
    
    @Column(name = "login")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String login;

    @Column(name = "email")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String email;

    @Column(name = "nombres")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String nombres;

    @Column(name = "apellidos")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String apellidos;

    @Column(name = "role", length = 50)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @Column(name = "status", length = 50)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    

}
