package animals.schema

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.{KeyedEntity, Schema}

case class File(id: Long = 0,
                filename: String,
                path: String) extends KeyedEntity[Long] {
  def this() = this(0, "", "")
}
object File {
  def apply(filename: String, path: String): File = File(0, filename, path)
}

object FilesSchema extends Schema {
  val file = table[File]("file")
}
