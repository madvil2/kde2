package animalsDb.configuration

object DBConfiguration {
  val DbURL = sys.env.getOrElse("JDBC_URL", "jdbc:postgresql://localhost:5432/animals")
  val DBLogin = sys.env.getOrElse("JDBC_LOGIN", "postgres")
  val DBpassword = sys.env.getOrElse("JDBC_PASSWORD", "")
  val driverClassName = sys.env.getOrElse("JDBC_DRIVER_CLASS_NAME", "org.postgresql.Driver")
}
