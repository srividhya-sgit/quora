package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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


}
