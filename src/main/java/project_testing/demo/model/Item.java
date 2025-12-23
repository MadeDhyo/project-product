package project_testing.demo.model;

import java.util.*;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

// 1. DATABASE ENTITY MAPPING
// Defines this class as a JPA entity and maps it to the "items" table in the database
@Entity
@Table(name = "items")
public class Item {

    // 2. PRIMARY KEY WITH UUID GENERATION
    // Uses Hibernate 6's modern UUID strategy to generate a unique string-based ID for each product
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) 
    private String id;

    // 3. PRODUCT ATTRIBUTES
    // Basic fields for storing the name, price, and current stock levels of the motorcycle
    private String name;
    private double price;
    private int stock;

    // 4. COLLECTION MAPPING (MULTI-VALUE ATTRIBUTE)
    // Stores a list of category tags in a separate "item_categories" table linked by item_id
    // @OnDelete(action = OnDeleteAction.CASCADE) ensures child categories are deleted when the item is removed
    @ElementCollection
    @CollectionTable(name = "item_categories", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "category")
    @OnDelete(action = OnDeleteAction.CASCADE) 
    private List<String> categories = new ArrayList<>();

    // 5. RELATIONAL MAPPING (MANY-TO-ONE)
    // Links many items to a single brand record using the "brand_id" foreign key
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    // 6. CONSTRUCTORS
    // Includes a default constructor for JPA and a parameterized one for easier object creation
    public Item() {}

    public Item(String name, double price, int stock, List<String> categories, Brand brand) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.categories = categories != null ? categories : new ArrayList<>();
        this.brand = brand;
    }

    // 7. GETTERS AND SETTERS
    // Standard methods to retrieve and modify the object's properties
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }

    public Brand getBrand() { return brand; }
    public void setBrand(Brand brand) { this.brand = brand; }
}