package org.example.service.custom.impl;

import org.example.dto.ProductDTO;
import org.example.dto.UserDTO;
import org.example.entity.Product;
import org.example.entity.User;
import org.example.repo.custom.ProductRepo;
import org.example.repo.custom.impl.ProductRepoIMPL;
import org.example.repo.custom.impl.UserRepoIMPL;
import org.example.service.custom.ProductService;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductServiceIMPL implements ProductService {

    private ProductRepo productRepo = new ProductRepoIMPL();
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public String save(ProductDTO productDTO) {
        try {
            productRepo.save(convertDTOToEntity(productDTO));
            return "Product saved";
        }catch (SQLException e){
            return "Sql Error";

        } catch (Exception e) {
            return "something went wrong";

        }
    }

    @Override
    public String update(ProductDTO productDTO) {
        return "";
    }

    @Override
    public ProductDTO search(Integer integer) {
        return null;
    }

    @Override
    public List<ProductDTO> getAll() {
        try {
            List<Product> all = productRepo.getAll();
            List<ProductDTO> ProductDTOs = new ArrayList<ProductDTO>();

            if (all != null) {
                for (Product product : all) {
                    ProductDTOs.add(convertEntityToDTO(product));
                }
                return ProductDTOs;
            }else {
                throw new RuntimeException("product not found");
            }

        } catch (Exception e) {
            throw new RuntimeException("Contact Develloper", e);
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            productRepo.delete(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ProductDTO convertEntityToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getCost(),
                product.getQuantity(),
                product.getSupplierid(),
                product.getExpirydate(),
                product.getDate()

        );

    }
    private Product convertDTOToEntity(ProductDTO productDTO) {
        return modelMapper.map(productDTO, Product.class);

    }

}
