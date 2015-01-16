package finder.record

import finder.messages.IndexRecord
import finder.spec.Dataset
import finder.util.FLogger

/*
  TODO - Add caching support for a given record
 */
abstract class RecordReader[R](dataset: Dataset[R], indexRecord: IndexRecord) extends FLogger {

  def read() = {
    log.info(s"Opening file for reading ${indexRecord.file}")
    var counter = 0
    (1 to indexRecord.recordsToSkip).foreach(i => {
      counter = counter + 1
      if (i > 0 && i % 100 == 0) log.info(s"Skipped $i records so far")
      next()
    })
    log.info(s"Skipped ${indexRecord.recordsToSkip} vs $counter records in total")
    next()
  }

  protected def next(): R

  def currentOffset: Long
}
