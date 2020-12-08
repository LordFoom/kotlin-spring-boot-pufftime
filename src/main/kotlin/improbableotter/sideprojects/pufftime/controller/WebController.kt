package improbableotter.sideprojects.pufftime.controller

import improbableotter.sideprojects.pufftime.grow.GrowDto
import improbableotter.sideprojects.pufftime.grow.GrowRepository
import improbableotter.sideprojects.pufftime.grow.GrowService
import improbableotter.sideprojects.pufftime.plant.PlantRepository
import improbableotter.sideprojects.pufftime.strain.StrainDto
import improbableotter.sideprojects.pufftime.strain.StrainRepository
import improbableotter.sideprojects.pufftime.strain.StrainService
import improbableotter.sideprojects.pufftime.user.User
import improbableotter.sideprojects.pufftime.user.UserDto
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
@RequestMapping("/")
class WebController(val userRepository: UserRepository,
                    val plantRepository: PlantRepository,
                    val growRepository: GrowRepository,
                    val growService: GrowService,
                    val strainRepository: StrainRepository,
                    val strainService: StrainService) {

    @GetMapping
    fun index(principal: Principal?): String {
        principal ?: return "home/home_signed_out"
        return "home/home_signed_in"
    }

    @GetMapping("/login")
    fun login(): String {
        return "home/login"
    }

    @RequestMapping("/login_error.html")
    fun loginError(model: Model): String {
        model["loginError"] = true
        return "home/login"
    }

    @GetMapping("/registration")
    fun register(model: Model): String {
        model["user"] = UserDto()
        return "home/registration"
    }

    @GetMapping("/registration_error")
    fun registerError(): String {
        return "home/registration"
    }

    @PostMapping("/registration")
    fun createUser(model: Model, @Valid @ModelAttribute("user") userDto: UserDto, result: BindingResult): String {
        if (result.hasErrors()) {
            return "home/registration"
        }

        var user: User? = userDto.username?.let { userRepository.findByUsername(it) }

        if (null == user) {
            if (null != userDto.email) {
                user = userDto.email?.let { userRepository.findByUsername(it) }
                if (null == user) {
                    val fromDto = User.fromDto(userDto)
                    model["registered"] = userRepository.save(fromDto)
                } else {
                    result.reject("email", "Email already in use")
                }
            }
        } else {
            result.reject("username", "Username taken.")
        }

        if (result.hasErrors()) {
            return "home/registration"
        }
        return "home/home_signed_in"
    }

    @GetMapping("/plants/users/{userId}")
    fun viewUserPlants(model: Model, @PathVariable("userId") userId: Long): String {
        val user = userRepository.findByIdOrNull(userId)!!
        model["plants"] = plantRepository.findByUserId(userId)
        model["header"] = "Plants for User: ${user.username}"
        return "plants/view_plants"
    }

    @GetMapping("/plants/grows/{growId}")
    fun viewGrowPlants(model: Model, @PathVariable("growId") growId: Long): String {
        val grow = growRepository.findByIdOrNull(growId)!!
        model["plants"] = plantRepository.findByGrowId(growId)
        model["header"] = "Plants for Grow: ${grow.id}, started: ${grow.getDisplayCreateDate()}"
        return "plants/view_plants"
    }

    @GetMapping("/strains")
    fun viewStrains(model: Model):String{
        model["strains"] = strainRepository.findAll()
        return "strains/view_strains"
    }

    @GetMapping("/strains/{strainId}")
    fun viewStrain(@PathVariable("strainId") strainId:Long, model: Model):String{
        model["strain"] = strainRepository.findByIdOrNull(strainId) ?: return "redirect:/?error"
        return "strains/view_strain"
    }

    @GetMapping("/strains/add")
    fun getAddStrainForm(model: Model, principal: Principal):String{
//        model["strain"] = CreateStrainDto()
        model["strain"] = StrainDto(username = principal.name)
        return "strains/add_strain"
    }

    @PostMapping("/strains/add")
    fun addStrain(@ModelAttribute @Valid dto:StrainDto, result: BindingResult):String {
        if(result.hasErrors())
            return "strains/add_strain"
        val strain = strainService.create(dto)
        return "redirect:/strains/${strain.id}?success"
    }

    @GetMapping("/strains/{strainId}/edit")
    fun getEditStrainForm(@PathVariable("strainId") strainId: Long, model: Model, principal: Principal):String{
//        model["strain"] = CreateStrainDto()
        val existingStrain = strainRepository.findByIdOrNull(strainId)!!
        model["strain"] = StrainDto(existingStrain.id!!,existingStrain.name, existingStrain.description,
                         user = existingStrain.createdBy, username = existingStrain.createdBy.username )
        return "strains/edit_strain"
    }

    @PostMapping("/strains/{strainId}/edit")
    fun postStrainEdit(@PathVariable("strainId") strainId: Long, @ModelAttribute @Valid dto:StrainDto, result: BindingResult):String{
        dto.id=strainId
        strainService.updateStrain(dto)
        return "redirect:/strains/${strainId}?success_edit"
    }

    @GetMapping("/grows")
    fun viewAllGrows(model: Model, principal: Principal):String{
        val user = userRepository.findByUsername(principal.name)!!
        model["grows"] = growRepository.findAllByUser(user);

        return "grows/view_grows"
    }

    @GetMapping("/grows/{growId}")
    fun viewGrow(@PathVariable growId: Long, model: Model):String{
        model["grow"] = growRepository.findByIdOrNull(growId)!!;
        return "grows/view_grow"
    }

    @GetMapping("/grows/add")
    fun getAddGrowForm(model: Model, principal: Principal):String{
        model["grow"] = GrowDto(username = principal.name, name="" )
        return "grows/add_grow"
    }

    @PostMapping("/grows/add")
    fun addGrow(@ModelAttribute @Valid dto:GrowDto, result: BindingResult):String {
        if(result.hasErrors())
            return "grows/add_grow"
        val grow = growService.create(dto)
        return "redirect:/grows/${grow.id}?success"
    }
}
