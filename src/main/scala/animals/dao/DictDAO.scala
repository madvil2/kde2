package animals.dao

import animals.Schema.{Dict, DictSchema}
import animals.dto.{DictDTO, DictListDTO}
import org.squeryl.{PrimitiveTypeMode, Table}
import xitrum.scope.request.Params

class DictDAO(tableName: String) extends DictCRUD {
  override val dictTable = DictSchema.tableByName(tableName)
}

object DictDAO {
  def apply(tableName: String): DictDAO = new DictDAO(tableName)

  def dictTableList = DictListDTO(DictSchema.tables.map(_.name).toList)
}


trait DictCRUD extends PrimitiveTypeMode {
  val dictTable: Table[Dict]

  def dictValues = inTransaction(from(dictTable)(d => select(d)).toList.map(_.toDTO))

  def insertValue(dict: DictDTO) = inTransaction(dictTable.insert(Dict(dict.value, dict.description)).toDTO)

  def updateDict(dict: DictDTO) = inTransaction(dictTable.update(d =>
    where(d.id === dict.id)
      set(
      d.value := dict.value,
      d.description := dict.description.orElse(d.description)
    )
  ))

  def deleteById(id: Long) = inTransaction(dictTable.deleteWhere(_.id === id))

}

