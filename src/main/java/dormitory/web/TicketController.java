package dormitory.web;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import dormitory.Ticket;
import dormitory.Motorcycle;
import dormitory.Student;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/ticket")
public class TicketController {
	private RestTemplate rest = new RestTemplate();

	// showListTickets
	@GetMapping
	public String showListTicket(Model model) {
		List<Ticket> listTicket = Arrays.asList(rest.getForObject("http://localhost:8080/ticket", Ticket[].class));
		for( Ticket i: listTicket ) {
			i.formatUsageTime();
		}
		model.addAttribute("listTicket", listTicket);
		return "showListTicketForm";
	}

	// addTicket
	@GetMapping("/addTicket")
	public String ShowAddTicketForm(Model model) {
		LocalDate today = LocalDate.now();
		int month = today.getMonthValue();
		int year = today.getYear();
		String time = "";
		if (month < 10)
			time = year + "-" + "0" + month + "-" + "01";
		else
			time = year + "-" + month + "-" + "01";
		// time
		String lableTime = month + "/" + year;
		model.addAttribute("lableTime", lableTime);
		// getAllMotorcycle
		List<Motorcycle> listMotorcycle = Arrays.asList(rest.getForObject(
				"http://localhost:8080/motorcycle/findMotorcycleToBuyTicket/{time}", Motorcycle[].class, time));
		model.addAttribute("listMotorcycle", listMotorcycle);
		return "addTicketForm";
	}

	@PostMapping("addTicket")
	public String saveTicket(@RequestParam("motorcycle_id") Long motorcycle_id, Model model) {
		Ticket ticket = new Ticket();
		Motorcycle motorcycle = rest.getForObject("http://localhost:8080/motorcycle/{id}", Motorcycle.class,
				motorcycle_id);
		// get Student_ID
		Long student_id = motorcycle.getStudentMoto().getId();
		// get CurrentTime
		LocalDate today = LocalDate.now();
		int month = today.getMonthValue();
		int year = today.getYear();
		String time = "";
		if (month < 10)
			time = year + "-" + "0" + month + "-" + "01";
		else
			time = year + "-" + month + "-" + "01";
		// contTicketOfStudentByTime
		String countTicketOfStudentByTime = rest.getForObject(
				"http://localhost:8080/ticket/countTicketOfStudentByTime/{student_id}/{time}", String.class, student_id,
				time);
		// handle
		int count = 0;
		if (countTicketOfStudentByTime == null)
			count = 0;
		else
			count = Integer.parseInt(countTicketOfStudentByTime);
		// add Ticket if count < 2
		if (count < 2) {
			ticket.setMotorcycle(motorcycle);
			ticket.setPrice(100000);
			ticket.setStudent_id(motorcycle.getStudentMoto().getId());
			rest.postForObject("http://localhost:8080/ticket", ticket, Ticket.class);
			return "redirect:/ticket";
		}

		else {// print msg
			String msg = "Sinh viên " + motorcycle.getStudentMoto().getFullname() + " đã đăng ký 2 vé cho tháng "
					+ month + " rồi!";
			model.addAttribute("msg", msg);
			String lableTime = month + "/" + year;
			model.addAttribute("lableTime", lableTime);
			// getAllMotorcycle
			List<Motorcycle> listMotorcycle = Arrays.asList(rest.getForObject(
					"http://localhost:8080/motorcycle/findMotorcycleToBuyTicket/{time}", Motorcycle[].class, time));
			model.addAttribute("listMotorcycle", listMotorcycle);
			return "addTicketForm";
		}

//			
	}

	// findTicketByLicencePlate
	@PostMapping("/findTicketByLicencePlate")
	public String findTicketByType(String licencePlate, Model model) {
		List<Ticket> listTicket = Arrays.asList(rest.getForObject(
				"http://localhost:8080/ticket/findTicketByLicencePlate/{licencePlate}", Ticket[].class, licencePlate));
		model.addAttribute("listTicket", listTicket);
		return "showListTicketForm";
	}

//
	// edit Ticket
	@GetMapping("/editTicket")
	public String editTicket(Long id, Model model) {
		LocalDate today = LocalDate.now();
		int month = today.getMonthValue();
		int year = today.getYear();
		String time = "";
		if (month < 10)
			time = year + "-" + "0" + month + "-" + "01";
		else
			time = year + "-" + month + "-" + "01";
		// id
		model.addAttribute("id", id);
		List<Motorcycle> listMotorcycle = Arrays.asList(rest.getForObject(
				"http://localhost:8080/motorcycle/findMotorcycleToBuyTicket/{time}", Motorcycle[].class, time));
		model.addAttribute("listMotorcycle", listMotorcycle);
		return "editTicketForm";
	}

	@PostMapping("/editTicket")
	public String UpdateTicket(@RequestParam("id") Long id, @RequestParam("motorcycle_id") Long motorcycle_id,
			Model model) {
		Ticket ticket = rest.getForObject("http://localhost:8080/ticket/{id}", Ticket.class, id);
		Motorcycle motorcycle = rest.getForObject("http://localhost:8080/motorcycle/{id}", Motorcycle.class,
				motorcycle_id);
		// get Student_ID
		Long student_id = motorcycle.getStudentMoto().getId();
		// get CurrentTime
		LocalDate today = LocalDate.now();
		int month = today.getMonthValue();
		int year = today.getYear();
		String time = "";
		if (month < 10)
			time = year + "-" + "0" + month + "-" + "01";
		else
			time = year + "-" + month + "-" + "01";
		// contTicketOfStudentByTime
		String countTicketOfStudentByTime = rest.getForObject(
				"http://localhost:8080/ticket/countTicketOfStudentByTime/{student_id}/{time}", String.class, student_id,
				time);
		// handle
		int count = 0;
		if (countTicketOfStudentByTime == null)
			count = 0;
		else
			count = Integer.parseInt(countTicketOfStudentByTime);
		// add Ticket if count < 2
		if (count < 2) {
			ticket.setMotorcycle(motorcycle);
			ticket.setStudent_id(motorcycle.getStudentMoto().getId());
			rest.postForObject("http://localhost:8080/ticket", ticket, Ticket.class);
			return "redirect:/ticket";
		}

		else {// print msg
			String msg = "Sinh viên " + motorcycle.getStudentMoto().getFullname() + " đã đăng ký 2 vé cho tháng "
					+ month + " rồi!";
			model.addAttribute("msg", msg);
			model.addAttribute("id", id);
			String lableTime = month + "/" + year;
			model.addAttribute("lableTime", lableTime);
			// getAllMotorcycle
			List<Motorcycle> listMotorcycle = Arrays.asList(rest.getForObject(
					"http://localhost:8080/motorcycle/findMotorcycleToBuyTicket/{time}", Motorcycle[].class, time));
			model.addAttribute("listMotorcycle", listMotorcycle);
			return "editTicketForm";
		}
	}

	// deleteTicket
	@GetMapping("/deleteTicket")
	public String deletePhong(Long id) {
		rest.delete("http://localhost:8080/ticket/{id}", id);
		return "redirect:/ticket";
	}
}
