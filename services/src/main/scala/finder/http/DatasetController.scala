package finder.http

import com.twitter.util.Future
import finder.config.DatasetConfig
import finder.index.IndexReader._
import finder.util.Utils._

// TODO - Cache the reader instances
class DatasetController(config: DatasetConfig) {
  def search(key: String): Future[List[_]] = {
    managed(config.index.reader) { indexReader =>
      Future(indexReader
        .findKey(key)
        .map { record =>
        managed(config.recordReader[Any](record))(_.read())
      })
    }
  }

  def get(id: String, ts: Long): Future[Option[_]] = {
    search(s"$id$ID_TS_SEPARATOR$ts").map(_.headOption).map ({
      case Some(datum) if config.dataset.timestamp(datum) == ts => Some(datum)
      case _ => None
    })
  }
}
