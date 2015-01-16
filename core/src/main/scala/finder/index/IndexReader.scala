package finder.index

import java.io.Closeable

import finder.config.IndexConfig
import finder.messages.IndexRecord
import org.iq80.leveldb.util.Slice
import org.iq80.leveldb.util.SliceComparator._

abstract class IndexReader(config: IndexConfig) extends Closeable {
  def findKey(prefix: String): List[IndexRecord]
}

object IndexReader {
  def prefixMatch(prefix: Array[Byte], key: Array[Byte]) = {
    val originalPrefix = new Slice(prefix)
    val actualKey = new Slice(key)
    SLICE_COMPARATOR.compare(originalPrefix, actualKey) < 0
  }
}
