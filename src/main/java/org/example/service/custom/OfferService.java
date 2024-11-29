package org.example.service.custom;

import org.example.dto.AI.RelationshipOfferDTO;

import org.example.dto.ProductDTO;
import org.example.dto.SupplierDTO;
import org.example.entity.AI.RelationshipOffer;
import org.example.service.custom.impl.ProductServiceIMPL;
import org.modelmapper.ModelMapper;
import org.example.repo.custom.offerRepo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OfferService {

    private final offerRepo offerRepo = new offerRepo();
    private final ModelMapper modelMapper = new ModelMapper();
    private final ProductServiceIMPL productService = new ProductServiceIMPL();

    public boolean saveOffer(Integer mainProduct, Integer freeProduct){
        try {
           return offerRepo.saveoffer(mainProduct,freeProduct);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<RelationshipOfferDTO> getAll() {

        List<RelationshipOfferDTO> dtos = new ArrayList<>();
        try {
            List<String> all = offerRepo.getAll();

            if (all.isEmpty()) {
                return dtos;
            }

            for (String s : all) {
                s = s.trim().replaceAll("[\\[\\]\\s]", "");
                String[] split = s.split(",");
                if (split.length < 4) {
                    throw new IllegalArgumentException("Invalid data format: " + s);
                }

                int offerId = Integer.parseInt(split[0]);
                String mainProduct = ConvetProductDtotoString(productService.search(Integer.parseInt(split[1])));
                String freeProduct = ConvetProductDtotoString(productService.search(Integer.parseInt(split[2])));
                String status = split[3];

                RelationshipOfferDTO dto = new RelationshipOfferDTO(offerId, mainProduct, freeProduct, status);
                dtos.add(dto);
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching relationship offers", e);
        }
        return dtos; // Return the list of DTOs
    }

    public List<Integer>mainProductIds(){
        List<Integer> ids = new ArrayList<>();
        try {
            ids = offerRepo.getMainProductIds();
            return ids;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer>freeProductIds(Integer mainProductId){
        List<Integer> ids = new ArrayList<>();
        try {
            ids = offerRepo.getFreeProductID(mainProductId);
            return ids;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String deleteOffer(Integer offerId){
        try {
            offerRepo.delete(offerId);
            return "Deleted";
        }catch (SQLException e){
            return "SQL";
        }
        catch (Exception e) {
            return "Error";
        }
    }

    public RelationshipOfferDTO convertEntityToDto(RelationshipOffer relationshipOffer){
        return modelMapper.map(relationshipOffer, RelationshipOfferDTO.class);
    }

    private String ConvetProductDtotoString(ProductDTO productDTO){
        return productDTO.getId()+" - "+productDTO.getName()+" - "+productDTO.getPrice()+"LKR";
    }

}
