package org.example.demo2.service.interfaces;

import org.example.demo2.model.SignalementPost;
import org.example.demo2.model.SignalementCommentaire;

public interface ISignalementService {
    void signalerPost(SignalementPost signalement);
    void signalerCommentaire(SignalementCommentaire signalement);
    boolean dejaSignale(int idUtilisateur, int idPost);
}
