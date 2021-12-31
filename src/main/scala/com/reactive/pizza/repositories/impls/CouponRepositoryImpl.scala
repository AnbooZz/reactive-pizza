package com.reactive.pizza.repositories.impls

import com.reactive.pizza.models.coupon.Coupon
import com.reactive.pizza.models.item.Item
import com.reactive.pizza.repositories.{ CouponRepository, ItemRepository }
import com.reactive.pizza.repositories.persistences.tables.CouponDAO
import com.reactive.pizza.repositories.persistences.{ ColumnCustomType, MySqlDBComponent }

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class CouponRepositoryImpl @Inject()(couponDAO: CouponDAO, dbComponent: MySqlDBComponent)(itemRepository: ItemRepository)(implicit val ec: ExecutionContext)
  extends CouponRepository with ColumnCustomType {

  //--------[ Properties ]----------------------
  import dbComponent.mysqlDriver.api._
  private val db = dbComponent.dbAction

  //----------[ Methods ]---------------------
  override def findById(code: Coupon.Id): Future[Option[Coupon]] = {
    for {
      couponR  <- db.run(couponDAO.coupons.filter(_.id === code).result.headOption)
      itemOptR <- couponR match {
        case Some(c) =>
          c._6 match {
            case Some(_) => Future.successful(None)
            case None    => itemRepository.findById(Item.Id(c._5))
          }
        case None    =>
          Future.successful(None)
      }
    } yield {
      couponR.map(couponDAO.apply(_, itemOptR))
    }
  }

  override def store(coupon: Coupon): Future[Unit] = db.run {
    couponDAO.coupons += couponDAO.unapply(coupon)
  }.map(_ => ())

  override def store(coupons: Seq[Coupon]): Future[Unit] = db.run {
    couponDAO.coupons ++= coupons.map(couponDAO.unapply)
  }.map(_ => ())
}
