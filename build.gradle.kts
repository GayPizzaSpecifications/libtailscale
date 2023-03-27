allprojects {
  group = "gay.pizza.tailscale"
  version = "0.1.3-SNAPSHOT"
}

tasks.withType<Wrapper> {
  gradleVersion = "8.0.2"
}
