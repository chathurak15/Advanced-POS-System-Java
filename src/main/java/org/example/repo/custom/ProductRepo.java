package org.example.repo.custom;

import org.example.entity.Product;
import org.example.repo.CrudRepository;

public interface ProductRepo extends CrudRepository<Product, Integer> {
}
