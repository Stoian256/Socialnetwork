package com.lab6.map_socialnetwork_gui.repository.memory;

import com.lab6.map_socialnetwork_gui.domain.Friendship;
import com.lab6.map_socialnetwork_gui.exceptions.RepoException;
import com.lab6.map_socialnetwork_gui.repository.IRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class FriendshipRepo implements IRepository<Friendship> {
    Map<Integer, Friendship> friendships;

    public FriendshipRepo() {
        this.friendships = new HashMap<>();
    }

    @Override
    public void add(Friendship friendship) throws RepoException {
        if (friendships.containsKey(friendship.getFriendshipID()))
            throw new RepoException("Prietenie existenta!\n");

        friendships.put(friendship.getFriendshipID(), friendship);
    }

    @Override
    public void remove(int ID) throws RepoException {
        if (!friendships.containsKey(ID))
            throw new RepoException("Prietenie inexistenta!\n");
        friendships.remove(ID);
    }

    @Override
    public int size() {
        return friendships.size();
    }

    @Override
    public Friendship getOne(int id) throws RepoException {
        if (!friendships.containsKey(id))
            throw new RepoException("Prietenie inexistenta!\n");
        return friendships.get(id);
    }

    @Override
    public Collection<Friendship> getAll() {
        return friendships.values();
    }

    @Override
    public void update(Friendship friendship) {

    }
}
