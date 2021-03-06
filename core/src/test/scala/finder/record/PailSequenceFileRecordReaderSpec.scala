package finder.record

import finder.index.LevelDBIndexReader
import finder.mock.FinderSpecWithMockData
import finder.models.UserDataset
import org.apache.hadoop.conf.Configuration
import org.scalatest.FlatSpec
import org.scalatest.Matchers.{be, convertToAnyShouldWrapper}

class PailSequenceFileRecordReaderSpec extends FlatSpec with FinderSpecWithMockData {
  val indexReader = new LevelDBIndexReader(finderConfig.datasets.head.index)

  "SequenceFileRecordReader" should "read the user data using the index information" in {
    val indices = indexReader.findKey("foo")
    indices.length should be > 0

    val indexRecord = indices.head
    val reader = new PailSequenceFileRecordReader(new UserDataset, indexRecord)
    val user = reader.read()
    user.timestamp should be > testTimestamp
    user.id should be >= 0
    user.name.startsWith("foo") should be(true)
  }
}
