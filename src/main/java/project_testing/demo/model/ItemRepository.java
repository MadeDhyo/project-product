package project_testing.demo.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, String> {

    List<Item> findByCategoriesContainingIgnoreCase(String category);
}

