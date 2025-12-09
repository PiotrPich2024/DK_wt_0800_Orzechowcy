package pl.edu.agh.to.przychodnia;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.edu.agh.to.przychodnia.Doctor.DoctorController;

@SpringBootTest
class PrzychodniaApplicationTests {

	@Autowired
	private DoctorController doctorController;

	@Test
	void contextLoads() {
	}

}
