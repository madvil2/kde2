package animalsDb.util

import org.squeryl.Table
import org.squeryl.adapters.PostgreSqlAdapter
import org.squeryl.internals.StatementWriter

class PostgreSqlAdapterFix extends PostgreSqlAdapter {
  override def usePostgresSequenceNamingScheme: Boolean = true

  // Это чтобы cast работал правильно. Взято из DatabaseAdapter.scala (в PostgreSqlAdapter опять что-то намудрили)
  override def quoteIdentifier(s: String) = s

  override def writeInsert[T](o: T, t: Table[T], sw: StatementWriter):Unit = {

    val o_ = o.asInstanceOf[AnyRef]
    val f = getInsertableFields(t.posoMetaData.fieldsMetaData)

    sw.write("insert into ")
    sw.write(quoteName(t.prefixedName))
    sw.write(" (")
    sw.write(f.map(fmd => quoteName(fmd.columnName)).mkString(", "))
    sw.write(") values ")
    sw.write(
      f.map(fmd => writeValue(o_, fmd, sw)
      ).mkString("(",",",")"))
  }
}