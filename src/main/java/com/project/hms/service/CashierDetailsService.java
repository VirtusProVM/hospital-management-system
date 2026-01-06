package com.project.hms.service;

import com.project.hms.entity.Cashier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashierDetailsService implements UserDetailsService {

    private CashierService cashierService;

    public CashierDetailsService(CashierService cashierService) {
        this.cashierService = cashierService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Cashier cashier = cashierService.findByUsername(username);

        if(cashier == null) throw new UsernameNotFoundException("Cashier not found");

        return new User(cashier.getUsername(), cashier.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_CASHIER")));
    }
}
