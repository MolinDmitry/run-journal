package ru.project.runjournal.run_journal.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.project.runjournal.run_journal.Entities.RegistrationForm;
import ru.project.runjournal.run_journal.Repositories.UsersRepository;


/**
 * @brief Контроллер регистрацонной формы 
 * 
 */
@Controller
@RequestMapping("/run-journal/register")
public class RegistrationController {

    private final UsersRepository usersRepo;
    private final PasswordEncoder passEncoder;

    @Autowired
    public RegistrationController(UsersRepository usersRepository, PasswordEncoder passwordEncoder){
        this.passEncoder = passwordEncoder;
        this.usersRepo = usersRepository;
    }


    @GetMapping
    public String showRegistrationForm(){
        return "register";
    }

    /**
     * @brief Обрабатывает запросы post
     * 
     * Сохраняет данные пользователя в БД , кодируя при этом пароль
     * @param regForm Форма регистрации. Не создаем как атрибут модели, однако для корректной работы форма в шаблоне страницы должна иметь 
     * id = "regForm" и имена полей ввода соответствующие полям класса RegistrationForm  
     * @return Возвращает перенаправление на страницу журнала тренировок
     */
    @PostMapping
    public String processRegistrationForm(RegistrationForm regForm){
        if (regForm.getPassword().equals(regForm.getRep_password()))
        {
            if (usersRepo.findByUsername(regForm.getUsername()).isPresent()) return "redirect:register?error_user_is_exist";
            else{
                usersRepo.save(regForm.toUser(passEncoder,
                                    "ROLE_USER",
                                    true,
                                    true,
                                    true,
                                    true));        
            return "redirect:run-journal/login";
            }            
        }
        else{
            return "redirect:run-journal/register?error_password_check";
        }
        
        
    }

}
