package nl.tudelft.oopp.group54.controllers.answers;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import nl.tudelft.oopp.group54.entities.Answer;
import nl.tudelft.oopp.group54.entities.AnswerKey;
import nl.tudelft.oopp.group54.entities.MapLoggers;
import nl.tudelft.oopp.group54.entities.Question;
import nl.tudelft.oopp.group54.entities.QuestionKey;
import nl.tudelft.oopp.group54.entities.User;
import nl.tudelft.oopp.group54.entities.UserKey;
import nl.tudelft.oopp.group54.repositories.AnswerRepository;
import nl.tudelft.oopp.group54.repositories.QuestionRepository;
import nl.tudelft.oopp.group54.repositories.UserRepository;
import nl.tudelft.oopp.group54.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public Map<String, Object> answerQuestion(Integer lectureId, String userId, Integer questionId, String answerText) {
        Map<String, Object> toBeReturned = new TreeMap<>();

        // LectureID cannot be null
        if (lectureId == null) {
            toBeReturned.put("success", false);
            toBeReturned.put("message", "LectureID cannot be null.");
            return toBeReturned;
        }

        // UserID cannot be null
        if (userId == null) {
            toBeReturned.put("success", false);
            toBeReturned.put("message", "UserID cannot be null.");
            return toBeReturned;
        }

        // QuestionID cannot be null
        if (questionId == null) {
            toBeReturned.put("success", false);
            toBeReturned.put("message", "QuestionID cannot be null.");
            return toBeReturned;
        }

        // Answer text size cannot be too long
        if (answerText.length() > 700) {
            toBeReturned.put("success", false);
            toBeReturned.put("message", "Answer text was unacceptable.");
            return toBeReturned;
        }

        Optional<User> foundUser = userRepository.findById(new UserKey(Integer.parseInt(userId), lectureId));
        Optional<Question> foundQuestion = questionRepository.findById(new QuestionKey(questionId, lectureId));

        if (foundUser.isEmpty()) {
            toBeReturned.put("success", false);
            toBeReturned.put("message", "Unrecognized userID.");
            return toBeReturned;
        }

        if (foundQuestion.isEmpty()) {
            toBeReturned.put("success", false);
            toBeReturned.put("message", "Question with this ID does not exist or has been asked in another lecture.");
            return toBeReturned;
        }

        // If the user is a student, they cannot answer the question
        if (foundUser.get().getRoleID() == 3) {
            toBeReturned.put("success", false);
            toBeReturned.put("message", "This user is not authorized to provide answers. ");
            return toBeReturned;
        }

        // Create the new answer
        Answer newAnswer = new Answer(new AnswerKey(null, lectureId),
                                        answerText,
                                        questionId,
                                        Integer.parseInt(userId));

        foundQuestion.get().setAnswered(true);
        foundQuestion.get().setAnswerText(answerText);

        questionRepository.flush();
        answerRepository.flush();

        String logMessage = "User " + userId + " (" + foundUser.get().getIpAddress() + ") answered question " + questionId;
        MapLoggers.getInstance().logWarning(lectureId,
                new Date() + " - " + logMessage,
                "server/Logs/" + lectureId + ".log");

        try {
            questionRepository.save(foundQuestion.get());
            answerRepository.save(newAnswer);
            toBeReturned.put("success", true);
        } catch (Exception e) {
            toBeReturned.put("success", false);
            toBeReturned.put("message", e.toString());
            return toBeReturned;
        }

        return toBeReturned;
    }
}
