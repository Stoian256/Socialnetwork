package com.lab6.map_socialnetwork_gui.repository.database;

import com.lab6.map_socialnetwork_gui.domain.Event;
import com.lab6.map_socialnetwork_gui.domain.FriendRequest;
import com.lab6.map_socialnetwork_gui.domain.Message;
import com.lab6.map_socialnetwork_gui.domain.User;
import com.lab6.map_socialnetwork_gui.exceptions.RepoException;
import com.lab6.map_socialnetwork_gui.model.FriendStatus;
import com.lab6.map_socialnetwork_gui.repository.paging.Page;
import com.lab6.map_socialnetwork_gui.repository.paging.Pageable;
import com.lab6.map_socialnetwork_gui.repository.paging.Paginator;
import com.lab6.map_socialnetwork_gui.repository.paging.PagingRepository;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class EventDbRepo implements PagingRepository<Event> {
    private final String url;
    private final String username;
    private final String password;

    public EventDbRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void add(Event event) throws RepoException {
        String sql = "insert into events (event_id, event_creator, event_name, photo_url, date, time) values (?, ?, ?, ?, ?, ?)";
        String sql1 = "insert into event_users (eve_id,user_id) values(?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, event.getEventID());
            ps.setInt(2, event.getCreatorID());
            ps.setString(3, event.getEventName());
            ps.setString(4,event.getPhotoURL());
            ps.setDate(5, Date.valueOf(event.getDateTime().toLocalDate()));
            ps.setTime(6, Time.valueOf(event.getDateTime().toLocalTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Integer participant : event.getParticipants()) {
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql1)) {

                ps.setInt(1, event.getEventID());
                ps.setInt(2, participant);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void remove(int ID) throws RepoException {
        String sql = "DELETE from events where id=(?)";
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
        String sql = "SELECT COUNT(*) FROM events";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Event getOne(int id) throws RepoException {
       // String sql = "SELECT * from events where id=(?)";
        String sql = "SELECT * FROM events ev LEFT JOIN event_users  uTe ON ev.event_id=uTe.eve_id \n" +
                "LEFT JOIN users u ON u.id=uTe.user_id where event_id=(?)";
        Event newEvent=null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int eventId = resultSet.getInt("event_id");
                int eventCreator = resultSet.getInt("event_creator");
                String eventName= resultSet.getString("event_name");
                String photoURL=resultSet.getString("photo_url");
                LocalDate date = LocalDate.parse(resultSet.getString("date"));
                LocalTime time = LocalTime.parse(resultSet.getString("time"));
                LocalDateTime localDateTime = LocalDateTime.of(date, time);
                int user_id = resultSet.getInt("id");

                if (newEvent != null)
                    newEvent.getParticipants().add(user_id);
                else {
                    List<Integer> participants = new ArrayList<Integer>();
                    participants.add(user_id);
                    newEvent = new Event(eventId, eventCreator,participants,eventName,photoURL, localDateTime);
                }
               // return new Event(eventId, eventCreator,eventName,photoURL, localDateTime);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newEvent;
    }

    @Override
    public Collection<Event> getAll() {
        Map<Integer,Event> events = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM events ev  LEFT JOIN event_users  uTe ON ev.event_id=uTe.eve_id \n" +
                     "LEFT JOIN users u ON u.id=uTe.user_id");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int eventId = resultSet.getInt("event_id");
                int eventCreator = resultSet.getInt("event_creator");
                String eventName= resultSet.getString("event_name");
                String photoURL=resultSet.getString("photo_url");
                LocalDate date = LocalDate.parse(resultSet.getString("date"));
                LocalTime time = LocalTime.parse(resultSet.getString("time"));
                LocalDateTime localDateTime = LocalDateTime.of(date, time);
                int user_id = resultSet.getInt("id");

                if (events.containsKey(eventId))
                    events.get(eventId).getParticipants().add(user_id);
                else {
                    List<Integer> participants = new ArrayList<Integer>();
                    participants.add(user_id);
                    Event newEvent = new Event(eventId, eventCreator,participants,eventName,photoURL, localDateTime);
                    events.put(eventId, newEvent);
                }
            }
            return events.values();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events.values();
    }

    @Override
    public void update(Event event) throws RepoException {
        if(event.getEventName().equals("join")) {
            String sql = "insert into event_users (eve_id,user_id) values(?,?)";

            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, event.getEventID());
                ps.setInt(2, event.getCreatorID());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RepoException("Sunteti inscris deja la eveniment!\n");
            }
        }
        else if (event.getEventName().equals("leave")) {
            String sqlVerificare="(SELECT COUNT(*) FROM event_users WHERE eve_id=(?) and user_id=(?))";
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sqlVerificare)) {
                ps.setInt(1, event.getEventID());
                ps.setInt(2, event.getCreatorID());
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()&&resultSet.getInt(1)==0) {
                    throw new RepoException("Nu sunteti inscris la acest eveniment!\n");
                }

            } catch (SQLException e) {
                e.getMessage();
            }

            String sql = "DELETE from event_users where eve_id=(?) and user_id=(?)";
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, event.getEventID());
                ps.setInt(2, event.getCreatorID());
                ps.executeUpdate();

            } catch (SQLException e) {
                e.getMessage();
            }
        }



    }


    @Override
    public Page<Event> findAll(Pageable pageable) {
        Paginator<Event> paginator = new Paginator<Event>(pageable, this.getAll());
        return paginator.paginate();
    }
}
