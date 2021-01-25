package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;

@Service
public class AnswerService {

    @Autowired
    private AnswerDao answerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(AnswerEntity answerEntity, UserAuthEntity userAuthEntity) throws AuthorizationFailedException {
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post an answer");
        }
        return answerDao.createAnswer(answerEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity getAnswerFromId(String uuid) throws AnswerNotFoundException {
        AnswerEntity answerEntity = answerDao.getAnswerById(uuid);
        if (answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }
        return answerEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity verifyAnsUserToEdit(UserAuthEntity userAuthEntity, AnswerEntity answerEntity) throws AuthorizationFailedException {
        UserEntity userEntity = userAuthEntity.getUserEntity();
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit an answer");
        }
        String euuid = answerEntity.getUuid();
        String uuuid = userEntity.getUuid();
        AnswerEntity verifiedAnsEdit = answerDao.verifyAnsUserToEdit(euuid, uuuid);
        if (verifiedAnsEdit == null) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        }
        return verifiedAnsEdit;
    }

    //To update new answer
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity newAns(AnswerEntity answerEntity) {
        AnswerEntity newAns = answerDao.newAns(answerEntity);
        return newAns;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity verifyAnsUserToDelete(UserAuthEntity userAuthEntityd, AnswerEntity answerEntity)throws AuthorizationFailedException {
        UserEntity userEntityd = userAuthEntityd.getUserEntity();
        if (userAuthEntityd.getLogoutAt()!= null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete an answer");
        }
        String duuid = answerEntity.getUuid();
        String deuuid = userEntityd.getUuid();
        AnswerEntity verifiedAnsDelete = answerDao.verifyAnsUserToEdit(duuid, deuuid);
        if (verifiedAnsDelete == null) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
        }
        return verifiedAnsDelete;
    }

    //To delete answer
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deleteAnswer(AnswerEntity answerEntity) {
        AnswerEntity deleteAnswer = answerDao.deleteAnswer(answerEntity);
        return deleteAnswer;
    }
}

