package improbableotter.sideprojects.pufftime.controller

import improbableotter.sideprojects.pufftime.pesticide.PesticideRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/pesticides")
class PesticideKontroller(val pesticideRepo: PesticideRepository) {
    @ModelAttribute("module")
    fun module():String{
        return "pesticide"
    }


    @GetMapping
    fun viewPesticides(model: Model):String{
        model["pesticides"]=pesticideRepo.findAll()

        return "pesticides/view_pesticides"
    }
}