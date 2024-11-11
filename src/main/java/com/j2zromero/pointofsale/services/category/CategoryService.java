package com.j2zromero.pointofsale.services.category;
import com.j2zromero.pointofsale.models.categories.Category;
import com.j2zromero.pointofsale.repositories.category.CategoryRepository;
import java.sql.SQLException;
import java.util.List;

public class CategoryService {
    private CategoryRepository categoryRepository = new CategoryRepository();

    public void add(Category category) throws SQLException {
        // Business logic can be added here
        categoryRepository.add(category);
    }

    public List<Category> getAll() throws SQLException {
        return categoryRepository.getAll();
    }

    public void update(Category category) throws SQLException {
        categoryRepository.update(category);
    }

    public void delete(int id) throws SQLException {
        categoryRepository.delete(id);
    }
}