package com.example.giteventdemo.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.example.giteventdemo.Models.Repo;

@Controller
public class RepoController {

	final String GIT_API_REPO_URL = "https://api.github.com/users/";


	@Bean
	static RestTemplate getRestTemplate() {
		return new RestTemplate();
	};

	@GetMapping("/getrepos")
	public String showUsers(Model model) {
		model.addAttribute("repos", new Repo());
		return "user";

	}

	@PostMapping("/getrepos")
	public String getRepos(@ModelAttribute Repo repo, Model model) {
		List<Repo> listRepo = new ArrayList<Repo>();
		try {

			StringBuffer url = new StringBuffer();

			url.append(GIT_API_REPO_URL).append(repo.getUser())
					.append("/repos");

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<String> entity = new HttpEntity<String>(headers);

			ResponseEntity<Repo[]> response = getRestTemplate().getForEntity(
					url.toString(), Repo[].class, entity);
			Repo[] repoObject = response.getBody();

		
			listRepo = Arrays.asList(repoObject);

			/*
			 * listRepo = Arrays.asList(repoObject).stream() .filter(e ->
			 * e.getRepo() instanceof Repo) .map(e -> (Repo) e.getRepo())
			 * .collect(Collectors.toList());
			 */

			
			
		    	  model.addAttribute("repos", listRepo);
		     

		} catch (RestClientException e) {
			listRepo = null;
			model.addAttribute("repos",listRepo);

			throw new RestClientException(
					"Exception occurred while getting Repos from the Github api "+e.getMessage());
		}

		return "repolist";
	}

}
