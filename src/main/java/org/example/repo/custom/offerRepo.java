package org.example.repo.custom;

import org.example.entity.AI.RelationshipOffer;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class offerRepo {
    public boolean saveoffer(Integer mainProduct, Integer freeProduct) throws Exception{
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO relationashipoffers(mainProductID, freeProductID,status) VALUES(?,?,?)");
        ps.setInt(1, mainProduct);
        ps.setInt(2, freeProduct);
        ps.setString(3, "active");
        return ps.executeUpdate()>0;
    }

    public List<Integer> search (Integer integer) throws Exception{
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT freeProductID FROM relationashipoffers where mainProductID=?");
        ps.setInt(1, integer);
        ResultSet rs = ps.executeQuery();
        List<Integer> list = new ArrayList<>();
        while(rs.next()){
            list.add(rs.getInt("freeProductID"));
        }
        return list;
    }

    public List<String> getAll() throws Exception {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM relationashipoffers");
        ResultSet rs = ps.executeQuery();

        List<String> all = new ArrayList<>();

        while (rs.next()) {
            List<String> list = new ArrayList<>();
            list.add(String.valueOf(rs.getInt("id")));
            list.add(rs.getString("mainProductID"));
            list.add(rs.getString("freeProductID"));
            list.add(rs.getString("status"));

            all.add(String.valueOf(list));
        }
        return all;
    }

    public List<Integer> getMainProductIds() throws Exception{
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT mainProductID FROM relationashipoffers");
        ResultSet rs = ps.executeQuery();
        List<Integer> list = new ArrayList<>();
        while (rs.next()) {
            list.add(rs.getInt("mainProductID"));
        }
        return list;
    }

    public List<Integer> getFreeProductID(Integer mainProductID) throws Exception{
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT freeProductID FROM relationashipoffers WHERE mainProductID=?");
        ps.setInt(1, mainProductID);
        ResultSet rs = ps.executeQuery();
        List<Integer> list = new ArrayList<>();
        if(rs.next()){
            list.add(rs.getInt("freeProductID"));
        }
        return list;
    }

    public boolean delete(Integer id) throws Exception{
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM relationashipoffers WHERE id=?");
        ps.setInt(1, id);
        return ps.executeUpdate()>0;
    }
}
