package org.example.demo2.dao.interfaces;

import org.example.demo2.model.Commentaire;

import java.util.List;

public interface ICommentaireDao {
    void add(Commentaire commentaire);
    List<Commentaire> findByPost(int idPost);
    int countByPost(int idPost);
    void delete(int id);
}
