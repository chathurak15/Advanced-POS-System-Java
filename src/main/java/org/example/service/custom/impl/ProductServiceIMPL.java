package org.example.service.custom.impl;

import org.example.dto.LowStockDTO;
import org.example.dto.ProductDTO;
import org.example.dto.SupplierDTO;
import org.example.dto.UserDTO;
import org.example.entity.LowStock;
import org.example.entity.Product;
import org.example.entity.Supplier;
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

    private final ProductRepoIMPL productRepo = new ProductRepoIMPL();
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

        try {
            productRepo.update(convertDTOToEntity(productDTO));
            return "Product updated";
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return "Sql Error";

        } catch (Exception e) {
            return "something went wrong";
        }
    }

    @Override
    public ProductDTO search(Integer integer) {
        try {
            return convertEntityToDTO(productRepo.search(integer));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public List<LowStockDTO> getLowStock() {
        try {
            List<LowStock> lowStock = productRepo.getLowStock();
            List<LowStockDTO> lowStockDTOS = new ArrayList<>();
            if (lowStock != null) {
                for (LowStock lowStockEntity : lowStock) {
                    lowStockDTOS.add(convertEntityToDTO(lowStockEntity));
                }
                return lowStockDTOS;
            }else {
                throw new RuntimeException("product not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Contact Develloper", e);
        }
    }

    public List<ProductDTO> getExpired(String date) {
        try {
            List<Product> all = productRepo.getExpired(date);
            List<ProductDTO> productDTOS = new ArrayList<>();

            if (all != null) {
                for (Product product : all) {
                    productDTOS.add(convertEntityToDTO(product));
                }return productDTOS;
            }else {
                throw new RuntimeException("product not found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProductDTO> getExpirySoon(String date, String today) {
        try {
            List<Product> all = productRepo.getExpirySoon(date,today);
            List<ProductDTO> productDTOS = new ArrayList<>();

            if (all != null) {
                for (Product product : all) {
                    productDTOS.add(convertEntityToDTO(product));
                }return productDTOS;
            }else {
                throw new RuntimeException("product not found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean AddDiscount(String discount, Integer id) {
        try {
            return productRepo.addDiscount(discount,id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProductDTO> getAllname(){
        try {
            List<Product> all = productRepo.getAllname();
            List<ProductDTO> dtos = new ArrayList<>();
            if (all != null) {
                for (Product product : all) {
                    dtos.add(convertEntityToDTO(product));
                }
                return dtos;
            }else {
                throw new RuntimeException("No product found");
            }
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
                product.getDate(),
                product.getDiscount()

        );

    }

    private Product convertDTOToEntity(ProductDTO productDTO) {
        return modelMapper.map(productDTO, Product.class);

    }

    private LowStockDTO convertEntityToDTO(LowStock lowStock) {
        return modelMapper.map(lowStock, LowStockDTO.class);
    }


}
