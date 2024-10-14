package app.edlc.taskapi.user.unittests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import app.edlc.taskapi.user.data.User;
import app.edlc.taskapi.user.data.UserRequestDto;
import app.edlc.taskapi.user.data.UserResponseDto;
import app.edlc.taskapi.user.data.mapper.UserMapper;
import app.edlc.taskapi.user.unittests.mocks.MockUser;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {
	
	@InjectMocks
	UserMapper userMapper;	
	MockUser mockUser;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockUser = new MockUser();
	}
	
	@Test
	public void shouldConvertRequestToEntity() {
		UserRequestDto requestDto = mockUser.mockRequestDto();
		
		User entity = userMapper.toEntity(requestDto);		
		assertAll("User Entity",
			() -> assertNull(entity.getId()),
			() -> assertEquals("Name Test0", entity.getName()),
			() -> assertEquals("Username Test0", entity.getUsername()),
			() -> assertEquals("Password Test0", entity.getPassword()),
			() -> assertTrue(entity.getAccountNonExpired()),
			() -> assertTrue(entity.getAccountNonLocked()),
			() -> assertTrue(entity.getCredentials_non_expired()),
			() -> assertTrue(entity.getEnabled())
	     );	
	}
	
	@Test
	public void shouldConvertEntityToResponse() {
		User entity = mockUser.mockEntity();
		
		UserResponseDto responseDto = userMapper.toResponseDto(entity);		
		assertAll("User Response DTO",
            () -> assertEquals(Long.valueOf(0L), responseDto.getId()),
            () -> assertEquals("Name Test0", responseDto.getName()),
            () -> assertEquals("Username Test0", responseDto.getUsername())
        );
	}
	
	@Test
	public void convertEntityToRequest() {
		User entity = mockUser.mockEntity();
		
		UserRequestDto requestDto = userMapper.toRequestDto(entity);		
        assertAll("User Request DTO",
            () -> assertEquals(Long.valueOf(0L), requestDto.getId()),
            () -> assertEquals("Name Test0", requestDto.getName()),
            () -> assertEquals("Username Test0", requestDto.getUsername()),
            () -> assertEquals("Password Test0", requestDto.getPassword())
        );
	}
}
