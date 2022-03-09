package com.lab6.map_socialnetwork_gui.repository.database;

import com.lab6.map_socialnetwork_gui.domain.Friendship;
import com.lab6.map_socialnetwork_gui.exceptions.RepoException;
import com.lab6.map_socialnetwork_gui.repository.memory.FriendshipRepo;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;


public class FriendshipDbRepo extends FriendshipRepo {
    private String url;
    private String username;
    private String password;

    /**
     * Consturctor com.lab6.map_socialnetwork_gui.repository pentru prietenii cu memorarea in baza de date
     *
     * @param url      url-ul bazei de date
     * @param username numele utilizatorului pentru logare
     * @param password parola pentru logare
     */
    public FriendshipDbRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        loadData();
    }

    protected void loadData() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int firstUser = resultSet.getInt("first_user");
                int secondUser = resultSet.getInt("second_user");
                LocalDate date = LocalDate.parse(resultSet.getString("date"));
                LocalTime time = LocalTime.parse(resultSet.getString("time"));
                LocalDateTime localDateTime = LocalDateTime.of(date, time);
                Friendship friendship = new Friendship(id, firstUser, secondUser, localDateTime);
                //super.add(friendship);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda ce adauga o prietenie in baza de date si in memorie
     *
     * @param friendship prietenia de adaugat
     * @throws RepoException daca exista deja o prietenie cu acelasi id
     */
    @Override
    public void add(Friendship friendship) throws RepoException {
        //super.add(friendship);
        String sql = "insert into friendships (id,first_user, second_user,date,time) values (? ,?, ?,?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, friendship.getFriendshipID());
            ps.setInt(2, friendship.getFirst());
            ps.setInt(3, friendship.getSecond());
            ps.setDate(4, Date.valueOf(friendship.getDate().toLocalDate()));
            ps.setTime(5, Time.valueOf(friendship.getDate().toLocalTime()));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda ce sterge o prietenie din baza de date si din memorie
     *
     * @param ID id-il prieteniei de sters
     * @throws RepoException daca nu exista o prietenie cu id-ul dat
     */
    @Override
    public void remove(int ID) throws RepoException {
        //super.remove(ID);
        String sql = "DELETE from friendships where id=(?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ID);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda ce calculeaza nr de prietenii din baza de date
     *
     * @return nr de prietenii din baza de date
     */
    @Override
    public int size() {
        String sql = "SELECT COUNT(*) FROM friendships";

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

    /**
     * Metoda ce retuneaza o prietenie pe baza id-ul dat
     *
     * @param id prietenie de cautat
     * @return prietenia din baza de date cu id-ul dat
     * @throws RepoException daca nu exista o prietenie cu id-ul dat
     */
    @Override
    public Friendship getOne(int id) throws RepoException {
        String sql = "SELECT * from friendships where id=(?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                int Id = resultSet.getInt("id");
                int firstUser = resultSet.getInt("first_user");
                int secondUser = resultSet.getInt("second_user");
                LocalDate date = LocalDate.parse(resultSet.getString("date"));
                LocalTime time = LocalTime.parse(resultSet.getString("time"));
                LocalDateTime localDateTime = LocalDateTime.of(date, time);
                return new Friendship(Id, firstUser, secondUser, localDateTime);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metoda ce returneaza preiteniile din baza de date
     *
     * @return o colectie cu prieteniile din baza de date
     */
    @Override
    public Collection<Friendship> getAll() {
        Set<Friendship> friendships = new LinkedHashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int firstUser = resultSet.getInt("first_user");
                int secondUser = resultSet.getInt("second_user");
                LocalDate date = LocalDate.parse(resultSet.getString("date"));
                LocalTime time = LocalTime.parse(resultSet.getString("time"));
                LocalDateTime localDateTime = LocalDateTime.of(date, time);
                Friendship friendship = new Friendship(id, firstUser, secondUser, localDateTime);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    /**
     * Metoda ce modfica o prietenie
     *
     * @param entity noile caracteristici ale prieteniei
     */
    public void update(Friendship entity) {
        String sql = "update friendships SET first_user=(?),second_user=(?), date=(?), time=(?) where id=(?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, entity.getFirst());
            ps.setInt(2, entity.getSecond());
            ps.setDate(3, Date.valueOf(entity.getDate().toLocalDate()));
            ps.setTime(4, Time.valueOf(entity.getDate().toLocalTime()));
            ps.setInt(5, entity.getFriendshipID());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
