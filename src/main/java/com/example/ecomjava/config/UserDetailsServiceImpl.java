package com.example.ecomjava.config;

import com.example.ecomjava.entity.UserEntity;
import com.example.ecomjava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity users = userRepository.findByEmailIgnoreCase(email);
        if(users == null)
        {
            throw new UsernameNotFoundException("Could not found user");
        }
        return new CustomUserDetail(users);
    }
}
