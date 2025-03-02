package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.example.models.Product;
import java.util.List;

public class DashboardController {
    @FXML private ListView<String> productList;

    @FXML
    public void initialize() {
        loadProducts();
    }

    private void loadProducts() {
        List<Product> products = Database.getProducts();
        for (Product product : products) {
            productList.getItems().add(product.getName() + " - $" + product.getPrice());
        }
    }
}

