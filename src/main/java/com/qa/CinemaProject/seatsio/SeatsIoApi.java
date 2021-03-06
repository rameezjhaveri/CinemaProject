package com.qa.CinemaProject.seatsio;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qa.CinemaProject.entities.MovieEvent;
import com.qa.CinemaProject.entities.Ticket;

import seatsio.SeatsioClient;
import seatsio.events.Event;

@Service
public class SeatsIoApi {
	
	@Value("${2D.screen}")
	private String key;
	private SeatsioClient client;
	
	public SeatsIoApi(@Value("${SeatsIo.Key}") String key) {
		this.client = new SeatsioClient(key);
	}
	
	public MovieEvent createEvent(long movieId, String screenId, String eventKey, Date date) {
		Event event = client.events.create(key, eventKey);
		MovieEvent tempEvent = new MovieEvent(movieId, screenId, event.key, date);
		return tempEvent;
	}
	
	public void bookTickets(List<Ticket> tickets, String eventKey, String holdToken) {
		tickets.stream().map(t -> t.getLocation()).forEach(t -> System.out.println(t));
		List<String> tickList = tickets.stream().map(t -> t.getLocation()).collect(Collectors.toList());
		System.out.println(tickList + " " + eventKey + " " + holdToken);
		client.events.book(eventKey, tickList, holdToken);
	}
}
