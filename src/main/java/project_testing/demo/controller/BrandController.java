package project_testing.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project_testing.demo.model.Brand;
import project_testing.demo.model.BrandRepository;
import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandRepository brandRepository;

    // Dependency injection to access the brand database repository
    public BrandController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    // 1. CREATE SINGLE BRAND
    // URL: POST http://localhost:8080/api/brands
    // Saves a new manufacturer entry into the brands table
    @PostMapping
    public Brand createBrand(@RequestBody Brand brand) {
        return brandRepository.save(brand);
    }

    // 2. READ ALL BRANDS
    // URL: GET http://localhost:8080/api/brands
    // Retrieves a complete list of all motorcycle manufacturers
    @GetMapping
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    // 3. DELETE SINGLE BRAND
    // URL: DELETE http://localhost:8080/api/brands/{id}
    // Removes one brand by its numeric ID; returns 204 No Content if successful
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        return brandRepository.findById(id).map(brand -> {
            brandRepository.delete(brand);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 4. DELETE BULK BRANDS
    // URL: DELETE http://localhost:8080/api/brands/bulk
    // Body: [1, 2, 3]
    // Removes multiple brand records using an array of numeric IDs
    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteBulkBrands(@RequestBody List<Long> ids) {
        brandRepository.deleteAllById(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}