package com.vaadin.bugrap.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.vaadin.bugrap.domain.entities.Reporter;
import org.vaadin.bugrap.domain.spring.ReporterRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ReporterRepository reporterRepository;

    @Autowired
    public UserDetailsServiceImpl(ReporterRepository reporterRepository) {
        this.reporterRepository = reporterRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Reporter reporter = reporterRepository.getByNameOrEmail(username,null);
        if (reporter == null) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            return new org.springframework.security.core.userdetails.User(reporter.getName(), reporter.getPassword(),
                   new ArrayList<GrantedAuthority>());
        }
    }

    /*
     * private static List<GrantedAuthority> getAuthorities(User user) { return
     * user.getRoles().stream() .map(role -> new SimpleGrantedAuthority("ROLE_"
     * + role)) .collect(Collectors.toList());
     * 
     * }
     */
}
