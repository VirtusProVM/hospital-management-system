package com.project.hms.service;

import com.project.hms.entity.Doctors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorDetailsService implements UserDetailsService {

    private DoctorService doctorService;

    public DoctorDetailsService(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Doctors doctor = doctorService.findByUsername(username);
        if(doctor == null) throw new UsernameNotFoundException("Doctor not found");

        return new User(doctor.getUsername(), doctor.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_DOCTOR")));
    }
}
