package com.upgrad.quora.service.entity;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "answer")
@NamedQueries(
        {
                @NamedQuery(name = "getAnswerFromId" , query = "select q from AnswerEntity q where q.uuid = :uuid"),
                @NamedQuery(name = "verifyAnsUserToEdit" , query = "select a from AnswerEntity a INNER JOIN UserEntity u on a.userEntity = u.uuid where a.uuid =:euuid and u.uuid = :uuuid"),
                @NamedQuery(name = "verifyAnsUserToDelete" , query = "select a from AnswerEntity a INNER JOIN UserEntity u on a.userEntity = u.uuid where a.uuid =:duuid and u.uuid = :deuuid"),
                @NamedQuery(name = "getAllAnswersToQuestion" , query = "select a from AnswerEntity a INNER JOIN QuestionEntity q on a.questionEntity = q.uuid where q.uuid = :uuid")
        }
)
public class AnswerEntity implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ans")
    @NotNull
    @Size(max = 255)
    private String answer;

    @Column(name = "uuid")
    @NotNull
    private String uuid;

    @Column(name = "date")
    @NotNull
    private ZonedDateTime date;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuestionEntity questionEntity;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public UserEntity getUser() {
        return userEntity;
    }

    public void setUser(UserEntity user) {
        this.userEntity = user;
    }

    public QuestionEntity getQuestion() {
        return questionEntity;
    }

    public void setQuestion(QuestionEntity question) {
        this.questionEntity = question;
    }
}

