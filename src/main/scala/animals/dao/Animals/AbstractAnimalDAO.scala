package animals.dao.Animals

import animals.dao.Animals.AnimalSpecies.{Cat, Dog}
import animals.dao.{AccountDAO, DictDAO, FileDAO}
import animals.dto.{AnimalInputDTO, AnimalOutputDTO, AnimalPropertiesOutputDTO, AnimalPropsInput, CatPropertiesOutputDTO, DogPropertiesOutputDTO}
import animals.errors.NotFound
import animals.schema.{Account, Animal, AnimalProperties, AnimalSchema, CatProperties, DictSchema, DogProperties}
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.{PrimitiveTypeMode, Table}
import xitrum.scope.request.Params
import animals.util.RichParams._
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JValue

sealed trait AnimalSpecies {
  val speciesName: String
}

object AnimalSpecies {

  case object Dog extends AnimalSpecies {
    override val speciesName: String = "dog"
  }

  case object Cat extends AnimalSpecies {
    override val speciesName: String = "cat"
  }

  def apply(speciesName: String) = speciesName match {
    case Dog.speciesName => Dog
    case Cat.speciesName => Cat
  }
}

trait ReadAnimal[P <: AnimalProperties, O <: AnimalPropertiesOutputDTO] extends PrimitiveTypeMode {
  implicit val formats = DefaultFormats

  val propsTable: Table[P]


  def propsToOutput(prop: P): O

  def propsById(id: Long) = inTransaction {
    from(propsTable)(prop => where(prop.animal_id === id)
      select (prop)
    ).headOption.getOrElse(throw NotFound(s"No props for animal with id $id"))
  }

  def animalsToOutput(animal: Animal, prop: P): AnimalOutputDTO[O] = inTransaction {
    import animal._
    AnimalOutputDTO[O](
      id,
      animal.name,
      age,
      weight,
      comment,
      DictDAO(DictSchema.dictSpecies).dictElem(species_id),
      DictDAO(DictSchema.dictSex).dictElem(sex_id),
      DictDAO(DictSchema.dictColor).dictElem(color_id),
      DictDAO(DictSchema.dictFur).dictElem(fur_id),
      DictDAO(DictSchema.dictEar).dictElem(ear_id),
      DictDAO(DictSchema.dictTail).dictElem(tail_id),
      DictDAO(DictSchema.dictSize).dictElem(size_id),
      AccountDAO.accountById(creator_id),
      card_number,
      status,
      file_id.map(FileDAO.fileById),
      properties = propsToOutput(prop)
    )
  }

  def baseAnimalConditions(animal: Animal, params: Params): LogicalBoolean = {
    animal.age isNotNull //TODO заглушка
  }

  def orderField(animal: Animal, params: Params) = {
    val orderField = params.byName("orderField") match {
      case Some("id") => animal.id.~
      case Some("age") => animal.age.~
      case _ => animal.name.~
    }
    params.byName("orderType").map(_.toLowerCase()) match {
      case Some("desc") => orderField desc
      case _ => orderField asc
    }
  }

  def animals(offset: Int, limit: Int, params: Params): List[AnimalOutputDTO[O]] = inTransaction {
    from(AnimalSchema.animals)(animal =>
      where(baseAnimalConditions(animal, params))
        select (animal)
        orderBy (orderField(animal, params))
    ).page(offset, limit).toList
      .map(animal => (animal -> propsById(animal.id)))
      .map {
        case (animal, prop) => animalsToOutput(animal, prop)
      }
  }

}


trait CUDAnimal[I <: AnimalPropsInput, P <: AnimalProperties, O <: AnimalPropertiesOutputDTO] extends ReadAnimal[P, O] {
  implicit val manifest: Manifest[AnimalInputDTO[I]]

  def inputToAnimalProp(animalId: Long, inputProp: I): P

  def createAnimal(animalInput: AnimalInputDTO[I]): AnimalOutputDTO[O] = inTransaction {
    val animal: Animal = AnimalSchema.animals.insert(animalInput.toAnimal)
    val props: P = propsTable.insert(inputToAnimalProp(animal.id, animalInput.properties))
    animalsToOutput(animal, props)
  }

  def createAnimal(jvalue: JValue): AnimalOutputDTO[O] = {
    createAnimal(jvalue.extract[AnimalInputDTO[I]])
  }

}


object AnimalsDAO {
  def apply(speciesName: String) = AnimalSpecies(speciesName) match {
    case Dog => new DogAnimalDAO
    case Cat => new CatAnimalDAO
  }


}

