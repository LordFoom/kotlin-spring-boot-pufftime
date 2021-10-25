package improbableotter.sideprojects.pufftime.controller

import improbableotter.sideprojects.pufftime.user.UserDto
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebKontrollerTest(@Autowired val restTemplate: TestRestTemplate) {

    @BeforeAll
    fun  setup(){
        println( ">> Let's gooo!" )
    }
//    @MockkBean
//    private lateinit var userRepository: UserRepository
//    @MockkBean
//    private lateinit var growRepo:GrowRepository

    @Test
    fun `Assert we can successfully create a user`() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val user = UserDto(username = "FOOM_TEST_NAME", email = "testfoom@gmail.com")

        val requestEntity: HttpEntity<UserDto> = HttpEntity<UserDto>(user, headers)
        val responseEntity = restTemplate.postForEntity("/registration", requestEntity, UserDto::class.java)
        println(responseEntity)

    }
}