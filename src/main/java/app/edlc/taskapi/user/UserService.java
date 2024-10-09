package app.edlc.taskapi.user;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import app.edlc.taskapi.constants.Constants;
import app.edlc.taskapi.user.data.Permission;
import app.edlc.taskapi.user.data.User;
import app.edlc.taskapi.user.data.UserRequestDto;
import app.edlc.taskapi.user.data.UserResponseDto;
import app.edlc.taskapi.user.data.mapper.UserMapper;
import app.edlc.taskapi.user.exception.UserNotFoundException;
import app.edlc.taskapi.user.exception.UsernameAlreadyExistsException;

@Service
public class UserService {	
	private Logger logger = Logger.getLogger(UserService.class.getName());
	
	@Autowired
	private UserMapper mapper;	
	@Autowired
	private PasswordEncoder passwordEncoder;	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	
	public UserResponseDto create(UserRequestDto userRequest) {
		logger.info("Creating new user");		
		
		if(userRepository.existsByUsername(userRequest.getUsername()))
			throw new UsernameAlreadyExistsException();		
		
		userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		User userEntity = mapper.toEntity(userRequest);
		
		// set permission
		Permission userPermission = permissionRepository.findById(Constants.ID_COMMON_USER_ROLE)
				.orElseThrow(() -> new UserNotFoundException());		
		userEntity.getPermissions().add(userPermission);
		
		UserResponseDto responseDto = mapper.toResponseDto(userRepository.save(userEntity));		
		return responseDto;
	}
	
	public void delete(Long id) {		
		logger.info("Deleting user");
		
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException());
		
		userRepository.delete(user);
	}
	
}
