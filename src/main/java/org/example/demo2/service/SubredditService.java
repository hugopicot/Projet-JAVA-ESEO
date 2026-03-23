package org.example.demo2.service;

import org.example.demo2.dao.SubredditDao;
import org.example.demo2.model.Subreddit;

import java.util.List;

public class SubredditService {
    private final SubredditDao subredditDao;

    public SubredditService() {
        this.subredditDao = new SubredditDao();
    }

    public List<Subreddit> getSubreddits() {
        return subredditDao.getAll();
    }

    public Subreddit getSubredditById(int id) {
        return subredditDao.findById(id);
    }

    public Subreddit creerSubreddit(String nom, String description) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du subreddit ne peut pas être vide.");
        }
        Subreddit sub = new Subreddit(nom.trim(), description);
        subredditDao.add(sub);
        return sub;
    }
}
