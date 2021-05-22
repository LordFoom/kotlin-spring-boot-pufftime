package improbableotter.sideprojects.pufftime.event

import improbableotter.sideprojects.pufftime.grow.GrowRepository
import improbableotter.sideprojects.pufftime.water.WateringHistory
import improbableotter.sideprojects.pufftime.water.WateringHistoryRepo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class EventService(val growRepository: GrowRepository, val wateringHistoryRepo: WateringHistoryRepo) {

    /**
     * This will bring back fullcalendar events, which we intend to use on the frontend
     */
    fun findEventHistoryBetween(growId: Long, start: Date?, end: Date?): List<Event> {
        val grow = growRepository.findByIdOrNull(growId)!!
        //null check our dates to correctly return timesleces
        val historyOfWater:List<WateringHistory> =
            start?.let{
                end?.let{
                    wateringHistoryRepo.findByGrowAndWateringDateBetween(grow, start, end)
                }?:wateringHistoryRepo.findAllByGrowAndWateringDateGreaterThan(grow, start)
            }?:end?.let{
                wateringHistoryRepo.findByGrowAndWateringDateLessThan(grow, end)
               }?:wateringHistoryRepo.findByGrowOrderByWateringDateDesc(grow)


         val events =historyOfWater.map {
            Event(id = ""+it.id!!, start = it.getAppropriateDate(), title = it.title())
        }

        val allEvents = mutableListOf<Event>()
        allEvents.addAll(events)
        //add the date grow started
        allEvents.add(Event(id="start"+grow.id, start = grow.getDisplayStartDate(), title = "Switched to Flower",  ))
        //add the date grow flipped to flower
        events + Event(id="flower"+grow.id, start = grow.getDisplayFloweringDate(), title = "Switched to Flower",  )
        //add the date we harvested
        grow.harvestDate?.let {
            events + Event(id = "harvest" + grow.id, start = grow.getDisplayHarvestDate(), title = "Harvested",)
        }


        //TODO add pesticeds
        //TODO add pests

        return events
    }

    fun findGrowWateringHistory(growId: Long): List<Event> {
       return findEventHistoryBetween(growId, null, null)
    }
}