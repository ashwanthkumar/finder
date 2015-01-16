package finder.index

import java.io.File
import java.util.{Map => JMap}

import finder.config.IndexConfig
import finder.index.IndexReader.prefixMatch
import finder.messages.IndexRecord
import org.iq80.leveldb.{CompressionType, Options}
import org.iq80.leveldb.impl.Iq80DBFactory._

import scala.collection.JavaConversions._

class LevelDBIndexReader(config: IndexConfig) extends IndexReader(config){
  lazy val options = new Options().createIfMissing(true).compressionType(CompressionType.NONE)
  lazy val db = factory.open(new File(config.params.string("db-location")), options)

  def findKey(prefix: String): List[IndexRecord] = {
    val iterator = db.iterator()
    options.comparator()
    iterator.seek(bytes(prefix))
    // TODO - Might have to look into this `toList` in case of OOM
    val matchingItems = iterator
      .takeWhile(prefixMatches(prefix))
      .map(_.getValue)
      .map(IndexRecord.apply)
      .toList
    iterator.close()
    matchingItems
  }

  def prefixMatches(prefix: String)(entry: JMap.Entry[Array[Byte], Array[Byte]]) = prefixMatch(bytes(prefix), entry.getKey)

  override def close(): Unit = db.close()
}

