package finder.util

object Utils {
  type Closable = {def close(): Unit}

  def managed[I, O](resource: I with Closable)(fn: (I) => O): Option[O] = {
    try {
      Option(fn(resource))
    } catch {
      case e: Exception => None
    } finally {
      resource.close()
    }
  }

}
