package finder.models

import finder.spec.Dataset
import finder.util.JSONUtil

case class User(name: String, age: Int, timestamp: Long)

class UserDataset extends Dataset[User] {
  override def key(input: User): String = input.name
  /**
   * Timestamp of the record in UTC timezone
   */
  override def timestamp(input: User): Long = input.timestamp
  override def serialize(input: User): Array[Byte] = JSONUtil.toJson(input).getBytes
  override def deserialize(input: Array[Byte]): User = JSONUtil.readAs(classOf[User])(new String(input))
}
