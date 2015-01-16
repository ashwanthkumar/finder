package finder.index

import java.io.Closeable

import finder.config.IndexConfig
import finder.messages.IndexRecord

abstract class IndexReader(config: IndexConfig) extends Closeable {
  def findKey(prefix: String): List[IndexRecord]
}

object IndexReader {
  /**
   * Separator we use while storing the key and timestamp in the index
   */
  val ID_TS_SEPARATOR = "#_#_#"

  def prefixMatch(prefix: Array[Byte], key: Array[Byte]) = {
    new String(key).startsWith(new String(prefix))
  }
}
