package org.example.demo2.dao.interfaces;

import org.example.demo2.model.Moderation;

import java.sql.SQLException;
import java.util.List;

public interface IModerationDao {
    List<Moderation> getAll() throws SQLException;
    Moderation findById(int id);
    void add(Moderation moderation);
    void update(Moderation moderation) throws SQLException;
    void delete(int id);
}
