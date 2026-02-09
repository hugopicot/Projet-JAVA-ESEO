package org.example.demo2.model;

import java.time.LocalDateTime;

public class Message {
    private int id_message;
    private int contenu;
    private LocalDateTime date_envoi;

    private int id_expediteur;
    private int id_destinataire;

    public Message(int id_message, int contenu, LocalDateTime date_envoi, int id_expediteur, int id_destinataire) {
        this.id_message = id_message;
        this.contenu = contenu;
        this.date_envoi = date_envoi;
        this.id_expediteur = id_expediteur;
        this.id_destinataire = id_destinataire;
    }

}
