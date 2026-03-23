package org.example.demo2.service;

import org.example.demo2.dao.SignalementDao;
import org.example.demo2.model.SignalementPost;
import org.example.demo2.model.SignalementCommentaire;

public class SignalementService {
    private final SignalementDao signalementDao;

    public SignalementService() {
        this.signalementDao = new SignalementDao();
    }

    public void signalerPost(int idUtilisateur, int idPost) {
        if (!signalementDao.existeSignalementPost(idUtilisateur, idPost)) {
            signalementDao.addSignalementPost(new SignalementPost(idUtilisateur, idPost));
        }
    }

    public void signalerCommentaire(int idUtilisateur, int idCommentaire) {
        // Logique similaire pour les commentaires
        signalementDao.addSignalementCommentaire(new SignalementCommentaire(idUtilisateur, idCommentaire));
    }
}
