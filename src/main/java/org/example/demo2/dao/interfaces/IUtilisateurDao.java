package org.example.demo2.dao.interfaces;

import org.example.demo2.model.Utilisateur;

import java.util.List;

public interface IUtilisateurDao {
    void add(Utilisateur utilisateur);
    List<Utilisateur> getAll();
    Utilisateur findById(int id);
    Utilisateur getUtilisateurByEmail(String email);
    void update(Utilisateur utilisateur);
    void delete(int id);
    void updateUtilisateur(Utilisateur utilisateur);
    void deleteUtilisateur(int id);
}
