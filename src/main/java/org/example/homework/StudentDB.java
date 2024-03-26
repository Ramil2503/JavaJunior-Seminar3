package org.example.homework;

import java.sql.*;

public class StudentDB {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql:jdbc", "postgres", "admin")) {
            acceptConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void acceptConnection(Connection connection) throws SQLException {
        createTable(connection);
        insertData(connection);

        updateRow(connection, "Student 1", "new surname for student 1");
        printAll(connection);
    }

    private static void updateRow(Connection connection, String name, String secondName) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("update student set second_name = ? where name = ?")) {
            stmt.setString(1, secondName);
            stmt.setString(2, name);

            stmt.executeUpdate();
        }
    }

    private static void printAll(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT id, name, second_name, age FROM student");

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String secondName = resultSet.getString("second_name");
                int age = resultSet.getInt("age");
                System.out.println("id = " + id + ", name = " + name + ", second name = " + secondName + ", age = " + age);
            }
        }
    }

    private static void insertData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int affectedRows = statement.executeUpdate("""
                     insert into student(id, name, second_name, age) values
                     (1, 'Student 1', 'Student_secondname 1', 18),
                     (2, 'Student 2', 'Student_secondname 2', 20),
                     (3, 'Student 3', 'Student_secondname 3', 25),
                     (4, 'Student 4', 'Student_secondname 4', 24),
                     (5, 'Student 5', 'Student_secondname 5', 17)
                    """);
            System.out.println("EXECUTE affected rows: " + affectedRows);
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                        create table student(
                        id bigint, 
                        name varchar(256),
                        second_name varchar(256),
                        age smallint
                    )
                       """);
        }
    }
}
