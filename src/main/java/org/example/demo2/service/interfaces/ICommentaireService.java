package org.example.demo2.service.interfaces;

import org.example.demo2.model.Commentaire;

import java.util.List;

public interface ICommentaireService {
    void ajouterCommentaire(Commentaire commentaire);
    List<Commentaire> getCommentairesByPost(int idPost);
    int getNombreCommentaires(int idPost);
    void supprimerCommentaire(int id);
    void ajouterReponse(int idParent, Commentaire reponse);
}
