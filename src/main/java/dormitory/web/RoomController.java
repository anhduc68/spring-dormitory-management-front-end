package dormitory.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import dormitory.Room;
@Controller
@RequestMapping("/room")
public class RoomController {
	private RestTemplate rest = new RestTemplate();

	// show listRooms
	@GetMapping
	public String showListRoom(Model model) {
		List<Room> listRoom = Arrays.asList(rest.getForObject("http://localhost:8080/room", Room[].class));
		model.addAttribute("listRoom", listRoom);
		return "showListRoomForm";
	}

	// addRoom
	@GetMapping("/addRoom")
	public String ShowAddRoomForm(Model model) {
		model.addAttribute("room", new Room());
		model.addAttribute("msg", "");
		return "addRoomForm";
	}

	@PostMapping("addRoom")
	public String savePhong(Room room, Model model) {
		String isExist = rest.getForObject("http://localhost:8080/room/isExist/{roomNumber}", String.class,
				room.getRoomNumber());
		if (isExist.equals("true")) {
			model.addAttribute("room", room);
			model.addAttribute("msg", "Tên phòng đã tồn tại");
			return "addRoomForm";
		} else {
			rest.postForObject("http://localhost:8080/room", room, Room.class);
			return "redirect:/room";
		}

	}

	// findByUnitPrice
	@PostMapping("findRoomByUnitPrice")
	public String findRoomByUnitPrice(int unitPrice, Model model) {
		List<Room> listRoom = Arrays.asList(
				rest.getForObject("http://localhost:8080/room/findByUnitPrice/{unitPrice}", Room[].class, unitPrice));
		model.addAttribute("listRoom", listRoom);
		return "showListRoomForm";
	}

	// edit Room
	@GetMapping("editRoom")
	public String editRoom(Long id, Model model) {
		Room room = rest.getForObject("http://localhost:8080/room/{id}", Room.class, id);
		model.addAttribute("room", room);
		return "editRoomForm";
	}

	@PostMapping("editRoom")
	public String updateRoom(Room room, Model model) {
		String isExist = rest.getForObject("http://localhost:8080/room/isExist/{roomNumber}", String.class,
				room.getRoomNumber());
		if (isExist.equals("true")) {
			model.addAttribute("room", room);
			model.addAttribute("msg", "Tên phòng đã tồn tại");
			return "addRoomForm";
		} else {
			rest.postForObject("http://localhost:8080/room", room, Room.class);
			return "redirect:/room";
		}

	}
	
	//deleteRoom
	@GetMapping("/deleteRoom")
	public String deletePhong(Long id) {
		rest.delete("http://localhost:8080/room/{id}", id);
		return "redirect:/room";
	}
}
