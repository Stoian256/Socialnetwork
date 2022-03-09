package com.lab6.map_socialnetwork_gui.repository.memory;

import com.lab6.map_socialnetwork_gui.domain.User;
import com.lab6.map_socialnetwork_gui.exceptions.RepoException;
import com.lab6.map_socialnetwork_gui.repository.IRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class UserInMemoryRepo implements IRepository<User> {
    Map<Integer, User> users;

    public UserInMemoryRepo() {
        users = new HashMap<>();
    }

    @Override
    public void add(User user) throws RepoException {
        if (users.containsKey(user.getUserID()))
            throw new RepoException("Utilizator existent!");

        users.put(user.getUserID(), user);

    }

    @Override
    public void remove(int userID) throws RepoException {
        if (!users.containsKey(userID))
            throw new RepoException("Utilizator inexistent!");
        users.remove(userID);
    }

    @Override
    public int size() {
        return users.size();
    }

    @Override
    public User getOne(int id) throws RepoException {
        if (!users.containsKey(id))
            throw new RepoException("Utilizator inexistent!");
        return users.get(id);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public void update(User user) {

    }
}
