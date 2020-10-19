package improbableotter.sideprojects.pufftime.controller

import improbableotter.sideprojects.pufftime.user.UserDto
import improbableotter.sideprojects.pufftime.user.UserRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.security.Principal

@Controller
@RequestMapping("/")
class WebController(val userRepository: UserRepository){

    @ModelAttribute("user")
    fun userRegistrationDto():UserDto{
        return UserDto()
    }

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

    @GetMapping("/registration")
    fun register(model: Model):String{
        return "home/registration"
    }

    @GetMapping("/registration_error")
    fun registerError():String{
        return "home/registration"
    }

    @PostMapping("/registration")
    fun createUser(@ModelAttribute("user") userDto: UserDto, model: Model, result: BindingResult): String {
        val user = userDto.username?.let { userRepository.findByUsername(it) }
        if(null == user){
            val savedUser = userRepository.save(userDto)
            model["registered"]=savedUser;
        }else
            result.reject("username", "Username taken.")

        if(result.hasErrors())
            return "home/registration";
        return "home/home_signed_in"
    }

}
