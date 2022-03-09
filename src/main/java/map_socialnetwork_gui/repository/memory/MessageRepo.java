package com.lab6.map_socialnetwork_gui.repository.memory;

import com.lab6.map_socialnetwork_gui.domain.Message;
import com.lab6.map_socialnetwork_gui.exceptions.RepoException;
import com.lab6.map_socialnetwork_gui.repository.IRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class MessageRepo implements IRepository<Message> {
    Map<Integer, Message> messages;

    public MessageRepo() {
        messages = new HashMap<>();
    }

    @Override
    public void add(Message message) throws RepoException {
        if (messages.containsKey(message.getMessageID()))
            throw new RepoException("Mesaj existent!\n");
        messages.put(message.getMessageID(), message);
    }

    @Override
    public void remove(int ID) throws RepoException {
        if (!messages.containsKey(ID))
            throw new RepoException("Mesaj inexistent!\n");
        messages.remove(ID);
    }

    @Override
    public int size() {
        return messages.size();
    }

    @Override
    public Message getOne(int id) throws RepoException {
        if (!messages.containsKey(id))
            throw new RepoException("Mesaj inexistent!\n");
        return messages.get(id);
    }

    @Override
    public Collection<Message> getAll() {
        return messages.values();
    }

    @Override
    public void update(Message message) throws RepoException {

    }
}
