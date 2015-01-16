package finder.record

import finder.messages.IndexRecord
import finder.spec.Dataset
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.SequenceFile.Reader.{file, start}
import org.apache.hadoop.io.{BytesWritable, SequenceFile}

class SequenceFileRecordReader[R](dataset: Dataset[R], conf: Configuration, indexRecord: IndexRecord) extends RecordReader[R](dataset, indexRecord) {
  val reader = new SequenceFile.Reader(conf, file(new Path(indexRecord.file)), start(indexRecord.splitOffset))

  def next(): R = {
    val bytesWritable = new BytesWritable()
    reader.next(bytesWritable)
    dataset.deserialize(bytesWritable.getBytes)
  }

  override def currentOffset: Long = reader.getPosition
}