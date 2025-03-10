package com.example.springboot.controllers;

import com.example.springboot.models.User;
import com.example.springboot.models.UserPrincipal;
import com.example.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController implements UserDetailsService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(produces = "application/json")
    public User addUser(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException(username);
        return new UserPrincipal(user);
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user;

    }

    @GetMapping(path = "authenticated")
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.getUserByUsername(auth.getPrincipal().toString());
        return user;
    }

//    @GetMapping(path = "role")
//    public String getRole() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String role = auth.getAuthorities().iterator().next().toString();
//        return "{\"role\": " + role + "}";
//    }

}
