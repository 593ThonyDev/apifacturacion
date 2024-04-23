package softech.apifacturacion.persistence.model;

import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.*;
import lombok.*;
import softech.apifacturacion.persistence.role.Role;
import softech.apifacturacion.status.UserStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements UserDetails {

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

    @Column(name = "email")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String email;

    @Column(name = "nombres")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String nombres;

    @Column(name = "apellidos")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String apellidos;

    @Column(name = "role")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Enumerated(EnumType.STRING)
    Role role;

    @Column(name = "status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Enumerated(EnumType.STRING)
    UserStatus status;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (status == UserStatus.ONLINE) {
            return true;
        }
        if (status == UserStatus.UPDATE_PASS) {
            return true;
        } else if (status == UserStatus.OFFLINE) {
            return false;
        } else {
            return false;
        }
    }

}
