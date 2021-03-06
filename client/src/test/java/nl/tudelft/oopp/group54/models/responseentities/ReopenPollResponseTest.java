package nl.tudelft.oopp.group54.models.responseentities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReopenPollResponseTest {

    ReopenPollResponse response;
    ReopenPollResponse response2;

    @BeforeEach
    void setUp() {
        final Boolean success = true;
        final String message = "Success message";

        response = new ReopenPollResponse();
        response.setSuccess(success);
        response.setMessage(message);

        response2 = new ReopenPollResponse(success, message);

    }

    @Test
    void getSuccess() {
        assertEquals(response.getSuccess(), true);
        assertEquals(response.getSuccess(), response2.getSuccess());

    }

    @Test
    void getMessage() {
        assertEquals(response.getMessage(), "Success message");
        assertEquals(response.getMessage(), response2.getMessage());
    }
}