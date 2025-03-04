package ru.project.runjournal.run_journal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/addactivity")
public class ActivityAddingController {

    @GetMapping
    public String showAddActivityPage(){
        return "addactivity";
    }
}
