package org.example.demo2.model;

import java.time.LocalDateTime;

public class Subreddit {
  public int id_subreddit;
   public String nom;
  public String description;
  public  LocalDateTime  date_creation;
  public  Subreddit(int id_subreddit, String nom, String description){
      this.id_subreddit = id_subreddit;
      this.nom = nom;
      this.description = description;
      this.date_creation = date_creation;

  }
  public  int getId_subreddit(){
      return id_subreddit ;
  }
  public  String getNom(){
      return nom ;
  }
  public  String getDescription(){
      return description ;
  }
   public  LocalDateTime  getDate_creation(){
      return date_creation ;
   }
}
