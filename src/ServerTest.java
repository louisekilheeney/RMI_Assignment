import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTest {
    private Bank bank;

    @BeforeEach
    public void setUp() {
        try {
            bank = new Bank();
        } catch (Exception e){}
    }

    @Test
    @DisplayName("Testing login functionality")
    public void testLogin() {
        long sessionID;

        // Not an account
        try{
            sessionID = bank.login("Not a user", "Incorrect password");
        } catch (Exception e) {
            assertEquals("Username not found", e.getMessage());
        }

        // Invalid password
        try{
            sessionID = bank.login("Jessica Haugh", "Incorrect password");
        } catch (Exception e) {
            assertEquals("Incorrect password", e.getMessage());
        }

        // Successful
        try{
            // Assert no exception thrown
            sessionID = bank.login("Jessica Haugh", "airplane45");
        } catch (Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }
}
