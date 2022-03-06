package com.bookwyrm.backend.unit.book.controller;

import com.bookwyrm.backend.book.controller.BookController;
import com.bookwyrm.backend.book.dao.BookDao;
import com.bookwyrm.backend.book.input.BookUploadInput;
import com.bookwyrm.backend.book.payload.BookDetailSearchPayload;
import com.bookwyrm.backend.book.payload.BookSearchPayload;
import com.bookwyrm.backend.book.payload.BookUploadPayload;
import com.bookwyrm.backend.book.service.BookService;
import com.bookwyrm.backend.comment.dao.CommentService;
import com.bookwyrm.backend.review.dao.ReviewService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.Assert;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;

@SpringJUnitConfig
@SpringBootTest
public class BookControllerTests {

    @Mock
    private BookService bookService;
    @Mock
    ReviewService reviewService;
    @Mock
    CommentService commentService;
    @InjectMocks
    private BookController controller;

    @Test
    public void testHappyPath(){
        MockitoAnnotations.openMocks(this);

        //Setup
        BookUploadInput input = new BookUploadInput();
        input.setAuthor("testAuthor");
        input.setTitle("testName");

        //Run
        ResponseEntity response =  controller.createBook(input);

        //Check results
        Assert.isTrue(response.getStatusCode() == HttpStatus.OK, "Expected successful endpoint call with 200 status");
        Assert.isNull(((BookUploadPayload)response.getBody()).getMessages() , "Expected successful endpoint call with no error messages");
    }
    @Test
    public void testBadRequest(){
        //Setup
        BookUploadInput input = new BookUploadInput();

        //Run
        ResponseEntity response =  (new BookController()).createBook(input);

        //Check results
        Assert.isTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST, "Expected failed endpoint call with 400 status");
        Assert.notEmpty(((BookUploadPayload)response.getBody()).getMessages(), "Expected failed endpoint call with error messages");
    }

    @Test
    public void testGoodSearch() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(bookService.findAllBooksWithTitle(any(String.class))).thenReturn(Arrays.asList(new BookDao("testTitle", "testAuthor")));

        //Run
        ResponseEntity response =  controller.searchBookByTitle("the test book");

        //Check results
        Assert.isTrue(response.getStatusCode() == HttpStatus.OK, "Expected successful endpoint call with 200 status");
        Assert.isNull(((BookSearchPayload)response.getBody()).getMessages() , "Expected successful endpoint call with no error messages");
    }

    @Test
    public void testGoodSearchById() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(bookService.findByBookId(any(String.class))).thenReturn(new BookDao("testTitle", "testAuthor"));

        //Run
        ResponseEntity response = controller.searchBookById("the test book");

        //Check results
        Assert.isTrue(response.getStatusCode() == HttpStatus.OK, "Expected successful endpoint call with 200 status");
        Assert.isNull(((BookDetailSearchPayload)response.getBody()).getMessages() , "Expected successful endpoint call with no error messages");
    }
}
