package animalsDb.Schema

import org.squeryl.{KeyedEntity, PrimitiveTypeMode, Schema, Table}
import PrimitiveTypeMode._
import animalsDb.dto.DictDTO
import animalsDb.errors.NotFound
import org.squeryl.{Schema, Table}
import cats.implicits._

case class Dict(id: Long = 0,
                value: String,
                description: Option[String]
               ) extends KeyedEntity[Long] {

  def this() = this(0, "", None)
}

object Dict {
  def apply(id: Option[Long], value: String, description: Option[String]): Dict =  new Dict(id.getOrElse(0), value, description)

  def apply(value: String, description: Option[String]): Dict = new Dict(value = value, description = description)

  implicit class DictToDTO(dict: Dict) {
    def toDTO = new DictDTO(dict.id.some, dict.value, dict.description)
  }
}

object DictSchema extends Schema{
  def tableByName(name: String): Table[Dict] = inTransaction{
      tables.find(_.name == name)
      .map(_.asInstanceOf[Table[Dict]])
      .getOrElse(throw NotFound(s"Table with name = $name"))
  }

  val dictTail = table[Dict]("dict_tail")
  val dictSize = table[Dict]("dict_size")
  val dictSpice = table[Dict]("dict_species")
  val dictSex = table[Dict]("dict_sex")
  val dictBreedDog = table[Dict]("dict_breed_dog")
  val dictBreedCat = table[Dict]("dict_breed_cat")
  val dictColor = table[Dict]("dict_color")
  val dictEar = table[Dict]("dict_ear")
  val dictDistrict = table[Dict]("dict_district")
  val dictFur = table[Dict]("dict_fur")
}
