package improbableotter.sideprojects.pufftime.lights

import improbableotter.sideprojects.pufftime.grow.GrowRepository
import improbableotter.sideprojects.pufftime.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class GrowLightService (
    private val growRepository: GrowRepository,
    private val growLightRepository: GrowLightRepository,
    private val lightRepository: LightRepository,
){
    fun create(dto:GrowLightDto):GrowLight{
        val light = lightRepository.findByIdOrNull(dto.lightId)!!
        val grow = growRepository.findByIdOrNull(dto.growId)!!
        val growLight  = GrowLight( light=light, grow=grow, createDate = dto.createDate, lastUpdate = dto.createDate)

        return growLightRepository.save(growLight)
    }
}
