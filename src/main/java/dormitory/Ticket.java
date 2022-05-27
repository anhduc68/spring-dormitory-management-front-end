package dormitory;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket implements Serializable {
	private Long id;
	private Motorcycle motorcycle;
	private int price;
	private Date usageTime;
	private Long student_id;
	
	private String usageTimeFormat;
	
	public void formatUsageTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
		this.usageTimeFormat = sdf.format(usageTime);
	}
}
