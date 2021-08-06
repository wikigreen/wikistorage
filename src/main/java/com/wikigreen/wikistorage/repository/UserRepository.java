package com.wikigreen.wikistorage.repository;

import com.wikigreen.wikistorage.model.User;
import com.wikigreen.wikistorage.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query( "update User u " +
            "set u.userStatus = :userStatus " +
            "where u.id = :id")
    void setStatusById(@Param("id")      Long id,
                       @Param("userStatus")  UserStatus userStatus);

    @Modifying
    @Query( "update User u " +
            "set u.password = :password " +
            "where u.nickName = :nickName")
    void changePasswordByNickName(@Param("nickName") String nickName,
                       @Param("password")  String password);

    List<User> findByUserStatusNot(UserStatus userStatus);

    Optional<User> findByIdAndUserStatus(Long id, UserStatus userStatus);

    boolean existsByEmail(String email);

    boolean existsByNickName(String nickName);

    User findByNickName(String nickName);
}
