package org.example.demo2.model;

import java.time.LocalDateTime;

public class Utilisateur {
  private  int  id_utilisateur ;
 private  String pseudo;
   private String email;
  private String mot_de_passe;
 private LocalDateTime date_inscription;
  private   int karma ;
  public Utilisateur(int id_utilisateur, String pseudo, String email, String mot_de_passe, LocalDateTime date_inscription,int karma){
      this.id_utilisateur=id_utilisateur;
      this.pseudo=pseudo;
      this.email= email;
      this.mot_de_passe=mot_de_passe;
      this.karma=karma;
      this.date_inscription=date_inscription;
  }
 public int getId_utilisateur(){
      return id_utilisateur;
 }
 public String getPseudo(){
      return pseudo;
 }
 public String getEmail(){
      return email;
 }
 public String getMot_de_passe(){
      return mot_de_passe;
 }
  public LocalDateTime getDate_inscription(){
      return date_inscription;
  }
  public int getKarma(){
      return karma;
  }
}
