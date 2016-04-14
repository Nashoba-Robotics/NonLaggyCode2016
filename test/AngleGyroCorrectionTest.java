import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import edu.nr.lib.AngleGyroCorrection;
import edu.nr.lib.AngleUnit;
import edu.nr.lib.navx.TestNavX;

public class AngleGyroCorrectionTest {

	
	public AngleGyroCorrectionTest() {
	}
	
	@Test
	public void testReset() {
		TestNavX navx = new TestNavX();
		AngleGyroCorrection correction = new AngleGyroCorrection(navx, AngleUnit.DEGREE);
		navx.setYaw(5, AngleUnit.DEGREE);
		assertNotEquals(correction.getAngleErrorDegrees(),0, 0.0001);
		correction.reset();
		assertEquals(correction.getAngleErrorDegrees(), 0,0.0001);
	}

}
