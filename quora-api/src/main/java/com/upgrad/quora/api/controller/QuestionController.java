package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    //Controller method for CreateQuestion
    @RequestMapping(method = RequestMethod.POST, path = "/question/create",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(QuestionRequest questionRequest, @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException {

        // Create new Question Entity
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setDate(ZonedDateTime.now());

        // Authorize the user
        final QuestionEntity createdQuestion = questionService.createQuestion(authorization, questionEntity);

        // Create QuestionResponse and return
        QuestionResponse questionResponse = new QuestionResponse().id(questionEntity.getUuid()).status("QUESTION CREATED");

        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.OK);
    }


    //Controller method for GetAllQuestions
    @RequestMapping(method = RequestMethod.GET,path = "/question/all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException {

        // Authorization and fetching all questions
        List<QuestionEntity> allQuestions = questionService.getAllQuestions(authorization);

        final List<QuestionDetailsResponse> questionResponseList = new ArrayList<>();

        // Extracting UUID and Content
        for (QuestionEntity question : allQuestions) {
            String uuid = question.getUuid();
            String content = question.getContent();
            questionResponseList.add(new QuestionDetailsResponse().id(uuid).content(content));
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionResponseList, HttpStatus.OK);
    }

    //Controller method for EditQuestionContent
    @RequestMapping(method = RequestMethod.PUT,path = "/question/edit/{questionId}",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestionContent(QuestionEditRequest questionEditRequest, @PathVariable("questionId") final String questionUuid,
                                                                    @RequestHeader("authorization") final String authorization)throws AuthorizationFailedException, InvalidQuestionException {

        // Setting the User
        String content = questionEditRequest.getContent();

        // Authorization anf edit using UUID
        QuestionEntity questionEntity = questionService.editQuestionContent(authorization, questionUuid, content);

        // Setting UUID and Status of Edited
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(questionEntity.getUuid()).status("QUESTION EDITED");

        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
    }


    //Controller method for Delete Question
    @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}")
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@RequestHeader("authorization") final String accessToken,
        @PathVariable("questionId") final String questionId)throws AuthorizationFailedException, InvalidQuestionException {

        QuestionEntity questionEntity = questionService.deleteQuestion(accessToken, questionId);

        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse();

        //Setting UUID for deletion
        questionDeleteResponse.setId(questionEntity.getUuid());

        //Status Update
        questionDeleteResponse.setStatus("QUESTION DELETED");

        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);
    }


}
