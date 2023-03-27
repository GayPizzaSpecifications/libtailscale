allprojects {
  group = "gay.pizza.tailscale"
  version = "0.1.4-SNAPSHOT"
}

tasks.withType<Wrapper> {
  gradleVersion = "8.0.2"
}
