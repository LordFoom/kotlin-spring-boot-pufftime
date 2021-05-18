package improbableotter.sideprojects.pufftime.controller

import improbableotter.sideprojects.pufftime.strain.StrainDto
import improbableotter.sideprojects.pufftime.strain.StrainRepository
import improbableotter.sideprojects.pufftime.strain.StrainService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid

@Controller
@RequestMapping("/strains")
class StrainKontroller(val strainRepo: StrainRepository,
                       val strainService: StrainService
                        ) {
    @ModelAttribute("module")
    fun module():String{
        return "strain"
    }


    @GetMapping
    fun viewStrains(model: Model): String {
        model["strains"] = strainRepo.findAll()
        return "strains/view_strains"
    }

    @GetMapping("/{strainId}")
    fun viewStrain(@PathVariable("strainId") strainId: Long, model: Model): String {
        model["strain"] = strainRepo.findByIdOrNull(strainId) ?: return "redirect:/?error"
        return "strains/view_strain"
    }

    @GetMapping("/add")
    fun getAddStrainForm(model: Model, principal: Principal): String {
//        model["strain"] = CreateStrainDto()
        model["strain"] = StrainDto(username = principal.name)
        return "strains/add_strain"
    }

    @PostMapping("/add")
    fun addStrain(@ModelAttribute @Valid dto: StrainDto, result: BindingResult): String {
        if (result.hasErrors()) {
            return "strains/add_strain"
        }
        val strain = strainService.create(dto)
        return "redirect:/strains/${strain.id}?success"
    }

    @GetMapping("/strains/{strainId}/edit")
    fun getEditStrainForm(@PathVariable("strainId") strainId: Long, model: Model, principal: Principal): String {
//        model["strain"] = CreateStrainDto()
        val existingStrain = strainRepo.findByIdOrNull(strainId)!!
        model["strain"] = StrainDto(
            existingStrain.id!!, existingStrain.name, existingStrain.description,
            user = existingStrain.createdBy, username = existingStrain.createdBy.username
        )
        return "strains/edit_strain"
    }

    @PostMapping("/strains/{strainId}/edit")
    fun editStrain(
        @PathVariable("strainId") strainId: Long,
        @ModelAttribute @Valid dto: StrainDto,
        result: BindingResult
    ): String {
        dto.id = strainId
        strainService.updateStrain(dto)
        return "redirect:/strains/${strainId}?success_edit"
    }
}