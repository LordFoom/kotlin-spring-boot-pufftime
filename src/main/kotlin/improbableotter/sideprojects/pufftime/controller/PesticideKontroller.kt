package improbableotter.sideprojects.pufftime.controller

import improbableotter.sideprojects.pufftime.pesticide.PesticideDto
import improbableotter.sideprojects.pufftime.pesticide.PesticideRepository
import improbableotter.sideprojects.pufftime.pesticide.PesticideService
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
@RequestMapping("/pesticides")
class PesticideKontroller(
    val pesticideRepo: PesticideRepository,
    val userRepo: UserRepository,
    val pesticideService: PesticideService
) {
    @ModelAttribute("module")
    fun module(): String {
        return "pesticide"
    }


    @GetMapping
    fun viewPesticides(model: Model): String {
        model["pesticides"] = pesticideRepo.findAll()

        return "pesticides/view_pesticides"
    }

    @GetMapping("/{pesticideId}")
    fun viewPesticide(@PathVariable pesticideId: Long, model: Model): String {
        model["pesticide"] = pesticideRepo.findByIdOrNull(pesticideId)!!
        return "pesticides/view_pesticide"
    }

    @GetMapping("/add")
    fun getAddPesticides(model: Model): String {
        model["pesticide"] = PesticideDto()
        return "pesticides/add_pesticide"
    }

    @PostMapping("/add")
    fun addPesticide(@ModelAttribute @Valid dto: PesticideDto, result: BindingResult, principal: Principal):String {

        dto.user = userRepo.findByUsername(principal.name)!!

        val pesticide = pesticideService.create(dto)

        return "redirect:/pesticides/${pesticide.id}?success"

    }
}