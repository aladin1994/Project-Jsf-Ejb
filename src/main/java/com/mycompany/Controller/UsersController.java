package com.mycompany.Controller;

import com.mycompany.DAO.UsersEJB;
import com.mycompany.Entity.Users;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "users")
@RequestScoped
public class UsersController {

    // Objet pour gérer les messages JSF
    FacesContext context = FacesContext.getCurrentInstance();

    // Modèle d'utilisateur pour les opérations CRUD
    private Users user;

    // Injection de la couche d'accès aux données (EJB)
    @EJB
    UsersEJB userEJB;

    // Liste des utilisateurs récupérée depuis la base de données
    private List<Users> userList = new ArrayList();

    // Méthode exécutée après la création du bean pour initialiser la liste des utilisateurs
    @PostConstruct
    public void getAllUsersList() {
        user = new Users();
        userList = userEJB.allUsers();
    }

    // Méthode pour préparer l'édition d'un utilisateur
    public String editUsers(Users userEdit) {
        this.user = userEdit;
        return "edit.xhtml";
    }

    // Méthode pour mettre à jour un utilisateur
    public String editUser() {
        System.out.println(this.user.getId());

        // Validation de l'e-mail
        if (!this.user.getEmail().contains("@") || !this.user.getEmail().contains(".")) {
            context.addMessage(null, new FacesMessage("E-mail invalide"));
            return null;
        }

        try {
            userEJB.editUsers(this.user);
            userList = userEJB.allUsers();
            return "index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage("Une erreur s'est produite lors de la mise à jour de l'utilisateur... \n " + e));
            return null;
        }
    }

    // Méthode pour ajouter un nouvel utilisateur
    public String addUser() {
        try {
            // Validation de l'e-mail
            if (!user.getEmail().contains("@") || !user.getEmail().contains(".")) {
                context.addMessage(null, new FacesMessage("E-mail invalide"));
                return null;
            }

            user = userEJB.saveUser(user);
            userList = userEJB.allUsers();
            context.addMessage(null, new FacesMessage("Utilisateur ajouté avec succès..."));
            return "index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage("Une erreur s'est produite lors de l'ajout d'un utilisateur... \n " + e));
            return null;
        }
    }

    // Méthode pour supprimer un utilisateur
    public String deleteUser(Users getUser) {
        try {
            userEJB.deleteUsers(getUser);
            userList = userEJB.allUsers();

            context.addMessage(null, new FacesMessage("Utilisateur supprimé avec succès..."));
            return "index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage("Une erreur s'est produite lors de la suppression de l'utilisateur... \n " + e));
            return null;
        }
    }

    // Getters and setters
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Users> getUserList() {
        return userList;
    }

    public void setUserList(List<Users> userList) {
        this.userList = userList;
    }
}
