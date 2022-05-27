package dormitory.web;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import dormitory.Service;
import dormitory.ServiceStat;
import dormitory.UsedService;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/service")
public class ServiceController {
	private RestTemplate rest = new RestTemplate();

	// show listServices
	@GetMapping
	public String showListService(Model model) {
		List<Service> listService = Arrays.asList(rest.getForObject("http://localhost:8080/service", Service[].class));
		model.addAttribute("listService", listService);
		return "showListServiceForm";
	}

	// addService
	@GetMapping("/addService")
	public String ShowAddServiceForm(Model model) {
		model.addAttribute("service", new Service());
		model.addAttribute("msg", "");
		return "addServiceForm";
	}

	@PostMapping("addService")
	public String saveService(Service service, Model model) {
		String isExist = rest.getForObject("http://localhost:8080/service/isExist/{name}", String.class,service.getName());
		if (isExist.equals("true")) {
			model.addAttribute("service", service);
			model.addAttribute("msg", "Tên Dịch Vụ đã tồn tại");
			return "addServiceForm";
		} else {
			rest.postForObject("http://localhost:8080/service", service, Service.class);
			return "redirect:/service";
		}

	}

	// findByName
	@PostMapping("findServiceByName")
	public String findServiceByUnitPrice(String name, Model model) {
		List<Service> listService = Arrays.asList(
				rest.getForObject("http://localhost:8080/service/findByName/{name}", Service[].class, name));
		model.addAttribute("listService", listService);
		return "showListServiceForm";
	}

	// edit Service
	@GetMapping("editService")
	public String editService(Long id, Model model) {
		Service service = rest.getForObject("http://localhost:8080/service/{id}", Service.class, id);
		model.addAttribute("service", service);
		return "editServiceForm";
	}

	@PostMapping("editService")
	public String updateService(Service service, Model model) {
		String isExist = rest.getForObject("http://localhost:8080/service/isExist/{name}", String.class,service.getName());
		if (isExist.equals("true")) {
			model.addAttribute("service", service);
			model.addAttribute("msg", "Tên Dịch Vụ đã tồn tại");
			return "addServiceForm";
		} else {
			rest.postForObject("http://localhost:8080/service", service, Service.class);
			return "redirect:/service";
		}
	}
	
	//deleteService
	@GetMapping("/deleteService")
	public String deletePhong(Long id) {
		rest.delete("http://localhost:8080/service/{id}", id);
		return "redirect:/service";
	}
	
	//stat
	@GetMapping("serviceStat")
	public String showServiceStatForm(Model model) {
		//get CurrentTime
		LocalDate today = LocalDate.now();
		int month = today.getMonthValue();
		int year = today.getYear();
		String time = "";
		if (month < 10)
			time = year + "-" + "0" + month + "-" + "01";
		else
			time = year + "-" + month + "-" + "01";
		List<ServiceStat> listServiceStat = Arrays.asList(
				rest.getForObject("http://localhost:8080/service/stat/{time}", ServiceStat[].class, time));
		model.addAttribute("listServiceStat", listServiceStat);
		// 
		int total = 0;
		for( ServiceStat i: listServiceStat) {
			total += i.getTotalAmount();
		}
		model.addAttribute("total", total);
		model.addAttribute("month", month);
		return "serviceStatForm";
	}
	
	@PostMapping("/serviceStat")
	public String showServiceStatFormByMonth( int month, Model model) {
		//get Time
		int year = Calendar.getInstance().get(Calendar.YEAR);
		String time = "";
		if (month < 10) {
			time = year + "-" + "0" + month + "-" + "01";
		} else
			time = year + "-" + month + "-" + "01";
		List<ServiceStat> listServiceStat = Arrays.asList(
				rest.getForObject("http://localhost:8080/service/stat/{time}", ServiceStat[].class, time));
		model.addAttribute("listServiceStat", listServiceStat);
		int total = 0;
		for( ServiceStat i: listServiceStat) {
			total += i.getTotalAmount();
		}
		model.addAttribute("total", total);
		model.addAttribute("month", month);
		return "serviceStatForm";
	}
	
	@GetMapping("serviceStat/detail")
	public String showServiceStatDetail( @RequestParam("month") Integer month, @RequestParam("service_id") Long service_id
			, Model model) {
		//get Time
				int year = Calendar.getInstance().get(Calendar.YEAR);
				String time = "";
				if (month < 10) {
					time = year + "-" + "0" + month + "-" + "01";
				} else
					time = year + "-" + month + "-" + "01";
				List<UsedService> listUsedService = Arrays.asList(
						rest.getForObject("http://localhost:8080/usedService/findUsedServiceByServiceIDAndTime/{service_id}/{time}", 
								UsedService[].class, service_id, time));
				
				int total = 0;
				for( UsedService i: listUsedService) {
					i.formatserviceUsageTime();
					total += i.getTotalAmount();
				}
				model.addAttribute("listUsedService", listUsedService);
				model.addAttribute("total", total);
				return "DetailUsedServiceForm";
	}
}
