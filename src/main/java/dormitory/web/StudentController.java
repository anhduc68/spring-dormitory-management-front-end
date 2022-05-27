package dormitory.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import dormitory.Rent;
import dormitory.Student;
import dormitory.Ticket;
import dormitory.UsedService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/student")
public class StudentController {
	private RestTemplate rest = new RestTemplate();

	// show listStudents
	@GetMapping
	public String showListStudent(Model model) {
		List<Student> listStudent = Arrays.asList(rest.getForObject("http://localhost:8080/student", Student[].class));
		model.addAttribute("listStudent", listStudent);

		// format dob
		for (Student i : listStudent) {
			i.format();
		}
		return "showListStudentForm";
	}

	// addStudent
	@GetMapping("/addStudent")
	public String ShowAddStudentForm(Model model) {
		model.addAttribute("student", new Student());
		model.addAttribute("msg", "");
		return "addStudentForm";
	}

	@PostMapping("addStudent")
	public String saveStudent(@RequestParam("studentID") String studentID, @RequestParam("idNumber") String idNumber,
			@RequestParam("fullname") String fullname, @RequestParam("dob") String dob,
			@RequestParam("classID") String classID, @RequestParam("hometown") String hometown, Model model)
			throws ParseException {
		String isExist = rest.getForObject("http://localhost:8080/student/isExist/{studentID}", String.class,
				studentID);
		if (isExist.equals("true")) {
			model.addAttribute("studentID", studentID);
			model.addAttribute("idNumber", idNumber);
			model.addAttribute("fullname", fullname);
			model.addAttribute("dob", dob);
			model.addAttribute("classID", classID);
			model.addAttribute("hometown", hometown);
			model.addAttribute("msg", "Mã Sinh Viên đã tồn tại");
			return "addStudentForm";
		} else {
			Student student = new Student();
			student.setStudentID(studentID);
			student.setIdNumber(idNumber);
			student.setFullname(fullname);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = (Date) sdf.parse(dob);
			student.setDob(d);
			student.setHometown(hometown);
			student.setClassID(classID);
			rest.postForObject("http://localhost:8080/student", student, Student.class);
			return "redirect:/student";
		}
	}

	// findByUnitPrice
	@PostMapping("findStudentByName")
	public String findStudentByUnitPrice(String name, Model model) {
		List<Student> listStudent = Arrays
				.asList(rest.getForObject("http://localhost:8080/student/findByName/{name}", Student[].class, name));
		model.addAttribute("listStudent", listStudent);
		return "showListStudentForm";
	}

	// edit Student
	@GetMapping("editStudent")
	public String editStudent(Long id, Model model) {
		Student student = rest.getForObject("http://localhost:8080/student/{id}", Student.class, id);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// get property
		String studentID = student.getStudentID();
		String idNumber = student.getIdNumber();
		String fullname = student.getFullname();
		String dob = sdf.format(student.getDob());
		String classID = student.getClassID();
		String hometown = student.getHometown();
		// set to model
		model.addAttribute("id", id);
		model.addAttribute("studentID", studentID);
		model.addAttribute("idNumber", idNumber);
		model.addAttribute("fullname", fullname);
		model.addAttribute("dob", dob);
		model.addAttribute("classID", classID);
		model.addAttribute("hometown", hometown);

		return "editStudentForm";
	}

	@PostMapping("editStudent")
	public String updateStudent(@RequestParam("id") Long id, @RequestParam("studentID") String studentID,
			@RequestParam("idNumber") String idNumber, @RequestParam("fullname") String fullname,
			@RequestParam("dob") String dob, @RequestParam("classID") String classID,
			@RequestParam("hometown") String hometown, Model model) throws ParseException {

		Student student = new Student();
		student.setId(id);
		student.setStudentID(studentID);
		student.setIdNumber(idNumber);
		student.setFullname(fullname);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = (Date) sdf.parse(dob);
		student.setDob(d);
		student.setHometown(hometown);
		student.setClassID(classID);
//			log.info(student.toString());
		rest.postForObject("http://localhost:8080/student", student, Student.class);
		return "redirect:/student";
	}

	// deleteStudent
	@GetMapping("/deleteStudent")
	public String deletePhong(Long id) {
		rest.delete("http://localhost:8080/student/{id}", id);
		return "redirect:/student";
	}

	// Show month bill

	@GetMapping("/requestBill")
	public String showRequestBillForm(Model model) {
		List<Student> listStudent = Arrays.asList(rest.getForObject("http://localhost:8080/student", Student[].class));
		model.addAttribute("listStudent", listStudent);
		return "requestBillForm";
	}

	@PostMapping("/requestBill")
	public String showBill(int month, Long student_id, Model model) {
		// gét Student
		Student student = rest.getForObject("http://localhost:8080/student/{student_id}", Student.class, student_id);
		student.format();
		// get Time
		int year = Calendar.getInstance().get(Calendar.YEAR);
		String time = "";
		if (month < 10) {
			time = year + "-" + "0" + month + "-" + "01";
		} else
			time = year + "-" + month + "-" + "01";
		String lableTime = month + "/" + year;
		model.addAttribute("lableTime", lableTime);
		// UsedServiceMoney
		//findUsedServiceByStudentIDAndTime
		List<UsedService> listUsedService = Arrays.asList(
				rest.getForObject("http://localhost:8080/usedService/findUsedServiceByStudentIDAndTime/{student_id}/{time}", 
						UsedService[].class, student_id, time));
		int service_money = 0;
		for( UsedService i: listUsedService ) {
			service_money += i.getTotalAmount();
			i.formatserviceUsageTime();
		}
		//findTicketByStudentIDAndTime
		List<Ticket> listTicket = Arrays.asList(
				rest.getForObject("http://localhost:8080/ticket/findTicketByStudentIDAndTime/{student_id}/{time}", 
						Ticket[].class, student_id, time));
		int ticket_money = 0;
		for( Ticket i: listTicket) {
			ticket_money+= i.getPrice();
			 i.formatUsageTime();
		}
		//findRentByStudentIDAndTime	
		List<Rent> listRent = Arrays.asList(
				rest.getForObject("http://localhost:8080/rent/findRentByStudentIDAndTime/{student_id}/{time}", 
						Rent[].class, student_id, time));
		int rent_money = 0;
		for( Rent i: listRent) {
			rent_money += i.getPrice();
			i.formatRentDate();
		}
		// handle null value
		
		
		
		int total = service_money + ticket_money + rent_money;
		
		// set to model 
		model.addAttribute("student", student);
		model.addAttribute("listUsedService", listUsedService);
		model.addAttribute("service_money", service_money);
		model.addAttribute("listTicket", listTicket);
		model.addAttribute("ticket_money", ticket_money);
		model.addAttribute("listRent", listRent);
		model.addAttribute("rent_money", rent_money);
		model.addAttribute("total", total);
		return "billForm";
	}
}
