package improbableotter.sideprojects.pufftime.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.security.Principal

@Controller
@RequestMapping("/")
class WebController{

    @GetMapping
    fun index( principal: Principal?):String{
        principal?:return "home/home_signed_out"
        return "home/home_signed_in"
    }
    @GetMapping("/login")
    fun login(): String {
        return "home/login"
    }

    @RequestMapping("/login_error.html")
    fun loginError(model: Model): String {
        model["loginError"]=true
        return "home/login"
    }
}
