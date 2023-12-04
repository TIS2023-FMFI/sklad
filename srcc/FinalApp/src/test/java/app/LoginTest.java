package app;

import Entity.DatabaseHandler;
import Entity.Users;
import Exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    public LoginTest() throws Exception {
        DatabaseHandler db = new DatabaseHandler();
    }

    @Test
    void checkName() throws Exception {
        Users user = DatabaseHandler.checkUser("Peter", "Peter");
        assertEquals("Peter", user.getName());
    }

    @Test
    void checkIsAdmin() throws Exception {
        Users user1 = DatabaseHandler.checkUser("Peter", "Peter");
        Users user2 = DatabaseHandler.checkUser("Fero", "Fero");
        assertTrue(user1.getAdmin());
        assertFalse(user2.getAdmin());
    }

    @Test
    void checkUserExists() throws Exception {
        Users user = DatabaseHandler.checkUser("Peter", "Peter");
        assertNotNull(user, "Existuje takýto používateľ");
    }

    @Test
    void testCorrectUser() throws Exception {
        Users user = DatabaseHandler.checkUser("admin", "admin");
        Assertions.assertEquals(Users.class, user.getClass());
    }
    @Test
    void checkUserDoesNotExist() {
        assertThrows(UserDoesNotExist.class, () -> DatabaseHandler.checkUser("ja", "ja"));
    }

    @Test
    void checkWrongPassword() {
        assertThrows(WrongPassword.class, () -> DatabaseHandler.checkUser("Peter", "ja"));
    }
}
