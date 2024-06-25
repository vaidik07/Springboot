package com.jwt.example.jwtExample3.services;

import com.jwt.example.jwtExample3.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private List<User> store = new ArrayList<>();
    public UserService(){
        store.add(new User(UUID.randomUUID().toString() , "Vaidik" , "vaidikparashar@gmail.com"));
        store.add(new User(UUID.randomUUID().toString() , "Durgesh" , "durgesh@gmail.com"));
        store.add(new User(UUID.randomUUID().toString() , "Harsh" , "harsh@gmail.com"));
        store.add(new User(UUID.randomUUID().toString() , "Deepak" , "deepak@gmail.com"));
    }

    public List<User> getUsers(){
        return this.store;
    }
}
