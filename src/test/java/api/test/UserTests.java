package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests {

	User userPayload;
	Faker faker;
	public Logger logger;

	@BeforeClass
	public void setUpData() {

		userPayload = new User();
		faker = new Faker();

		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password());
		userPayload.setPhone(faker.phoneNumber().cellPhone());

		// For logger
		logger = LogManager.getLogger(this.getClass());

	}

	@Test(priority = 1)
	public void testPostUser() {
		logger.info("***** Creating User *****");
		Response response = UserEndPoints.createUser(userPayload);
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("*****  User Created*****");
	}

	@Test(priority = 2)
	public void testGetUser() {
		logger.info("***** Reading User Info  *****");
		Response response = UserEndPoints.readUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("***** User Info Display *****");

	}

	@Test(priority = 3)
	public void testUpdateUser() {

		logger.info("*************** Updating User ***************");
		// update data using payload
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());

		Response response = UserEndPoints.updateUser(this.userPayload.getUsername(), userPayload);
		response.then().log().body();
		Assert.assertEquals(response.getStatusCode(), 200);

		// Get User Details After update

		Response responseAfterUpdate = UserEndPoints.readUser(this.userPayload.getUsername());
		responseAfterUpdate.then().log().all();
		Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);

		logger.info("*************** Updated User ***************");
	}

	@Test(priority = 4)
	public void testDeleteUser() {
		logger.info("***** Deleting User Info  *****");
		Response response = UserEndPoints.deleteUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("***** Deleted User Info  *****");
	}

}
