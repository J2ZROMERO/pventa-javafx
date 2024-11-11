package com.j2zromero.pointofsale.services.brand;


import com.j2zromero.pointofsale.models.brands.Brand;
import com.j2zromero.pointofsale.repositories.brand.BrandRepository;
import java.sql.SQLException;
import java.util.List;

public class BrandService {
    private BrandRepository brandRepository = new BrandRepository();

    public void add(Brand brand) throws SQLException {
        // Business logic can be added here
        brandRepository.add(brand);
    }

    public List<Brand> getAll() throws SQLException {
        return brandRepository.getAll();
    }

    public void update(Brand brand) throws SQLException {
        brandRepository.update(brand);
    }

    public void delete(int id) throws SQLException {
        brandRepository.delete(id);
    }
}