package com.armi.infrastructure.adapter.in.web;

import com.armi.model.Store;
import com.armi.repository.StoreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@CrossOrigin(origins = "*")
public class StoreWebAdapter {

    private final StoreRepository storeRepository;

    public StoreWebAdapter(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @GetMapping
    public ResponseEntity<List<Store>> getAllStores() {
        return ResponseEntity.ok(storeRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Store> createStore(@RequestBody Store store) {
        return ResponseEntity.ok(storeRepository.save(store));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Store>> createStoresBulk(@RequestBody List<Store> stores) {
        return ResponseEntity.ok(storeRepository.saveAll(stores));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        storeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    @Transactional
    public ResponseEntity<Void> deleteAllStores() {
        storeRepository.truncateStores();
        return ResponseEntity.noContent().build();
    }
}
