package improbableotter.sideprojects.pufftime.plant

import improbableotter.sideprojects.pufftime.grow.GrowRepository
import improbableotter.sideprojects.pufftime.strain.StrainRepository
import improbableotter.sideprojects.pufftime.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PlantService(private val growRepository: GrowRepository,
                   private val strainRepository: StrainRepository,
                   private val userRepository: UserRepository,
                   private val plantRepository: PlantRepository) {

    /**
     * A grow can easily have multiple plants, so dtos contain a field expressing a number of plants they should
     * be turned into
     */
    fun createPlants(dto: PlantDto): List<Plant> {
        dto.grow = dto.grow ?: growRepository.findByIdOrNull(dto.growId)!!
        val strain = strainRepository.findByIdOrNull(dto.strainId)!!
        dto.user = dto.user ?: userRepository.findByIdOrNull(dto.userId)!!

        val numPlants = dto.numPlants?:1
        var lstPlants = ArrayList<Plant>()
        for (i in 1..numPlants) {
            lstPlants.add(plantRepository.save(Plant.fromDto(dto, strain)))
        }
        return lstPlants
    }
}