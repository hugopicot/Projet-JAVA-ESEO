package org.example.demo2.service.interfaces;

import java.util.List;

public interface IAbonnementService {
    void abonner(int idUtilisateur, int idSubreddit);
    void desabonner(int idUtilisateur, int idSubreddit);
    boolean estAbonne(int idUtilisateur, int idSubreddit);
    List<Integer> getAbonnementsUtilisateur(int idUtilisateur);
}
