package com.mycompany.DAO;

import com.mycompany.Entity.Users;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class UsersEJB {

    @PersistenceContext(name = "inetumbd_PU")
    private EntityManager em;

    // Récupère la liste de tous les utilisateurs
    public List<Users> allUsers() {
        TypedQuery<Users> query = em.createNamedQuery("allUsers", Users.class);
        return query.getResultList();
    }

    // Récupère un utilisateur par son nom d'utilisateur
    public Users getXUser(String username) {
        return em.find(Users.class, username);
    }

    // Récupère un utilisateur par son adresse e-mail
    public Users getXEmail(String email) {
        return em.find(Users.class, email);
    }

    // Ajoute un nouvel utilisateur dans la base de données
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Users saveUser(Users user) {
        em.persist(user);
        em.flush();
        return user;
    }

    // Met à jour un utilisateur dans la base de données
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Users mergeUser(Users user) {
        System.out.println(user.getId());
        em.merge(em.find(Users.class, user.getId()));
        return user;
    }

    // Supprime un utilisateur de la base de données
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteUsers(Users user) throws Exception {
        try {
            em.remove(em.merge(user));
            em.flush();
        } catch (Exception e) {
            System.out.println(e);
            throw new Exception("Une erreur s'est produite lors de la suppression de l'utilisateur.", e);
        }
    }

    // Modifie les informations d'un utilisateur dans la base de données
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Users editUsers(Users user) throws Exception {
        try {
            em.merge(user);
            em.flush();
            return user;
        } catch (Exception e) {
            System.out.println(e);
            throw new Exception("Une erreur s'est produite lors de la mise à jour de l'utilisateur.", e);
        }
    }
}
