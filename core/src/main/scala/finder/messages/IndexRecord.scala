package finder.messages

import finder.util.JSONUtil

case class IndexRecord(file: String, timestamp: Long, splitOffset: Long, recordNumber: Int, key: String) {
  def recordsToSkip = recordNumber - 1
}

object IndexRecord {
  def apply(jsonInBytes: Array[Byte]) = {
    JSONUtil.readAs(classOf[IndexRecord])(new String(jsonInBytes))
  }
}

