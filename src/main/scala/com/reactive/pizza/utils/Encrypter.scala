package com.reactive.pizza.utils

import java.security.{ MessageDigest, SecureRandom }
import java.util.UUID

object Encrypter {
  private lazy val SOLT = "124edaa9-e888-480f-a442-9d101c931a0e"

  /**
   * @return encrypted string has length is 32
   */
  def generateId: String = {
    val prng      = SecureRandom.getInstance("SHA1PRNG")
    val randomNum = Integer.valueOf(prng.nextInt).toString
    val salt      = MessageDigest.getInstance("MD5")

    salt.update(UUID.randomUUID.toString.getBytes("UTF-8"))
    salt.digest(randomNum.getBytes()).map("%02x".format(_)).mkString
  }

  /**
   * @return encrypted string has length is 32
   */
  def encrypt(value: String): String = {
    val salt = MessageDigest.getInstance("MD5")
    salt.update(SOLT.getBytes())
    salt.digest(value.getBytes).map("%02x".format(_)).mkString
  }
}
