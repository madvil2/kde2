Миграции проводятся через ликвибейс. 

Необходимо добавить в zshrc:

export PATH=$PATH:/Applications/liquibase-4.0.0-beta1
export DB_HOST_ANIMALS_DB=
export DB_PORT_ANIMALS_DB=
export PATH_TO_DB_CHANGELOG_MASTER_ANIMALS_DB="changeLogMaster.xml"
export DB_NAME_ANIMALS_DB=
export DB_USER_ANIMALS_DB=
export DB_PASS_ANIMALS_DB=
export CLASSPATH_ANIMALS_DB="./postgresAdapter/postgresql-42.2.5.jar"

alias migrateAnimals='liquibase --classpath=$CLASSPATH_ANIMALS_DB --driver=org.postgresql.Driver --changeLogFile=$PATH_TO_DB_CHANGELOG_MASTER_ANIMALS_DB --url="jdbc:postgresql://$DB_HOST_ANIMALS_DB:$DB_PORT_ANIMALS_DB/$DB_NAME_ANIMALS_DB" --username=$DB_USER_ANIMALS_DB --password=$DB_PASS_ANIMALS_DB migrate'