package dormitory;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Room implements Serializable{
	private Long id;
	private String roomNumber;
	private String type;
	private int unitPrice;
	private int maxPeople;
}
