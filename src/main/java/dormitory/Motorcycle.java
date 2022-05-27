package dormitory;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Motorcycle implements Serializable {
	private Long id;

	private Student studentMoto;

	private String type;

	private String licencePlate;

}
