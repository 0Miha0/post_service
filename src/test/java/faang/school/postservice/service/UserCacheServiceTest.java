package faang.school.postservice.service;

import faang.school.postservice.client.UserServiceClient;
import faang.school.postservice.dto.user.UserDto;
import faang.school.postservice.mapper.UsersCacheMapper;
import faang.school.postservice.model.redis.CacheUser;
import faang.school.postservice.repository.redis.CacheUsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserCacheServiceTest {
    private static final long ID = 1L;
    @Mock
    private CacheUsersRepository repository;

    @Mock
    private UsersCacheMapper mapper;

    @Mock
    private UserServiceClient userClient;

    @InjectMocks
    private UserCacheService userCacheService;


    private UserDto userDto;
    private CacheUser cacheUser;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(ID)
                .username("Test User")
                .build();
        cacheUser = CacheUser.builder()
                .id(ID)
                .username("Test User")
                .build();
    }

    @Test
    void testSaveCacheUserSuccessfully() {
        when(userClient.getUser(ID)).thenReturn(userDto);
        when(mapper.toCacheUser(userDto)).thenReturn(cacheUser);
        when(repository.save(cacheUser)).thenReturn(cacheUser);

        CacheUser savedUser = userCacheService.saveCacheUser(ID);

        assertNotNull(savedUser);
        assertEquals(cacheUser, savedUser);
        verify(userClient, times(1)).getUser(ID);
        verify(mapper, times(1)).toCacheUser(userDto);
        verify(repository, times(1)).save(cacheUser);
    }

    @Test
    void testGetCacheUserSuccessfully() {
        when(repository.findById(ID)).thenReturn(Optional.of(cacheUser));

        Optional<CacheUser> retrievedUser = userCacheService.getCacheUser(ID);

        assertTrue(retrievedUser.isPresent());
        assertEquals(cacheUser, retrievedUser.get());
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void shouldReturnEmptyWhenCacheUserNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        Optional<CacheUser> retrievedUser = userCacheService.getCacheUser(ID);

        assertFalse(retrievedUser.isPresent());
        verify(repository, times(1)).findById(ID);
    }
}