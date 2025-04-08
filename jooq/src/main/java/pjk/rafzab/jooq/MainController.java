package pjk.rafzab.jooq;

import org.jooq.impl.QOM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.jooq.DSLContext;
import pjk.rafzab.jooq.entities.ProductEntity;
import pjk.rafzab.jooq.generated.tables.Product;
import pjk.rafzab.jooq.generated.tables.ProductRoles;
import pjk.rafzab.jooq.generated.tables.Roles;
import pjk.rafzab.jooq.generated.tables.records.ProductRecord;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.*;

@RestController
@RequestMapping("/")
public class MainController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DSLContext ctx;

    @GetMapping("/create")
    public ResponseEntity<?> create() {
        ProductEntity product = ProductEntity.builder()
                .name("Jooq")
                .description("Jooq")
                .date(Instant.now())
                .quantity(0)
                .price(BigDecimal.TEN)
                .sku("Jooq")
                .build();
        var response = productRepository.save(product);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> get() {
        var response = productRepository.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/jooq")
    public ResponseEntity<?> jooq() {
        List<ProductDTO> response = ctx.select(
                        Product.PRODUCT.ID,
                        Product.PRODUCT.DESCRIPTION,
                        Product.PRODUCT.NAME
                )
                .from(Product.PRODUCT)
                .fetch(mapping((t1, t2, t3) -> new ProductDTO(t1, t2, t3)));

        var x = ctx.selectFrom(Product.PRODUCT).fetch(r -> {
            return r.getId();
        });

        String sql = "SELECT id, name, description FROM product WHERE price > ?";
        var x2 = ctx.fetch(sql, 0)
                .map(record -> {
                    return new ProductDTO(
                            record.get("id", Long.class),
                            record.get("name", String.class),
                            record.get("description", String.class)
                    );
                });

        var result = ctx.select(
                        Product.PRODUCT.ID,
                        Product.PRODUCT.NAME,
                        Product.PRODUCT.DESCRIPTION,
                        multiset(
                                select(Roles.ROLES.ID, Roles.ROLES.NAME, Roles.ROLES.DESCRIPTION)
                                        .from(ProductRoles.PRODUCT_ROLES)
                                        .join(Roles.ROLES).on(ProductRoles.PRODUCT_ROLES.ROLE_ID.eq(Roles.ROLES.ID))
                                        .where(ProductRoles.PRODUCT_ROLES.PRODUCT_ID.eq(Product.PRODUCT.ID))
                                        .orderBy(Roles.ROLES.ID)
                        ).convertFrom(r -> r.map(mapping(RoleDTO::new)))
                )
                .from(Product.PRODUCT)
                .orderBy(Product.PRODUCT.ID)
                .fetch(mapping(ProductWithRolesDTO::new));


        var x4 = ctx.select(
                        Product.PRODUCT.ID.as("product_id"),
                        Product.PRODUCT.NAME.as("product_name"),
                        Product.PRODUCT.DESCRIPTION.as("product_description"),
                        // Użycie multiset do zagnieżdżenia ról
                        ctx.select(
                                        Roles.ROLES.ID.as("role_id"),
                                        Roles.ROLES.NAME.as("role_name"),
                                        Roles.ROLES.DESCRIPTION.as("role_description")
                                )
                                .from(ProductRoles.PRODUCT_ROLES)
                                .join(Roles.ROLES).on(ProductRoles.PRODUCT_ROLES.ROLE_ID.eq(Roles.ROLES.ID))
                                .where(ProductRoles.PRODUCT_ROLES.PRODUCT_ID.eq(Product.PRODUCT.ID))
                                .asMultiset()
                                .convertFrom(r -> r.map(record -> {

                                    return new RoleDTO(
                                            record.get("role_id", Long.class),
                                            record.get("role_name", String.class),
                                            record.get("role_description", String.class)
                                    );
                                }))
                )
                .from(Product.PRODUCT)
                .fetch();

        return ResponseEntity.ok(response);
    }

}

