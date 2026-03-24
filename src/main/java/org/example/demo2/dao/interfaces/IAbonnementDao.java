package org.example.demo2.dao.interfaces;

import java.util.List;

public interface IAbonnementDao {
    void ajouter(int idUtilisateur, int idSubreddit);
    List<Integer> getAbonnementsUtilisateur(int idUtilisateur);
    boolean estAbonne(int idUtilisateur, int idSubreddit);
    void supprimer(int idUtilisateur, int idSubreddit);
}
