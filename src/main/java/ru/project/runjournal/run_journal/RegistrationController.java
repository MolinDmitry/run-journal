package ru.project.runjournal.run_journal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @brief Контроллер регистрацонной формы 
 * 
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {

    @SuppressWarnings("unused")
    private final UsersRepository usersRepo;
    @SuppressWarnings("unused")
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
        usersRepo.save(regForm.toUser(passEncoder,
                                    "ROLE_USER",
                                    true,
                                    true,
                                    true,
                                    true));        
        return "redirect:activities";
    }

}
