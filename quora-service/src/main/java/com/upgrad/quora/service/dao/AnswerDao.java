package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

//Dao class used for database work
@Repository
public class AnswerDao {

    @PersistenceContext private EntityManager entityManager;

    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }
    //It will fetch the answer
    public AnswerEntity getAnswerById(final String answerId) {
        try {
            return entityManager.createNamedQuery("getAnswerById", AnswerEntity.class).setParameter("uuid", answerId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    //it will update the answer
    public void updateAnswer(AnswerEntity answerEntity) {
        entityManager.merge(answerEntity);
    }
    //method used for delete ans from database
    public AnswerEntity deleteAnswer(final String answerId) {
        AnswerEntity deleteAnswer = getAnswerById(answerId);
        if (deleteAnswer != null) {
            entityManager.remove(deleteAnswer);
        }
        return deleteAnswer;
    }

    // method used for getting all answer for a specific question from database.
    public List<AnswerEntity> getAllAnswersToQuestion(final String questionId) {
        return entityManager.createNamedQuery("getAllAnswersToQuestion", AnswerEntity.class).setParameter("uuid", questionId).getResultList();
    }
}