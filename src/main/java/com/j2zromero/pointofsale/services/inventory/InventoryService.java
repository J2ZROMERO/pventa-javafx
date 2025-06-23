package com.j2zromero.pointofsale.services.inventory;

import com.j2zromero.pointofsale.models.inventories.Inventory;
import com.j2zromero.pointofsale.repositories.inventory.InventoryRepository;

import java.sql.SQLException;
import java.util.List;

public class InventoryService {

    private InventoryRepository inventoryRepository = new InventoryRepository();

    // Method to add an inventory record
    public boolean add(Inventory inventory) throws SQLException {
        // You can add business logic here before calling the repository
        return inventoryRepository.add(inventory);
    }

    // Method to get all inventory records
    public List<Inventory> getAll() throws SQLException {
        return inventoryRepository.getAll();
    }

    // Method to update an inventory record
    public void update(Inventory inventory) throws SQLException {
        inventoryRepository.update(inventory);
    }

    // Method to delete an inventory record by ID
    public void delete(long id) throws SQLException {
        inventoryRepository.delete(id);
    }

    public Inventory getInventoryByProductCode(String productCode) throws SQLException {
        return  inventoryRepository.getInventorByProductCode(productCode);
    }
}
