package project_testing.demo.controller;

import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;
import project_testing.demo.model.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Products", description = "Products CRUD API")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ItemRepository itemRepository;

    public ProductController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // 1. READ ALL ITEMS (WITH PAGINATION & BASIC FILTERING)
    // URL: GET http://localhost:8080/api/v1/products?page=0&size=10
    @Operation(summary = "Get all products", description = "Fetch all products with pagination")
    @GetMapping
    public ItemListResponse readAllItems(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(required = false) String category,
    @RequestParam(required = false) Long brandId 
) {
    List<Item> results;
        if (brandId != null) {
            results = itemRepository.findByBrandId(brandId);
            return new ItemListResponse(results.size(), results);
        } 
        if (category != null && !category.isEmpty()) {
            results = itemRepository.findByCategoriesContainingIgnoreCase(category);
            return new ItemListResponse(results.size(), results);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> pageResult = itemRepository.findAll(pageable);
        return new ItemListResponse((int) pageResult.getTotalElements(), pageResult.getContent());
    }

    // 2. FILTER ITEMS (ADVANCED SEARCH BY NAME, CATEGORY, OR BRAND)
    // URL: GET http://localhost:8080/api/v1/products/filter?name=KTM
    @Operation(summary = "Filter products", description = "Filter items by brand name or category")
    @GetMapping("/filter")
    public ItemListResponse filterItems(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) Long brandId,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(required = false) Double minPrice
    ) {
        List<Item> results;
        
        if (maxPrice != null) {
        results = itemRepository.findByPriceLessThanEqual(maxPrice);
        } else if (minPrice != null) {
        results = itemRepository.findByPriceGreaterThanEqual(minPrice);
        } else if (name != null && !name.trim().isEmpty()) {
            results = itemRepository.findByNameContainingIgnoreCase(name);
        } else if (brandId != null) {
            results = itemRepository.findByBrandId(brandId);
        } else if (category != null && !category.trim().isEmpty()) {
            results = itemRepository.findByCategoriesContainingIgnoreCase(category);
        } else {
            results = itemRepository.findAll();
        }
        
        return new ItemListResponse(results.size(), results);
    }

    // 3. READ SINGLE ITEM BY ID
    // URL: GET http://localhost:8080/api/v1/products/{uuid}
    @GetMapping("/{id}")
    public ResponseEntity<Item> readOneItem(@PathVariable String id) {
        return itemRepository.findById(id)
            .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 4. CREATE SINGLE ITEM
    // URL: POST http://localhost:8080/api/v1/products
    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item newItem) {
        Item saved = itemRepository.save(newItem);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // 5. CREATE BULK ITEMS
    // URL: POST http://localhost:8080/api/v1/products/bulk
    @Operation(summary = "Upload Bulk Products", description = "API for uploading multiple products at once")
    @PostMapping("/bulk")
    public ResponseEntity<List<Item>> createBulkItems(@RequestBody List<Item> newItems) {
        List<Item> savedItems = itemRepository.saveAll(newItems);
        return new ResponseEntity<>(savedItems, HttpStatus.CREATED);
    }

    // 6. UPDATE ITEM NAME
    // URL: PUT http://localhost:8080/api/v1/products/{uuid}
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItemName(@PathVariable String id, @RequestBody Map<String, String> updates) {
        return itemRepository.findById(id).map(existingItem -> {
            if (updates.containsKey("name")) {
                existingItem.setName(updates.get("name"));
            }
            Item saved = itemRepository.save(existingItem);
            return new ResponseEntity<>(saved, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 7. DELETE SINGLE ITEM
    // URL: DELETE http://localhost:8080/api/v1/products/{uuid}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable String id) {
        try {
            return itemRepository.findById(id).map(item -> {
                itemRepository.delete(item);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            e.printStackTrace(); 
            return new ResponseEntity<>("Database Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }   
    }

    // 8. DELETE BULK ITEMS
    // URL: DELETE http://localhost:8080/api/v1/products/bulk
    // Body (JSON): ["uuid-1", "uuid-2"]
    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteBulkItems(@RequestBody List<String> ids) {
        itemRepository.deleteAllById(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}