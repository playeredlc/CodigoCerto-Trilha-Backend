package app.edlc.taskapi.user.data.mapper;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import app.edlc.taskapi.user.data.User;
import app.edlc.taskapi.user.data.UserRequestDto;
import app.edlc.taskapi.user.data.UserResponseDto;

@Component
public class UserMapper {
	
	private final ModelMapper modelMapper;
	
	public UserMapper() {
		this.modelMapper = new ModelMapper();
	}

    public User toEntity(UserRequestDto dto) {		
		User entity = modelMapper.map(dto, User.class);		
		if (entity.getPermissions() == null)
			entity.setPermissions(new ArrayList<>());
		entity.setAccount_non_expired(true);
		entity.setAccount_non_locked(true);
		entity.setCredentials_non_expired(true);
		entity.setEnabled(true);
		return entity;
	}
    
    public UserRequestDto toRequestDto(User entity) {
		return modelMapper.map(entity, UserRequestDto.class);
	}
    
    public UserResponseDto toResponseDto(User entity) {
    	return modelMapper.map(entity, UserResponseDto.class);
    }
}
