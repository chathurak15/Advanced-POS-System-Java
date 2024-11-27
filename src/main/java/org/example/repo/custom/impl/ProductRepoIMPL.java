package org.example.repo.custom.impl;

import org.example.entity.LowStock;
import org.example.entity.Product;
import org.example.entity.Supplier;
import org.example.repo.custom.ProductRepo;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepoIMPL implements ProductRepo {

    @Override
    public boolean save(Product product) throws Exception {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO products (name,category,price,cost,quantity,supplier_id,expiry_date,added_at,discount) " +
            " VALUES (?,?,?,?,?,?,?,?,?)");
        ps.setString(1, product.getName());
        ps.setString(2, product.getCategory());
        ps.setDouble(3, product.getPrice());
        ps.setDouble(4, product.getCost());
        ps.setInt(5, product.getQuantity());
        ps.setInt(6, product.getSupplierid());
        ps.setString(7, product.getExpirydate());
        ps.setString(8, product.getDate());
        ps.setString(9, product.getDiscount());

        if (product.getDiscount() == null || product.getDiscount().isEmpty()) {
            ps.setNull(9, java.sql.Types.VARCHAR); // Set null for the discount column
        } else {
            ps.setString(9, product.getDiscount());
        }

    return ps.executeUpdate()>0;
    }

    @Override
    public boolean update(Product product) throws Exception {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE products SET name=?, category=?,price=?,cost=?,quantity=?,supplier_id=?,expiry_date=?,added_at=?,discount=? WHERE id=?");
        ps.setString(1, product.getName());
        ps.setString(2, product.getCategory());
        ps.setDouble(3, product.getPrice());
        ps.setDouble(4, product.getCost());
        ps.setInt(5, product.getQuantity());
        ps.setInt(6, product.getSupplierid());
        ps.setString(7, product.getExpirydate());
        ps.setString(8, product.getDate());
        ps.setInt(10, product.getId());

        if (product.getDiscount()== null || product.getDiscount().isEmpty()) {
            ps.setNull(9, java.sql.Types.VARCHAR);
        }else {
            ps.setString(9, product.getDiscount());
        }
        return ps.executeUpdate()>0;
    }

    @Override
    public boolean delete(Integer integer) throws Exception {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM products WHERE id = ?");
        ps.setInt(1, integer);

        return ps.executeUpdate()>0;
    }

    @Override
    public Product search(Integer integer) {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM products WHERE id = ?");
            ps.setInt(1, integer);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setCategory(rs.getString("category"));
                product.setPrice(rs.getDouble("price"));
                product.setCost(rs.getDouble("cost"));
                product.setQuantity(rs.getInt("quantity"));
                product.setSupplierid(rs.getInt("supplier_id"));
                product.setExpirydate(rs.getString("expiry_date"));
                product.setDate(rs.getString("added_at"));
                product.setDiscount(rs.getString("discount"));
                return product;
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<Product> getAll() throws Exception {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM products");
        ResultSet rs = ps.executeQuery();
        List<Product> products = new ArrayList<Product>();
        while (rs.next()) {
            Product product = new Product();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setCategory(rs.getString("category"));
            product.setPrice(rs.getDouble("price"));
            product.setCost(rs.getDouble("cost"));
            product.setQuantity(rs.getInt("quantity"));
            product.setSupplierid(rs.getInt("supplier_id"));
            product.setExpirydate(rs.getString("expiry_date"));
            product.setDate(rs.getString("added_at"));
            products.add(product);
        }
        return products;
    }

    public List<LowStock> getLowStock() throws Exception{
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement(
                "SELECT products.id,products.name, products.quantity , suppliers.name, suppliers.email" +
                        " FROM products " +
                        " JOIN suppliers ON products.supplier_id = suppliers.id" +
                        " WHERE products.quantity=0");

        ResultSet rs = ps.executeQuery();
        List<LowStock> lowStocks = new ArrayList<>();

        while (rs.next()) {
            LowStock lowStock = new LowStock();
            lowStock.setProductId(rs.getInt("products.id"));
            lowStock.setProductName(rs.getString("products.name"));
            lowStock.setSupplierName(rs.getString("suppliers.name"));
            lowStock.setSupplierEmail(rs.getString("suppliers.email"));
            lowStock.setQuantity(rs.getInt("products.quantity"));
            lowStocks.add(lowStock);
        }
        return lowStocks;

    }

    public List<Product> getExpired(String date) throws Exception{
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM products WHERE expiry_date <= ? ORDER BY expiry_date ASC");
        ps.setString(1, date);
        ResultSet rs = ps.executeQuery();
        List<Product> products = new ArrayList<>();

        while (rs.next()) {
            Product product = new Product();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setCategory(rs.getString("category"));
            product.setPrice(rs.getDouble("price"));
            product.setCost(rs.getDouble("cost"));
            product.setQuantity(rs.getInt("quantity"));
            product.setSupplierid(rs.getInt("supplier_id"));
            product.setExpirydate(rs.getString("expiry_date"));
            product.setDate(rs.getString("added_at"));

            products.add(product);
        }
        return products;
    }

    public boolean addDiscount(String Discount ,Integer id) throws Exception{
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE products SET discount=? WHERE id=?");
        ps.setString(1, Discount);
        ps.setInt(2, id);
        return ps.executeUpdate()>0;
    }

    public List<Product> getAllname() throws Exception {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT id,name FROM products");
        ResultSet rs = ps.executeQuery();
        List<Product> products = new ArrayList<>();
        while(rs.next()){
            Product product = new Product();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            products.add(product);
        }
        return products;
    }
}
