package improbableotter.sideprojects.pufftime.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute

@Controller
class PesticideKontroller {
    @ModelAttribute("module")
    fun module():String{
        return "pesticide"
    }

}