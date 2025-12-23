package project_testing.demo.controller;

import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import project_testing.demo.model.Item;


@RestController
@RequestMapping("/api/items")
public class HelloController {
    // 1. IN-MEMORY DATA STORAGE
    // Temporary list used to store items in RAM while the application is running
    private final List<Item> items = new ArrayList<>();

    // 2. READ ALL ITEMS
    // URL: GET http://localhost:8080/api/items
    // Returns the entire list of temporary items
    @GetMapping
    public List<Item> readAllItems() {
        return items;
    }

    // 3. CREATE ITEM
    // URL: POST http://localhost:8080/api/items
    // Adds a new item to the in-memory list and returns a success message
    @PostMapping
    public ResponseEntity<String> createItem(@RequestBody Item newItem) {
        items.add(newItem);
        return new ResponseEntity<>("Created item: " + newItem, HttpStatus.CREATED);
    }

    // 4. READ ONE ITEM BY ID
    // URL: GET http://localhost:8080/api/items/{id}
    // Searches the list for an item matching the provided ID
    @GetMapping("/{id}")
    public ResponseEntity<Item> readOneItem(@PathVariable String id) {
        for (Item item : items) {
            if (item.getId().equals(id)) {
                return new ResponseEntity<>(item, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // 5. UPDATE ITEM
    // URL: PUT http://localhost:8080/api/items/{id}
    // Replaces an existing item in the list with updated data
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable String id, @RequestBody Item updatedItem) {
        for (int i = 0; i < items.size(); i++) {
            Item existingItem = items.get(i);
            if (existingItem.getId().equals(id)) {
                updatedItem.setId(id);
                items.set(i, updatedItem);
                return new ResponseEntity<>(items.get(i), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // 6. DELETE ITEM
    // URL: DELETE http://localhost:8080/api/items/{id}
    // Removes the specified item from the in-memory list
    @DeleteMapping("/{id}")
    public ResponseEntity<Item> deleteItem(@PathVariable String id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(id)) {
                Item deletedItem = items.remove(i); 
                return new ResponseEntity<>(deletedItem, HttpStatus.OK);
            }
        }
    
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
}