package com.reactive.pizza.repositories.impls

import com.reactive.pizza.models.coupon.Coupon
import com.reactive.pizza.models.item.Item
import com.reactive.pizza.repositories.{ CouponRepository, ItemRepository }
import com.reactive.pizza.repositories.persistences.tables.CouponDAO
import com.reactive.pizza.repositories.persistences.{ ColumnCustomType, MySqlDBComponent }
import play.api.libs.json.Json

import javax.inject._
import scala.concurrent.Future

@Singleton
class CouponRepositoryImpl @Inject()(couponDAO: CouponDAO, dbComponent: MySqlDBComponent)(itemRepository: ItemRepository)
  extends CouponRepository with ColumnCustomType {

  //--------[ Properties ]----------------------
  import dbComponent.mysqlDriver.api._
  private val db          = dbComponent.dbAction
  private implicit val ec = dbComponent.dbEC

  //----------[ Methods ]---------------------
  override def countAll(): Future[Int] = db.run {
    couponDAO.coupons.map(_.id).size.result
  }

  override def findById(code: Coupon.Id): Future[Option[Coupon]] = {
    for {
      couponR  <- db.run(couponDAO.coupons.filter(_.id === code).result.headOption)
      items    <- couponR match {
        case Some(c) =>
          c._6 match {
            case Some(_) =>
              Future.successful(Nil)
            case None    =>
              itemRepository.filterByIds {
                Json.parse(c._5).as[Seq[String]].map(Item.Id)
              }
          }
        case None    =>
          Future.successful(Nil)
      }
    } yield {
      couponR.map(couponDAO.apply(_, items))
    }
  }

  override def store(coupon: Coupon): Future[Unit] = db.run {
    couponDAO.coupons += couponDAO.unapply(coupon)
  }.map(_ => ())

  override def store(coupons: Seq[Coupon]): Future[Unit] = db.run {
    couponDAO.coupons ++= coupons.map(couponDAO.unapply)
  }.map(_ => ())
}
