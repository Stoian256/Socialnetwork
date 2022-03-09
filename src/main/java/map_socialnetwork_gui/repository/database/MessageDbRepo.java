package com.lab6.map_socialnetwork_gui.repository.database;

import com.lab6.map_socialnetwork_gui.domain.Message;
import com.lab6.map_socialnetwork_gui.domain.User;
import com.lab6.map_socialnetwork_gui.exceptions.RepoException;
import com.lab6.map_socialnetwork_gui.repository.IRepository;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


public class MessageDbRepo implements IRepository<Message> {

    private String url;
    private String username;
    private String password;

    public MessageDbRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void add(Message message) throws RepoException {
        String sql = "insert into messages (mess_id,from_id, message,date,time,reply) values (?, ?, ?,?,?,?)";
        String sql1 = "insert into message_users (message_id,user_id) values(?,?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, message.getMessageID());
            ps.setInt(2, message.getFrom().getUserID());
            ps.setString(3, message.getMessage().toString());
            ps.setDate(4, Date.valueOf(message.getDate().toLocalDate()));
            ps.setTime(5, Time.valueOf(message.getDate().toLocalTime()));
            if (message.getReply() != null)
                ps.setInt(6, message.getReply().getMessageID());
            else
                ps.setNull(6, Types.NULL);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (User user : message.getTo()) {
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql1)) {

                ps.setInt(1, message.getMessageID());
                ps.setInt(2, user.getUserID());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void remove(int ID) throws RepoException {
        String sql = "DELETE from messages where mess_id=(?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ID);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int size() {
        String sql = "SELECT COUNT(*) FROM messages";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                int size = resultSet.getInt(1);
                return size;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Message getOne(int id) throws RepoException {
        String sql = "SELECT * FROM messages mes INNER JOIN message_users  uTm ON mes.mess_id=uTm.message_id \n" +
                "INNER JOIN users u ON u.id=uTm.user_id where mess_id=(?)";
        Message newMessage = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int mess_id = resultSet.getInt("mess_id");
                int from = resultSet.getInt("from_id");
                String message = resultSet.getString("message");
                LocalDate date = LocalDate.parse(resultSet.getString("date"));
                LocalTime time = LocalTime.parse(resultSet.getString("time"));
                LocalDateTime localDateTime = LocalDateTime.of(date, time);
                int reply = resultSet.getInt("reply");
                int user_id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                if (newMessage != null)
                    newMessage.getTo().add(new User(user_id, firstName, lastName, username, password));
                else {
                    List<User> to = new ArrayList<User>();
                    to.add(new User(user_id, firstName, lastName, username, password));
                    Message replyMessage = null;
                    if (reply != 0)
                        replyMessage = new Message(reply, null, null, null, null);
                    newMessage = new Message(id, new User(from, "", "", "", ""), to, message, localDateTime, replyMessage);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (newMessage == null)
            throw new RepoException("Mesaj inexistent!");
        return newMessage;
    }

    @Override
    public Collection<Message> getAll() {
        Map<Integer, Message> messages = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages mes INNER JOIN message_users  uTm ON mes.mess_id=uTm.message_id\n" +
                     "INNER JOIN users u ON u.id=uTm.user_id");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("mess_id");
                int from = resultSet.getInt("from_id");
                String message = resultSet.getString("message");
                LocalDate date = LocalDate.parse(resultSet.getString("date"));
                LocalTime time = LocalTime.parse(resultSet.getString("time"));
                LocalDateTime localDateTime = LocalDateTime.of(date, time);
                int reply = resultSet.getInt("reply");
                int user_id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                if (messages.containsKey(id))
                    messages.get(id).getTo().add(new User(user_id, firstName, lastName, username, password));
                else {
                    List<User> to = new ArrayList<User>();
                    to.add(new User(user_id, firstName, lastName, username, password));
                    Message replyMessage = null;
                    if (reply != 0)
                        replyMessage = new Message(reply, null, null, null, null);
                    Message newMessage = new Message(id, new User(from, "", "", "", ""), to, message, localDateTime, replyMessage);
                    messages.put(id, newMessage);
                }
            }
            return messages.values();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages.values();
    }

    @Override
    public void update(Message message) throws RepoException {
        String sql = "update messages SET message=(?), date=(?), time=(?) where mess_id=(?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, message.getMessage());
            ps.setDate(2, Date.valueOf(message.getDate().toLocalDate()));
            ps.setTime(3, Time.valueOf(message.getDate().toLocalTime()));
            ps.setInt(4, message.getMessageID());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
