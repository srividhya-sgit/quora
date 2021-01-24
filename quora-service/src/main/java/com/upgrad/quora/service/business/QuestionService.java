package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.ZonedDateTime;
import java.util.ArrayList;
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



}
