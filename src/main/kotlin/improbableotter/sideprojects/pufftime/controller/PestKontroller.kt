package improbableotter.sideprojects.pufftime.controller

import improbableotter.sideprojects.pufftime.pest.Pest
import improbableotter.sideprojects.pufftime.pest.PestDto
import improbableotter.sideprojects.pufftime.pest.PestRepository
import improbableotter.sideprojects.pufftime.pest.PestService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException
import java.security.Principal
import javax.validation.Valid

@Controller
@RequestMapping("/pests")
class PestKontroller (val pestRepo: PestRepository, val pestService: PestService){
    @ModelAttribute("module")
    fun module():String{
        return "pest"
    }

    @GetMapping
    fun viewPests(model: Model):String{
        model["pests"] = pestRepo.findAll()
        return "pests/view_pests"
    }


    @GetMapping("/add")
    fun getAddPestForm(model: Model):String{
        model["pest"] = PestDto()
        return "pests/add_pest"
    }

    @PostMapping("/add")
    fun addPest(@ModelAttribute @Valid dto: PestDto, result:BindingResult):String{
        val pest = pestRepo.save(Pest.fromDto(dto))
        return "redirect:/pests/${pest.id}?success"
    }

    @GetMapping("/{pestId}")
    fun viewPest(@PathVariable("pestId") pestId:Long, model:Model):String{
        model["pest"]= pestRepo.findByIdOrNull(pestId)?:throw IllegalArgumentException("No such pest")
        return "pests/view_pest"
    }
}