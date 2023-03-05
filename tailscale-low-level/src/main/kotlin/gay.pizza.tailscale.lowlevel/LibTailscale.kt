package gay.pizza.tailscale.lowlevel

import com.sun.jna.Library
import com.sun.jna.ptr.IntByReference

interface LibTailscale : Library {
  fun tailscale_new(): TailscaleHandle
  fun tailscale_start(handle: TailscaleHandle): TailscaleError
  fun tailscale_up(handle: TailscaleHandle): TailscaleError
  fun tailscale_close(handle: TailscaleHandle): TailscaleError

  fun tailscale_set_dir(handle: TailscaleHandle, dir: String): TailscaleError
  fun tailscale_set_hostname(handle: TailscaleHandle, host: String): TailscaleError
  fun tailscale_set_authkey(handle: TailscaleHandle, authkey: String): TailscaleError
  fun tailscale_set_control_url(handle: TailscaleHandle, controlUrl: String): TailscaleError
  fun tailscale_set_ephemeral(handle: TailscaleHandle, ephemeral: Int): TailscaleError

  fun tailscale_set_logfd(handle: TailscaleHandle, fd: Int): TailscaleError

  fun tailscale_dial(handle: TailscaleHandle, network: String, addr: String, conn: TailscaleConnHandleOut): TailscaleError
  fun tailscale_listen(handle: TailscaleHandle, network: String, addr: String, listener: TailscaleListenerHandleOut): TailscaleError
  fun tailscale_listener_close(handle: TailscaleListenerHandle): TailscaleError
  fun tailscale_accept(handle: TailscaleListenerHandle, conn: TailscaleConnHandleOut): TailscaleError

  fun tailscale_errmsg(handle: TailscaleHandle, chars: CharArray, len: Int): Int
}

typealias TailscaleHandle = Int
typealias TailscaleConnHandle = Int
typealias TailscaleListenerHandle = Int
typealias TailscaleConnHandleOut = IntByReference
typealias TailscaleListenerHandleOut = IntByReference
typealias TailscaleError = Int
