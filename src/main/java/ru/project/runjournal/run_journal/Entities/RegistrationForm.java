package ru.project.runjournal.run_journal.Entities;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * @breif Класс регистрационных данных, поступающих от формы регистрации
 */

@Data
@AllArgsConstructor
public class RegistrationForm {
    private String username;
    private String password;
    private String rep_password;
    private String emailString;
    private String firstName;
    private String lastName;
    private Short  yearOfBirth;
    private String sex;
    private String city;
    private Short userWeight; // кг
    private Short userHeight; // см

    // private final String userRole; // ROLE_USER, ROLE_ADMIN
    // private final boolean accountNonExpired; //активный/неактивный аккаунт
    // private final boolean accountNonLocked;
    // private final boolean credentialsNonExpired;
    // private final boolean accountEnabled;

    /**
     * @brief Конвертирует данные регистрацонной формы в объект класса User
     * 
     * @param passwordEncoder Bean-компонент, с помощью него производится шифрование данных при сохранении в БД
     * @return Объект класса User с данными из регистрацонной формы
     */
    public Users toUser(PasswordEncoder passwordEncoder,
                        String userRole,
                        boolean accountNonExpired,
                        boolean accountNonLocked,                    
                        boolean credentialsNonExpired,
                        boolean accountEnabled
    ){
        Byte sex_code = this.sex.equals("мужской") ? (byte)1: (byte)0;
        return new Users(
            username, 
            passwordEncoder.encode(password),
            emailString,
            firstName,
            lastName,
            yearOfBirth,
            sex_code,
            city,
            userWeight,
            userHeight,
            (byte)3,
            (byte)0,
            userRole,
            accountNonExpired,
            accountNonLocked,
            credentialsNonExpired,
            accountEnabled
            );
    }

}
