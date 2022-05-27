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
public class Parking implements Serializable {
	private Long id;
	private Motorcycle motorcycleParking;
	private Date checkin;
	private Date checkout;
	private int price;
	
	private String checkinFormat;
	private String checkoutFormat;
	
	public void formatCheckin() {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
		this.checkinFormat = sdf.format(checkin);
	}
	public void formatCheckout() {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
		this.checkoutFormat = sdf.format(checkout);
	}

}
