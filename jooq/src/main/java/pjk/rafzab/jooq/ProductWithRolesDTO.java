package pjk.rafzab.jooq;

import java.util.List;

public record ProductWithRolesDTO(
        Long productId,
        String productName,
        String productDescription,
        List<RoleDTO> roles
) {
}