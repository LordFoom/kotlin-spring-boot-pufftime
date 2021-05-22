package improbableotter.sideprojects.pufftime.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/pests")
class PestKontroller {
    @ModelAttribute("module")
    fun module():String{
        return "pest"
    }

}