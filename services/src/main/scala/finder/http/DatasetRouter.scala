package finder.http

import com.twitter.finatra.{Request, Controller}
import finder.config.FinderConfigReader
import finder.index.IndexReader
import finder.index.IndexReader.ID_TS_SEPARATOR


class DatasetRouter extends Controller {
  val config = FinderConfigReader.load()
  get("/:dataset/search/:key") { request =>
    val dataset = request.routeParams("dataset")
    val identifier = request.routeParams("key")
    new DatasetController(config.dataset(dataset)).search(identifier).map(render.json)
  }

  get("/:dataset/get/:id/:timestamp") { request =>
    val dataset = request.routeParams("dataset")
    val identifier = request.routeParams("id")
    val timestamp = request.routeParams("timestamp").toLong

    new DatasetController(config.dataset(dataset)).get(identifier, timestamp).map(render.json)
  }

  error { request =>
    request.error match {
      case Some(e: Exception) =>
        log.error(e, e.getMessage)
        render.status(500).plain(e.getMessage).toFuture
      case _ =>
        render.status(500).plain("Something went wrong!").toFuture
    }
  }


}
