package finder.datagen

import finder.spec.Dataset
import finder.util.FLogger
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.SequenceFile.Writer._
import org.apache.hadoop.io.{BytesWritable, NullWritable, SequenceFile}

class MockDataWriter[R](location: String, dataset: Dataset[R]) extends FLogger {
  lazy val writer = SequenceFile.createWriter(new Configuration(), file(new Path(location)), keyClass(classOf[BytesWritable]), valueClass(classOf[NullWritable]))

  def write(input: R): Unit = {
    writer.append(new BytesWritable(dataset.serialize(input)), NullWritable.get())
  }

  def write(inputs: Iterable[R]): Unit = {
    log.info(s"Writing mock data to $location")
    inputs.foreach(write)
  }

  def close(): Unit = {
    log.info(s"Closing mock data writer $location")
    writer.close()
  }
}
