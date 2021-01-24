package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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



}
