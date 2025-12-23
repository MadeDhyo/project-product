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

    @Operation(summary = "Get all products", description = "Fetch all products with pagination")
    @GetMapping
    public ItemListResponse readAllItems(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> pageResult = itemRepository.findAll(pageable);
        return new ItemListResponse((int) pageResult.getTotalElements(), pageResult.getContent());
    }

    @Operation(summary = "Filter products", description = "Filter items by brand name or category")
    @GetMapping("/filter")
    public ItemListResponse filterItems(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String brand
    ) {
        List<Item> results;
        if (brand != null && !brand.isEmpty()) {
            results = itemRepository.findByBrandNameIgnoreCase(brand);
        } else if (category != null && !category.isEmpty()) {
            results = itemRepository.findByCategoriesContainingIgnoreCase(category);
        } else {
            results = itemRepository.findAll();
        }
        return new ItemListResponse(results.size(), results);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> readOneItem(@PathVariable String id) {
        return itemRepository.findById(id)
            .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item newItem) {
        Item saved = itemRepository.save(newItem);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Operation(summary = "Upload Bulk Products", description = "API for uploading multiple products at once")
    @PostMapping("/bulk")
    public ResponseEntity<List<Item>> createBulkItems(@RequestBody List<Item> newItems) {
        List<Item> savedItems = itemRepository.saveAll(newItems);
        return new ResponseEntity<>(savedItems, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItemName(@PathVariable String id, @RequestBody Map<String, String> updates) {
        return itemRepository.findById(id).map(existingItem -> {
            // Extract "name" from the request body
            if (updates.containsKey("name")) {
                existingItem.setName(updates.get("name"));
            }
            
            // Save the updated entity back to the database
            Item saved = itemRepository.save(existingItem);
            return new ResponseEntity<>(saved, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Item> deleteItem(@PathVariable String id) {
        return itemRepository.findById(id).map(item -> {
            itemRepository.delete(item);
            return new ResponseEntity<>(item, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteBulkItems(@RequestBody List<String> ids) {
        itemRepository.deleteAllById(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}