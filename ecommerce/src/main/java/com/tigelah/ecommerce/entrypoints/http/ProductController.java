package com.tigelah.ecommerce.entrypoints.http;

import com.tigelah.ecommerce.application.product.command.CreateProductCmd;
import com.tigelah.ecommerce.application.product.command.UpdateProductCmd;
import com.tigelah.ecommerce.application.product.dto.ProductDTO;
import com.tigelah.ecommerce.application.product.query.ProductSearchQuery;
import com.tigelah.ecommerce.application.product.usecase.*;
import com.tigelah.ecommerce.commons.PageResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

  private final CreateProductUseCase createUC;
  private final UpdateProductUseCase updateUC;
  private final DeleteProductUseCase deleteUC;
  private final GetProductUseCase getUC;
  private final SearchProductsUseCase searchUC;

  public ProductController(CreateProductUseCase c, UpdateProductUseCase u, DeleteProductUseCase d, GetProductUseCase g, SearchProductsUseCase s){
    this.createUC = c;
    this.updateUC = u;
    this.deleteUC = d;
    this.getUC = g;
    this.searchUC = s;
  }

  @GetMapping("/search")
  public ResponseEntity<PageResult<ProductDTO>> search(
    @RequestParam(required = false) String name,
    @RequestParam(required = false) String category,
    @RequestParam(required = false) BigDecimal minPrice,
    @RequestParam(required = false) BigDecimal maxPrice,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
  ) {
    if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
      return ResponseEntity.badRequest().build();
    }

    var q = new ProductSearchQuery(name, category, minPrice, maxPrice, page, size);
    return ResponseEntity.ok(searchUC.handle(q));
  }

  public record CreateRequest(
    @NotBlank String name,
    String description,
    @NotNull @DecimalMin(value = "0.00") BigDecimal price,
    String category,
    @NotNull @PositiveOrZero BigInteger stock
  ){}

  public record UpdateRequest(
    @NotBlank String name,
    String description,
    @NotNull @DecimalMin(value = "0.00") BigDecimal price,
    String category,
    @NotNull @PositiveOrZero BigInteger stock
  ){}

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<UUID> create(@Valid @RequestBody CreateRequest req){
    var id = createUC.handle(new CreateProductCmd(req.name(), req.description(), req.price(), req.category(), req.stock()));
    return ResponseEntity.ok(id);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDTO> get(@PathVariable UUID id){
    return ResponseEntity.ok(getUC.handle(id));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody UpdateRequest req){
    updateUC.handle(id, new UpdateProductCmd(req.name(), req.description(), req.price(), req.category(), req.stock()));
    return ResponseEntity.noContent().build();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id){
    deleteUC.handle(id);
    return ResponseEntity.noContent().build();
  }
}
