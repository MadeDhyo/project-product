package project_testing.demo.model;
import java.util.*;
import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Item {

    @Id
    private String id;

    private String name;
    private double price;
    private int stock;

    @ElementCollection
    @CollectionTable(name = "item_categories", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "category")
    private List<String> categories = new ArrayList<>();

    public Item() {
        this.id = UUID.randomUUID().toString();
    }

    public Item(String name, double price, int stock, List<String> categories) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.categories = categories != null ? categories : Collections.emptyList();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public List<String> getCategories() {return categories; }

    public void setId(String id) { 
        this.id = id; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public void setPrice(double price) { 
        this.price = price; 
    }
    
    public void setStock(int stock) { 
        this.stock = stock; 
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}