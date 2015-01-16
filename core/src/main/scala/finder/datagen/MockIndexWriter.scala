package finder.datagen

import java.io.File

import finder.messages.IndexRecord
import finder.spec.Dataset
import finder.util.JSONUtil
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.BytesWritable
import org.apache.hadoop.io.SequenceFile.Reader
import org.apache.hadoop.io.SequenceFile.Reader._
import org.iq80.leveldb.impl.Iq80DBFactory._
import org.iq80.leveldb.{CompressionType, Options}

class MockIndexWriter[R](location: String, dataset: Dataset[R]) {
  lazy val options = new Options().createIfMissing(true).compressionType(CompressionType.NONE)
  lazy val db = factory.open(new File(location), options)

  def createIndex(dataLocation: String) = {
    val reader = new Reader(new Configuration, file(new Path(dataLocation)))
    val bytesWritable = new BytesWritable()
    var recordsToSkip = 0 // TODO: Hack to make the test pass, need to fix this for good
    while (reader.next(bytesWritable)) {
      recordsToSkip = recordsToSkip + 1
      val data = dataset.deserialize(bytesWritable.getBytes)
      val index = IndexRecord(dataLocation, dataset.timestamp(data), 0, recordsToSkip, key = dataset.key(data))
      writeIndex(data, index)
    }
  }

  private def writeIndex(input: R, indexRecord: IndexRecord) = {
    db.put(bytes(dataset.indexKey(input)), bytes(JSONUtil.toJson(indexRecord)))
  }

  def close(): Unit = {
    db.close()
  }

}
