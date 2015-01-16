package finder.index

import finder.mock.FinderSpecWithMockData
import org.scalatest.FlatSpec
import org.scalatest.Matchers.{be, convertToAnyShouldWrapper, have}

class LevelDBIndexReaderSpec extends FlatSpec with FinderSpecWithMockData {
  val reader = new LevelDBIndexReader(finderConfig.datasets.head.index)

  "LevelDB" should "findKey for a given prefix" in {
    reader.findKey("foo").length should be > 0
    reader.findKey("bar").length should be > 0
    reader.findKey("baz").length should be > 0
    val indices = reader.findKey("foobar")
    val indexRecord = indices.head
    indexRecord.file should be(s"${testDir.getAbsolutePath}/data.seq")
    indexRecord.key.startsWith("foobar") should be(true)
    indexRecord.timestamp should be >= testTimestamp
    indexRecord.splitOffset should be(0)
    indexRecord.recordNumber should be >= 0
  }

  it should "return Nil when no prefix is available" in {
    reader.findKey("nonExistentPrefix") should be(Nil)
  }

  it should "return all elements when empty prefix is given" in {
    reader.findKey("") should have length 100
  }
}
