package finder.spec

trait SerDe[R] {
  def serialize(input: R): Array[Byte]
  def deserialize(input: Array[Byte]): R
}
