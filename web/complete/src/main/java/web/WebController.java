package web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {

   
    @GetMapping("/getAvgPx")
    public String avgPxForm(Model model) {
        model.addAttribute("getAvgPx", new GetAvgPx());
        return "getAvgPx";
    }
 

    @PostMapping("/getAvgPx")
    public String avgPxSubmit(@ModelAttribute GetAvgPx avgPx) {
        return "avgPx";
    }

}
