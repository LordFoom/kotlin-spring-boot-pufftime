package improbableotter.sideprojects.pufftime.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute

@Controller
class PestKontroller {
    @ModelAttribute("module")
    fun module():String{
        return "pest"
    }

}