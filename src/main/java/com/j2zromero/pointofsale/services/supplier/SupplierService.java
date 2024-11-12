package com.j2zromero.pointofsale.services.supplier;
import com.j2zromero.pointofsale.models.suppliers.Supplier;
import com.j2zromero.pointofsale.repositories.suppliers.SupplierRepository;

import java.sql.SQLException;
import java.util.List;

public class SupplierService {

    private SupplierRepository supplierRepository = new SupplierRepository();

    // Método para agregar un proveedor
    public void add(Supplier supplier) throws SQLException {
        // Aquí puedes agregar lógica de negocio antes de la inserción
        supplierRepository.add(supplier);
    }

    // Método para obtener todos los proveedores
    public List<Supplier> getAll() throws SQLException {
        return supplierRepository.getAll();
    }

    // Método para actualizar un proveedor
    public void update(Supplier supplier) throws SQLException {
        supplierRepository.update(supplier);
    }

    // Método para eliminar un proveedor por ID
    public void delete(int id) throws SQLException {
        supplierRepository.delete(id);
    }
}