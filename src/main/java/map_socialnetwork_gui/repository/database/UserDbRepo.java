package com.lab6.map_socialnetwork_gui.repository.database;

import com.lab6.map_socialnetwork_gui.domain.FriendRequest;
import com.lab6.map_socialnetwork_gui.domain.User;
import com.lab6.map_socialnetwork_gui.exceptions.RepoException;
import com.lab6.map_socialnetwork_gui.repository.memory.UserInMemoryRepo;
import com.lab6.map_socialnetwork_gui.repository.paging.Page;
import com.lab6.map_socialnetwork_gui.repository.paging.Pageable;
import com.lab6.map_socialnetwork_gui.repository.paging.Paginator;
import com.lab6.map_socialnetwork_gui.repository.paging.PagingRepository;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class UserDbRepo extends UserInMemoryRepo implements PagingRepository<User>{
    private String url;
    private String username;
    private String password;

    /**
     * Consturctor com.lab6.map_socialnetwork_gui.repository pentru utilizatori cu memorarea in baza de date
     *
     * @param url      url-ul bazei de date
     * @param username numele utilizatorului pentru logare
     * @param password parola pentru logare
     */
    public UserDbRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        loadData();
    }

    protected void loadData() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                User user = new User(id, firstName, lastName, username, password);
                //super.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda ce adauga un utilizator in baza de date si in memorie
     *
     * @param user utilizatorul de adaugat
     * @throws RepoException daca exista deja un utilizator cu acelasi id
     */
    @Override
    public void add(User user) throws RepoException {
        //super.add(user);
        String sql = "insert into users (id, first_name, last_name, username, password) values (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, user.getUserID());
            ps.setString(2, user.getUserFirstName());
            ps.setString(3, user.getUserLastName());
            ps.setString(4, user.getUserUsername());
            ps.setString(5, user.getUserPassword());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda ce sterge un utilizator din baza de date si din memorie
     *
     * @param ID id-il utilizatorului de sters
     * @throws RepoException daca nu exista un utilizator cu id-ul dat
     */
    @Override
    public void remove(int ID) throws RepoException {
        //super.remove(ID);
        String sql = "DELETE from users where id=(?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ID);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda ce calculeaza nr de utilizatori din baza de date
     *
     * @return nr utilizatori din baza de date
     */
    @Override
    public int size() {
        String sql = "SELECT COUNT(*) FROM users";

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
     * Metoda ce retuneaza un utilizaot rpe baza id-ul dat
     *
     * @param id utilizatorului de cautat
     * @return utilizatorul din baza de date cu id-ul dat
     * @throws RepoException daca nu exista un utilizator cu id-ul dat
     */
    @Override
    public User getOne(int id) throws RepoException {
        String sql = "SELECT * from users where id=(?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                User user = new User(userId, firstName, lastName, username, password);
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metoda ce returneaza utilizaoriii din baza de date
     *
     * @return o colectie cu utilizatorii din baza de date
     */
    @Override
    public Collection<User> getAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                User user = new User(id, firstName, lastName, username, password);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Metoda ce modfica un utilizator
     *
     * @param entity noile caracteristici ale utilizatorului
     */
    @Override
    public void update(User entity) {
        String sql = "update users SET first_name=(?), last_name=(?), username=(?), password=(?) where id=(?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getUserFirstName());
            ps.setString(2, entity.getUserLastName());
            ps.setString(3, entity.getUserUsername());
            ps.setString(4, entity.getUserPassword());
            ps.setInt(5, entity.getUserID());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        Paginator<User> paginator = new Paginator<User>(pageable, this.getAll());
        return paginator.paginate();
    }
}
