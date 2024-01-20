package com.ramiro.curso.springboot.restapp.restdemo.controllers;

// import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ramiro.curso.springboot.restapp.restdemo.models.User;
import com.ramiro.curso.springboot.restapp.restdemo.models.dto.UserDto;

@RestController
@RequestMapping("/api")
public class restController {

    

    // con DTO
    @RequestMapping(path = "/details", method = RequestMethod.GET)
    public UserDto details() { // devuelve un objeto Map convertido a JSON
        
        UserDto userDto = new UserDto();
        User user = new User("Ramiro","Alvite");
        userDto.setUser(user);
        userDto.setTitle("Hola mundo Spring Boot");
        
        
        return userDto;
    }
    
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public List<User> list() {
        
        User user = new User("Ramiro","Alvite");
        User user2 = new User("Carlos","Santana");
        User user3 = new User("Teresa","Rivera");

        // List<User> users = new ArrayList<>();
        // users.add(user);
        // users.add(user2);
        // users.add(user3);

        List<User> users = Arrays.asList(user, user2, user3);

        return users;

    }
    
    //  con Map
    @RequestMapping(path = "/details-map", method = RequestMethod.GET)
    public Map<String, Object> detailsMap() { // devuelve un objeto Map convertido a JSON
        Map<String, Object> body = new HashMap<>();

        body.put("title", "Hola mundo");
        body.put("name", "Ramiro");
        body.put("lastname", "Alvite");

        return body;
    }


}
