package improbableotter.sideprojects.pufftime.controller

import improbableotter.sideprojects.pufftime.lights.LightDto
import improbableotter.sideprojects.pufftime.lights.LightRepository
import improbableotter.sideprojects.pufftime.lights.LightService
import improbableotter.sideprojects.pufftime.user.UserRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.security.Principal
import java.text.SimpleDateFormat

@Controller
@RequestMapping("/lights")
class LightKontroller(
    val userRepo: UserRepository,
    val lightsRepo: LightRepository,
    val lightService: LightService
) {

    @GetMapping
    fun viewLights(model: Model, principal: Principal): String {
        val user = userRepo.findByUsername(principal.name)!!
        model["lights"] = lightsRepo.findAllByUserOrderByCreateDateDesc(user)
        return "lights/view_lights"
    }

    @GetMapping("/add")
    fun getAddLightForm(model: Model, principal: Principal): String {
        model["light"] = LightDto(username = principal.name)
        return "lights/add_light"
    }

    @PostMapping("/add")
//    fun addLight(@ModelAttribute @Valid dto: LightDto, result:BindingResult):String{
    fun addLight(
        @RequestParam("name") name: String,
        @RequestParam("brand") brand: String,
        @RequestParam("power") power: String,
        @RequestParam("temperatureColor") temperatureColor: String,
        @RequestParam("purchaseDate") purchaseDate: String,
        @RequestParam("notes") notes: String,
        principal: Principal,
        attributes: RedirectAttributes
    ): String {
//        if (result.hasErrors()) {
//            return "lights/add_light"
//        }

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
//        simpleDateFormat.parse(purchaseDate)
        val dto = LightDto(
            username = principal.name,
            name = "name",
            brand = brand,
            power = power,
            temperatureColor = temperatureColor,
            purchaseDate = simpleDateFormat.parse(purchaseDate),
            notes = notes
        )
        val light = lightService.create(dto)
        return "redirect:lights/${light.id}?success"

    }

}

