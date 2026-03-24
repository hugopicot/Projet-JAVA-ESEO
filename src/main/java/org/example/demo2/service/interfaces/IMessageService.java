package org.example.demo2.service.interfaces;

import org.example.demo2.model.Message;

import java.util.List;

public interface IMessageService {
    void envoyerMessage(Message message);
    List<Message> getMessagesRecus(int idDestinataire);
    List<Message> getMessagesEnvoyes(int idExpediteur);
    void marquerCommeLu(int id);
    void supprimerMessage(int id);
}
