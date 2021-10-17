package improbableotter.sideprojects.pufftime.controller

import improbableotter.sideprojects.pufftime.common.Status
import improbableotter.sideprojects.pufftime.grow.GrowDto
import improbableotter.sideprojects.pufftime.grow.GrowRepository
import improbableotter.sideprojects.pufftime.grow.GrowService
import improbableotter.sideprojects.pufftime.grow.GrowStatus
import improbableotter.sideprojects.pufftime.lights.GrowLightDto
import improbableotter.sideprojects.pufftime.lights.GrowLightRepository
import improbableotter.sideprojects.pufftime.lights.GrowLightService
import improbableotter.sideprojects.pufftime.lights.LightRepository
import improbableotter.sideprojects.pufftime.note.Note
import improbableotter.sideprojects.pufftime.note.NoteRepository
import improbableotter.sideprojects.pufftime.picture.Picture
import improbableotter.sideprojects.pufftime.picture.PictureRepository
import improbableotter.sideprojects.pufftime.plant.PlantDto
import improbableotter.sideprojects.pufftime.plant.PlantRepository
import improbableotter.sideprojects.pufftime.plant.PlantService
import improbableotter.sideprojects.pufftime.plant.PlantStatus
import improbableotter.sideprojects.pufftime.storage.StorageService
import improbableotter.sideprojects.pufftime.strain.StrainRepository
import improbableotter.sideprojects.pufftime.user.UserRepository
import improbableotter.sideprojects.pufftime.water.NuteStatus
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
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.ConcurrentSkipListMap
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
    val storageService: StorageService,
    val noteRepo: NoteRepository,
    val wateringHistoryRepo: WateringHistoryRepo,
    val lightRepo: LightRepository,
    val growLightService: GrowLightService,
    val growLighRepo: GrowLightRepository,
) {

    @ModelAttribute("module")
    fun module():String{
        return "grow"
    }

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
        model["strains"] = strainRepo.findByStatusIdEqualsOrderByName(Status.ACTIVE.ordinal)
        model["growLights"] = growLighRepo.findByGrowIdOrderByCreateDate(growId)
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
        principal: Principal,
        attributes: RedirectAttributes
    ): String {
        grow.id = growId
        grow.user = userRepo.findByUsername(principal.name)
        model["grow"] = growService.update(grow).toDto()
        attributes.addFlashAttribute("info_message", "Grow successfully edited")
        return "redirect:/grows/$growId"
    }

    @GetMapping("/add")
    fun getAddGrowForm(model: Model, principal: Principal): String {
        model["grow"] = GrowDto(username = principal.name, name = "")
        return "grows/add_grow"
    }

    @PostMapping("/add")
    fun addGrow(@ModelAttribute @Valid dto: GrowDto, result: BindingResult, attributes: RedirectAttributes): String {
        if (result.hasErrors()) {
            return "grows/add_grow"
        }
        val grow = growService.create(dto)
        attributes.addFlashAttribute("info_message", "Grow successfully created!")
        return "redirect:/grows/${grow.id}"
    }

    @GetMapping("/{growId}/plants/add")
    fun getAddPlantToGrowForm(@PathVariable growId: Long, model: Model, principal: Principal): String {
        val user = userRepo.findByUsername(principal.name)!!
        model["grow"] = growRepo.findByIdOrNull(growId)!!
        model["strains"] = strainRepo.findByStatusIdEqualsOrderByName(Status.ACTIVE.ordinal)
        model["plant"] = PlantDto(userId = user.id, growId = growId)

        return "plants/add_plant"
    }

    @GetMapping("/{growId}/lights/add")
    fun getAddLightToGrowForm(@PathVariable growId: Long, model: Model, principal: Principal): String {
        val user = userRepo.findByUsername(principal.name)!!
        model["grow"] = growRepo.findByIdOrNull(growId)!!
        model["lights"] = lightRepo.findAllByStatusIdOrderByIdDesc(Status.ACTIVE.ordinal)
        model["growLight"] = GrowLightDto(userId=user.id!!, growId = growId)

        return "lights/add_light_to_grow"
    }

    @PostMapping("/{growId}/lights/add")
    fun addLightToGrow(
        @PathVariable growId: Long,
        @ModelAttribute @Valid dto: GrowLightDto,
        result: BindingResult,
        model: Model,
        attributes: RedirectAttributes
    ): String {
        val growLight = growLightService.create(dto)
        model["grow"] = growRepo.findByIdOrNull(growId)!!
        attributes.addFlashAttribute("info_message", "Light added")

        return "redirect:/grows/$growId"
    }

    @PostMapping("/{growId}/plants/add")
    fun addPlantToGrow(
        @PathVariable growId: Long,
        @ModelAttribute @Valid dto: PlantDto,
        result: BindingResult,
        model: Model,
        attributes: RedirectAttributes
    ): String {
        plantService.createPlants(dto)

        attributes.addFlashAttribute("info_message", "Plant added")
        return "redirect:/grows/$growId"
    }


    @GetMapping("/{growId}/plants/{plantId}/delete")
    fun deletePlant(@PathVariable growId: Long, @PathVariable plantId: Long, model: Model): String {
        model["grow"] = growRepo.findByIdOrNull(growId)!!
        plantRepo.deleteById(plantId)
        return "redirect:/grows/$growId"
    }

    //TODO move this to plants kontroller
    @GetMapping("/{growId}/plants/{plantId}/pics")
    fun viewPlantGallery(@PathVariable growId: Long, @PathVariable plantId: Long, model: Model): String {
        model["grow"] = growRepo.findByIdOrNull(growId)!!
        model["plant"] = plantRepo.findByIdOrNull(plantId)!!

        return "plants/view_plant_pics"
    }

    @GetMapping("/{growId}/pics")
    fun viewGrowGallery(@PathVariable growId: Long, model: Model): String {
        model["grow"] = growRepo.findByIdOrNull(growId)!!

        return "grows/view_grow_pics"
    }

    @PostMapping("/{growId}/pics")
    fun uploadPic(
        @PathVariable growId: Long, @RequestParam("plantId", required = false) plantId: Long?,
        @RequestParam("file") file: MultipartFile, @RequestParam("notes") notes: String?,
        attributes: RedirectAttributes
    ): String {
        if (file.isEmpty) {
            attributes.addFlashAttribute("error_message", "Please select file to upload.")
            return "redirect:/grows/${growId}"
        }

        val picFilePaths = storageService.store(file)
        attributes.addFlashAttribute("message", "Sucessfully uploaded pic!")
        //could be a pic for either the entire grow or just a plant
        val plant = plantId?.let{plantRepo.findByIdOrNull(plantId)}
        //but we have to have a grow
        val grow = growRepo.findByIdOrNull(growId) ?: throw IllegalStateException("No grow found for id $growId")
        val pic = Picture(filePath = picFilePaths.first, smallFilePath = picFilePaths.second, plant = plant, grow = grow, notes = notes)
        val savedPic = plantPicRepo.save(pic)
        attributes["pic"] = savedPic
        attributes.addFlashAttribute("info_message","Successfully added pic" )
        return "redirect:/grows/${growId}"

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
        attributes.addFlashAttribute("info_message", "Note success")
        return "redirect:/grows/${growId}"
    }

    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    @GetMapping("/{growId}/flower")
    fun switchToFlower( @PathVariable("growId") growId: Long,
                        @RequestParam(required = false) strFlowerDate: String? , attributes: RedirectAttributes): String {
        val flowerDate = strFlowerDate?.let { simpleDateFormat.parse(it) } ?: Date()

        val grow = growRepo.findByIdOrNull(growId)!!
        //bale out if
        grow.flowerDate?.let {
            attributes.addFlashAttribute("error_message", "Grow is already flowering")
            return "redirect:/grows/${growId}"
        }

        grow.flowerDate = flowerDate
        grow.status = GrowStatus.FLOWERING
        grow.plants.forEach{ it.status = PlantStatus.FLOWERING }
        growRepo.save(grow)
        attributes.addFlashAttribute("info_message", "Note success")
        return "redirect:/grows/${growId}"
    }


    @GetMapping("/{growId}/harvest")
    fun switchToHarvested(
        @PathVariable("growId") growId: Long, @RequestParam(required = false) strHarvestDate: String? , attributes: RedirectAttributes): String {
        val harvestDate = strHarvestDate?.let { simpleDateFormat.parse(it) } ?: Date()

        val grow = growRepo.findByIdOrNull(growId)!!
        grow.harvestDate?.let {
            attributes.addFlashAttribute("error_message", "Already harvested")
            return "redirect:/grows/${growId}"
        }

        grow.harvestDate = harvestDate
        grow.status = GrowStatus.HARVESTED
        grow.plants.forEach { it.status = PlantStatus.HARVESTED }
        //cascade should do this for us right?
        growRepo.save(grow)

        attributes.addFlashAttribute("info_message", "Harvested")
        return "redirect:/grows/${growId}"
    }

    @GetMapping("/{growId}/water")
    fun getWateringForm(@PathVariable("growId") growId:Long, model: Model):String{
        val grow = growRepo.findByIdOrNull(growId)!!
        model["grow"] = grow
        model["plants"] = grow.plants
        return "water/water_grow"
    }


    /**
     * If we want to select the plant in the drop list this is the one we select
     */
    @GetMapping("/{growId}/water/{plantId}")
    fun getWateringFormWithPlant(@PathVariable("growId") growId:Long,
                        @PathVariable("plantId") plantId:Long,
                        model: Model):String{
        val grow = growRepo.findByIdOrNull(growId)!!
        plantRepo.findByIdOrNull(plantId)?.let { model["plant"] = it }
        model["grow"] = grow
        model["plants"] = grow.plants

        return "water/water_grow"
    }
    @PostMapping("/{growId}/water")
    fun waterPlants(@PathVariable("growId") growId: Long,
                    @RequestParam(required = false) strWateringDate:String?,
                    @RequestParam(required = false) hasNutes:Boolean?,
                    @RequestParam(required = false) amount:Double?,
                    @RequestParam(required = false) notes: String?,
                    attributes:RedirectAttributes):String {

        val wateringDate = strWateringDate?.let {
            if(strWateringDate.length>8) simpleDateFormat.parse(it)
            else Date()
        } ?: Date()
        val grow = growRepo.findByIdOrNull(growId)!!

        val nuteStatus = hasNutes?.let{if(hasNutes) NuteStatus.NUTES else NuteStatus.NONE}?:NuteStatus.NONE
        //TODO get the feed in here
        wateringHistoryRepo.save(WateringHistory(wateringDate = wateringDate,
            literAmount = amount?:0.0,
            grow = grow,
            notes = notes,
            nutes = nuteStatus))

        attributes.addFlashAttribute("info_message", "Watered!!")
        return "redirect:/grows/${growId}"

    }


    @GetMapping("/{growId}/watering_history")
    fun getGrowWateringHistoryPage(@PathVariable("growId") growId:Long, model: Model):String{
        model["grow"] = growRepo.findByIdOrNull(growId)!!

       return "water/grow_water_history"
    }

    @GetMapping("/{growId}/chart")
    fun getGrowWateringChart(@PathVariable("growId") growId:Long, model: Model):String{
        val grow = growRepo.findByIdOrNull(growId)!!
        model["grow"] = grow
        //from flower start to end we get the date
        val startDate = grow.flowerDate?:Date()
        val beginning = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val endDate = grow.harvestDate?:Date()
        val end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        //get number of days
        val days = beginning.until(end, ChronoUnit.DAYS).toInt()

        val dayWaterMap = ConcurrentSkipListMap<Int, Double>()
        val waterHistoryList = wateringHistoryRepo.findByGrowOrderByWateringDateAsc(grow)
        //initialize the map
        //how do I sort watering into dates

        var whc = 0
        val dayColors = mutableListOf<String>()
        val dayBorders = mutableListOf<String>()

        for(i in 0..days){
            if (whc >= waterHistoryList.size) {
                dayWaterMap[i]=0.0
                dayColors.add("rgba(54, 162, 235, 0.2)")
                dayBorders.add("rgba(54, 162, 235, 1)")
                continue
            }
            val wateringDate = waterHistoryList[whc].wateringDate!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            dayWaterMap[i] = when{
                beginning.plusDays(i.toLong()).isEqual(wateringDate) -> {
                    if(waterHistoryList[whc].nutes == NuteStatus.NUTES){
                        dayColors.add("rgba(255, 99, 132, 0.2)")
                        dayBorders.add("rgba(255, 99, 132, 1)")
                    }else{
                        dayColors.add("rgba(54, 162, 235, 0.2)")
                        dayBorders.add("rgba(54, 162, 235, 1)")
                    }
                    whc += 1
                    waterHistoryList[whc-1].literAmount?:1.0
                }
                else -> {
                    dayColors.add("rgba(54, 162, 235, 0.2)")
                    dayBorders.add("rgba(54, 162, 235, 1)")
                    0.0
                }
            }
        }
        model["day_labels"] = dayWaterMap.keys
        model["day_values"] = dayWaterMap.values
        model["day_colors"] = dayColors
        model["day_borders"] = dayBorders
        return "water/grow_water_chart"
    }

    /**
     * Pests got your plant? Sorry :(
     */
    @PostMapping("/{growId}/spoil")

    fun spoilPlant(@PathVariable("growId") growId: Long, @RequestParam spoilPlantId: Long, attributes: RedirectAttributes):String {
        val plant = plantRepo.findByIdOrNull(spoilPlantId)!!
        plant.status = PlantStatus.SPOILED
        plantRepo.save(plant)
        attributes.addFlashAttribute("info_message", "Plant was spoiled....sorry")
        return "redirect:/grows/${growId}"
    }
}