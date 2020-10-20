package improbableotter.sideprojects.pufftime.controller

import improbableotter.sideprojects.pufftime.user.User
import improbableotter.sideprojects.pufftime.user.UserDto
import improbableotter.sideprojects.pufftime.user.UserRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.security.Principal
import javax.validation.Valid

@Controller
@RequestMapping("/")
class WebController(val userRepository: UserRepository) {

//    @ModelAttribute("user")
//    fun userRegistrationDto():UserDto{
//        return UserDto()
//    }

    @GetMapping
    fun index(principal: Principal?): String {
        principal ?: return "home/home_signed_out"
        return "home/home_signed_in"
    }

    @GetMapping("/login")
    fun login(): String {
        return "home/login"
    }

    @RequestMapping("/login_error.html")
    fun loginError(model: Model): String {
        model["loginError"] = true
        return "home/login"
    }

    @GetMapping("/registration")
    fun register(model: Model): String {
        model["user"] = UserDto()
        return "home/registration"
    }

    @GetMapping("/registration_error")
    fun registerError(): String {
        return "home/registration"
    }

    @PostMapping("/registration")
    fun createUser(@ModelAttribute("user") @Validated userDto: UserDto, model: Model, result: BindingResult): String {
        var user: User? = userDto.username?.let { userRepository.findByUsername(it) }

        if(null == user) {
            if (null != userDto.email) {
                user = userDto.email?.let{userRepository.findByUsername(it)}
                if(null==user){
                    val fromDto = User.fromDto(userDto)
                    val savedUser: User = userRepository.save(fromDto)
                    model["registered"] = savedUser;
                } else
                    result.reject("email", "Email already in use")            }
        } else
            result.reject("username", "Username taken.")

        if (result.hasErrors())
            return "home/registration";
        return "home/home_signed_in"
    }

}
