package modules

import javax.inject.{Inject, Singleton}

import com.github.mauricio.async.db.pool.{PartitionedConnectionPool, PoolConfiguration}
import com.github.mauricio.async.db.postgresql.pool.PostgreSQLConnectionFactory
import com.github.mauricio.async.db.postgresql.util.URLParser
import io.getquill.{PostgresAsyncContext, SnakeCase}
import play.api.inject.{ApplicationLifecycle, Binding, Module}
import play.api.{Configuration, Environment}

import scala.concurrent.ExecutionContext

class DatabaseModule extends Module {
  def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = {
    Seq(
      bind[Database].to[DatabaseImpl]
    )
  }
}

trait Database {
  val ctx: PostgresAsyncContext[SnakeCase]
}

@Singleton
class DatabaseImpl @Inject()(lifecycle: ApplicationLifecycle, playConfig: Configuration) (implicit ec: ExecutionContext) extends Database {

  private val maybeDbUrl = playConfig.getOptional[String]("db.default.url")

  private val config = maybeDbUrl.map(URLParser.parse(_)).getOrElse(URLParser.DEFAULT)

  private val connectionFactory = new PostgreSQLConnectionFactory(config)

  private val defaultPoolConfig = PoolConfiguration.Default

  private val maxObjects = playConfig.getOptional[Int]("db.default.max-objects").getOrElse(defaultPoolConfig.maxObjects)
  private val maxIdleMillis = playConfig.getOptional[Long]("db.default.max-idle-millis").getOrElse(defaultPoolConfig.maxIdle)
  private val maxQueueSize = playConfig.getOptional[Int]("db.default.max-queue-size").getOrElse(defaultPoolConfig.maxQueueSize)
  private val validationInterval = playConfig.getOptional[Long]("db.default.max-queue-size").getOrElse(defaultPoolConfig.validationInterval)

  private val poolConfig = new PoolConfiguration(maxObjects, maxIdleMillis, maxQueueSize, validationInterval)

  private val numberOfPartitions = playConfig.getOptional[Int]("db.default.number-of-partitions").getOrElse(4)

  private val pool = new PartitionedConnectionPool(
    connectionFactory,
    poolConfig,
    numberOfPartitions,
    ec
  )

  lifecycle.addStopHook { () =>
    pool.close
  }

  val ctx = new PostgresAsyncContext(SnakeCase, pool)

}
