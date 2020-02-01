package utils

import java.net.URI

import javax.inject.Inject
import play.api.{Configuration, Environment}
import play.api.inject.{ApplicationLifecycle, Module}
import scalikejdbc.async.AsyncConnectionPool

import scala.concurrent.Future
import scala.util.Try

class AsyncDB @Inject() (configuration: Configuration, lifecycle: ApplicationLifecycle) {

  val dbConfigs = configuration.get[Configuration]("db")

  dbConfigs.subKeys.foreach { dbName =>
    val dbUrl = new URI(dbConfigs.get[Configuration](dbName).get[String]("url"))
    val dbType = dbUrl.getScheme match {
      case "postgres" => "postgresql"
      case s => s
    }
    val Array(username, password) = dbUrl.getUserInfo.split(":")
    val host = dbUrl.getHost
    val port = dbUrl.getPort
    val db = dbUrl.getPath
    val jdbcConnectionString = s"jdbc:$dbType://$host:$port/$db"

    AsyncConnectionPool.add(Symbol(dbName), jdbcConnectionString, username, password)
  }

  lifecycle.addStopHook { () =>
    Future.fromTry {
      Try {
        AsyncConnectionPool.closeAll()
      }
    }
  }

}


class AsyncDBModule extends Module {
  def bindings(env: Environment, config: Configuration) = Seq(bind[AsyncDB].toSelf.eagerly)
}
