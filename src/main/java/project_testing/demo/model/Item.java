package project_testing.demo.model;

import java.util.*;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String name;
    private double price;
    private int stock;

    @ElementCollection
    @CollectionTable(name = "item_categories", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "category")
    private List<String> categories = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    public Item() {}

    public Item(String name, double price, int stock, List<String> categories, Brand brand) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.categories = categories != null ? categories : Collections.emptyList();
        this.brand = brand;
    }

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