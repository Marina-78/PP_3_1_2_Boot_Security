package ru.kata.spring.boot_security.demo.UserDao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        List<User> users = entityManager.createQuery("select a from User a", User.class).getResultList();
        return users;
    }

    @Override
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public User getUser(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void editUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void removeUser(Long id) {
        entityManager.remove(getUser(id));
    }

    @Override
    public User getUserByEmail(String email) {
        return (User) entityManager.createQuery("select u from User u where u.email = :email")
                .setParameter("email", email).getSingleResult();
    }

    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = entityManager.createQuery("select r from Role r", Role.class).getResultList();
        return roles;
    }

}

