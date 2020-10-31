package animalsDb

import animalsDb.configuration.DBConfiguration
import animalsDb.util.PostgreSqlAdapterFix
import org.squeryl.{Session, SessionFactory}
import xitrum.Server

object Boot {
  def main(args: Array[String]) {
    Server.start()

    Class.forName("org.postgresql.Driver");

    SessionFactory.concreteFactory = Some(() =>
      Session.create(
        java.sql.DriverManager.getConnection(DBConfiguration.DbURL, DBConfiguration.DBLogin, DBConfiguration.DBpassword),
    new PostgreSqlAdapterFix))



    Server.stopAtShutdown()
  }
}
