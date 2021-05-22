package improbableotter.sideprojects.pufftime.controller

import improbableotter.sideprojects.pufftime.grow.GrowRepository
import improbableotter.sideprojects.pufftime.plant.PlantRepository
import improbableotter.sideprojects.pufftime.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/plants/")
class PlantKontroller(val userRepo: UserRepository,
                      val plantRepo: PlantRepository,
                      val growRepo: GrowRepository) {
    @GetMapping("/users/{userId}")
    fun viewUserPlants(model: Model, @PathVariable("userId") userId: Long): String {
        val user = userRepo.findByIdOrNull(userId)!!
        model["plants"] = plantRepo.findByUserId(userId)
        model["header"] = "Plants for User: ${user.username}"
        return "plants/view_plants"
    }

    @GetMapping("/grows/{growId}")
    fun viewGrowPlants(model: Model, @PathVariable("growId") growId: Long): String {
        val grow = growRepo.findByIdOrNull(growId)!!
        model["plants"] = plantRepo.findByGrowId(growId)
        model["header"] = "Plants for Grow: ${grow.id}, started: ${grow.getDisplayCreateDate()}"
        return "plants/view_plants"
    }

    @GetMapping("{id}")
    fun viewPlant(model: Model, @PathVariable("id") id:Long):String{
        model["plant"] = plantRepo.findByIdOrNull(id)!!
        return "plants/view_plant"
    }
}