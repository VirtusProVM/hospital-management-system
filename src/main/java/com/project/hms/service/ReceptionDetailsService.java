package com.project.hms.service;

import com.project.hms.entity.Reception;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceptionDetailsService implements UserDetailsService {

    private ReceptionService receptionService;

    public ReceptionDetailsService(ReceptionService receptionService) {
        this.receptionService = receptionService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Reception reception = receptionService.findByUsername(username);

        if(reception == null) throw new UsernameNotFoundException("Doctor not found");

        return new User(reception.getUsername(), reception.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_RECEPTION")));
    }
}
