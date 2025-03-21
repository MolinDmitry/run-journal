package ru.project.runjournal.run_journal;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import ru.project.runjournal.run_journal.Entities.Users;
import ru.project.runjournal.run_journal.Repositories.UsersRepository;

/**
 * @brief Класс конфигурации модуля безопасности
 * 
 * 
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {
     /**
     * @brief Создаем Bean-компонент шифровальщика паролей
     * 
     * Возможные варианты:
     *  BCryptPasswordEncoder – применяет надежное шифрование bcrypt;
     *  NoOpPasswordEncoder – не применяет шифрования (не используется в продакшене);
     *  Pbkdf2PasswordEncoder – применяет шифрование PBKDF2;
     *  SCryptPasswordEncoder – применяет шифрование Scrypt;
     *  StandardPasswordEncoder – применяет шифрование SHA-256 (устарел, не используется в продакшене).
     *  
     * Пароли хранятся в БД в зашифрованном виде
     * @return Объект PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * @brief Компонент службы хранения учетных записей
     * 
     * Метод userDetailsService() принимает параметр UserRepository. 
     * Чтобы создать bean-компонент, он возвращает лямбда-функцию,  
     * которая принимает параметр username и использует его для вызова  
     * метода findByUsername() репозитория UserRepository.
     * 
     * @param userRepo репозиторий данных пользователей
     * @return Возвращает найденную учетную запись, либо бросает исключение, если она не найдена
     */
   @Bean
   public UserDetailsService userDetailsService(UsersRepository usersRepo){
        return username->{
            Optional<Users> user = usersRepo.findByUsername(username);
            if (user.isPresent()) {
                return user.get();
            }
            else{
                throw new UsernameNotFoundException("User /'"+username+"/' not found");
            }
            
        };
   }

   /**
    * @brief Настраивает защиту веб-запросов
    *
    * Приведенный подход рекомендуется применять как новый. В версии 7.0
    * @param http
    * @return
    * @throws Exception
    */
   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    http
    .authorizeHttpRequests((authorizeHttpRequests) ->
        authorizeHttpRequests
            .requestMatchers("/run-journal/login","/run-journal/register").permitAll()
            .anyRequest().authenticated()
    )
    .formLogin(formLogin -> formLogin
                .loginPage("/run-journal/login")
                .defaultSuccessUrl("/run-journal")
                .permitAll()
            )
            .rememberMe(Customizer.withDefaults())
    .logout((logout) -> logout.logoutUrl("/run-journal/logout"));
return http.build();
   }

}
