package finder.mock

import java.util.UUID.randomUUID

import com.google.common.io.Files
import com.typesafe.config.ConfigFactory
import finder.config.{FinderConfigReader, FinderConfig}
import finder.models.{User, UserDataset}
import org.apache.commons.io.FileUtils
import org.scalatest.{BeforeAndAfterAll, Suite}

import scala.util.Random

trait FinderSpecWithMockData extends BeforeAndAfterAll {
  this: Suite =>
  val testTimestamp = System.currentTimeMillis()
  lazy val sampleUsers = (1 to 100).map(index => User(s"${namePrefixForData(index)}${randomUUID().toString}", Random.nextInt(100), testTimestamp + index))
  val testDir = Files.createTempDir()
  val testConfig =
    s"""
      |finder {
      |  datasets {
      |    descriptors: [{
      |      name: "user"
      |      impl: "finder.models.UserDataset"
      |      index {
      |        params {
      |          db-location: "${testDir.getAbsolutePath}/index"
      |        }
      |      }
      |    }]
      |  }
      |}
      |
    """.stripMargin
  lazy val finderConfig = FinderConfigReader.load(ConfigFactory.parseString(testConfig))

  private def namePrefixForData(index: Int) = {
    val mockPrefixes = List("foo", "bar", "baz", "foobar", "buzz", "fizz", "fizzbuzz")
    mockPrefixes(index % mockPrefixes.length)
  }

  override def beforeAll() {
    val dataWriter = new MockDataWriter(s"${testDir.getAbsolutePath}/data.seq", new UserDataset)
    dataWriter.write(sampleUsers)
    dataWriter.close()

    val indexWriter = new MockIndexWriter(s"${testDir.getAbsolutePath}/index/", new UserDataset)
    indexWriter.createIndex(s"${testDir.getAbsolutePath}/data.seq")
    indexWriter.close()
  }

  override def afterAll() {
    FileUtils.deleteDirectory(testDir)
  }
}
