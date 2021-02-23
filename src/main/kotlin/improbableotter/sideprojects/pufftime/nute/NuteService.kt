package improbableotter.sideprojects.pufftime.nute

import improbableotter.sideprojects.pufftime.user.UserRepository
import org.springframework.data.repository.findByIdOrNull

class NuteService(
    val nuteRepo: NuteRepository,
    val userRepo: UserRepository
) {

    fun create(dto: NuteDto): Nute {
        dto.user = userRepo.findByIdOrNull(dto.id)
        return nuteRepo.save(Nute.fromDto(dto))
    }
}