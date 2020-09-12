//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

import com.fjordtek.bookstore.model.*;

@Controller
public class BookController {

	protected static final String landingPageURL      = "index";
	protected static final String bookListPageURL     = "booklist";
	protected static final String bookAddPageURL      = "bookadd";
	protected static final String bookDeletePageURL   = "bookdelete";
	protected static final String bookEditPageURL     = "bookedit";

	private HttpServerLogger     httpServerLogger     = new HttpServerLogger();
	private HttpExceptionHandler httpExceptionHandler = new HttpExceptionHandler();

	@Autowired
	private BookRepository       bookRepository;

 @RequestMapping(
         value    = bookListPageURL,
         method   = { RequestMethod.GET, RequestMethod.POST }
 )
 public String defaultWebFormGet(Model dataModel, HttpServletRequest requestData) {

     httpServerLogger.logMessageNormal(
             requestData,
             bookListPageURL + ": " + "HTTPOK"
     );

     dataModel.addAttribute("books", bookRepository.findAll());
     
     return bookListPageURL;

 }

 //////////////////////////////
 // ADD BOOK
 
 @RequestMapping(
		 value    = bookAddPageURL,
		 method   = { RequestMethod.GET, RequestMethod.PUT }
 )
 public String webFormAddBook(Model dataModel, HttpServletRequest requestData) {
	 
     httpServerLogger.logMessageNormal(
             requestData,
             bookAddPageURL + ": " + "HTTPOK"
     );
	 
	 dataModel.addAttribute("book", new Book());
	 
     return bookAddPageURL;
 }

 @RequestMapping(
		 value = bookAddPageURL,
		 method = RequestMethod.POST
 )
 public String webFormSaveNewBook(Book book, HttpServletRequest requestData) {

     httpServerLogger.logMessageNormal(
             requestData,
             bookEditPageURL + ": " + "HTTPOK"
     );

     bookRepository.save(book);
     
     return "redirect:" + bookListPageURL;
 }
 
 //////////////////////////////
 // DELETE BOOK
 
 @RequestMapping(
		 value  = bookDeletePageURL + "/{id}",
		 method = RequestMethod.GET
 )
 public String webFormDeleteBook(
		 @PathVariable("id") long bookId,
		 HttpServletRequest requestData
 ) {
	 
     httpServerLogger.logMessageNormal(
             requestData,
             bookDeletePageURL + ": " + "HTTPOK"
     );
	 
 	bookRepository.deleteById(bookId);
 	
    return "redirect:../" + bookListPageURL;
 }

 //////////////////////////////
 // UPDATE BOOK
 
 @RequestMapping(
		 value  = bookEditPageURL + "/{id}",
		 method = RequestMethod.GET
 )
 public String webFormEditBook(
		 @PathVariable("id") long bookId,
		 Model dataModel, HttpServletRequest requestData
 ) {
	 
     httpServerLogger.logMessageNormal(
             requestData,
             bookEditPageURL + ": " + "HTTPOK"
     );

    Book book = bookRepository.findById(bookId).get();
    dataModel.addAttribute("book", book);

    return bookEditPageURL;
 }
 
 @RequestMapping(
		 value = bookEditPageURL + "/{id}",
		 method = RequestMethod.POST
 )
 public String webFormUpdateBook(@ModelAttribute("book") Book book, HttpServletRequest requestData) {

     httpServerLogger.logMessageNormal(
             requestData,
             bookEditPageURL + ": " + "HTTPOK"
     );

     bookRepository.save(book);
     
     return "redirect:../" + bookListPageURL;
 }
 
 
 //////////////////////////////
 // REDIRECTS
 
 @RequestMapping(
         value  = { "/", landingPageURL },
         method =  RequestMethod.GET
 )
 @ResponseStatus(HttpStatus.FOUND)
 public String redirectToDefaultWebForm() {
     return "redirect:" + bookListPageURL;
 }
 
 // Other URL requests
@RequestMapping(
        value  = "*",
        method = { RequestMethod.GET, RequestMethod.POST }
)
 public String errorWebForm(HttpServletRequest requestData) {
    return httpExceptionHandler.notFoundErrorHandler(requestData);
 }
 
}
