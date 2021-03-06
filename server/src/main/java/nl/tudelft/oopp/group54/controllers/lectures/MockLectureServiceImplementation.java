package nl.tudelft.oopp.group54.controllers.lectures;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

@Service
public class MockLectureServiceImplementation implements LectureService {
    @Override
    public Map<String, Object> createNewLecture(Date startTime, String lectureName) {

        Map<String, Object> toBeReturned = new TreeMap<>();

        toBeReturned.put("success", true);
        toBeReturned.put("lectureID", 123456);
        toBeReturned.put("lecturerID", 555555);
        toBeReturned.put("studentID", 234567);
        toBeReturned.put("moderatorID", 420420);

        return toBeReturned;
    }

    @Override
    public Map<String, Object> joinOngoingLecture(Integer lectureId, String roleCode, String userName) {
        Map<String, Object> toBeReturned = new TreeMap<>();

        toBeReturned.put("success", true);
        toBeReturned.put("userID", 123456);
        toBeReturned.put("userName", userName);
        toBeReturned.put("role", "Student");

        return toBeReturned;
    }

    @Override
    public Map<String, Object> getLectureMetadata(Integer lectureId) {
        Map<String, Object> toBeReturned = new TreeMap<>();

        toBeReturned.put("success", "true");
        toBeReturned.put("start", "10:45:00");
        toBeReturned.put("end", "12:45:00");
        toBeReturned.put("count", 139);

        return toBeReturned;
    }

    @Override
    public Map<String, Object> postLectureFeedback(Integer lectureID, String userId, Integer lectureFeedbackId) {
        return null;
    }

    @Override
    public Map<String, Object> getLectureFeedback(Integer lectureID, String userId) {
        return null;
    }
}
