package finder.index

import org.iq80.leveldb.impl.Iq80DBFactory._
import org.scalatest.FlatSpec
import org.scalatest.Matchers.{be, convertToAnyShouldWrapper}

class IndexReaderSpec extends FlatSpec {
  "IndexReader" should "do prefix match properly" in {
    IndexReader.prefixMatch(bytes("prefix"), bytes("prefixBasedKey")) should be(true)
    IndexReader.prefixMatch(bytes("prefix"), bytes("keyBasedPrefix")) should be(false)
  }
}
