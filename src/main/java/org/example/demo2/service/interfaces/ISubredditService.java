package org.example.demo2.service.interfaces;

import org.example.demo2.model.Subreddit;

import java.util.List;

public interface ISubredditService {
    void creerSubreddit(Subreddit subreddit);
    List<Subreddit> getAllSubreddits();
    Subreddit getSubredditById(int id);
    void mettreAJourSubreddit(Subreddit subreddit);
    void supprimerSubreddit(int id);
}
