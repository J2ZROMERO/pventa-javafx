package com.j2zromero.pointofsale.services.category;

import com.j2zromero.pointofsale.models.categories.Category;
import com.j2zromero.pointofsale.repositories.category.CategoryRepository;

import java.sql.SQLException;
import java.util.List;

public class CategoryService {
    private CategoryRepository repository = new CategoryRepository();

    public boolean add(Category category) throws SQLException {
        return repository.add(category);
    }

    public List<Category> getAll() throws SQLException {
        return repository.getAll();
    }

    public void update(Category category) throws SQLException {
        repository.update(category);
    }

    public void delete(long id) throws SQLException {
        repository.delete(id);
    }
}