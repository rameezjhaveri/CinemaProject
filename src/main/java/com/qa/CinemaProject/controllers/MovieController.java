package com.qa.CinemaProject.controllers;

import java.util.Date;
import java.util.List;


import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.qa.CinemaProject.constants.MappingConstants.CREATE_MOVIE;
import static com.qa.CinemaProject.constants.MappingConstants.GET_ALL_MOVIES;
import static com.qa.CinemaProject.constants.MappingConstants.GET_MOVIE_ID;
import static com.qa.CinemaProject.constants.MappingConstants.UPDATE_MOVIE;
import static com.qa.CinemaProject.constants.MappingConstants.DELETE_MOVIE;
import static com.qa.CinemaProject.constants.MappingConstants.PRICE_LIST;
import static com.qa.CinemaProject.constants.MappingConstants.SEND_EMAIL;
import static com.qa.CinemaProject.constants.MappingConstants.GET_ALL_BOOKINGS;
import static com.qa.CinemaProject.constants.MappingConstants.GET_USER_BOOKINGS;
import static com.qa.CinemaProject.constants.MappingConstants.BOOKING;
import static com.qa.CinemaProject.constants.MappingConstants.GET_POPULAR;
import static com.qa.CinemaProject.constants.MappingConstants.GET_SUCCESS_STATUS;
import static com.qa.CinemaProject.constants.MappingConstants.CHECK_MOVIES;
import com.qa.CinemaProject.email.Email;
import com.qa.CinemaProject.email.EmailApplication;
import com.qa.CinemaProject.entities.Booking;
import com.qa.CinemaProject.entities.BookingPayment;
import com.qa.CinemaProject.entities.Movie;
import com.qa.CinemaProject.entities.MovieEvent;
import com.qa.CinemaProject.entities.MovieTemp;
import com.qa.CinemaProject.entities.Popular;
import com.qa.CinemaProject.entities.PriceList;
import com.qa.CinemaProject.entities.Screen;
import com.qa.CinemaProject.service.BookingService;
import com.qa.CinemaProject.service.MovieEventService;
import com.qa.CinemaProject.service.MovieService;
import com.qa.CinemaProject.service.PaymentService;
import com.qa.CinemaProject.service.ScreenService;
import com.stripe.exception.StripeException;

@RequestMapping
@RestController
@CrossOrigin(origins = "http://localhost:3000") 
public class MovieController {
	
	private MovieService movieService;
	private PaymentService paymentService;
	private BookingService bookingService;
	private ScreenService screenService;
	private MovieEventService eventService;
	private EmailApplication email;
  
	@Value("${adult.price}")
	private String adultPrice;
	@Value("${child.price}")
	private String childPrice;
	@Value("${concessions.price}")
	private String concessionsPrice;
	
	@Value("${movie.one}")
	private String movieOne;
	@Value("${movie.two}")
	private String movieTwo;
	@Value("${movie.three}")
	private String movieThree;
	
	public MovieController(MovieService movieService, PaymentService paymentService, BookingService bookingService, EmailApplication email, ScreenService screenService, MovieEventService eventService) {
		this.movieService = movieService;
		this.paymentService = paymentService;
		this.bookingService = bookingService;
		this.screenService = screenService;
		this.eventService = eventService;
		this.email = email;
	}
	
	@PostMapping(CREATE_MOVIE)
	public void createMovie(@RequestBody Movie movie){
		this.movieService.createMovie(movie);
	}
	
	@GetMapping(GET_ALL_MOVIES)
	public List<Movie> getAllMovies() {
		return this.movieService.getAllMovies();
	}
	
	@GetMapping(GET_MOVIE_ID)
	public Movie getMovie(@PathVariable Long id) {
		return this.movieService.getMovie(id);
	}
	
	@PostMapping(UPDATE_MOVIE)
	public void updateMovie(@RequestBody Movie movie) {
		this.movieService.updateMovie(movie);
	}
	
	@DeleteMapping(DELETE_MOVIE)
	public void deleteMovie(@PathVariable Long id) {
		this.movieService.deleteMovie(id);
	}
	
	@GetMapping(PRICE_LIST)
	public PriceList getPriceList() {
		return new PriceList(adultPrice, childPrice, concessionsPrice);
	} 
  
    @PostMapping(SEND_EMAIL)
	public Email sendEmail(@RequestBody Email body) throws AddressException, MessagingException {
		email.sendMail(body);
		return body;
	}
	
	@PostMapping(BOOKING)
	public void booking(@RequestBody BookingPayment bookingPayment ) throws StripeException {
		System.out.println(bookingPayment.getEventToken());
		int cost = bookingPayment.getBooking().getTickets().stream().mapToInt(t -> t.getPrice()).sum();
		this.paymentService.makePayment(bookingPayment.getToken(),cost);
		if(paymentService.getStatus().equals("success")) {
			bookingPayment.getBooking().setUserId(bookingPayment.getUserId());
			this.bookingService.saveBooking(bookingPayment.getBooking(),bookingPayment.getHoldToken(), bookingPayment.getEventToken());
		}
	}
	
//	@PostMapping(CREATE_SINGLE_BOOKING)
//	public void saveBooking(@RequestBody Booking booking) {
//		this.bookingService.saveBooking(booking);
//	}
	
	@GetMapping(GET_ALL_BOOKINGS)
	public List<Booking> getAllBookings(){
		return this.bookingService.getAllBookings();
	}
	
	@GetMapping(GET_POPULAR)
	public Popular getPopular() {
		return movieService.getPopular(movieOne, movieTwo, movieThree);
	}
	
//	@PostMapping("/testSeats")
//	public void testSeats(@RequestBody Booking booking) {
//		this.seatsIo.bookTickets(booking.getTickets(), "33cdea62-50da-4fa7-a835-c09009a9a99b");
//	}
	
	@GetMapping(GET_SUCCESS_STATUS)
	public String getSuccessStatus() {
		return paymentService.getStatus();
	}
	
	@PostMapping(CHECK_MOVIES)
	public void checkMovies(@RequestBody List<Movie> movies) {
		this.movieService.checkAndAddMovies(movies);
	}
	
	@GetMapping("/testMovieTemp")
	public MovieTemp test() {
		return new MovieTemp(1, "", "", new Date());
	}
	
	@PostMapping("/getevents")
	public List<MovieEvent> getEvents(@RequestBody Movie movie){
		return eventService.getEventsByMovie(movie);
	}
	
	@PostMapping("/savescreen")
	public void saveScreen(@RequestBody Screen screen) {
		this.screenService.saveScreen(screen);
	}
	
	@GetMapping(GET_USER_BOOKINGS)
	@ResponseBody
	public List<Booking> getUserBookings(@PathVariable String id){
		System.out.println(id);
		return this.bookingService.getUserBookings(id);
	}

}
