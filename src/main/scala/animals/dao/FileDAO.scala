package animals.dao

import java.io.{BufferedOutputStream, FileOutputStream}

import animals.schema.{File, FileSchema}
import animals.configuration.GlobalConstants
import animals.errors.NotFound
import io.netty.handler.codec.http.multipart.FileUpload
import org.squeryl.PrimitiveTypeMode
import cats.implicits._

object FileDAO extends PrimitiveTypeMode {

  //todo делать это нормально
  private def uniquePath(filename: Option[String]) = {
    filename.map(GlobalConstants.ResDir + "/"+ _)
  }

  private def createNewFile(filename: String, path: String): Long = transaction(FileSchema.file.insert(File(filename, path = path))).id

  def filepathById(id: Option[Long]) = transaction {
    id.flatMap(FileSchema.file.lookup(_).map(_.path))
  }

  def fileById(id: Long) = inTransaction {
    FileSchema.file.lookup(id).map(_.toDTO).getOrElse(throw NotFound(s"file with id = $id"))
  }

  def createFile(file: Option[FileUpload]): Option[Long] = {
    def createAndInsert(file: FileUpload, path: String): Long = {
      val bos = new BufferedOutputStream(new FileOutputStream(path))
      try {
        Stream.continually(bos.write(file.get()))
      }
      finally {
        bos.close()
      }
      createNewFile(file.getFilename, path)
    }

    val filename = file.map(_.getFilename)

    val newFilePath = uniquePath(filename)

    (file, newFilePath) match {
      case (Some(fi), Some(path)) => createAndInsert(fi, path).some
      case _ => None
    }
  }
}
