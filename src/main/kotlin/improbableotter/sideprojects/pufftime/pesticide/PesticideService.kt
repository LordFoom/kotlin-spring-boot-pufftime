package improbableotter.sideprojects.pufftime.pesticide

import org.springframework.stereotype.Service
import java.lang.IllegalStateException

@Service
class PesticideService(val pesticideRepo: PesticideRepository) {

    fun create(dto: PesticideDto):Pesticide{
        pesticideRepo.findByName(dto.name)?.let{throw IllegalStateException("Pesticide '${dto.name}' already exists")}
        return pesticideRepo.save(Pesticide.fromDto(dto))
    }
}