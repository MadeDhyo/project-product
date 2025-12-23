package project_testing.demo.controller;

import org.springframework.web.bind.annotation.*;
import project_testing.demo.model.Brand;
import project_testing.demo.model.BrandRepository;
import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandRepository brandRepository;

    public BrandController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @PostMapping
    public Brand createBrand(@RequestBody Brand brand) {
        return brandRepository.save(brand);
    }

    @GetMapping
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }
}