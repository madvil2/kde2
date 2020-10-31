package animals.dao.Animals
import animals.dto.{AnimalInputDTO, CatPropsInputDTO, DogPropertiesOutputDTO, DogPropsInputDTO}
import animals.schema.{Animal, AnimalProperties, AnimalSchema, CatProperties, DogProperties}
import org.squeryl.Table


class DogAnimalDAO(implicit val manifest: Manifest[AnimalInputDTO[DogPropsInputDTO]]) extends CUDAnimal[DogPropsInputDTO, DogProperties, DogPropertiesOutputDTO] {

  override val propsTable: Table[DogProperties] = AnimalSchema.dogProperties

  override def propsToOutput(prop: DogProperties): DogPropertiesOutputDTO = prop.toDTO

  override def inputToAnimalProp(animalId: Long, inputProp: DogPropsInputDTO): DogProperties = inputProp.toAnimal(animalId)
}


