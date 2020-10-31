package animals.schema

import animals.dto.FileDTO
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.{KeyedEntity, Schema}

case class File(id: Long = 0,
                filename: String,
                path: String) extends KeyedEntity[Long] {
  def this() = this(0, "", "")
}
object File {
  def apply(filename: String, path: String): File = File(0, filename, path)
  implicit class FileToDTO(file: File) {
    def toDTO = FileDTO(file.id, file.filename, file.path)
  }

}

object FileSchema extends Schema {
  val file = table[File]("file")
}
