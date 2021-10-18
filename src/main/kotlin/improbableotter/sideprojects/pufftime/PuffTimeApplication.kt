package improbableotter.sideprojects.pufftime

import improbableotter.sideprojects.pufftime.user.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@SpringBootApplication
class PuffTimeApplication

@EnableWebSecurity
class ApplicationSecurityConfig() : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var uds: UserDetailsServiceImpl;

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(uds)
    }

    override fun configure(http: HttpSecurity?) {
        http?.authorizeRequests()
            ?.antMatchers(
                "/registration",
                "/register",
                "/js/**",
                "/css/**",
                "/img/**",
                "/webjars/**")?.permitAll()
            ?.antMatchers("/", "/home")?.permitAll()
            ?.antMatchers("/users/**")?.hasAuthority("ROLE_ADMIN")
            ?.anyRequest()?.authenticated()
            ?.and()
            ?.formLogin()
            ?.loginPage("/login")
            ?.permitAll()
            ?.and()
            ?.logout()
            ?.invalidateHttpSession(true)
            ?.clearAuthentication(true)
            ?.logoutRequestMatcher(AntPathRequestMatcher("/logout"))
            ?.permitAll()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }
}

fun main(args: Array<String>) {
    runApplication<PuffTimeApplication>(*args)
}
