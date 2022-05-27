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

import dormitory.Motorcycle;
import dormitory.Student;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/motorcycle")
public class MotorcycleController {
	private RestTemplate rest = new RestTemplate();

	// show listMotorcycles
	@GetMapping
	public String showListMotorcycle(Model model) {
		List<Motorcycle> listMotorcycle = Arrays
				.asList(rest.getForObject("http://localhost:8080/motorcycle", Motorcycle[].class));
		model.addAttribute("listMotorcycle", listMotorcycle);
		return "showListMotorcycleForm";
	}

	// addMotorcycle
	@GetMapping("/addMotorcycle")
	public String ShowAddMotorcycleForm(Model model) {
		model.addAttribute("msg", "");
		// getAllStudet
		List<Student> listStudent = Arrays.asList(rest.getForObject("http://localhost:8080/student", Student[].class));
		model.addAttribute("listStudent", listStudent);
		return "addMotorcycleForm";
	}

	@PostMapping("addMotorcycle")
	public String saveMotorcycle(@RequestParam("type") String type, @RequestParam("licencePlate") String licencePlate,
			@RequestParam("student_id") String student_id, Model model) {
		String isExist = rest.getForObject("http://localhost:8080/motorcycle/isExist/{motorcycleNumber}", String.class,
				licencePlate);
		if (isExist.equals("true")) {
			model.addAttribute("msg", "Biển sỗ xe đã tồn tại");
			model.addAttribute("type", type);
			model.addAttribute("licencePlate", licencePlate);
			List<Student> listStudent = Arrays
					.asList(rest.getForObject("http://localhost:8080/student", Student[].class));
			model.addAttribute("listStudent", listStudent);
			return "addMotorcycleForm";
		} else {
			Motorcycle motorcycle = new Motorcycle();
			motorcycle.setLicencePlate(licencePlate);
			motorcycle.setType(type);
			Student student = rest.getForObject("http://localhost:8080/student/{id}", Student.class, student_id);
			motorcycle.setStudentMoto(student);
			log.info(motorcycle.toString());
			rest.postForObject("http://localhost:8080/motorcycle", motorcycle, Motorcycle.class);
			return "redirect:/motorcycle";
		}
	}

	// findByType
	@PostMapping("/findMotorcycleByType")
	public String findMotorcycleByType(String type, Model model) {
		List<Motorcycle> listMotorcycle = Arrays.asList(
				rest.getForObject("http://localhost:8080/motorcycle/findByType/{type}", Motorcycle[].class, type));
		model.addAttribute("listMotorcycle", listMotorcycle);
		return "showListMotorcycleForm";
	}

	// edit Motorcycle
	@GetMapping("/editMotorcycle")
	public String editMotorcycle(Long id, Model model) {
		Motorcycle motorcycle = rest.getForObject("http://localhost:8080/motorcycle/{id}", Motorcycle.class, id);
		String type = motorcycle.getType();
		String licencePlate = motorcycle.getLicencePlate();
		model.addAttribute("id", id);
		model.addAttribute("type", type);
		model.addAttribute("licencePlate", licencePlate);
		List<Student> listStudent = Arrays.asList(rest.getForObject("http://localhost:8080/student", Student[].class));
		model.addAttribute("listStudent", listStudent);
		return "editMotorcycleForm";
	}

	@PostMapping("/editMotorcycle")
	public String UpdateMotorcycle(@RequestParam("id") Long id, @RequestParam("type") String type,
			@RequestParam("licencePlate") String licencePlate, @RequestParam("student_id") String student_id,
			Model model) {
		Motorcycle motorcycle = new Motorcycle();
		motorcycle.setId(id);
		motorcycle.setLicencePlate(licencePlate);
		motorcycle.setType(type);
		Student student = rest.getForObject("http://localhost:8080/student/{id}", Student.class, student_id);
		motorcycle.setStudentMoto(student);
		log.info(motorcycle.toString());
		rest.postForObject("http://localhost:8080/motorcycle", motorcycle, Motorcycle.class);
		return "redirect:/motorcycle";
	}

	// deleteMotorcycle
	@GetMapping("/deleteMotorcycle")
	public String deletePhong(Long id) {
		rest.delete("http://localhost:8080/motorcycle/{id}", id);
		return "redirect:/motorcycle";
	}
}
