package org.example.demo2.model;

import java.time.LocalDateTime;

public class Subreddit {
  public int id_subreddit;
   public String nom;
  public String description;
  public  LocalDateTime  date_creation;
  public  Subreddit(int id_subreddit, String nom, String description, LocalDateTime date_creation){
      this.id_subreddit = id_subreddit;
      this.nom = nom;
      this.description = description;
      this.date_creation = date_creation;

  }
}
