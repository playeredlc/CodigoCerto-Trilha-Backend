package app.edlc.taskapi.user;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class UserService implements UserDetailsService {	
	private Logger logger = Logger.getLogger(UserService.class.getName());
	
	@Autowired
	private UserMapper mapper;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Finding user by username " + username);
		
		if (userRepository.existsByUsername(username))
			return userRepository.findByUsername(username);
		else
			throw new UsernameNotFoundException("User with " + username + " username not found.");		
	}
	
	public UserResponseDto create(UserRequestDto userRequest) {
		logger.info("Creating new user");
		
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		if (userRepository.existsByUsername(userRequest.getUsername()))
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
	
	public ResponseEntity<?> delete(Long id, String username) {		
		logger.info("Deleting user");
		
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException());
		
		if (!user.getUsername().equals(username))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		
		userRepository.delete(user);
		
		return ResponseEntity.noContent().build();
	}	
}
