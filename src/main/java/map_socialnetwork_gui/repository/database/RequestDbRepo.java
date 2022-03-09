package com.lab6.map_socialnetwork_gui.repository.database;

import com.lab6.map_socialnetwork_gui.domain.FriendRequest;
import com.lab6.map_socialnetwork_gui.domain.User;
import com.lab6.map_socialnetwork_gui.exceptions.RepoException;
import com.lab6.map_socialnetwork_gui.model.FriendStatus;
import com.lab6.map_socialnetwork_gui.repository.IRepository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;


public class RequestDbRepo implements IRepository<FriendRequest> {
    private final String url;
    private final String username;
    private final String password;

    public RequestDbRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void add(FriendRequest friendRequest) throws RepoException {
        String sql = "insert into requests (id, first_user, second_user, status, date, time) values (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, friendRequest.getRequestID());
            ps.setInt(2, friendRequest.getFirstUser().getUserID());
            ps.setInt(3, friendRequest.getSecondUser().getUserID());
            ps.setString(4, friendRequest.getStatus().toString());
            ps.setDate(5, Date.valueOf(friendRequest.getDateTime().toLocalDate()));
            ps.setTime(6, Time.valueOf(friendRequest.getDateTime().toLocalTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            // throw new RepoException("Cerere nu s-a putut trimite!");
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int ID) throws RepoException {
        String sql = "DELETE from requests where id=(?)";
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
        String sql = "SELECT COUNT(*) FROM requests";

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
    public FriendRequest getOne(int id) throws RepoException {
        String sql = "SELECT * from requests where id=(?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                int requestId = resultSet.getInt("id");
                int firstUser = resultSet.getInt("first_user");
                int secondUser = resultSet.getInt("second_user");
                FriendStatus friendStatus = FriendStatus.valueOf(resultSet.getString("status"));
                LocalDate date = LocalDate.parse(resultSet.getString("date"));
                LocalTime time = LocalTime.parse(resultSet.getString("time"));
                LocalDateTime localDateTime = LocalDateTime.of(date, time);

                return new FriendRequest(requestId, new User(firstUser, "", "", "", ""), new User(secondUser, "", "", "", ""), friendStatus, localDateTime);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<FriendRequest> getAll() {
        Set<FriendRequest> friendRequests = new LinkedHashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from requests");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int firstUser = resultSet.getInt("first_user");
                int secondUser = resultSet.getInt("second_user");
                FriendStatus friendStatus = FriendStatus.valueOf(resultSet.getString("status"));
                LocalDate date = LocalDate.parse(resultSet.getString("date"));
                LocalTime time = LocalTime.parse(resultSet.getString("time"));
                LocalDateTime localDateTime = LocalDateTime.of(date, time);

                FriendRequest friendrequest = new FriendRequest(id, new User(firstUser, "", "", "", ""), new User(secondUser, "", "", "", ""), friendStatus, localDateTime);
                friendRequests.add(friendrequest);
            }
            return friendRequests;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendRequests;
    }

    @Override
    public void update(FriendRequest friendRequest) throws RepoException {
        String sql = "update requests SET status=(?) where id=(?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, friendRequest.getStatus().toString());
            ps.setInt(2, friendRequest.getRequestID());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
