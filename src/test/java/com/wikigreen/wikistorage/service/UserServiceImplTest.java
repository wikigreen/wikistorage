package com.wikigreen.wikistorage.service;

import com.wikigreen.wikistorage.model.Role;
import com.wikigreen.wikistorage.model.User;
import com.wikigreen.wikistorage.model.UserStatus;
import com.wikigreen.wikistorage.repository.FileRepository;
import com.wikigreen.wikistorage.repository.RoleRepository;
import com.wikigreen.wikistorage.repository.UserRepository;
import com.wikigreen.wikistorage.service.exceptions.CustomEntityNotFoundException;
import com.wikigreen.wikistorage.service.exceptions.NotUniqueDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Mock
    UserRepository userRepository;

    @Mock
    FileStore fileStore;

    @Mock
    RoleRepository roleRepository;

    @Spy
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Mock
    FileRepository fileRepository;

    User basicUser;

    List<Role> basicRoles;

    final static int ROLE_ADMIN_INDEX = 0;
    final static int ROLE_MODERATOR_INDEX = 1;
    final static int ROLE_USER_INDEX = 2;

    @BeforeEach
    void setUp() {
        basicUser = new User(
                new ArrayList<>(),
                new ArrayList<>(),
                "Vladimir",
                "Hrynevych",
                "email@em@ail",
                "wikigreen",
                "password",
                new ArrayList<>(),
                UserStatus.ACTIVE
        );
        basicUser.setId(1L);

        basicRoles = new ArrayList<>();

        basicRoles.add(new Role("ROLE_ADMIN", new ArrayList<>()));
        basicRoles.add(new Role("ROLE_MODERATOR", new ArrayList<>()));
        basicRoles.add(new Role("ROLE_USER", new ArrayList<>()));

        basicRoles.get(0).setId(1L);
        basicRoles.get(1).setId(2L);
        basicRoles.get(2).setId(3L);
    }

    private User getUserWithChanges(User user, UnaryOperator<User> operator) {
        return operator.apply(user);
    }

    @Test
    void testIfEmailIsAlreadyTaken() {
        when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        User user = new User();
        user.setEmail("email@em.ail");

        Assertions.assertThrows(NotUniqueDataException.class, () -> userServiceImpl.save(user));
    }

    @Test
    void testIfNickNameIsAlreadyTaken() {
        when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        when(userRepository.existsByNickName(Mockito.anyString())).thenReturn(true);

        User user = new User();
        user.setEmail("email@em.ail");
        user.setNickName("hello");

        Assertions.assertThrows(NotUniqueDataException.class, () -> userServiceImpl.save(user));
    }

    @Test
    void testIfUserDoesNotHaveRoles() {
        when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        when(userRepository.existsByNickName(Mockito.anyString())).thenReturn(false);

        basicUser.setId(null);

        when(roleRepository.findByRoleNameIn(List.of("ROLE_USER"))).thenReturn(List.of(basicRoles.get(ROLE_USER_INDEX)));
        when(userRepository.save(basicUser)).thenReturn(getUserWithChanges(basicUser, u -> {
            u.setId(1L);
            return u;
        }));

        User actualUser = userServiceImpl.save(basicUser);
        assertEquals(1, actualUser.getRoles().size());
        assertEquals(basicRoles.get(ROLE_USER_INDEX).getRoleName(), actualUser.getRoles().get(0).getRoleName());
    }

    @Test
    void testWhenUserHasRole() {
        when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        when(userRepository.existsByNickName(Mockito.anyString())).thenReturn(false);

        List<Role> roles = new ArrayList<>(basicRoles);
        roles.remove(ROLE_MODERATOR_INDEX);
        basicUser.setRoles(roles);

        when(roleRepository.findByRoleNameIn(List.of("ROLE_ADMIN", "ROLE_USER"))).thenReturn(roles);

        basicUser.setId(null);

        when(userRepository.save(basicUser)).thenReturn(getUserWithChanges(basicUser, u -> {
            u.setId(1L);
            return u;
        }));

        User actualUser = userServiceImpl.save(basicUser);
        assertEquals(2, actualUser.getRoles().size());
    }

    @Test
    void testWhenUserDoesNotHaveStatus() {
        when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        when(userRepository.existsByNickName(Mockito.anyString())).thenReturn(false);

        basicUser.setId(null);

        basicUser.setUserStatus(null);

        when(roleRepository.findByRoleNameIn(List.of("ROLE_USER"))).thenReturn(List.of(basicRoles.get(ROLE_USER_INDEX)));
        when(userRepository.save(basicUser)).thenReturn(getUserWithChanges(basicUser, u -> {
            u.setId(1L);
            return u;
        }));

        User actualUser = userServiceImpl.save(basicUser);
        assertEquals(UserStatus.ACTIVE, actualUser.getUserStatus());
    }

    @Test
    void testGetByIdWhenUserDoesNotExist() {
        when(userRepository.existsById(Mockito.anyLong())).thenReturn(false);

        Assertions.assertThrows(CustomEntityNotFoundException.class, () -> userServiceImpl.getById(1L));
    }

    @Test
    void testGetByNickNameWhenNickNameDoesNotExist() {
        when(userRepository.existsByNickName(Mockito.anyString())).thenReturn(false);

        Assertions.assertThrows(CustomEntityNotFoundException.class, () -> userServiceImpl.getByNickName("wiki"));
    }

    @Test
    void testUpdateWhenIdIsNotFound() {
        when(userRepository.existsById(Mockito.anyLong())).thenReturn(false);

        Assertions.assertThrows(CustomEntityNotFoundException.class, () -> userServiceImpl.update(basicUser));
    }

    @Test
    void testUpdateWhenEmailIsNotUnique() {
        when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);

        User oldUser = new User(basicUser);
        oldUser.setEmail("oldEmail@em.ail");

        when(userRepository.getById(Mockito.anyLong())).thenReturn(oldUser);
        when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        Assertions.assertThrows(NotUniqueDataException.class, () -> userServiceImpl.update(basicUser));
    }

    @Test
    void testUpdateWhenNickNameIsNotUnique() {
        when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);

        User oldUser = new User(basicUser);
        oldUser.setNickName("oldNick");

        when(userRepository.getById(Mockito.anyLong())).thenReturn(oldUser);
        when(userRepository.existsByNickName(Mockito.anyString())).thenReturn(true);

        Assertions.assertThrows(NotUniqueDataException.class, () -> userServiceImpl.update(basicUser));
    }

    @Test
    void testDeleteWhenUserDoesNotExist(){
        when(userRepository.existsById(Mockito.anyLong())).thenReturn(false);

        Assertions.assertThrows(CustomEntityNotFoundException.class, () -> userServiceImpl.update(basicUser));
    }
}
