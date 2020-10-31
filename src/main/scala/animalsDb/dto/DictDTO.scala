package animalsDb.dto

import animalsDb.Schema.Dict

case class DictDTO(id: Option[Long],
                   value: String,
                   description: Option[String],
                   tableName: Option[String] = None)

object DictDTO {
  def apply(id: Option[Long], value: String, description: Option[String]): DictDTO = new DictDTO(id, value, description)

  implicit class DictDTOtoDict(dict: DictDTO) {
    def toDict = new Dict(dict.id.getOrElse(0), dict.value, dict.description)
  }
}

case class DictListDTO(dicts: List[String])

