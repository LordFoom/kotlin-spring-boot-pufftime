package improbableotter.sideprojects.pufftime.plant

import improbableotter.sideprojects.pufftime.grow.GrowRepository
import improbableotter.sideprojects.pufftime.strain.StrainRepository
import improbableotter.sideprojects.pufftime.user.UserRepository
import org.springframework.data.repository.findByIdOrNull

class PlantService(private val growRepository: GrowRepository,
                   private val strainRepository: StrainRepository,
                   private val userRepository: UserRepository,
                   private val plantRepository: PlantRepository) {

    fun createPlant(dto: PlantDto): Plant {
        dto.grow = dto.grow ?: growRepository.findByIdOrNull(dto.growId)!!
        dto.strain = dto.strain ?: strainRepository.findByIdOrNull(dto.strainId)!!
        dto.user = dto.user ?: userRepository.findByIdOrNull(dto.userId)!!

        return plantRepository.save(Plant.fromDto(dto))
    }
}