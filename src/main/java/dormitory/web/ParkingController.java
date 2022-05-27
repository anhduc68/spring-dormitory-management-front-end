package dormitory.web;

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

import dormitory.Parking;
import dormitory.Motorcycle;
import dormitory.Student;
import dormitory.Ticket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/parking")
public class ParkingController {
	private RestTemplate rest = new RestTemplate();

	// showListParkings
	@GetMapping
	public String showParkingForm(Model model) {
		List<Motorcycle> listMotorcycle = Arrays
				.asList(rest.getForObject("http://localhost:8080/motorcycle/findMotorcycleToPark", Motorcycle[].class));
		model.addAttribute("listMotorcycle", listMotorcycle);
		return "parkingForm";
	}

	// checkin
	@GetMapping("/checkin")
	public String checkin(@RequestParam("motorcycle_id") Long motorcycle_id) {
		// get Motorcycle
		Motorcycle motorcycle = rest.getForObject("http://localhost:8080/motorcycle/{id}", Motorcycle.class,
				motorcycle_id);
		Parking parking = new Parking();
		parking.setCheckin(new Date());
		parking.setMotorcycleParking(motorcycle);
		rest.postForObject("http://localhost:8080/parking", parking, Parking.class);
		return "redirect:/parking";
	}

	// show form checkout
	@GetMapping("/checkout")
	public String checkout(Model model) {
		// findParkingToCheckout
		List<Parking> listParking = Arrays
				.asList(rest.getForObject("http://localhost:8080/parking/findParkingToCheckout", Parking[].class));
		model.addAttribute("listParking", listParking);

		// format checkin
		for (Parking i : listParking) {
			i.formatCheckin();
		}
		return "checkoutForm";
	}

	// checkout for Parking
	@GetMapping("/checkoutParking")
	public String checkoutParking(Model model, @RequestParam("parking_id") Long parking_id) {
		// do sth
		Parking parking = rest.getForObject("http://localhost:8080/parking/{parking_id}", Parking.class, parking_id);
		model.addAttribute("parking", parking);
		parking.formatCheckin();
		// log.info(parking.toString());
		long motorcycle_id = parking.getMotorcycleParking().getId();
		// get current month
		LocalDate today = LocalDate.now();
		int month = today.getMonthValue();
		int year = today.getYear();
		String time = "";
		if (month < 10)
			time = year + "-" + "0" + month + "-" + "01";
		else
			time = year + "-" + month + "-" + "01";
		// check motorcycle have month Ticket
		String isExist = rest.getForObject(
				"http://localhost:8080/ticket/isExistByMotorcycleIDAndTime/{motorcycle_id}/{time}", String.class,
				motorcycle_id, time);

		// if motorcycle hasn't month ticket
		// price is 3000
		if (isExist == null) {
			parking.setCheckout(new Date());
			parking.formatCheckout();
			parking.setPrice(3000);
			rest.postForObject("http://localhost:8080/parking", parking, Parking.class);

			model.addAttribute("isMonthTicket", "Không");
		}
		// count the number checkin of this motorcycle on this day
		// if the number checkin > 2 : price 3000
		// else price : 0
		else {
			model.addAttribute("isMonthTicket", "Có");
			// get current Date
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy=MM-dd");
			String currentTime = sdf.format(new Date());
			String countNumberParkingOfMotorcycleOnDay = rest.getForObject(
					"http://localhost:8080/parking/countNumberParkingOfMotorcycleOnDay/{motorcycle_id}/{time}",
					String.class, motorcycle_id, currentTime);
			int count = 0;
			if (countNumberParkingOfMotorcycleOnDay == null)
				count = 0;
			else
				count = Integer.parseInt(countNumberParkingOfMotorcycleOnDay);
			//
			if (count < 3) {
				parking.setPrice(0);
			} else
				parking.setPrice(3000);
			parking.setCheckout(new Date());
			parking.formatCheckout();
			rest.postForObject("http://localhost:8080/parking", parking, Parking.class);
			// print Number Parking of Motorcycle On Day
			String msg = "Số lượt gửi xe trong ngày: " + count;
			model.addAttribute("msg", msg);
		}
		return "confirmCheckoutForm";
	}

	// findMotorcycleToParkByLicencePlate
	@PostMapping("/findByLicencePlate")
	public String findByLicencePlate(String licencePlate, Model model) {
		List<Motorcycle> listMotorcycle = Arrays.asList(rest.getForObject(
				"http://localhost:8080/motorcycle/findMotorcycleToParkByLicencePlate" + "/{licencePlate}",
				Motorcycle[].class, licencePlate));
		model.addAttribute("listMotorcycle", listMotorcycle);
		return "parkingForm";
	}

}
