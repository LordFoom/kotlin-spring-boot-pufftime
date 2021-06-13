package improbableotter.sideprojects.pufftime.controller

import improbableotter.sideprojects.pufftime.event.Event
import improbableotter.sideprojects.pufftime.event.EventService
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import java.util.*

@RestController
@RequestMapping("/events")
class EventKontroller(val eventService: EventService) {

    @GetMapping("/{growId}")
    fun getEvents(@PathVariable growId: Long,
    @RequestParam(required = false) start:String?,
    @RequestParam(required = false) end:String?): List<Event>
    {
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
//        val startDate: Date = start?.let { dateFormat.parse(it) } ?: Date()
//        val endDate: Date = end?.let { dateFormat.parse(it) } ?: Date()
        return eventService.findGrowWateringHistory(growId)

    }
}