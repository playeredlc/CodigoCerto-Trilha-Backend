package app.edlc.taskapi.user.unittests.mocks;

import java.util.ArrayList;

import app.edlc.taskapi.user.data.User;
import app.edlc.taskapi.user.data.UserRequestDto;
import app.edlc.taskapi.user.data.UserResponseDto;

public class MockUser {
	
	public User mockEntity() {
		return mockEntity(0);
	}
	
	public UserRequestDto mockRequestDto() {
		return mockRequestDto(0);
	}
	
	public UserResponseDto mockResponseDto() {
		return mockResponseDto(0);
	}
	
	public User mockEntity(int number) {
		User entity = new User();
		
		entity.setId(Long.valueOf(number));
		entity.setName("Name Test" + number);
		entity.setUsername("Username Test" + number);
		entity.setPassword("Password Test" + number);
		entity.setAccount_non_expired(true);
		entity.setAccount_non_locked(true);
		entity.setCredentials_non_expired(true);
		entity.setEnabled(true);
		entity.setPermissions(new ArrayList<>());
		
		return entity;
	}
	
	public UserRequestDto mockRequestDto(int number) {
		UserRequestDto requestDto = new UserRequestDto();
		
		requestDto.setName("Name Test" + number);
		requestDto.setUsername("Username Test" + number);
		requestDto.setPassword("Password Test" + number);
		
		return requestDto;
	}
	
	public UserResponseDto mockResponseDto(int number) {
		UserResponseDto responseDto = new UserResponseDto();
		
		responseDto.setId(Long.valueOf(number));
		responseDto.setName("Name Test" + number);
		responseDto.setUsername("Username Test" + number);
		
		return responseDto;
	}
}
