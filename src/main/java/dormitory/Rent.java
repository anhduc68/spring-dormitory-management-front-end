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
public class Rent implements Serializable {
	private Long id;
	private Student student;
	private Room room;
	private Date rentDate;
	private int price;
	
	private String rentDateFormat;
	
	public void formatRentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
		this.rentDateFormat = sdf.format(rentDate);
	}

}
