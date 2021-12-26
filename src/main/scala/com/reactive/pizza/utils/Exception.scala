package com.reactive.pizza.utils

//------------[ Core ]-----------------
abstract class BaseException(message: String) extends Exception {
  override def getMessage: String = this.message
}
abstract class DBException(message: String) extends BaseException(message)
//-----------[ Commons ]----------------
class UnSupportedTypeException(msg: String) extends BaseException(msg)
//-----------[ DB Exceptions ]--------------
class DBMappingException(msg: String)      extends DBException(msg)
class EntityNotFoundException(msg: String) extends DBException(msg)

