package com.wikigreen.wikistorage.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User extends BaseEntity {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<File> files;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<Event> events;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickName;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @Column(name = "user_status")
    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus;

    public User(User user){
        this.files = user.files;
        this.events = user.events;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.email = user.email;
        this.nickName = user.nickName;
        this.password = user.password;
        this.roles = user.roles;
        this.userStatus = user.userStatus;
    }
}
