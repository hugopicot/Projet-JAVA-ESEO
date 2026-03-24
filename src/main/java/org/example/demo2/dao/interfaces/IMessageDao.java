package org.example.demo2.dao.interfaces;

import org.example.demo2.model.Message;

import java.util.List;

public interface IMessageDao {
    void add(Message message);
    List<Message> getAll();
    List<Message> getByExpediteur(int idExpediteur);
    List<Message> getByDestinataire(int idDestinataire);
    void updateLu(int id);
    void delete(int id);
}
