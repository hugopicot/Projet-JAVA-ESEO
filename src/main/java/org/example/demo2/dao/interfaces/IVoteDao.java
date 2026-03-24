package org.example.demo2.dao.interfaces;

import org.example.demo2.model.Vote;

import java.util.List;

public interface IVoteDao {
    void add(Vote vote);
    List<Vote> getAll();
    Vote findById(int id);
    Vote findByUserAndPost(int idUtilisateur, int idPost);
    void update(Vote vote);
    void delete(int id);
}
