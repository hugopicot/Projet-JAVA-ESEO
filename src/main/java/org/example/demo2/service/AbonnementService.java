package org.example.demo2.service;

import org.example.demo2.dao.AbonnementDao;

import java.util.List;

public class AbonnementService {
    private final AbonnementDao abonnementDao;

    public AbonnementService() {
        this.abonnementDao = new AbonnementDao();
    }

    public boolean estAbonne(int idUtilisateur, int idSubreddit) {
        return abonnementDao.estAbonne(idUtilisateur, idSubreddit);
    }

    public void toggleAbonnement(int idUtilisateur, int idSubreddit) {
        if (abonnementDao.estAbonne(idUtilisateur, idSubreddit)) {
            abonnementDao.supprimer(idUtilisateur, idSubreddit);
        } else {
            abonnementDao.ajouter(idUtilisateur, idSubreddit);
        }
    }

    public List<Integer> getAbonnementsIds(int idUtilisateur) {
        return abonnementDao.getAbonnementsUtilisateur(idUtilisateur);
    }
}
