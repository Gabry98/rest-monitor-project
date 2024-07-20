package io.rest.monitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.rest.monitor.controller.UserController;
import io.rest.monitor.entity.UserEntity;
import io.rest.monitor.request.UserRequestPayload;
import io.rest.monitor.service.UserService;
import jakarta.ws.rs.core.Response;
import static io.rest.monitor.util.ConstantUtils.USER_NOT_FOUND;
import static io.rest.monitor.util.ConstantUtils.RESPONSE_NOT_FOUND;
import static io.rest.monitor.util.ConstantUtils.RESPONSE_INTERNAL_SERVER_ERROR;

class UserControllerTest {
    
	@Mock
	private UserService userService;
	
	@InjectMocks
	private UserController userController;
	
	private AutoCloseable closeable;
	
	private static final String GENERIC_EXCEPTION_MESSAGE = "test";
	
	private static final String TEST_FILE = "file_for_creation_test.csv";
	
	private static final int RESPONSE_OK = 200;
	
	@BeforeEach
	void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		closeable.close();
	}
	
	@Test
	void testGetUser() {
		when(userService.getUser(1)).thenReturn(new UserEntity());
		Response res = userController.getUser(1);
		assertEquals(RESPONSE_OK, res.getStatus());
	}
	
	@Test
	void testGetUserNotFound() {
		when(userService.getUser(1)).thenThrow(new IllegalArgumentException(USER_NOT_FOUND));
		Response res = userController.getUser(1);
		assertEquals(RESPONSE_NOT_FOUND, res.getStatus());
	}
	
	@Test
	void testGetUserGenericException() {
		when(userService.getUser(1)).thenThrow(new IllegalArgumentException(GENERIC_EXCEPTION_MESSAGE));
		Response res = userController.getUser(1);
		assertEquals(RESPONSE_INTERNAL_SERVER_ERROR, res.getStatus());
	}
	
	@Test
	void testFilterUser() {
		UserRequestPayload payload = new UserRequestPayload();
		when(userService.filterUser(payload)).thenReturn(List.of(new UserEntity()));
		Response res = userController.filterUser(payload);
		assertEquals(RESPONSE_OK, res.getStatus());
	}
	
	@Test
	void testCreateUser() {
		UserRequestPayload payload = new UserRequestPayload();
		when(userService.createUser(payload)).thenReturn(new UserEntity());
		Response res = userController.createUser(payload);
		assertEquals(RESPONSE_OK, res.getStatus());
	}
	
	@Test
	void testCreateUserWithException() {
		UserRequestPayload payload = new UserRequestPayload();
		when(userService.createUser(payload)).thenThrow(new IllegalArgumentException(GENERIC_EXCEPTION_MESSAGE));
		Response res = userController.createUser(payload);
		assertEquals(RESPONSE_INTERNAL_SERVER_ERROR, res.getStatus());
	}
	
	@Test
	void testCreateUserWithFile() throws IOException {
		String testFile = TEST_FILE;
		when(userService.createUserWithFile(testFile)).thenReturn(List.of(new UserEntity()));
		Response res = userController.createUserWithFile(testFile);
		assertEquals(RESPONSE_OK, res.getStatus());
	}
	
	@Test
	void testCreateUserWithFileException() throws IOException {
		String testFile = TEST_FILE;
		when(userService.createUserWithFile(testFile)).thenThrow(new IllegalArgumentException(GENERIC_EXCEPTION_MESSAGE));
		Response res = userController.createUserWithFile(testFile);
		assertEquals(RESPONSE_INTERNAL_SERVER_ERROR, res.getStatus());
	}
	
	@Test
	void testModifyUser() {
		UserRequestPayload payload = new UserRequestPayload();
		when(userService.modifyUser(1, payload)).thenReturn(GENERIC_EXCEPTION_MESSAGE);
		Response res = userController.modifyUser(1, payload);
		assertEquals(RESPONSE_OK, res.getStatus());
	}
	
	@Test
	void testModifyUserNotFound() {
		UserRequestPayload payload = new UserRequestPayload();
		when(userService.modifyUser(1, payload)).thenThrow(new IllegalArgumentException(USER_NOT_FOUND));
		Response res = userController.modifyUser(1, payload);
		assertEquals(RESPONSE_NOT_FOUND, res.getStatus());
	}
	
	@Test
	void testModifyUserGenericException() {
		UserRequestPayload payload = new UserRequestPayload();
		when(userService.modifyUser(1, payload)).thenThrow(new IllegalArgumentException(GENERIC_EXCEPTION_MESSAGE));
		Response res = userController.modifyUser(1, payload);
		assertEquals(RESPONSE_INTERNAL_SERVER_ERROR, res.getStatus());
	}
	
	@Test
	void testDeleteUser() {
		when(userService.deleteUser(1)).thenReturn(GENERIC_EXCEPTION_MESSAGE);
		Response res = userController.deleteUser(1);
		assertEquals(RESPONSE_OK, res.getStatus());
	}
	

}