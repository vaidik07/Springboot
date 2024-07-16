package com.example.Ecom.controllers;

import com.example.Ecom.Services.ItemService;
import com.example.Ecom.model.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
public class itemsController {
    @Autowired
    private ItemService itemsService;

    @GetMapping
    public ResponseEntity<List<Items>> getAllItems() {
        return ResponseEntity.ok(itemsService.getAllItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Items> getItemById(@PathVariable String id) {
        Optional<Items> item = itemsService.getItemById(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Items> createItem(@RequestBody Items item) {
        return ResponseEntity.ok(itemsService.saveItems(item));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Items> updateItem(@PathVariable String id, @RequestBody Items item) {
        Optional<Items> existingItem = itemsService.getItemById(id);
        if (existingItem.isPresent()) {
            item.setId(id);
            return ResponseEntity.ok(itemsService.saveItems(item));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        itemsService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
