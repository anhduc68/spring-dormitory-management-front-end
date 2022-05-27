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
public class Student implements Serializable {
	private Long id;

	private String studentID;

	private String idNumber;

	private String fullname;
	private Date dob;

	private String classID;

	private String hometown;
	
	public String  dobFormat;
	
	public void format() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		this.dobFormat = sdf.format(dob);
	}
}
