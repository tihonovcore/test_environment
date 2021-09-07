package com.tihonovcore.testenv.service;

import com.tihonovcore.testenv.model.User;
import com.tihonovcore.testenv.repository.RoleRepository;
import com.tihonovcore.testenv.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder encoder;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findUserByName(name);
        if (user == null) throw new UsernameNotFoundException("User not found");

        return user;
    }

    public boolean saveUser(User user) {
        if (userRepository.findUserByName(user.getName()) != null) {
            return false;
        }

        user.setRole(roleRepository.userRole());
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);

        return true;
    }
}
