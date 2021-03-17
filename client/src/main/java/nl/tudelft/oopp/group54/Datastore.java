package nl.tudelft.oopp.group54;

import com.sun.javafx.collections.ImmutableObservableList;

import nl.tudelft.oopp.group54.models.QuestionModel;
import nl.tudelft.oopp.group54.models.responseentities.CreateLectureResponse;
import nl.tudelft.oopp.group54.models.responseentities.JoinLectureResponse;
import nl.tudelft.oopp.group54.widgets.QuestionView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Datastore {

  static Datastore instance;

  ObservableList<QuestionView> currentUnansweredQuestionViews;
  ObservableList<QuestionView> currentAnsweredQuestionViews;

  CreateLectureResponse createLectureResponse;
  JoinLectureResponse joinLectureResponse;

  String serviceEndpoint = "http://localhost:8080";
  
  Long userId = 0L;
  Integer lectureId = 0;

  private Datastore() {
    this.currentUnansweredQuestionViews = FXCollections.observableArrayList();
    this.currentAnsweredQuestionViews = FXCollections.observableArrayList();
    createLectureResponse = null;
    joinLectureResponse = null;
  }

  public static Datastore getInstance() {
    if (instance == null) {
      instance = new Datastore();
    }
    return instance;
  }

  public String getServiceEndpoint() {
    return this.serviceEndpoint;
  }

  public ObservableList<QuestionView> getCurrentUnansweredQuestionViews() {
    return currentUnansweredQuestionViews;
  }

  public void setCurrentUnansweredQuestionViews(ObservableList<QuestionView> currentUnansweredQuestionViews) {
    if(currentUnansweredQuestionViews == null)
        this.currentUnansweredQuestionViews.clear();
    else
        this.currentUnansweredQuestionViews = currentUnansweredQuestionViews;
  }

  public ObservableList<QuestionView> getCurrentAnsweredQuestionViews() {
    return currentAnsweredQuestionViews;
  }

  public void setCurrentAnsweredQuestionViews(ObservableList<QuestionView> currentAnsweredQuestionViews) {
    if(currentAnsweredQuestionViews == null)
        this.currentAnsweredQuestionViews.clear();
    else
        this.currentAnsweredQuestionViews = currentAnsweredQuestionViews;
  }

  public void addUnansweredQuestion(QuestionModel question){
    QuestionView q = new QuestionView(question.getQuestionText(), question.getQuestionId());
    this.currentUnansweredQuestionViews.add(q);
  }

  public void addAnsweredQuestion(QuestionModel question){
	QuestionView q = new QuestionView(question.getQuestionText(), question.getQuestionId());
    this.currentAnsweredQuestionViews.add(q);
  }


  public CreateLectureResponse getCreateLectureResponse() {
    return createLectureResponse;
  }

  public void setCreateLectureResponse(CreateLectureResponse createLectureResponse) {
    this.createLectureResponse = createLectureResponse;
  }

  public JoinLectureResponse getJoinLectureResponse() {
	  return joinLectureResponse;
  }

  public void setJoinLectureResponse(JoinLectureResponse joinLectureResponse) {
	  this.joinLectureResponse = joinLectureResponse;
  }
  
  public void setLectureId(Integer lectureId) {
	  this.lectureId = lectureId;
  }
  
  public Integer getLectureId() {
	  return lectureId;
  }
  
  public void setUserId(Long userId) {
	  this.userId = userId;
  }
  
  public Long getUserId() {
	  return userId;
  }
  
}
