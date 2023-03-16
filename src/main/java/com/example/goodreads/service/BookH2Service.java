package com.example.goodreads.service;
import com.example.goodreads.model.BookRowMapper;

import com.example.goodreads.repository.BookRepository;
import com.example.goodreads.model.Book;
import java.util.*;
import  org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class BookH2Service implements BookRepository{
    @Autowired
    private JdbcTemplate db;
    @Override
    public ArrayList<Book> getBooks(){
        List<Book> bookList=db.query("SELECT * FROM Book", new BookRowMapper());
        ArrayList<Book> books =new ArrayList<>(bookList);
        return books;

    }
    @Override
    public Book getBookById(int bookId){
        try{
            Book book=db.queryForObject("SELECT * FROM book WHERE id=?",new BookRowMapper(), bookId);
            return book;

        }catch(Exception E){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
            
        }
    @Override
    public Book addBook(Book book){
        db.update("INSERT INTO book(name,imageUrl) values(?,?)", book.getName(), book.getImageUrl());
        Book savedBook = db.queryForObject("SELECT * FROM book WHERE name=? and imageUrl=?", 
        new BookRowMapper(), book.getName(),book.getImageUrl());
        return savedBook;
    }
    @Override
    public Book updateBook(int bookId, Book book){
        if(book.getName()!=null){
            db.update("Update book SET name=? WHERE id=?", book.getName(), bookId);
        }
        if(book.getImageUrl()!=null){
            db.update("Update book SET imageUrl=? WHERE id=?", book.getImageUrl(),bookId);
        }
        return getBookById(bookId);

    }
    @Override
    public void deleteBook(int bookId){
        db.update("DELETE FROM book WHERE id=?", bookId);
        
    }
}