package dormitory.web;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import dormitory.Rent;
import dormitory.Room;
import dormitory.RoomStat;
import dormitory.Student;
import dormitory.UsedService;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/rent")
public class RentController {
	private RestTemplate rest = new RestTemplate();
	
	@GetMapping
	public String showRentForm(Model model) {
		
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
		List<Student> listStudent = Arrays.asList(rest.getForObject("http://localhost:8080/student/findStudentToRentByTime/{time}", Student[].class, time));
		model.addAttribute("listStudent", listStudent);
		//getListRoomStat
		List<RoomStat> list = Arrays.asList(rest.getForObject("http://localhost:8080/room//findListRoomStatByTime/{time}", RoomStat[].class, time));
		List<RoomStat> listRoomStat = fillRoomStat(list);
		model.addAttribute("listRoomStat", listRoomStat);
		//
		Rent rent = new Rent();
		model.addAttribute("rent", rent);
		return "rentForm";
	}
	
	@PostMapping("/add")
	public String addRent(Model model, Long student_id, Long room_id) {
		Student student = rest.getForObject("http://localhost:8080/student/{id}", Student.class, student_id);
		Room room = rest.getForObject("http://localhost:8080/room/{id}", Room.class, room_id);
		
		Rent rent = new Rent();
		rent.setPrice(room.getUnitPrice());
		rent.setRoom(room);
		rent.setStudent(student);
		rest.postForObject("http://localhost:8080/rent", rent, Rent.class);
		log.info(student+" "+room);
		return "redirect:/rent";
	}
	
	//fillRoomStat
	public List<RoomStat> fillRoomStat( List<RoomStat> list) {
		List<RoomStat> listResult = new ArrayList<>();
		for(  RoomStat i:list) {
			if( i.getEmptySlot()>=1 ) {
				listResult.add(i);
			}
		}
		return listResult;
	}
}
