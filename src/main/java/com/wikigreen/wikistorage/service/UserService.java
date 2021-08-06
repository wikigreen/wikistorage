package com.wikigreen.wikistorage.service;

import com.wikigreen.wikistorage.dto.UserCreateDto;
import com.wikigreen.wikistorage.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    User save(User user);

    User getById(Long id);

    @Transactional
    User getByNickName(String nickName);

    User update(User user);

    void deleteById(Long id);

    void banById(Long id);

    boolean existsByEmail(String email);

    boolean existsByNickName(String nickName);

    List<User> getAll();

    void changePassword(String name, String oldPassword, String newPassword);
}
