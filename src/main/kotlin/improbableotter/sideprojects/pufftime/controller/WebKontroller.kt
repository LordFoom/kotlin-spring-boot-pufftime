package improbableotter.sideprojects.pufftime.controller

import improbableotter.sideprojects.pufftime.grow.GrowRepository
import improbableotter.sideprojects.pufftime.grow.GrowService
import improbableotter.sideprojects.pufftime.note.NoteRepository
import improbableotter.sideprojects.pufftime.picture.PictureRepository
import improbableotter.sideprojects.pufftime.plant.PlantRepository
import improbableotter.sideprojects.pufftime.plant.PlantService
import improbableotter.sideprojects.pufftime.storage.StorageService
import improbableotter.sideprojects.pufftime.strain.StrainDto
import improbableotter.sideprojects.pufftime.strain.StrainRepository
import improbableotter.sideprojects.pufftime.strain.StrainService
import improbableotter.sideprojects.pufftime.user.User
import improbableotter.sideprojects.pufftime.user.UserDto
import improbableotter.sideprojects.pufftime.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid

@Controller
@RequestMapping("/")
class WebKontroller(
    val userRepo: UserRepository,
    val growRepo: GrowRepository,
) {

    @GetMapping
    fun index(model: Model, principal: Principal?): String {
        principal ?: return "home/home_signed_out"

        val grows = growRepo.findAllByUserUsernameOrderByStartDateDesc(principal.name)
        if(grows.size>0)
            model["grow"] = grows[0]


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
    fun createUser(model: Model, @Valid @ModelAttribute("user") userDto: UserDto, result: BindingResult): String {
        if (result.hasErrors()) {
            return "home/registration"
        }

         userDto
            .username
            ?.let{userRepo.findByUsername(it) }
            ?.let{ result.reject("username", "Username taken") }
            ?: userDto.email
                 ?.let {
                     userRepo.findByEmail(it)
                         ?.let { result.reject("email", "Email already in user") }
                         ?: {
                             val fromDto = User.fromDto(userDto)
                             model["registered"] = userRepo.save(fromDto)
                         }
                 }?: result.reject("email", "Need an email address")

//        if (null == user) {
//            if (null != userDto.email) {
//                user = userDto.email?.let { userRepo.findByUsername(it) }
//                if (null == user) {
//                    val fromDto = User.fromDto(userDto)
//                    model["registered"] = userRepo.save(fromDto)
//                } else {
//                    result.reject("email", "Email already in use")
//                }
//            }
//        } else {
//            result.reject("username", "Username taken.")
//        }

        if (result.hasErrors()) {
            return "home/registration"
        }
        return "home/home_signed_in"
    }

}
