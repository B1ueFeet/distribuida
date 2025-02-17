plugins {
    id("java")
    id("io.freefair.lombok") version "8.11"
    id("org.springframework.boot") version "3.4.0"
}

group = "com.programacion.distribuida"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {

//    REST
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.0")
    //Discovery
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery:4.2.0")
    //Actuador
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.4.0")
    //JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.0")
    implementation("org.postgresql:postgresql:42.7.4")

}
