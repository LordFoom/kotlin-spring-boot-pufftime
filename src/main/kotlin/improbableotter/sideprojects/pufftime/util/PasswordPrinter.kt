package improbableotter.sideprojects.pufftime.util

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

//@JvmStatic
fun main(args: Array<String>) {
    val passwordEncoder = BCryptPasswordEncoder()
    println("Keep it subtle. Keep it safe.")
    println(passwordEncoder.encode("crazyhippy420blazeit"))
}