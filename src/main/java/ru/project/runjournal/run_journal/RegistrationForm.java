package ru.project.runjournal.run_journal;

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
    private String firstName;
    private String lastName;
    private Short  yearOfBirth;
    private Byte sex; // 0 - женский, 1 - мужской
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
        return new Users(
            username, 
            passwordEncoder.encode(password),
            firstName,
            lastName,
            yearOfBirth,
            sex,
            city,
            userWeight,
            userHeight,
            userRole,
            accountNonExpired,
            accountNonLocked,
            credentialsNonExpired,
            accountEnabled
            );
    }

}
