package com.example.Ecom.controllers;

import com.example.Ecom.Services.productViewService;
import com.example.Ecom.model.ProductView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-views")
public class productViewController {
    @Autowired
    private productViewService productViewService;

    @PostMapping("/create")
    public ResponseEntity<?> createProductView(@RequestBody ProductView productView){
        if(productViewService.findProductView(productView.getCustomerId() , productView.getItemId()).isPresent()){
            return new ResponseEntity<>("Product view already exists", HttpStatus.CONFLICT);
        }
        ProductView createdProductView = productViewService.createProductView(productView);
        createdProductView.setUpdatedAt(new Date(System.currentTimeMillis()));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProductView);
    }

    @PutMapping("/increment")
    public ResponseEntity<?> incrementProductView(@RequestBody ProductView productView) {
        ProductView updatedProductView = productViewService.updateProductViewCount(productView.getCustomerId(), productView.getItemId());
        if (updatedProductView == null) {
            return new ResponseEntity<>("No such file", HttpStatus.NOT_FOUND);
        }
        //updatedProductView.setUpdatedAt(new Date(System.currentTimeMillis()));
        return new ResponseEntity<>(updatedProductView, HttpStatus.OK);
    }
}


