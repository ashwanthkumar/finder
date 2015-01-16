package finder.spec

trait Dataset[R] extends SerDe[R] with Serializable {
  def key(input: R): String

  /**
   * Timestamp of the record in UTC timezone
   */
  def timestamp(input: R): Long

  /**
   * Key used while indexing
   */
  def indexKey(input: R) = s"${key(input)}#_#_#${timestamp(input)}"
}
