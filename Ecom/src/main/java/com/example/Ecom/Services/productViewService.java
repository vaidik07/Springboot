package com.example.Ecom.Services;

import com.example.Ecom.Repositories.ProductViewRepository;
import com.example.Ecom.model.ProductView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class productViewService {
    @Autowired
    private ProductViewRepository productViewRepository;

    public ProductView createProductView(ProductView productView){
        return productViewRepository.save(productView);
    }
    public Optional<ProductView> findProductView(String customerId , String itemId){
        return productViewRepository.findByCustomerIdAndItemId(customerId , itemId);
    }
    public ProductView updateProductViewCount(String customerId, String itemId) {
        Optional<ProductView> optionalProductView = productViewRepository.findByCustomerIdAndItemId(customerId, itemId);
        if (optionalProductView.isPresent()) {
            ProductView productView = optionalProductView.get();
            productView.setCount(productView.getCount() + 1);
            productView.setUpdatedAt(new Date(System.currentTimeMillis()));
            return productViewRepository.save(productView); // this should update the existing entity
        } else { 
            return null;
        }
    }

}