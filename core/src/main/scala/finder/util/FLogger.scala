package finder.util

import org.slf4j.LoggerFactory

trait FLogger { me =>
  implicit lazy val log = LoggerFactory.getLogger(me.getClass)
}
