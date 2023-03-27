plugins {
  tailscale_base
}

dependencies {
  implementation(project(":tailscale"))
  implementation(project(":tailscale-channel"))
}
