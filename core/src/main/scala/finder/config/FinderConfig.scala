package finder.config

import finder.index.IndexReader
import finder.spec.Dataset
import org.apache.hadoop.conf.Configuration

case class FinderConfig(datasets: List[DatasetConfig]) {
  def hadoopConfig() = new Configuration()
}

case class DatasetConfig(name: String, impl: String, index: IndexConfig) {
  def dataset[R]: Dataset[R] = {
    Class.forName(impl).newInstance().asInstanceOf[Dataset[R]]
  }
}

case class IndexConfig(readerImpl: String, params: Params) {
  def reader: IndexReader = {
    Class.forName(readerImpl).getConstructor(classOf[IndexConfig]).newInstance(this).asInstanceOf[IndexReader]
  }
}

case class Params(private val map: Map[String, _]) {
  def apply(key: String) = map(key)
  def get(key: String) = map.get(key)
  def getOrElse[T](key: String, default: T) = map.getOrElse(key, default)

  def string(key: String) = map(key).asInstanceOf[String]
  def int(key: String) = map(key).asInstanceOf[Int]
  def long(key: String) = map(key).asInstanceOf[Long]

}
