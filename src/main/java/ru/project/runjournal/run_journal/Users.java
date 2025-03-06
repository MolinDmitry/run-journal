package ru.project.runjournal.run_journal;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @brief Класс сущности пользователей
 */
@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
public class Users implements UserDetails{
    @Id
    @SequenceGenerator(name = "USER_SEQ", sequenceName =  "users_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
    private Long id;
    private final String username;
    private final String password;
    private final String emailString;
    private final String firstName;
    private final String lastName;
    private final Short  yearOfBirth;
    private final Byte sex; // 0 - женский, 1 - мужской
    private final String city;
    private final Short userWeight; // кг
    private final Short userHeight; // см
    private final Byte primaryTimeZoneOffsetHours; // смещение в часах основного часового пояса тренировок -12...12
    private final Byte primaryTimeZoneOffsetMinuts; // смещение в минутах основного часового пояса тренировок 0..59

    private final String userRole; // ROLE_USER, ROLE_ADMIN
    private final boolean accountNonExpired; //активный/неактивный аккаунт
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean accountEnabled;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Arrays.asList(new SimpleGrantedAuthority(this.userRole));
    }

    @Override
    public boolean isAccountNonExpired(){
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked(){
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled(){
        return this.accountEnabled;
    }




}
