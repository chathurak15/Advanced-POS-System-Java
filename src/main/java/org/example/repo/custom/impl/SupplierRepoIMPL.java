package org.example.repo.custom.impl;

import org.example.entity.Supplier;
import org.example.repo.custom.SupplierRepo;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepoIMPL implements SupplierRepo {


    @Override
    public boolean save(Supplier supplier) throws Exception {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO suppliers(name,email,number)" +
                " VALUES (?,?,?)");
        ps.setString(1, supplier.getName());
        ps.setString(2, supplier.getEmail());
        ps.setString(3, supplier.getNumber());

        if(supplier.getNumber().isEmpty()||supplier.getEmail()==null){
            ps.setNull(3, java.sql.Types.VARCHAR);
        }else {
            ps.setString(3, supplier.getNumber());
        }
        return ps.executeUpdate()>0;
    }

    @Override
    public boolean update(Supplier supplier) throws Exception {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE suppliers SET name=?,email=?,number=? WHERE id=?");
        ps.setString(1, supplier.getName());
        ps.setString(2, supplier.getEmail());
        ps.setString(3, supplier.getNumber());
        ps.setInt(4, supplier.getId());
        return ps.executeUpdate()>0;
    }

    @Override
    public boolean delete(Integer Id) throws Exception {
       Connection connection = DBConnection.getInstance().getConnection();
       PreparedStatement ps = connection.prepareStatement("DELETE FROM suppliers WHERE id=?");
       ps.setInt(1, Id);
       return ps.executeUpdate()>0;
    }

    @Override
    public Supplier search(Integer integer) throws Exception {
        return null;
    }

    @Override
    public List<Supplier> getAll() throws Exception {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM suppliers");
        ResultSet rs = ps.executeQuery();
        List<Supplier> suppliers = new ArrayList<>();
        while(rs.next()){
            Supplier supplier = new Supplier();
            supplier.setId(rs.getInt("id"));
            supplier.setName(rs.getString("name"));
            supplier.setEmail(rs.getString("email"));
            supplier.setNumber(rs.getString("number"));
            suppliers.add(supplier);

        }
        return suppliers;
    }
}
