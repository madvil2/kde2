package animals.schema

import org.squeryl.{KeyedEntity, PrimitiveTypeMode, Schema, Table}
import PrimitiveTypeMode._
import animals.dto.DictElemDTO
import animals.errors.NotFound
import org.squeryl.{Schema, Table}
import cats.implicits._

case class DictElem(id: Long = 0,
                    value: String,
                    description: Option[String]
               ) extends KeyedEntity[Long] {

  def this() = this(0, "", None)
}

object DictElem {
  def apply(id: Option[Long], value: String, description: Option[String]): DictElem =  new DictElem(id.getOrElse(0), value, description)

  def apply(value: String, description: Option[String]): DictElem = new DictElem(value = value, description = description)

  implicit class DictToDTO(dict: DictElem) {
    def toDTO = new DictElemDTO(dict.id.some, dict.value, dict.description)

  }
}

object DictSchema extends Schema{
  def tableByName(name: String): Table[DictElem] = inTransaction{
      tables.find(_.name == name)
      .map(_.asInstanceOf[Table[DictElem]])
      .getOrElse(throw NotFound(s"Table with name = $name"))
  }

  val dictTail = table[DictElem]("dict_tail")
  val dictSize = table[DictElem]("dict_size")
  val dictSpice = table[DictElem]("dict_species")
  val dictSex = table[DictElem]("dict_sex")
  val dictBreedDog = table[DictElem]("dict_breed_dog")
  val dictBreedCat = table[DictElem]("dict_breed_cat")
  val dictColor = table[DictElem]("dict_color")
  val dictEar = table[DictElem]("dict_ear")
  val dictDistrict = table[DictElem]("dict_district")
  val dictFur = table[DictElem]("dict_fur")
}
