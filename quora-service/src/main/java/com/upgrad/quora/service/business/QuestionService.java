package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired private UserAuthDao userAuthDao;

    @Autowired private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(String authorization, QuestionEntity questionEntity)
            throws AuthorizationFailedException {
        //User authentication
        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByToken(authorization);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else {

            ZonedDateTime logoutAt = userAuthEntity.getLogoutAt();

            if (logoutAt != null) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
            } else {
                // randomUUID assignment to question
                questionEntity.setUuid(UUID.randomUUID().toString());
                questionEntity.setUserEntity(userAuthEntity.getUserEntity());
                return questionDao.createQuestion(questionEntity);
            }
        }
    }

    // getAllQuestions service
    public List<QuestionEntity> getAllQuestions(final String authorization)
            throws AuthorizationFailedException {

        // User Authentication
        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByToken(authorization);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else {
            ZonedDateTime logoutAt = userAuthEntity.getLogoutAt();
            if (logoutAt != null) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
            } else {
                return questionDao.getAllQuestions();
            }
        }
    }

    // EditQuestion service
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestionContent(final String authorization, String questionUuid, String content)
            throws AuthorizationFailedException, InvalidQuestionException {

        //User Authentication
        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByToken(authorization);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else {

            ZonedDateTime logoutAt = userAuthEntity.getLogoutAt();
            if (logoutAt != null) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");

            } else {
                // Get question by questionUuid
                QuestionEntity questionByUuid = questionDao.getQuestionByUuid(questionUuid);
                if (questionByUuid == null) {
                    throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
                } else {

                    // comparing UUID whether owner or not to edit question
                    Integer questionEditorId = userAuthEntity.getUserEntity().getId();
                    Integer questionOwnerId = questionByUuid.getUserEntity().getId();
                    if (!questionEditorId.equals(questionOwnerId)) {
                        throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
                    } else {
                        questionByUuid.setContent(content);
                        return questionDao.editQuestion(questionByUuid);
                    }
                }
            }
        }
    }

    //Delete Question Service
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(final String accessToken, final String questionId)
            throws AuthorizationFailedException, InvalidQuestionException {

        //User Authentication
        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByToken(accessToken);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete the question");
        }

        QuestionEntity questionEntity = questionDao.getQuestionByUuid(questionId);

        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
        if (!questionEntity.getUserEntity().getUuid().equals(userAuthEntity.getUserEntity().getUuid())
                && !userAuthEntity.getUserEntity().getRole().equals("admin")) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
        }
        questionDao.deleteQuestion(questionEntity);

        return questionEntity;
    }

    //getAllQuestionsByUser Service
    public List<QuestionEntity> getAllQuestionsByUser(final String userId, final String accessToken)
            throws AuthorizationFailedException, UserNotFoundException {

        //User Authentication
        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByToken(accessToken);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions posted by a specific user");
        }

        UserEntity user = userDao.getUserById(userId);

        if (user == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }

        return questionDao.getAllQuestionsByUser(user);
    }

    public QuestionEntity validateQuestion(String questionId) throws InvalidQuestionException {
        QuestionEntity questionEntity = questionDao.getQuestionByUuid(questionId);
        if(questionEntity==null) {
            throw new InvalidQuestionException("QUES-001","The question entered is invalid");
        }
        return questionEntity;
    }



}
