package finder.config

import finder.index.IndexReader
import finder.messages.IndexRecord
import finder.record.RecordReader
import finder.spec.Dataset

case class FinderConfig(datasets: List[DatasetConfig])

case class DatasetConfig(name: String, impl: String, recordReaderImpl: String, index: IndexConfig) {
  def dataset[R]: Dataset[R] = {
    Class.forName(impl).newInstance().asInstanceOf[Dataset[R]]
  }

  def recordReader[R](indexRecord: IndexRecord): RecordReader[R] = {
    Class.forName(recordReaderImpl).getConstructor(classOf[Dataset[R]], classOf[IndexRecord]).newInstance(dataset, indexRecord).asInstanceOf[RecordReader[R]]
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
