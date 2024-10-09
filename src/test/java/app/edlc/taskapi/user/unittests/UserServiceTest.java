package app.edlc.taskapi.user.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import app.edlc.taskapi.constants.Constants;
import app.edlc.taskapi.user.PermissionRepository;
import app.edlc.taskapi.user.UserRepository;
import app.edlc.taskapi.user.UserService;
import app.edlc.taskapi.user.data.Permission;
import app.edlc.taskapi.user.data.User;
import app.edlc.taskapi.user.data.UserRequestDto;
import app.edlc.taskapi.user.data.UserResponseDto;
import app.edlc.taskapi.user.data.mapper.UserMapper;
import app.edlc.taskapi.user.exception.UserNotFoundException;
import app.edlc.taskapi.user.exception.UsernameAlreadyExistsException;
import app.edlc.taskapi.user.unittests.mocks.MockUser;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private UserMapper mapper;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private UserRepository userRepository;
	@Mock
	private PermissionRepository permissionRepository;
	
	private MockUser mockUser;
	private User userEntity;
	private UserRequestDto userRequestDto;
	private UserResponseDto userResponseDto;
	
	@BeforeEach
	void setup() {
		mockUser = new MockUser();
		userEntity = mockUser.mockEntity();
		userRequestDto = mockUser.mockRequestDto();
		userResponseDto = mockUser.mockResponseDto();
	}
	
	@Test
    public void create_ShouldReturnUserResponseDto_WhenUserIsCreated() {
        when(userRepository.existsByUsername(userRequestDto.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(userRequestDto.getPassword())).thenReturn("encodedPassword");
        when(mapper.toEntity(userRequestDto)).thenReturn(userEntity);
        when(permissionRepository.findById(Constants.ID_COMMON_USER_ROLE)).thenReturn(Optional.of(new Permission()));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(mapper.toResponseDto(userEntity)).thenReturn(userResponseDto);

        UserResponseDto result = userService.create(userRequestDto);

        assertEquals(userResponseDto, result);
        verify(userRepository).save(userEntity);
    }
	
	@Test
    public void create_ShouldThrowUsernameAlreadyExistsException_WhenUsernameExists() {
        when(userRepository.existsByUsername(userRequestDto.getUsername())).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> userService.create(userRequestDto));
    }
	
    @Test
    public void delete_ShouldDeleteUser_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        userService.delete(1L);

        verify(userRepository).delete(userEntity);
    }
    
    @Test
    public void delete_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.delete(1L));
    }
}
