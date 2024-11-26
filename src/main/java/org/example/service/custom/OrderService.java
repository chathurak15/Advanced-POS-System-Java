package org.example.service.custom;

import org.example.dto.OrderDTO;
import org.example.dto.OrderItemDTO;
import org.example.entity.Order;
import org.example.entity.OrderItem;
import org.example.repo.custom.OrderRepo;
import org.modelmapper.ModelMapper;

import javax.swing.text.html.parser.Entity;
import java.sql.SQLException;


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

    public Order convertDTOtoEntity(OrderDTO orderDTO) {
        return modelMapper.map(orderDTO, Order.class);
    }
    public OrderItem convertororderItemDTOtoEntity(OrderItemDTO orderItemDTO) {
       return modelMapper.map(orderItemDTO, OrderItem.class);
    }
}
