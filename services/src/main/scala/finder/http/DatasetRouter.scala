package finder.http

import com.twitter.finatra.Controller
import finder.config.FinderConfigReader


class DatasetRouter extends Controller {
  val config = FinderConfigReader.load()
  get("/:dataset/search") { request =>
    val dataset = request.routeParams("dataset")
    val key = request.params("key")
    new DatasetController(config.dataset(dataset)).search(key).map(render.json)
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
