package finder.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object JSONUtil {
  val mapper = new ObjectMapper().registerModule(DefaultScalaModule)

  def readAs[T](typeClazz: Class[T])(json: String): T = mapper.readValue(json, typeClazz)

  def toJson(input: AnyRef) = mapper.writeValueAsString(input)
}
