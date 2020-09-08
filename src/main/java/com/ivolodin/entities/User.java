package com.ivolodin.entities;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"email","username", "name", "surname"})
@Entity
@Table(name = "users")
public class User {
    @ToString.Exclude
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "user_name")
    private String username;

    @ToString.Exclude
    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birth_date")
    private LocalDate birthdate;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Ticket> tickets;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;
}
