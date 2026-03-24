package org.example.demo2.dao.interfaces;

import org.example.demo2.model.Subreddit;

import java.util.List;

public interface ISubredditDao {
    void add(Subreddit subreddit);
    List<Subreddit> getAll();
    Subreddit findById(int id);
    void update(Subreddit subreddit);
    void delete(int id);
}
