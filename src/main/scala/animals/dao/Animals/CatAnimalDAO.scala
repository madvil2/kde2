package animals.dao.Animals

import animals.dto.{AnimalInputDTO, CatPropertiesOutputDTO, CatPropsInputDTO}
import animals.schema.{AnimalSchema, CatProperties}
import org.squeryl.Table

class CatAnimalDAO(implicit val manifest: Manifest[AnimalInputDTO[CatPropsInputDTO]]) extends CUDAnimal[CatPropsInputDTO, CatProperties, CatPropertiesOutputDTO]{

  override val propsTable: Table[CatProperties] = AnimalSchema.catProperties

  override def propsToOutput(prop: CatProperties): CatPropertiesOutputDTO = prop.toDTO

  override def inputToAnimalProp(animalId: Long, inputProp: CatPropsInputDTO): CatProperties = inputProp.toAnimal(animalId)
}
