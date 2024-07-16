package com.example.Ecom.Services;

import com.example.Ecom.Repositories.OrdersRepository;
import com.example.Ecom.model.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;

    public List<Orders> getAllOrders(){
        return ordersRepository.findAll();
    }

    public Optional<Orders> getOrderById(String id){
        return ordersRepository.findById(id);
    }

    public Orders saveOrders(Orders order){
        return ordersRepository.save(order);
    }

    public void deleteOrder(String id){
        ordersRepository.deleteById(id);
    }
}
