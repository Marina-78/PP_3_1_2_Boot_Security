package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.UserDao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private UserDao userDao;
    private PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder encoder) {
        this.userDao = userDao;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.getUserByEmail(email);
        if (userDao == null) {
            throw new UsernameNotFoundException("Unknown user: " + email);
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    @Transactional
    public void saveUser(User user, List<String> roles) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>());
        List<Role> bdRoles = getAllRoles();
        for (Role role : bdRoles) {
            if (roles.contains(role.getRole())) {
                user.getRoles().add(role);
            }
        }
        userDao.saveUser(user);
    }

    @Override
    public User getUser(Long id) {
        return userDao.getUser(id);
    }

    @Override
    @Transactional
    public void editUser(User user, List<String> roles) {
        if (!user.getPassword().equals(getUser(user.getId()).getPassword())) {
            user.setPassword(encoder.encode(user.getPassword()));
        }
        user.setRoles(new HashSet<>());
        List<Role> bdRoles = getAllRoles();
        for (Role role : bdRoles) {
            if (roles.contains(role.getRole())) {
                user.getRoles().add(role);
            }
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userDao.editUser(user);
    }

    @Override
    @Transactional
    public void removeUser(Long id) {
        userDao.removeUser(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    public List<Role> getAllRoles() {
        return userDao.getAllRoles();
    }
}
