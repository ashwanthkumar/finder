package finder.config

import com.typesafe.config.{ConfigException, ConfigFactory}
import finder.messages.IndexRecord
import finder.models.{UserDataset, User}
import finder.record.PailSequenceFileRecordReader
import org.apache.hadoop.io.SequenceFile
import org.scalatest.FlatSpec
import org.scalatest.Matchers.{be, convertToAnyShouldWrapper}

class FinderConfigReaderSpec extends FlatSpec {

  "FinderConfigReader" should "load default configuration from classpath" in {
    val finderConfig = FinderConfigReader.load()
    finderConfig.datasets.length should be(1)

    val userDataset = finderConfig.datasets.head
    userDataset.name should be("user")
    userDataset.impl should be("finder.models.UserDataset")
    userDataset.dataset[User].isInstanceOf[UserDataset] should be(true)

    userDataset.recordReaderImpl should be("finder.record.PailSequenceFileRecordReader")
    userDataset.recordReader[User](IndexRecord("foo", 0, 0, 0, "key")).isInstanceOf[PailSequenceFileRecordReader[User]] should be(true)

    userDataset.index.readerImpl should be("finder.index.LevelDBIndexReader")
    userDataset.index.params.string("db-location") should be("user-db")
  }

  it should "throw exception when one of the required field is not present" in {
    val errorConfig =
      """
        |finder {
        |  datasets {
        |    descriptors: [{
        |      impl: "finder.models.UserDataset"
        |      index {
        |        params {
        |          db-location: "user-db"
        |        }
        |      }
        |    }]
        |  }
        |}
        |
      """.stripMargin

    intercept[ConfigException.Missing] {
      FinderConfigReader.load(ConfigFactory.parseString(errorConfig))
    }
  }
}
