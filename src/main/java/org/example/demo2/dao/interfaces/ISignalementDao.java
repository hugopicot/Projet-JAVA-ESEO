package org.example.demo2.dao.interfaces;

import org.example.demo2.model.SignalementPost;
import org.example.demo2.model.SignalementCommentaire;

public interface ISignalementDao {
    void addSignalementPost(SignalementPost signalement);
    void addSignalementCommentaire(SignalementCommentaire signalement);
    boolean existeSignalementPost(int idUtilisateur, int idPost);
}
