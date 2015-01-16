package finder.http

import com.twitter.finatra.FinatraServer

object FinderService extends FinatraServer {
  register(new DatasetRouter)
}
