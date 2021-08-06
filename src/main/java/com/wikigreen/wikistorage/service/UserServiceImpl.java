package com.wikigreen.wikistorage.service;

import com.wikigreen.wikistorage.model.FileStatus;
import com.wikigreen.wikistorage.model.Role;
import com.wikigreen.wikistorage.model.User;
import com.wikigreen.wikistorage.model.UserStatus;
import com.wikigreen.wikistorage.repository.FileRepository;
import com.wikigreen.wikistorage.repository.RoleRepository;
import com.wikigreen.wikistorage.repository.UserRepository;
import com.wikigreen.wikistorage.service.exceptions.CustomEntityNotFoundException;
import com.wikigreen.wikistorage.service.exceptions.InvalidPasswordException;
import com.wikigreen.wikistorage.service.exceptions.NotUniqueDataException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
//@NoArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private FileRepository fileRepository;
    private FileStore fileStore;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            FileStore fileStore,
            BCryptPasswordEncoder passwordEncoder,
            FileRepository fileRepository,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.fileStore = fileStore;
        this.passwordEncoder = passwordEncoder;
        this.fileRepository = fileRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public User save(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new NotUniqueDataException("This email is already taken");
        }
        if (userRepository.existsByNickName(user.getNickName())) {
            throw new NotUniqueDataException("This nickname is already taken");
        }
        List<Role> userRoles;

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            userRoles = roleRepository.findByRoleNameIn(List.of("ROLE_USER"));
        } else {
            userRoles = roleRepository.findByRoleNameIn(
                    user.getRoles().stream().map(Role::getRoleName)
                            .collect(Collectors.toList())
            );
        }

        if (user.getUserStatus() == null) {
            user.setUserStatus(UserStatus.ACTIVE);
        }

        user.setFiles(new ArrayList<>());
        user.setEvents(new ArrayList<>());

        user.setRoles(userRoles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userWithId = userRepository.save(user);
        fileStore.createBucket(userWithId.getId());
        return userWithId;
    }

    @Override
    @Transactional
    public User getById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new CustomEntityNotFoundException(id.toString(), "User", "id");
        }

        return userRepository.getById(id);
    }

    @Override
    @Transactional
    public User getByNickName(String nickName) {
        if (!userRepository.existsByNickName(nickName)) {
            throw new CustomEntityNotFoundException(nickName, "User", "nickname");
        }
        User user = userRepository.findByNickName(nickName);
        Hibernate.initialize(user.getRoles());
        return user;
    }

    @Override
    @Transactional
    public User update(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new CustomEntityNotFoundException(user.getId().toString(), "User", "id");
        }

        User oldUser = userRepository.getById(user.getId());

        if (!user.getEmail().equals(oldUser.getEmail())
                && userRepository.existsByEmail(user.getEmail())) {
            throw new NotUniqueDataException("This email is already taken");
        }
        if (!user.getNickName().equals(oldUser.getNickName())
                && userRepository.existsByNickName(user.getNickName())) {
            throw new NotUniqueDataException("This nickname is already taken");
        }

        if (user.getRoles() == null) {
            user.setRoles(oldUser.getRoles());
        }

        if (user.getUserStatus() == null) {
            user.setUserStatus(oldUser.getUserStatus());
        }

        user.setEvents(oldUser.getEvents());
        user.setFiles(oldUser.getFiles());
        user.setPassword(oldUser.getPassword());

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new CustomEntityNotFoundException(id.toString(), "User", "id");
        }
        User user = userRepository.getById(id);

        user.getFiles().forEach(file -> {
            fileStore.delete(file.getOwner().getId(), file.getId());
            fileRepository.setStatusById(file.getId(), FileStatus.DELETED);
        });

        userRepository.setStatusById(id, UserStatus.DELETED);
    }

    @Override
    @Transactional
    public void banById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new CustomEntityNotFoundException(id.toString(), "User", "id");
        }
        userRepository.setStatusById(id, UserStatus.BANNED);
    }

    @Override
    @Transactional
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public boolean existsByNickName(String nickName) {
        return userRepository.existsByNickName(nickName);
    }

    @Override
    @Transactional
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void changePassword(String nickName, String oldPassword, String newPassword) {
        User user = userRepository.findByNickName(nickName);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidPasswordException("Wrong password");
        }

        userRepository.changePasswordByNickName(nickName, passwordEncoder.encode(newPassword));
    }
}

