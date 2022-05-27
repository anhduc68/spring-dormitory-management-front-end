package dormitory;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visitor implements Serializable {

	private Long id;
	private Student studentVis;
	private String idNumber;
	private String fullname;
	private Date dob;
	private Date visitDate;

}
