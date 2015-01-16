package finder.datagen

import java.io.File

import finder.models.{UserDataset, User}

import scala.io.Source

class SampleDatagenerator(output: String) {
  def generate(): Unit = {
    val testDir = new File(output)
    testDir.mkdirs()

    val dataWriter = new MockDataWriter(s"${testDir.getAbsolutePath}/data.seq", new UserDataset)
    dataWriter.write(readUsers.toIterable)
    dataWriter.close()

    val indexWriter = new MockIndexWriter(s"${testDir.getAbsolutePath}/index/", new UserDataset)
    indexWriter.createIndex(s"${testDir.getAbsolutePath}/data.seq")
    indexWriter.close()
  }

  def readUsers = {
    Source.fromInputStream(getClass.getResourceAsStream("/datagen/users.csv"))
      .getLines()
      .drop(1)
      .map(toUser)
  }

  def toUser(line: String) = {
    val parts = line.split(",")
    val id = parts(2).toInt
    User(parts(0), parts(1), id, System.currentTimeMillis() + id)
  }
}

/**
 * $ SampleDataGenerator [outputFolder=example-user-data]
 */
object SampleDatagenerator extends App {
  val output = {
    if(args.length > 1) args(0)
    else "example-user-data"
  }
  new SampleDatagenerator(output).generate()
}