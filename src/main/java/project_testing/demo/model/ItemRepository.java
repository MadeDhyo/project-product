package project_testing.demo.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {

    List<Item> findByCategoriesContainingIgnoreCase(String category);

    List<Item> findByNameContainingIgnoreCase(String name);

    List<Item> findByBrandNameIgnoreCase(String brandName);

    List<Item> findByBrandId(Long brandId);

    // 1. Find items with price less than or equal to the provided value
    List<Item> findByPriceLessThanEqual(double price);

    // 2. Find items with price greater than or equal to the provided value
    List<Item> findByPriceGreaterThanEqual(double price);
}