package animals.dto

import animals.schema.DictElem

case class DictElemDTO(id: Option[Long],
                       value: String,
                       description: Option[String],
                       tableName: Option[String] = None)

object DictElemDTO {
  def apply(id: Option[Long], value: String, description: Option[String]): DictElemDTO = new DictElemDTO(id, value, description)

  implicit class DTOtoDictElem(dict: DictElemDTO) {
    def toDictElem = new DictElem(dict.id.getOrElse(0), dict.value, dict.description)
  }
}

case class DictDTO(dictName: String,
                   dictElems: List[DictElemDTO])
object DictDTO {
  def apply(dictName: String, dictElem: DictElemDTO): DictDTO = new DictDTO(dictName, dictElem :: Nil)
}

case class DictListDTO(dicts: List[String])


