package com.akash.portfolio.service;

import com.akash.portfolio.entity.Admin;
import com.akash.portfolio.repository.AdminRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * Lets Spring Security log in admins stored in the "admins" table
 * (by username or email).
 */
@Service
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public AdminUserDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        Admin admin = adminRepository.findByUsername(login)
                .or(() -> adminRepository.findByEmail(login))
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

        return User.withUsername(admin.getUsername())
                .password(admin.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(admin.getRole())))
                .disabled(!admin.isEnabled())
                .accountLocked(!admin.isAccountNonLocked())
                .accountExpired(!admin.isAccountNonExpired())
                .credentialsExpired(!admin.isCredentialsNonExpired())
                .build();

    }

}
