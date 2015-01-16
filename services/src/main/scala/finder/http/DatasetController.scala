package finder.http

import com.twitter.util.Future
import finder.config.DatasetConfig
import finder.util.Utils._

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
}
