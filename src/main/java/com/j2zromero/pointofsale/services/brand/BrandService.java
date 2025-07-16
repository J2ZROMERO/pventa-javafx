package com.j2zromero.pointofsale.services.brand;

import com.j2zromero.pointofsale.models.brands.Brand;
import com.j2zromero.pointofsale.repositories.brand.BrandRepository;

import java.sql.SQLException;
import java.util.List;

public class BrandService {
    private BrandRepository repository = new BrandRepository();

    public boolean add(Brand brand) throws SQLException {
        return repository.add(brand);
    }

    public List<Brand> getAll() throws SQLException {
        return repository.getAll();
    }

    public void update(Brand brand) throws SQLException {
        repository.update(brand);
    }

    public void delete(long id) throws SQLException {
        repository.delete(id);
    }
}