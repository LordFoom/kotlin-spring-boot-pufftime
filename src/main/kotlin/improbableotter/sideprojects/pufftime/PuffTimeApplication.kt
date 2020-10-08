package improbableotter.sideprojects.pufftime

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@SpringBootApplication
class PuffTimeApplication

@EnableWebSecurity
class ApplicationSecurityConfig : WebSecurityConfigurerAdapter(){


    @Autowired

    override fun configure(auth: AuthenticationManagerBuilder?) {
        super.configure(auth)
    }
}
fun main(args: Array<String>) {
    runApplication<PuffTimeApplication>(*args)
}
