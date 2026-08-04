package com.armi.repository;

import com.armi.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StoreRepository extends JpaRepository<Store, Long> {
    @Modifying
    @Query(value = "DELETE FROM stores", nativeQuery = true)
    void truncateStores();
}
