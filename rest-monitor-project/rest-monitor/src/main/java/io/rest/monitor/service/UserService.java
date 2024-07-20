package io.rest.monitor.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.eclipse.microprofile.context.ThreadContext;

import io.quarkus.logging.Log;
import io.rest.monitor.entity.UserEntity;
import io.rest.monitor.repository.UserRepository;
import io.rest.monitor.request.UserRequestPayload;
import io.smallrye.context.api.CurrentThreadContext;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import static io.rest.monitor.util.ConstantUtils.USER_NOT_FOUND;
import static io.rest.monitor.util.ConstantUtils.USER_NAME;
import static io.rest.monitor.util.ConstantUtils.USER_SURNAME;
import static io.rest.monitor.util.ConstantUtils.USER_EMAIL;
import static io.rest.monitor.util.ConstantUtils.USER_ADDRESS;

@Singleton
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
@CurrentThreadContext(cleared = { ThreadContext.TRANSACTION })
public class UserService {
	
	@Inject
	private UserRepository userRepository;
	
	private static final Long RATE_OF_DIVISION = 10000000000L;
	
	public UserEntity getUser(Integer userId) {
		Log.info("Searching the user with id " + userId);
		UserEntity entity = userRepository.findById(userId);
		if (entity == null) {
			String errorMsg = USER_NOT_FOUND;
			throw new IllegalArgumentException(errorMsg);
		}
		Log.info("Search done with success!");
		return entity;
	}
	
	public String modifyUser(Integer userId, UserRequestPayload payload) {
		Log.info("Modifying the user with id " + userId);
		UserEntity entity = userRepository.findById(userId);
		if (entity == null) {
			String errorMsg = USER_NOT_FOUND;
			throw new IllegalArgumentException(errorMsg);
		}
		entity.setUserAddress(payload.getUserAddress() == null ? entity.getUserAddress() : payload.getUserAddress());
		entity.setUserEmail(payload.getUserEmail() == null ? entity.getUserEmail() : payload.getUserEmail());
		entity.setUserName(payload.getUserName() == null ? entity.getUserName() : payload.getUserName());
		entity.setUserSurname(payload.getUserSurname() == null ? entity.getUserSurname() : payload.getUserSurname());
		String success = "User with id " + userId + " successfully modified!";
		Log.info(success);
		return success;
	}
	
	public List<UserEntity> createUserWithFile(String fileDir) throws IOException {
		Log.info("Defining new users with CSV file in directory: " + fileDir);
		File file = new File(fileDir);
		Log.info("Abstract representation of file defined.");
		FileReader fileReader = new FileReader(file);
		Log.info("FileReader created.");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		Log.info("BufferedReader created.");
	    String fileLine;
	    String[] usersInformations;
	    List<UserEntity> entities = new ArrayList<>();
	    Log.info("Processing data inside the file.");
	    while ((fileLine = bufferedReader.readLine()) != null) {
	    	usersInformations = fileLine.split(",");
	        UserRequestPayload payload = new UserRequestPayload();
	        payload.setUserName(usersInformations[0]);
	        payload.setUserSurname(usersInformations[1]);
	        payload.setUserEmail(usersInformations[2]);
	        payload.setUserAddress(usersInformations[3]);
	        entities.add(createUser(payload));
	    }
	    bufferedReader.close();
	    Log.info("Creation done!");
		return entities;
	}
	
	public UserEntity createUser(UserRequestPayload payload) {
		Log.info("Creating a new user.");
		Long uuid = Math.abs(UUID.randomUUID().getLeastSignificantBits()) / RATE_OF_DIVISION;
		Integer userId = Integer.valueOf(uuid.toString());
		String address = payload.getUserAddress() == null ? "" : payload.getUserAddress();
		String email = payload.getUserEmail() == null ? "" : payload.getUserEmail();
		String name = payload.getUserName() == null ? "" : payload.getUserName();
		String surname = payload.getUserSurname() == null ? "" : payload.getUserSurname();
		UserEntity entity = new UserEntity(userId, name, surname, email, address);
		if (userRepository.findById(entity.getUserId()) != null) {
			String errorMsg = "User with id " + userId + " already exists!";
			throw new IllegalArgumentException(errorMsg);
		}
		userRepository.persist(entity);
		Log.info("User created successfully!");
		return entity;
	}
	
	public String deleteUser(Integer userId) {
		Log.info("Deleting the user with id " + userId);
		boolean deleteResult = userRepository.deleteById(userId);
		if (!deleteResult) {
			String warningMsg = USER_NOT_FOUND;
			Log.warn(warningMsg);
			return warningMsg;
		}
		String success = "User with id " + userId + " successfully deleted!";
		Log.info(success);
		return success;
	}
	
	public List<UserEntity> filterUser(UserRequestPayload payload) {
		Log.info("Filtering some users with specified values.");
		Map<String, Object> mappedValues = new HashMap<>();
		updateMap(mappedValues, payload);
		String query = createQuery(mappedValues);
		List<UserEntity> entities = userRepository.find(query, mappedValues).list();
		Log.info("Operation of filtering done!");
		return entities;
	}
	
	private void updateMap(Map<String, Object> mappedValues, UserRequestPayload payload) {
		mappedValues.put(USER_NAME, payload.getUserName() == null ? null : payload.getUserName().toString());
		mappedValues.put(USER_SURNAME, payload.getUserSurname() == null ? null : payload.getUserSurname().toString());
		mappedValues.put(USER_EMAIL, payload.getUserEmail() == null ? null : payload.getUserEmail().toString());
		mappedValues.put(USER_ADDRESS, payload.getUserAddress() == null ? null : payload.getUserAddress().toString());
		mappedValues.values().removeAll(Collections.singleton(null));
	}
	
	private String createQuery(Map<String, Object> mappedValues) {
		StringBuilder query = new StringBuilder();
		query.append("");
		Set<String> keys = mappedValues.keySet();
		for (String key : keys) {
			if (!query.toString().equals("")) {
				query.append(" and ");
			}
			query.append(key + " = :" + key);
		}
		return query.toString();
	}

}
