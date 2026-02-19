package org.example.demo2.model;

import java.time.LocalDateTime;

public class Message {
    private  int id_message ;
    private int contenu;
    private LocalDateTime date_envoi ;
    private boolean lu;
    private  int    id_expediteur;
   private int id_destinataire;
   public  Message(int id_message, int contenu, boolean lu, LocalDateTime date_envoi, int id_expediteur, int id_destinataire){
       this.id_message=id_message;
       this.contenu=contenu;
       this.date_envoi=date_envoi;
       this.id_expediteur=id_expediteur;
       this.id_destinataire=id_destinataire;
   }

    public int getId_message() {
        return id_message;
    }

    public int getContenu() {
        return contenu;
    }

    public LocalDateTime getDate_envoi() {
        return date_envoi;
    }

    public boolean getLu() {
        return lu;
    }

    public int getId_expediteur() {
        return id_expediteur;
    }

    public int getId_destinataire() {
        return id_destinataire;
    }

}
