package io.novatec.todoui;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Controller
public class TodouiApplication {

	@Value("${backend.url}")
	String endpoint;
	RestTemplate template = new RestTemplate();

	@PostConstruct
	public void postConstruct(){

		System.out.println(" UI initialized for backend at "+endpoint);
	}

	@GetMapping
	public String getItems(Model model){

		System.out.println(" Invoking: "+ endpoint + "/todos/");
		ResponseEntity<String[]> response = template.getForEntity(endpoint+"/todos/", String[].class);
		if(response != null) model.addAttribute("items", response.getBody());
		return "items";

	}

	@PostMapping
	public String addItem(String toDo){

		template.postForEntity(endpoint+"/todos/"+toDo, null, String.class);
		return "redirect:/";

	}

	@PostMapping("{toDo}")
	public String setItemDone(@PathVariable String toDo){


		template.delete(endpoint+"/todos/"+toDo);
		return "redirect:/";

	}

	public static void main(String[] args) {
		SpringApplication.run(TodouiApplication.class, args);
	}
}
