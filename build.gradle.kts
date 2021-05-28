import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

var springBootVersion  = project.properties["springBootVersion"];
plugins {
    val kotlinVersion = "1.4.10"
    id("org.springframework.boot") version "2.3.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("org.jetbrains.kotlin.plugin.jpa") version "$kotlinVersion"
    kotlin("jvm") version "$kotlinVersion"
    kotlin("plugin.spring") version "$kotlinVersion"
    id("io.freefair.lombok") version "5.1.1"
}

group = "improbableotter.sideprojects"
version = "0.0.1-EARLY-DAZE"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")

    implementation ( "org.webjars:bootstrap:4.5.0" )
    implementation ( "org.webjars:webjars-locator:0.40" )
    implementation ("org.webjars:font-awesome:5.11.2")
    implementation ("org.webjars:bootstrap-datepicker:1.9.0")
    implementation ( "org.webjars:fullcalendar:5.6.0" )
    implementation ( "org.webjars.npm:lightbox2:2.11.1" )
//    implementation("net.coobird:thumbnailator:0.4.14" )
// https://mvnrepository.com/artifact/org.webjars.npm/moment
//    implementation (group= "org.webjars.npm", name= "moment", version= "2.29.1")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("mysql:mysql-connector-java")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.h2database:h2" )
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
