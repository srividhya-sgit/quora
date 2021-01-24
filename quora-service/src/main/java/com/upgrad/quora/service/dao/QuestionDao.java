package com.upgrad.quora.service.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    //CreateQuestion method implemented
    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    //getAllQuestions method implemented
    public List<QuestionEntity> getAllQuestions() {
        List<QuestionEntity> questionsList =
                entityManager.createNamedQuery("getAllQuestions", QuestionEntity.class).getResultList();
        return questionsList;
    }

    // getQuestionByUuid method implemented in tryCatch.
    public QuestionEntity getQuestionByUuid(String questionUuid) {
        try {
            return entityManager.createNamedQuery("getQuestionByUuid", QuestionEntity.class)
                    .setParameter("questionId", questionUuid).getSingleResult();
        }
        catch (NoResultException nre) {
            return null;
        }
    }

    //editQuestion method implemented
    public QuestionEntity editQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    // DeleteQuestion method implemented
    public void deleteQuestion(QuestionEntity questionEntity) {
        entityManager.remove(questionEntity);
    }

    // getAllQuestionsByUser method implemented./
    public <UserEntity> List<QuestionEntity> getAllQuestionsByUser(final UserEntity userId) {
        return entityManager.createNamedQuery("getAllQuestionByUser", QuestionEntity.class)
                .setParameter("user", userId).getResultList();
    }


}
