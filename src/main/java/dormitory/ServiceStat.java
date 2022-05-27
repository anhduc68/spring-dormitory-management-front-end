package dormitory;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceStat implements Serializable {
	private Long id;
	private String name;
	private int unitPrice;
	private int totalUsage;
	private int totalAmount;
}
