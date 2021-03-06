package nl.tudelft.oopp.group54.models.responseentities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JoinLectureResponseTest {

    JoinLectureResponse response;
    JoinLectureResponse response2;
    JoinLectureResponse response3;

    @BeforeEach
    void setUp() {


        final Boolean success = true;
        final Long userID = 1L;
        final String userName = "userName";
        final String role = "Moderator";
        final String message = "This is a join message";
        final Integer privilegeId = 3;

        response = new JoinLectureResponse();
        response.setSuccess(success);
        response.setUserID(userID);
        response.setUserName(userName);
        response.setRole(role);
        response.setMessage(message);
        response.setPrivilegeId(privilegeId);

        response2 = new JoinLectureResponse(
                success,
                userID,
                userName,
                role,
                message,
                privilegeId
        );

        response3 = new JoinLectureResponse();
    }


    @Test
    void getSuccess() {
        assertEquals(response.getSuccess(), response2.getSuccess());
    }

    @Test
    void getUserID() {
        assertEquals(response.getUserID(), response2.getUserID());
    }

    @Test
    void getUserName() {
        assertEquals(response.getUserName(), response2.getUserName());
    }

    @Test
    void getRole() {
        assertEquals(response.getRole(), response2.getRole());
    }

    @Test
    void getMessage() {
        assertEquals(response.getMessage(), response2.getMessage());
    }

    @Test
    void getPrivilegeId() {
        assertEquals(response.getPrivilegeId(), response2.getPrivilegeId());
    }

    @Test
    void testToString() {
        assertEquals(response.toString(), response2.toString());

        String responseString = response.toString();
        assertTrue(responseString.contains(response.getSuccess().toString()));
        assertTrue(responseString.contains(response.getUserID().toString()));
        assertTrue(responseString.contains(response.getUserName()));
        assertTrue(responseString.contains(response.getRole()));
        assertTrue(responseString.contains(response.getMessage()));
        assertFalse(responseString.contains("privilegeId"));

    }
}