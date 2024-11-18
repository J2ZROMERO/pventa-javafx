package com.j2zromero.pointofsale.services.product;

import com.j2zromero.pointofsale.models.products.Product;
import com.j2zromero.pointofsale.repositories.product.ProductRepository;
import com.j2zromero.pointofsale.utils.UnitType;

import java.sql.SQLException;
import java.util.List;

public class ProductService {

    private ProductRepository productRepository = new ProductRepository();

    // Método para agregar un producto
    public void add(Product product) throws SQLException {
        // Aquí puedes agregar lógica de negocio antes de la inserción
        productRepository.add(product);
    }

    // Método para obtener todos los productos
    public List<Product> getAll() throws SQLException {
        return productRepository.getAll();
    }

    // Método para actualizar un producto
    public void update(Product product) throws SQLException {
        productRepository.update(product);
    }

    // Método para eliminar un producto por ID
    public void delete(Long id) throws SQLException {
        productRepository.delete(id);
    }

    // Método para Obtener todos las unidades de medida
    public List<UnitType> getMeasurementTypes() throws SQLException {
        return productRepository.getMeasurementTypes();

    }

    // Method to get a product by ID
    public Product getByCode(String code) throws SQLException {
        return productRepository.getById(code);
    }

}