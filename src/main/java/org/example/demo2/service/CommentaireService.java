package org.example.demo2.service;

import org.example.demo2.dao.CommentaireDao;
import org.example.demo2.model.Commentaire;

import java.util.List;

public class CommentaireService {
    private final CommentaireDao commentaireDao;

    public CommentaireService() {
        this.commentaireDao = new CommentaireDao();
    }

    public Commentaire ajouterCommentaire(String contenu, int idUtilisateur, int idPost, Integer idParent) {
        if (contenu == null || contenu.trim().isEmpty()) {
            throw new IllegalArgumentException("Le commentaire ne peut pas être vide.");
        }
        
        Commentaire com = new Commentaire(contenu.trim(), idUtilisateur, idPost, idParent);
        commentaireDao.add(com);
        return com;
    }

    public List<Commentaire> getCommentairesPost(int idPost) {
        return commentaireDao.findByPost(idPost);
    }

    public int getNombreCommentaires(int idPost) {
        return commentaireDao.countByPost(idPost);
    }

    public void supprimerCommentaire(int idCommentaire) {
        commentaireDao.delete(idCommentaire);
    }
}
