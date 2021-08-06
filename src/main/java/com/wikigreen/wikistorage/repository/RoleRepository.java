package com.wikigreen.wikistorage.repository;

import com.wikigreen.wikistorage.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByRoleNameIn(List<String> roleNames);
}
