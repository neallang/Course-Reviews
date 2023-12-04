package edu.virginia.sde.reviews;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class DatabaseDriverTest {
    //DatabaseDriver databaseDriver = new DatabaseDriver("dbc:sqlite::memory:");
    User testUser = new User("Stan", "password");
    DatabaseDriver databaseDriver = new DatabaseDriver("test.sqlite");

    @Test
    void createTables() throws SQLException{
        databaseDriver.connect();
        databaseDriver.createTables();
        User user = new User("stan", "password");
        databaseDriver.addUser(user);
        databaseDriver.commit();
        databaseDriver.disconnect();

    }

    @Test
    void destroyTables() throws SQLException{
        databaseDriver.connect();
        databaseDriver.clearTables();
        databaseDriver.commit();
    }
//    @BeforeEach
//    void setUp() throws SQLException{
//        databaseDriver.connect();
//        databaseDriver.createTables();
//        databaseDriver.commit();
//    }
//
//    @Test
//    void addUser() throws SQLException{
//        databaseDriver.addUser(testUser);
//
//    }
//
//    @AfterEach
//    void clear() throws SQLException{
//        databaseDriver.clearTables();
//        databaseDriver.commit();
//        databaseDriver.disconnect();
//    }
}