package org.example.service.custom;

import com.mysql.cj.xdevapi.JsonArray;
import org.example.dto.OrderDTO;
import org.example.dto.OrderItemDTO;
import org.example.entity.Order;
import org.example.entity.OrderItem;
import org.example.repo.custom.OrderRepo;
import org.modelmapper.ModelMapper;
import java.sql.SQLException;
import java.util.List;


public class OrderService {

    private final OrderRepo orderRepo = new OrderRepo();
    private final ModelMapper modelMapper = new ModelMapper();

    public Integer addOrder(OrderDTO orderDTO) {
        try {
            Integer id = orderRepo.addOrder(convertDTOtoEntity(orderDTO));
            return id;
        } catch (SQLException e){
            System.out.println(e.getMessage());;
        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }
        return 0;
    }

    public boolean addOrderItem(OrderItemDTO orderItemDTO) {
        try {
            orderRepo.AddOrderItem(convertororderItemDTOtoEntity(orderItemDTO));
            return true;
        } catch (SQLException e){
            System.out.println(e.getMessage());;
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());;
            return false;
        }
    }

    public String getSales(String string){
        try {
            return orderRepo.getSales(string);
        }catch (SQLException e){
            return "0";
        }catch (Exception e) {
            return "0";
        }
    }
    public String getlast30Sales(String string){
        try {
            return orderRepo.getlast30DaysSales(string);
        }catch (SQLException e){
            return "0";
        }catch (Exception e) {
            return "0";
        }
    }
    public String getlast60Sales(String string){
        try {
            return orderRepo.getlast60DaysSales(string);
        }catch (SQLException e){
            return "0";
        }catch (Exception e) {
            return "0";
        }
    }

    public Order convertDTOtoEntity(OrderDTO orderDTO) {
        return modelMapper.map(orderDTO, Order.class);
    }
    public OrderItem convertororderItemDTOtoEntity(OrderItemDTO orderItemDTO) {
       return modelMapper.map(orderItemDTO, OrderItem.class);
    }
}
