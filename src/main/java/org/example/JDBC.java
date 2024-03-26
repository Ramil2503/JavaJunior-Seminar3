package org.example;

import javax.xml.stream.events.StartElement;
import java.net.ConnectException;
import java.sql.*;

public class JDBC {
    public static void main(String[] args) {
        // JDBC Java Database Connectivity

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql:jdbc", "postgres", "admin")) {
            acceptConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static void acceptConnection(Connection connection) throws SQLException {
        // createTable(connection);
        // insertData(connection);
        // selectWhereID(connection);
        // deleteRandomRow(connection);

        updateRow(connection, "Person 1", "new surname for person 1");
        printAll(connection);
    }

    private static void updateRow(Connection connection, String name, String secondName) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("update person set second_name = ? where name = ?")) {
            stmt.setString(1, secondName);
            stmt.setString(2, name);

            stmt.executeUpdate();
        }

//    try (Statement statement = connection.createStatement()) {
//      statement.executeUpdate("update person set secondName = " + secondName + "where name = " + name);
//    }
    }

    private static void deleteRandomRow(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            System.out.println("Deleted: " + statement.executeUpdate("DELETE FROM person WHERE id = 2"));
        }
    }

    private static void printAll(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT id, name, second_name FROM person");

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String secondName = resultSet.getString("second_name");
                System.out.println("id = " + id + ", name = " + name + ", second name = " + secondName);
            }
        }
    }

    private static void selectWhereID(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT id, name FROM person WHERE id > 3");

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                System.out.println("id = " + id + ", name = " + name);
            }
        }
    }

    private static void insertData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int affectedRows = statement.executeUpdate("""
                     insert into person(id, name, second_name) values
                     (1, 'Person 1', 'Person_secondname 1'),
                     (2, 'Person 2', 'Person_secondname 2'),
                     (3, 'Person 3', 'Person_secondname 3'),
                     (4, 'Person 4', 'Person_secondname 4'),
                     (5, 'Person 5', 'Person_secondname 5')
                    """);
            System.out.println("EXECUTE affected rows: " + affectedRows);
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                        create table person(
                        id bigint, 
                        name varchar(256),
                        second_name varchar(256)
                    )
                       """);
        }
    }
}
