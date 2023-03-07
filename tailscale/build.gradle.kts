plugins {
  tailscale_module
}

dependencies {
  api(project(":libtailscale"))
  implementation("net.java.dev.jna:jna-platform:5.13.0")
}
