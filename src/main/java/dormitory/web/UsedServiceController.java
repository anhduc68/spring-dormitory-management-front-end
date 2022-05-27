package dormitory.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import dormitory.Service;
import dormitory.Student;
import dormitory.UsedService;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/usedService")
public class UsedServiceController {
	private RestTemplate rest = new RestTemplate();

	@GetMapping
	public String showUsedServiceForm(Model model) {
		// get List Student
		List<Student> listStudent = Arrays.asList(rest.getForObject("http://localhost:8080/student", Student[].class));
		model.addAttribute("listStudent", listStudent);

		// get List Service
		List<Service> listService = Arrays.asList(rest.getForObject("http://localhost:8080/service", Service[].class));
		model.addAttribute("listStudent", listStudent);

		model.addAttribute("listService", listService);

		return "showUsedServiceForm";
	}
	
	@PostMapping("/add")
	public String addUsedService(@RequestParam("student_id") Long student_id, @RequestParam("service_id") Long service_id
			,@RequestParam("quantity") int quantity ) {
		Student student = rest.getForObject("http://localhost:8080/student/{id}", Student.class, student_id);
		Service service = rest.getForObject("http://localhost:8080/service/{id}", Service.class, service_id);
		UsedService usedService = new UsedService();
		usedService.setQuantity(quantity);
		usedService.setService(service);
		usedService.setStudentServ(student);
		usedService.setTotalAmount(quantity*service.getUnitPrice());
		rest.postForObject("http://localhost:8080/usedService", usedService, UsedService.class);
		return "home";
	}
}
