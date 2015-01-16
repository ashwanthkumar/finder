package finder.spec

import finder.index.IndexReader
import finder.index.IndexReader.ID_TS_SEPARATOR

trait Dataset[R] extends SerDe[R] with Serializable {
  def key(input: R): String

  /**
   * Timestamp of the record in UTC timezone
   */
  def timestamp(input: R): Long

  /**
   * Key used while indexing
   */
  def indexKey(input: R) = s"${key(input)}${ID_TS_SEPARATOR}${timestamp(input)}"
}
