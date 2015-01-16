package finder.config

import java.net.InetAddress
import java.util.{Map => JMap}

import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConversions._

class FinderConfigReader {
  val defaultConfig = {
    def hostName = InetAddress.getLocalHost.getHostName
    ConfigFactory.load
      .withFallback(ConfigFactory.parseResources(s"my-config/$hostName.application.conf"))
  }

  def load(): FinderConfig = {
    load(defaultConfig)
  }

  def load(config: Config): FinderConfig = {
    buildFinderConfig(config.withFallback(defaultConfig).getConfig("finder"))
  }

  def buildFinderConfig(config: Config) = {
    FinderConfig(datasets(config.getConfig("datasets")))
  }

  def datasets(config: Config) = {
    val defaultDatasetConfig = config.getConfig("default")
    config.getConfigList("descriptors").map(descriptor(defaultDatasetConfig)).toList
  }

  def descriptor(defaultConfig: Config)(config: Config) = {
    val descriptorConfig = config.withFallback(defaultConfig)
    DatasetConfig(
      descriptorConfig.getString("name"),
      descriptorConfig.getString("impl"),
      index(descriptorConfig.getConfig("index"))
    )
  }

  def index(config: Config) = {
    val indexReaderImplementation = config.getString("reader")
    val params = Params(config.getValue("params").unwrapped().asInstanceOf[JMap[String, _]].toMap)
    IndexConfig(indexReaderImplementation, params)
  }
}

object FinderConfigReader {
  def load() = new FinderConfigReader().load()

  def load(config: Config) = new FinderConfigReader().load(config)
}