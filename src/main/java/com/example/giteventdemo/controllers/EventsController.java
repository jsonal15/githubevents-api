package com.example.giteventdemo.controllers;

import java.util.Arrays;
import org.springframework.http.HttpEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.giteventdemo.Models.Events;

@Controller
public class EventsController {

	final String GIT_API_EVENTS_URL = "https://api.github.com/repos/";

	@Bean
	static RestTemplate getRestTemplate() {
		return new RestTemplate();
	};

	@GetMapping("/showevents")
	public String showEvents(@RequestParam String reponame, Model model) {
	try {
			StringBuffer sEventsUrl = new StringBuffer();
			sEventsUrl.append(GIT_API_EVENTS_URL).append(reponame)
					.append("/events");

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<String> entity = new HttpEntity<String>(headers);

			ResponseEntity<Events[]> response = getRestTemplate().getForEntity(
					sEventsUrl.toString(), Events[].class, entity);
			Events[] events = response.getBody();


			model.addAttribute("events", Arrays.asList(events));
		} catch (RestClientException e) {

			throw new RestClientException(
					"Exception occurred while getting events from the Github api "+e.getMessage());
		}

		return "eventslist";
	}

}
