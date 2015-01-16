package finder.util

object Utils {
  type Closable = {def close(): Unit}

  def managed[I, O](resource: I with Closable)(fn: (I) => O) = {
    try {
      fn(resource)
    } finally {
      resource.close()
    }
  }

}
