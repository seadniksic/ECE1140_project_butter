/*import org.junit.assert;
import org.junit.test;

public Class TrainControllerTest{
	@BeforeEach
	void setup() throws RemoteException{
		tc_Test = new Train_Controller();
	}
//Testing Setting Kp and Ki
	//Kp and Ki should always be set together, and not set singularly
	@test
	void kp_Test(){
		tc_Test.setKp(10);
		assertEquals(0, tc_Test.getKp());
	}
	//Kp and Ki should always be set together, and not set singularly
	@test
	void ki_Test(){
		tc_Test.setKi(5);
		assertEquals(0, tc_Test.getKi());
	}
	//Setting default without entering Kp and Ki is ok to allow.Default is 0
	@test
	void kp_Ki_default(){
		tc_Test.setDefaultKpKi(true);
		assertEquals(true, tc_Test.getDefaultKpKi());
	}
	//This tests the overall setting as the GUI would and tests for changing values
	@test
	void kp_Ki_overall(){

		tc_Test.setKp(100);
		tc_Test.setKi(25);
		tc_Test.setDefaultKpKi(true);

		assertEquals(100, tc_Test.getKp());
		assertEquals(25, tc_Test.getKi());
		assertEquals(true, tc_Test.getDefaultKpKi());

		tc_Test.setKp(75);
		assertEquals(75, tc_Test.getKp());

		tc_Test.setKi(20);
		assertEquals(20, tc_Test.getKi());

		tc_Test.setDefaultKpKi(false);
		assertEquals(false. tc_Test.getDefaultKpKi());
	}



}*/