package improbableotter.sideprojects.pufftime.controller

import improbableotter.sideprojects.pufftime.common.Status
import improbableotter.sideprojects.pufftime.grow.GrowDto
import improbableotter.sideprojects.pufftime.grow.GrowRepository
import improbableotter.sideprojects.pufftime.grow.GrowService
import improbableotter.sideprojects.pufftime.grow.GrowStatus
import improbableotter.sideprojects.pufftime.note.Note
import improbableotter.sideprojects.pufftime.note.NoteRepository
import improbableotter.sideprojects.pufftime.picture.Picture
import improbableotter.sideprojects.pufftime.picture.PictureRepository
import improbableotter.sideprojects.pufftime.plant.PlantDto
import improbableotter.sideprojects.pufftime.plant.PlantRepository
import improbableotter.sideprojects.pufftime.plant.PlantService
import improbableotter.sideprojects.pufftime.storage.StorageService
import improbableotter.sideprojects.pufftime.strain.StrainRepository
import improbableotter.sideprojects.pufftime.strain.StrainService
import improbableotter.sideprojects.pufftime.user.UserRepository
import improbableotter.sideprojects.pufftime.water.WateringHistory
import improbableotter.sideprojects.pufftime.water.WateringHistoryRepo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.security.Principal
import java.text.SimpleDateFormat
import java.util.*
import javax.validation.Valid

/**
 * TODO Move all the grow methods here
 */
@Controller
@RequestMapping("/grows")
class GrowKontroller(
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
    val wateringHistoryRepo: WateringHistoryRepo,
) {

    @GetMapping("")
    fun viewAllGrows(model: Model, principal: Principal): String {
        val user = userRepo.findByUsername(principal.name)!!
        model["grows"] = growRepo.findAllByUser(user)
        model["title"] = "Grows, grows, grows, grows"


        return "grows/view_grows"
    }

    @GetMapping("/{growId}")
    fun viewGrow(@PathVariable growId: Long, model: Model): String {
        model["grow"] = growRepo.findByIdOrNull(growId)!!
        model["plants"] = plantRepo.findByGrowIdOrderByStrainDesc(growId)
        model["title"] = "Grow Watching"
        noteRepo.findTopByGrowIdOrderByIdDesc(growId)?.let { model["note"] = it }
        return "grows/view_grow"
    }

    @GetMapping("/{growId}/edit")
    fun getGrowEditForm(@PathVariable growId: Long, model: Model): String {
        val existingGrow = growRepo.findByIdOrNull(growId)!!
        model["grow"] = existingGrow.toDto()
        return "grows/edit_grow"
    }

    @PostMapping("/{growId}/edit")
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

    @GetMapping("/add")
    fun getAddGrowForm(model: Model, principal: Principal): String {
        model["grow"] = GrowDto(username = principal.name, name = "")
        return "grows/add_grow"
    }

    @PostMapping("/add")
    fun addGrow(@ModelAttribute @Valid dto: GrowDto, result: BindingResult): String {
        if (result.hasErrors()) {
            return "grows/add_grow"
        }
        val grow = growService.create(dto)
        return "redirect:/grows/${grow.id}?success"
    }

    @GetMapping("/{growId}/plants/add")
    fun getAddPlantToGrowForm(@PathVariable growId: Long, model: Model, principal: Principal): String {
        val user = userRepo.findByUsername(principal.name)!!
        model["grow"] = growRepo.findByIdOrNull(growId)!!
        model["strains"] = strainRepo.findByStatusIdEquals(Status.ACTIVE.ordinal)
        model["plant"] = PlantDto(userId = user.id, growId = growId)

        return "plants/add_plant"
    }

    @PostMapping("/{growId}/plants/add")
    fun addPlantToGrow(
        @PathVariable growId: Long,
        @ModelAttribute @Valid dto: PlantDto,
        result: BindingResult,
        model: Model
    ): String {
        plantService.createPlants(dto)
        return "redirect:/$growId?plantSuccess"
    }

    @GetMapping("/{growId}/plants/{plantId}/delete")
    fun deletePlant(@PathVariable growId: Long, @PathVariable plantId: Long, model: Model): String {
        model["grow"] = growRepo.findByIdOrNull(growId)!!
        plantRepo.deleteById(plantId)
        return "redirect:/grows/$growId"
//        return "grows/view_grow"
    }

    @GetMapping("/{growId}/plants/{plantId}/pics")
    fun getPlantGallery(@PathVariable growId: Long, @PathVariable plantId: Long, model: Model): String {
        model["grow"] = growRepo.findByIdOrNull(growId)!!
        model["plant"] = plantRepo.findByIdOrNull(plantId)!!

        return "plants/view_plant"
//        return "grows/view_grow"
    }

//    @GetMapping("/{growId}/plants/{plantId}/pics/upload")
//    fun getPlantPicUploadForm(@PathVariable growId: Long, @PathVariable plantId: Long, model: Model):String {
//        model["grow"] = growRepo.findByIdOrNull(growId)!!
//        model["plant"] = plantRepo.findByIdOrNull(plantId)!!
//
//        return "plants/upload_plant_pic"
//    }

    @PostMapping("/{growId}/pics")
    fun uploadPic(
        @PathVariable growId: Long, @RequestParam("plantId", required = false) plantId: Long,
        @RequestParam("file") file: MultipartFile, @RequestParam("notes") notes: String?,
        attributes: RedirectAttributes
    ): String {
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

    @PostMapping("/{growId}/notes")
    fun uploadNote(
        @PathVariable growId: Long, @RequestParam("notePlantId", required = false) plantId: Long,
        @RequestParam("notes") note: String, attributes: RedirectAttributes ): String {
        val plant = plantRepo.findByIdOrNull(plantId)
        //but we have to have a grow
        val grow = growRepo.findByIdOrNull(growId) ?: throw IllegalStateException("No grow found for id $growId")
        val noteEntity = Note(plant = plant, grow = grow, note = note)
        noteRepo.save(noteEntity)
        return "redirect:/grows/${growId}?note_success"
    }

    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    @GetMapping("/{growId}/flower")
    fun switchToFlower(
        @PathVariable("growId") growId: Long, @RequestParam(required = false) strFlowerDate: String? ): String {
        val flowerDate = strFlowerDate?.let { simpleDateFormat.parse(it) } ?: Date()

        val grow = growRepo.findByIdOrNull(growId)!!
        //bale out if
        grow.flowerDate?.let {
            return "redirect:/grows/${growId}?already_flowering"
        }

        grow.flowerDate = flowerDate
        grow.status = GrowStatus.FLOWERING
        growRepo.save(grow)
        return "redirect:/grows/${growId}?flowering_success"
    }


    @GetMapping("/{growId}/harvest")
    fun switchToHarvested(
        @PathVariable("growId") growId: Long, @RequestParam(required = false) strHarvestDate: String? ): String {
        val harvestDate = strHarvestDate?.let { simpleDateFormat.parse(it) } ?: Date()

        val grow = growRepo.findByIdOrNull(growId)!!
        grow.harvestDate?.let {
            return "redirect:/grows/${growId}?already_harvested"
        }

        grow.harvestDate = harvestDate
        growRepo.save(grow)
        return "redirect:/grows/${growId}?harvesting_success"
    }

    @GetMapping("/{growId}/watering")
    fun waterPlants(@PathVariable("growId") growId: Long,
                   @RequestParam(required = false) strWateringDate:String?,
                   @RequestParam(required = false) notes: String?):String {

        val wateringDate = strWateringDate?.let { simpleDateFormat.parse(it) } ?: Date()
        val grow = growRepo.findByIdOrNull(growId)!!

        //TODO get the feed in here
        wateringHistoryRepo.save(WateringHistory(wateringDate = wateringDate, grow = grow, notes = notes))

        return "redirect:/grows/${growId}?watering_success"

    }
}