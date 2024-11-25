package org.example.service.custom.impl;

import org.example.dto.SupplierDTO;
import org.example.entity.Supplier;
import org.example.repo.custom.impl.SupplierRepoIMPL;
import org.example.service.custom.SupplierService;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierServiceIMPL implements SupplierService {

    private final ModelMapper modelMapper = new ModelMapper();
    private final SupplierRepoIMPL supplierRepo = new SupplierRepoIMPL();

    @Override
    public String save(SupplierDTO supplierDTO) {
        try {
            supplierRepo.save(convertDtoToEntity(supplierDTO));
            return "saved";
        }catch (SQLException e){
            if(e.getErrorCode()==1062){
                return "Duplicate Email";
            }else {
                return "not saved";
            }
        } catch (Exception e) {
            return "not saved";
        }
    }

    @Override
    public String update(SupplierDTO supplierDTO) {
        try {
            supplierRepo.update(convertDtoToEntity(supplierDTO));
            return "Supplier updated";
        }catch (SQLException e){
            return "SQL Error";
        } catch (Exception e) {
            return "not saved";
        }
    }

    @Override
    public SupplierDTO search(Integer integer) {
        return null;
    }

    @Override
    public List<SupplierDTO> getAll() {
        try {
            List<Supplier> all = supplierRepo.getAll();
            List<SupplierDTO> dtos = new ArrayList<>();

            if (all != null) {
                for (Supplier Supplier : all) {
                    dtos.add(convertEntityToDto(Supplier));
                } return dtos;

            } else {
                throw new RuntimeException("No users found");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            return supplierRepo.delete(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<SupplierDTO> getAllname(){
        try {
            List<Supplier> all = supplierRepo.getAllname();
            List<SupplierDTO> dtos = new ArrayList<>();
            if (all != null) {
                for (Supplier Supplier : all) {
                    dtos.add(convertEntityToDto(Supplier));
                }
                return dtos;
            }else {
                throw new RuntimeException("No users found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Supplier convertDtoToEntity(SupplierDTO supplierDTO) {
        return modelMapper.map(supplierDTO, Supplier.class);
    }
    public SupplierDTO convertEntityToDto(Supplier supplier) {
        return modelMapper.map(supplier, SupplierDTO.class);
    }
}
