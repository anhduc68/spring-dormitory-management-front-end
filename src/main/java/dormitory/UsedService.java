package dormitory;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsedService implements Serializable {
	private Long id;

	private Student studentServ;

	private Service service;

	private int quantity;

	private int totalAmount;

	private Date serviceUsageTime;
	
	private String serviceUsageTimeFormat;
	
	public  void formatserviceUsageTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
		this.serviceUsageTimeFormat = sdf.format(serviceUsageTime);
	}
}
