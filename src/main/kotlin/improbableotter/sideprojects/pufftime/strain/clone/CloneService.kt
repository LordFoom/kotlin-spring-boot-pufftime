package improbableotter.sideprojects.pufftime.strain.clone

import improbableotter.sideprojects.pufftime.strain.Strain
import java.util.*

class CloneService(val cloneRepository: CloneRepository) {

    fun createCuttings(strain: Strain, cloneDate: Date, numCuttings: Long) {
        for(i in 1..numCuttings ){
           cloneRepository.save(Clone(cloneDate = cloneDate, strain = strain))
        }
    }
}