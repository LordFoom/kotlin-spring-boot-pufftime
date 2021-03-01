package improbableotter.sideprojects.pufftime.nute

import improbableotter.sideprojects.pufftime.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class NuteService(
    val nuteRepo: NuteRepository,
    val userRepo: UserRepository
) {

    fun create(dto: NuteDto): Nute {
        dto.user = userRepo.findByIdOrNull(dto.userId)!!
        return nuteRepo.save(Nute.fromDto(dto))
    }
}