package improbableotter.sideprojects.pufftime.controller

import improbableotter.sideprojects.pufftime.nute.NuteDto
import improbableotter.sideprojects.pufftime.nute.NuteRepository
import improbableotter.sideprojects.pufftime.nute.NuteService
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
import javax.validation.Valid

@Controller
@RequestMapping("/nutes")
class NuteKontroller(
    val nuteRepo: NuteRepository,
    val userRepo: UserRepository,
    val nuteService: NuteService
) {

    @GetMapping
    fun viewNutes(model: Model, principal: Principal): String {
        val user = userRepo.findByUsername(principal.name)!!
        model["nutes"] = nuteRepo.findByUser(user)
        return "nutes/view_nutes"

    }

    @GetMapping("/add")
    fun getAddNuteForm(model: Model, principal: Principal): String {
        val user = userRepo.findByUsername(principal.name)!!
        model["nute"] = NuteDto(userId = user.id)

        return "nutes/add_nute"
    }

    @PostMapping("/add")
    fun addNute(@ModelAttribute @Valid dto: NuteDto, result: BindingResult, model: Model):String {
        if(result.hasErrors())
            return "nutes/add_nute"

        val nute = nuteService.create(dto)
        return "redirect:/nutes/${nute.id}?success"
    }

}