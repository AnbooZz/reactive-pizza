package com.reactive.pizza.controllers.common

import com.reactive.pizza.utils.SecurityPolicyException
import play.api.mvc.{ AnyContent, Request, RequestHeader }
import java.util.regex.Pattern

trait XssFilter {
  private val patterns = List(
    // Avoid anything between script tags
    Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
    // Avoid anything in a src='...' type of expression
    Pattern.compile("src[\r\n]*=[\r\n]*'(.*?)'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    Pattern.compile("src[\r\n]*=[\r\n]*\"(.*?)\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    // Remove any lonesome </script> tag
    Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
    // Remove any lonesome <script ...> tag
    Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    // Avoid eval(...) expressions
    Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    // Avoid expression(...) expressions
    Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    // Avoid javascript:... expressions
    Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
    // Avoid vbscript:... expressions
    Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
    // Avoid onload= expressions
    Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
  )

  def checkBody(request: Request[AnyContent]): Request[AnyContent] = {
    request.body.asJson.foreach { textBody =>
      val isInvalid = patterns.exists(_.matcher(textBody.toString).find())
      if (isInvalid) throw new SecurityPolicyException
    }
    request.body.asText.foreach { textBody =>
      val isInvalid = patterns.exists(_.matcher(textBody).find())
      if (isInvalid) throw new SecurityPolicyException
    }
    request.body.asFormUrlEncoded.foreach {
      checkQueryMapping
    }
    request.body.asMultipartFormData.foreach { r =>
      checkQueryMapping(r.dataParts)
    }

    request
  }

  def checkRequestParam(request: RequestHeader): RequestHeader = {
    checkQueryMapping(request.queryString)
    request
  }

  private def checkQueryMapping(mapper: Map[String, Seq[String]]): Unit = {
    mapper.foreach {
      case (key, values) =>
        val mkValue = values.mkString(",")
        val isInvalid = patterns.exists { pattern =>
          pattern.matcher(key).find() || pattern.matcher(mkValue).find()
        }
        if (isInvalid) throw new SecurityPolicyException
    }
  }
}
