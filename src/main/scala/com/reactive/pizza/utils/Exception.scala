package com.reactive.pizza.utils

//------------[ Core ]-----------------
abstract class BaseException(message: String) extends Exception {
  override def getMessage: String = this.message
}
//-----------[ Commons ]----------------
class UnSupportedTypeException(msg: String) extends BaseException(msg)
//-----------[ DB Exceptions ]--------------
abstract class DBException(msg: String)    extends BaseException(msg)
class DBMappingException(msg: String)      extends DBException(msg)
class EntityNotFoundException(msg: String) extends DBException(msg)
class RollbackException(msg: String)       extends DBException(msg)
//-----------[ API Exceptions ]--------------------------------
abstract class APIException(msg: String) extends BaseException(msg)
class UnAuthorizedException              extends APIException("")
class SecurityPolicyException            extends APIException("") {
  override def getMessage = "Your request is illegal"
}
class PasswordNotMatchException extends APIException("") {
  override def getMessage = "Password and RePassword don't match"
}
class ExistedUsernameException(username: String) extends APIException("") {
  override def getMessage = s"Username: $username is existed. Please use another"
}

