package animals.dao

import animals.schema.{DictElem, DictSchema}
import animals.dto.{DictDTO, DictElemDTO, DictListDTO}
import animals.errors.NotFound
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.{PrimitiveTypeMode, Table}
import xitrum.scope.request.Params
import animals.util.RichParams._

class DictDAO(tableName: String) extends DictCRUD {
  override val dictTable = DictSchema.tableByName(tableName)
}



object DictDAO {
  def apply(tableName: String): DictDAO = new DictDAO(tableName)
  def apply(table: Table[DictElem]): DictDAO = new DictDAO(table.name)
  def dictTableList = DictListDTO(DictSchema.tables.map(_.name).toList)
}


trait DictCRUD extends PrimitiveTypeMode {
  val dictTable: Table[DictElem]

  lazy val tableName = dictTable.name

  def addWhereCondition(dict: DictElem, params: Params): LogicalBoolean = {
    val valueFilter = params.byName("value")
    (dict.value ilike valueFilter).inhibitWhen(valueFilter.isEmpty)
  }
  def dictValues(params: Params) = {
    inTransaction(DictDTO(tableName, from(dictTable)(d => where(addWhereCondition(d, params))
      select(d)
    ).toList.map(_.toDTO)))
  }

  def dictElem(id: Long) = inTransaction{
    from(dictTable)(d => where(d.id === id) select d)
      .headOption
      .map(_.toDTO(tableName))
      .getOrElse(throw NotFound(s"elem with id = $id and tableName = $tableName"))
  }

  def insertValue(dict: DictElemDTO) = inTransaction(
    DictDTO(
      tableName,
      dictTable.insert(DictElem(dict.value.toLowerCase(), dict.description)).toDTO
    )
  )

  def updateDict(dict: DictElemDTO) = inTransaction(dictTable.update(d =>
    where(d.id === dict.id)
      set(
      d.value := dict.value.toLowerCase(),
      d.description := dict.description.orElse(d.description)
    )
  ))

  def deleteById(valueId: Long): Long = inTransaction(dictTable.deleteWhere(_.id === valueId)) match {
    case 0 => throw NotFound(s"Dict name = $tableName, id = $valueId")
    case _ => valueId
  }

}

