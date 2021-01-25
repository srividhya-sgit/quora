package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public AnswerEntity getAnswerById(String uuid) {
        try {
            return entityManager.createNamedQuery("getAnswerFromId", AnswerEntity.class).setParameter("uuid", uuid)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public AnswerEntity verifyAnsUserToEdit(String euuid, String uuuid)
    {

        try {
            return entityManager.createNamedQuery("verifyAnsUserToEdit", AnswerEntity.class).setParameter("euuid", euuid).setParameter("uuuid",uuuid).getSingleResult();
        }catch (NoResultException nre)
        {
            return null;
        }
    }

    public AnswerEntity newAns(AnswerEntity answerEntity)
    {
        return entityManager.merge(answerEntity);
    }

    public AnswerEntity verifyAnsUserToDelete(String duuid, String deuuid)
    {

        try {
            return entityManager.createNamedQuery("verifyAnsUserToDelete", AnswerEntity.class).setParameter("duuid", duuid).setParameter("deuuid",deuuid).getSingleResult();
        }catch (NoResultException nre)
        {
            return null;
        }
    }

    public AnswerEntity deleteAnswer(AnswerEntity answerEntity)
    {
        entityManager.remove(answerEntity);
        return answerEntity;
    }

    public List<AnswerEntity> getAllAnswersToQuestion(String questionId)
    {
        return entityManager.createNamedQuery("getAllAnswersToQuestion",AnswerEntity.class)
                .setParameter("uuid",questionId).getResultList();
    }


}
