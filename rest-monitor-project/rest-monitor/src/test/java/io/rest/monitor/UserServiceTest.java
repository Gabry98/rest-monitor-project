package io.rest.monitor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.rest.monitor.entity.UserEntity;
import io.rest.monitor.repository.UserRepository;
import io.rest.monitor.request.UserRequestPayload;
import io.rest.monitor.service.UserService;
import static io.rest.monitor.util.ConstantUtils.USER_NOT_FOUND;
import static io.rest.monitor.util.ConstantUtils.USER_NAME;
import static io.rest.monitor.util.ConstantUtils.USER_SURNAME;
import static io.rest.monitor.util.ConstantUtils.USER_EMAIL;
import static io.rest.monitor.util.ConstantUtils.USER_ADDRESS;

public class UserServiceTest {
	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	PanacheQuery<UserEntity> query;
	
	private AutoCloseable closeable;
	
	private String csvDirectory;
	
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
		when(userRepository.findById(1)).thenReturn(new UserEntity());
		assertDoesNotThrow(() -> userService.getUser(1));
	}
	
	@Test
	void testGetUserException() {
		when(userRepository.findById(1)).thenReturn(null);
		Throwable t = assertThrows(IllegalArgumentException.class, () -> userService.getUser(1));
		assertEquals(USER_NOT_FOUND, t.getMessage());
	}
	
	@Test
	void testModifyUserAllNullValues() {
		when(userRepository.findById(1)).thenReturn(new UserEntity());
		assertDoesNotThrow(() -> userService.modifyUser(1, new UserRequestPayload()));
	}
	
	@Test
	void testModifyUserAllInitializedValues() {
		when(userRepository.findById(1)).thenReturn(new UserEntity());
		assertDoesNotThrow(() -> userService.modifyUser(1, new UserRequestPayload(USER_NAME, USER_SURNAME, 
				USER_EMAIL, USER_ADDRESS)));
	}
	
	@Test
	void testModifyUserException() {
		when(userRepository.findById(1)).thenReturn(null);
		Throwable t = assertThrows(IllegalArgumentException.class, () -> 
			userService.modifyUser(1, new UserRequestPayload()));
		assertEquals(USER_NOT_FOUND, t.getMessage());
	}
	
	@Test
	void testCreateUserAllNullValuesAndException() {
		when(userRepository.findById(anyInt())).thenReturn(new UserEntity());
		assertThrows(IllegalArgumentException.class, () -> 
			userService.createUser(new UserRequestPayload()));
	}
	
	@Test
	void testCreateUserAllInitializedValues() {
		assertDoesNotThrow(() -> userService.createUser(new UserRequestPayload(USER_NAME, USER_SURNAME, 
				USER_EMAIL, USER_ADDRESS)));
	}
	
	@Test
	void testCreateUserWithFile() {
		System.out.println(csvDirectory);
		assertDoesNotThrow(() -> userService.createUserWithFile("src/test/resources/file_for_creation_test.csv"));
	}
	
	@Test
	void testDeleteUser() {
		when(userRepository.deleteById(1)).thenReturn(true);
		assertDoesNotThrow(() -> userService.deleteUser(1));
	}
	
	@Test
	void testDeleteUserNotFound() {
		when(userRepository.deleteById(1)).thenReturn(false);
		String msg = userService.deleteUser(1);
		assertEquals(USER_NOT_FOUND, msg);
	}
	
	@Test
	void testFilterUserAllInitializedValues() {
		when(userRepository.find(anyString(), anyMap())).thenReturn(query);
		when(query.list()).thenReturn(List.of(new UserEntity()));
		assertDoesNotThrow(() -> userService
				.filterUser(new UserRequestPayload("testName", "testSurname", "testEmail", "testAddress")));
	}
	
	@Test
	void testFilterUserAllNullValues() {
		when(userRepository.find(anyString(), anyMap())).thenReturn(query);
		when(query.list()).thenReturn(List.of(new UserEntity()));
		assertDoesNotThrow(() -> userService.filterUser(new UserRequestPayload()));
	}

}
