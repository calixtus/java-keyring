/*
 * Copyright © 2019, Java Keyring
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
package com.github.javakeyring.kde;

import com.github.javakeyring.BackendNotSupportedException;
import com.github.javakeyring.Keyring;
import com.github.javakeyring.KeyringStorageType;
import com.github.javakeyring.PasswordAccessException;
import com.github.javakeyring.internal.KeyringBackend;
import com.github.javakeyring.internal.freedesktop.FreedesktopKeyringBackend;
import com.github.javakeyring.internal.kde.KWalletBackend;
import com.sun.jna.Platform;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;

public class KWalletBackendTest {

  private static final String SERVICE = "com.github.javakeyring.kde.test";

  private static final String ACCOUNT = "username_kde";

  private static final String PASSWORD = "password_kde";

  /**
   * Test of setup method, of class KWalletKeyringBackend.
   */
  @Test
  public void testSetup() throws Exception {
    assumeTrue(Platform.isLinux());
    assumeTrue(catchThrowable(FreedesktopKeyringBackend::new) instanceof BackendNotSupportedException);
    assertThat(catchThrowable(KWalletBackend::new)).as("Setup should succeed").doesNotThrowAnyException();
  }

  /**
   * Test of getPassword method, of class KWalletKeyringBackend.
   */
  @Test
  public void testPasswordFlow() throws Exception {
    assumeTrue(Platform.isLinux() && Keyring.create().getKeyringStorageType() == KeyringStorageType.KWALLET);
    KeyringBackend backend = new KWalletBackend();
    catchThrowable(() -> backend.deletePassword(SERVICE, ACCOUNT));
    checkExistenceOfPasswordEntry(backend);
    backend.setPassword(SERVICE, ACCOUNT, PASSWORD);
    assertThat(backend.getPassword(SERVICE, ACCOUNT)).isEqualTo(PASSWORD);
    backend.deletePassword(SERVICE, ACCOUNT);
    assertThatThrownBy(() -> backend.getPassword(SERVICE, ACCOUNT)).isInstanceOf(PasswordAccessException.class);
  }

  private static void checkExistenceOfPasswordEntry(KeyringBackend backend) {
    assertThatThrownBy(() -> backend.getPassword(SERVICE, ACCOUNT))
            .as("Please remove password entry '%s' " + "by using Keychain Access before running the tests", SERVICE)
            .isNotNull();
  }
}
