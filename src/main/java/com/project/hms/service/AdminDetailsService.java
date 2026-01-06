package com.project.hms.service;

import com.project.hms.entity.Admin;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminDetailsService implements UserDetailsService {

    private AdminService adminService;

    public AdminDetailsService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminService.findByUsername(username);
        if (admin == null) throw new UsernameNotFoundException("User not found");

        return new User(admin.getUsername(), admin.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

    }


}
