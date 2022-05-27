package dormitory.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import dormitory.Student;
import dormitory.UsedService;
import dormitory.Visitor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/visitor")
public class VisitorController {
	private RestTemplate rest = new RestTemplate();
	
	@GetMapping
	public String showVisitorForm(Model model) {
		
		//getCurrentMonth
				LocalDate today = LocalDate.now();
				int month = today.getMonthValue();
				int year = today.getYear();
				String time="";
				if( month < 10 )
					time = year+"-"+"0"+month+"-"+"01";
				else time =year+"-"+month+"-"+"01";
				//time
				String lableTime = month+"/"+year;
				model.addAttribute("lableTime", lableTime);
				//getListStudentToRent
				List<Student> listStudent = Arrays.asList(rest.getForObject("http://localhost:8080/student/findCurrentRentStudent/{time}", Student[].class, time));
				model.addAttribute("listStudent", listStudent);
		return "visitorForm";
	}
	
	@PostMapping("/add") 
	public String addVistor(@RequestParam("idNumber") String idNumber, @RequestParam("fullname") String fullname, 
			@RequestParam("student_id") Long student_id, @RequestParam("dob") String dob) throws ParseException {
		Student student = rest.getForObject("http://localhost:8080/student/{id}", Student.class, student_id);
		Visitor vistor = new Visitor();
		vistor.setStudentVis(student);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = (Date) sdf.parse(dob);
		vistor.setDob(d);
		vistor.setFullname(fullname);
		vistor.setIdNumber(idNumber);
		rest.postForObject("http://localhost:8080/visitor", vistor, Visitor.class);
		return "home";
	}
}
