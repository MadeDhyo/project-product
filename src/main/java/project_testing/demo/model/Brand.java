package project_testing.demo.model;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

// 1. DATABASE ENTITY MAPPING
// Marks this class as a JPA entity and maps it to the "brands" table in MySQL
@Entity
@Table(name = "brands")
public class Brand {
    
    // 2. PRIMARY KEY CONFIGURATION
    // Uses auto-increment (IDENTITY) for the numeric brand ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 3. BRAND ATTRIBUTES
    // Basic field to store the name of the motorcycle manufacturer
    private String name;

    // 4. RELATIONAL MAPPING (ONE-TO-MANY)
    // Links one brand to multiple items; 'cascade' ensures items are managed with the brand
    // @JsonIgnore prevents infinite loops when converting the brand data to JSON
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Item> items;

    // 5. CONSTRUCTORS
    // Default constructor required by JPA for entity instantiation
    public Brand() {}

    // 6. GETTERS AND SETTERS
    // Methods to access and modify the brand's data fields
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
}