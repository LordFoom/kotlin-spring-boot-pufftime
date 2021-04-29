package improbableotter.sideprojects.pufftime.event

import java.util.*

/**
 * We use this class to get a nice calendar view using fullcaledar.js at the time of writing
 */
data class Event(
        val id:Long,
        val start: Date,
        val title:String,

        )