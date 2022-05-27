package dormitory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {
	private Long id;

	private String name;

	private int unitPrice;
}
