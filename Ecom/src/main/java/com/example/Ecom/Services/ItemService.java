package com.example.Ecom.Services;

import com.example.Ecom.Repositories.ItemsRepository;
import com.example.Ecom.model.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    private ItemsRepository itemsRepository;

    public List<Items> getAllItems(){
        return itemsRepository.findAll();
    }
    public Optional<Items> getItemById(String id){
        return itemsRepository.findById(id);
    }

    public Items saveItems(Items item){
        return itemsRepository.save(item);
    }

    public void deleteItem(String id){
        itemsRepository.deleteById(id);
    }


}
