package improbableotter.sideprojects.pufftime.controller

import improbableotter.sideprojects.pufftime.common.Status
import improbableotter.sideprojects.pufftime.grow.GrowDto
import improbableotter.sideprojects.pufftime.grow.GrowRepository
import improbableotter.sideprojects.pufftime.grow.GrowService
import improbableotter.sideprojects.pufftime.note.Note
import improbableotter.sideprojects.pufftime.note.NoteRepository
import improbableotter.sideprojects.pufftime.picture.Picture
import improbableotter.sideprojects.pufftime.picture.PictureRepository
import improbableotter.sideprojects.pufftime.plant.PlantDto
import improbableotter.sideprojects.pufftime.plant.PlantRepository
import improbableotter.sideprojects.pufftime.plant.PlantService
import improbableotter.sideprojects.pufftime.storage.StorageService
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
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.security.Principal
import javax.validation.Valid

@Controller
@RequestMapping("/")
class WebController(
    val userRepo: UserRepository,
    val plantRepo: PlantRepository,
    val plantPicRepo: PictureRepository,
    val plantService: PlantService,
    val growRepo: GrowRepository,
    val growService: GrowService,
    val strainRepo: StrainRepository,
    val strainService: StrainService,
    val storageService: StorageService,
    val noteRepo: NoteRepository,
) {

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

        var user: User? = userDto.username?.let { userRepo.findByUsername(it) }

        if (null == user) {
            if (null != userDto.email) {
                user = userDto.email?.let { userRepo.findByUsername(it) }
                if (null == user) {
                    val fromDto = User.fromDto(userDto)
                    model["registered"] = userRepo.save(fromDto)
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
        val user = userRepo.findByIdOrNull(userId)!!
        model["plants"] = plantRepo.findByUserId(userId)
        model["header"] = "Plants for User: ${user.username}"
        return "plants/view_plants"
    }

    @GetMapping("/plants/grows/{growId}")
    fun viewGrowPlants(model: Model, @PathVariable("growId") growId: Long): String {
        val grow = growRepo.findByIdOrNull(growId)!!
        model["plants"] = plantRepo.findByGrowId(growId)
        model["header"] = "Plants for Grow: ${grow.id}, started: ${grow.getDisplayCreateDate()}"
        return "plants/view_plants"
    }

    @GetMapping("/strains")
    fun viewStrains(model: Model): String {
        model["strains"] = strainRepo.findAll()
        return "strains/view_strains"
    }

    @GetMapping("/strains/{strainId}")
    fun viewStrain(@PathVariable("strainId") strainId: Long, model: Model): String {
        model["strain"] = strainRepo.findByIdOrNull(strainId) ?: return "redirect:/?error"
        return "strains/view_strain"
    }

    @GetMapping("/strains/add")
    fun getAddStrainForm(model: Model, principal: Principal): String {
//        model["strain"] = CreateStrainDto()
        model["strain"] = StrainDto(username = principal.name)
        return "strains/add_strain"
    }

    @PostMapping("/strains/add")
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

    @GetMapping("/grows")
    fun viewAllGrows(model: Model, principal: Principal): String {
        val user = userRepo.findByUsername(principal.name)!!
        model["grows"] = growRepo.findAllByUser(user)

        return "grows/view_grows"
    }

    @GetMapping("/grows/{growId}")
    fun viewGrow(@PathVariable growId: Long, model: Model): String {
        model["grow"] = growRepo.findByIdOrNull(growId)!!
        model["plants"] = plantRepo.findByGrowIdOrderByStrainDesc(growId)
        noteRepo.findTopByGrowIdOrderByIdDesc(growId)?.let { model["note"] = it }
        return "grows/view_grow"
    }

    @GetMapping("/grows/{growId}/edit")
    fun getGrowEditForm(@PathVariable growId: Long, model: Model): String {
        val existingGrow = growRepo.findByIdOrNull(growId)!!
        model["grow"] = existingGrow.toDto()
        return "grows/edit_grow"
    }

    @PostMapping("/grows/{growId}/edit")
    fun editGrow(
        @PathVariable growId: Long,
        @ModelAttribute @Valid grow: GrowDto,
        result: BindingResult,
        model: Model,
        principal: Principal
    ): String {
        grow.id = growId
        grow.user = userRepo.findByUsername(principal.name)
        model["grow"] = growService.update(grow).toDto()
        return "redirect:/grows/$growId?success_edit"
    }

    @GetMapping("/grows/add")
    fun getAddGrowForm(model: Model, principal: Principal): String {
        model["grow"] = GrowDto(username = principal.name, name = "")
        return "grows/add_grow"
    }

    @PostMapping("/grows/add")
    fun addGrow(@ModelAttribute @Valid dto: GrowDto, result: BindingResult): String {
        if (result.hasErrors()) {
            return "grows/add_grow"
        }
        val grow = growService.create(dto)
        return "redirect:/grows/${grow.id}?success"
    }

    @GetMapping("/grows/{growId}/plants/add")
    fun getAddPlantToGrowForm(@PathVariable growId: Long, model: Model, principal: Principal): String {
        val user = userRepo.findByUsername(principal.name)!!
        model["grow"] = growRepo.findByIdOrNull(growId)!!
        model["strains"] = strainRepo.findByStatusIdEquals(Status.ACTIVE.ordinal)
        model["plant"] = PlantDto(userId = user.id, growId = growId)

        return "plants/add_plant"
    }

    @PostMapping("/grows/{growId}/plants/add")
    fun addPlantToGrow(
        @PathVariable growId: Long,
        @ModelAttribute @Valid dto: PlantDto,
        result: BindingResult,
        model: Model
    ): String {
        plantService.createPlants(dto)
        return "redirect:/grows/$growId?plantSuccess"
    }

    @GetMapping("/grows/{growId}/plants/{plantId}/delete")
    fun deletePlant(@PathVariable growId: Long, @PathVariable plantId: Long, model: Model): String {
        model["grow"] = growRepo.findByIdOrNull(growId)!!
        plantRepo.deleteById(plantId)
        return "redirect:/grows/$growId"
//        return "grows/view_grow"
    }

    @GetMapping("/grows/{growId}/plants/{plantId}/pics")
    fun getPlantGallery(@PathVariable growId: Long, @PathVariable plantId: Long, model: Model): String {
        model["grow"] = growRepo.findByIdOrNull(growId)!!
        model["plant"] = plantRepo.findByIdOrNull(plantId)!!

        return "plants/view_plant"
//        return "grows/view_grow"
    }

//    @GetMapping("/grows/{growId}/plants/{plantId}/pics/upload")
//    fun getPlantPicUploadForm(@PathVariable growId: Long, @PathVariable plantId: Long, model: Model):String {
//        model["grow"] = growRepo.findByIdOrNull(growId)!!
//        model["plant"] = plantRepo.findByIdOrNull(plantId)!!
//
//        return "plants/upload_plant_pic"
//    }

    @PostMapping("/grows/{growId}/pics")
    fun uploadPic(
        @PathVariable growId: Long, @RequestParam("plantId", required = false) plantId: Long,
        @RequestParam("file") file: MultipartFile, @RequestParam("notes") notes: String?,
        attributes: RedirectAttributes ): String {
        if (file.isEmpty) {
            attributes.addFlashAttribute("error_message", "Please select file to upload.")
            return "redirect:/grows/${growId}"
        }

        val picFilePath = storageService.store(file)
        attributes.addFlashAttribute("message", "Sucessfully uploaded pic!")
        //could be a pic for either the entire grow or just a plant
        val plant = plantRepo.findByIdOrNull(plantId)
        //but we have to have a grow
        val grow = growRepo.findByIdOrNull(growId) ?: throw IllegalStateException("No grow found for id $growId")
        val pic = Picture(filePath = picFilePath, plant = plant, grow = grow, notes = notes)
        val savedPic = plantPicRepo.save(pic)
        attributes["pic"] = savedPic;
        return "redirect:/grows/${growId}?pic_success"

    }

    @PostMapping("/grows/{growId}/notes")
    fun uploadNote(
    @PathVariable growId: Long, @RequestParam("notePlantId", required = false) plantId: Long,
    @RequestParam("notes") note:String, attributes: RedirectAttributes ):String{
        val plant = plantRepo.findByIdOrNull(plantId)
        //but we have to have a grow
        val grow = growRepo.findByIdOrNull(growId) ?: throw IllegalStateException("No grow found for id $growId")
        val noteEntity = Note(plant = plant, grow = grow, note = note)
        noteRepo.save(noteEntity)
        return "redirect:/grows/${growId}?note_success"
    }

}
