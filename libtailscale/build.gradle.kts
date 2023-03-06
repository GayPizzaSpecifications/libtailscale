plugins {
  tailscale_module
}

dependencies {
  api("net.java.dev.jna:jna:5.13.0")
  implementation(project(":externals:libtailscale-native"))
}
