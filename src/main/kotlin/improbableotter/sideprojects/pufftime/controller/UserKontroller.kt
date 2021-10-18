package improbableotter.sideprojects.pufftime.controller

import improbableotter.sideprojects.pufftime.user.UserRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import java.security.Principal

@Controller
@RequestMapping("/users/")
class UserKontroller(val userRepo: UserRepository) {
    @ModelAttribute
    fun module():String{
        return "user"
    }

    @GetMapping
    fun viewUsers(model:Model, principal: Principal):String{
        //TODO paginate this..and grows...and plants
        model["users"] = userRepo.findAll()
        return "users/view_users.html"
    }
}