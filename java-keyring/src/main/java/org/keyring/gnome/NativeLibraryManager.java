/*
 * Copyright © 2017, Rex Hoffman
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.keyring.gnome;

import org.keyring.BackendNotSupportedException;

import com.sun.jna.Native;

/**
 * Global native library manager.
 */
class NativeLibraryManager {

  @SuppressWarnings("deprecation")
  public static synchronized void loadNativeLibraries() throws BackendNotSupportedException {
    if (glib2 != null && gklib != null) {
      return;
    }

    try {
      glib2 = (GLIB2) Native.loadLibrary("glib-2.0", GLIB2.class);
      gklib = (GKLib) Native.loadLibrary("gnome-keyring", GKLib.class);
    } catch (UnsatisfiedLinkError ex) {
      throw new BackendNotSupportedException("Failed to load native library");
    }
  }

  /**
   * An instance of CoreFoundationLibrary.
   */
  public static GLIB2 glib2 = null;

  /**
   * An instance of SecurityLibrary.
   */
  public static GKLib gklib = null;

} // class NativeLibraryManager