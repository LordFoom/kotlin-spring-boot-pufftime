package improbableotter.sideprojects.pufftime.lights

import improbableotter.sideprojects.pufftime.user.UserRepository
import org.springframework.stereotype.Service

@Service
class LightService (val lightRepo: LightRepository,
                    val userRepo: UserRepository){

    fun create(dto: LightDto):Light{
        dto.user = dto.user?:userRepo.findByUsername(dto.username!!)!!
        return lightRepo.save(Light.fromDto(dto))

    }
}