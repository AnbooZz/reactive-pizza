package com.reactive.pizza.repositories

import com.google.inject.ImplementedBy
import com.reactive.pizza.models.coupon.Coupon
import com.reactive.pizza.repositories.impls.CouponRepositoryImpl

import scala.concurrent.Future

@ImplementedBy(classOf[CouponRepositoryImpl])
trait CouponRepository {
  //-----------[ Queries ]-----------------
  def countAll(): Future[Int]
  def findById(code: Coupon.Id): Future[Option[Coupon]]
  //-----------[ Commands ]---------------
  def store(coupon: Coupon): Future[Unit]
  def store(coupons: Seq[Coupon]): Future[Unit]
}
