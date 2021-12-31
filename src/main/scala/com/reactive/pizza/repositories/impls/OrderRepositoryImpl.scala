package com.reactive.pizza.repositories.impls

import com.reactive.pizza.models.order.Order
import com.reactive.pizza.repositories.OrderRepository
import com.reactive.pizza.repositories.persistences.tables.OrderDAO
import com.reactive.pizza.repositories.persistences.{ ColumnCustomType, MySqlDBComponent }
import com.reactive.pizza.utils.RollbackException

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class OrderRepositoryImpl @Inject()(orderDAO: OrderDAO, dbComponent: MySqlDBComponent)(implicit val ec: ExecutionContext)
  extends OrderRepository with ColumnCustomType {

  //--------------[ Properties ]---------------------------
  import dbComponent.mysqlDriver.api._
  private val db = dbComponent.dbAction

  //----------[ Methods ]---------------------
  override def store(order: Order): Future[Unit] = db.run {
    (for {
      _ <- orderDAO.orders    += orderDAO.unapply(order)
      _ <- orderDAO.customers += orderDAO.unapply(order.customerInfo, order.id)
    } yield ()).flatMap { _ =>
      DBIO.failed(new RollbackException("Rollback for store order"))
    }.transactionally
  }
}
