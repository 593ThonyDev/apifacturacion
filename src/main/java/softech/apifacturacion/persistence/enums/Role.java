package softech.apifacturacion.persistence.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMINISTRADOR,
    EMISOR,
    CAJERO;

    @Override
    public String getAuthority() {
        return this.name();
    }

}
