package com.reactive.pizza.dummy

import com.reactive.pizza.models.coupon.{ Coupon, GiftCoupon, MoneyCoupon }
import com.reactive.pizza.models.coupon.Coupon.{ Effect, Unit }
import com.reactive.pizza.repositories.CouponRepository
import org.joda.time.DateTime

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class CouponDummy @Inject()(couponRepository: CouponRepository)(implicit val ec: ExecutionContext) {
  def init = {
    for {
      number <- couponRepository.countAll()
      _      <- number > 0 match {
                 case true  => Future.successful(())
                 case false => couponRepository.store(CouponDummy.coupons)
               }
    } yield ()
  }

  init
}

object CouponDummy {
  val coupon1 = GiftCoupon(
    id          = Coupon.Id("SN12"),
    descr       = "Sinh nhật rộn ràng, tặng ngay 1 pizza + 1 cocacola",
    expiredDate = new DateTime().plusDays(100),
    effects     = Seq(Effect.Online, Effect.Store),
    values      = Seq(ItemDummy.pizza1, ItemDummy.drink1)
  )
  val coupon2 = GiftCoupon(
    id          = Coupon.Id("GIOVANG"),
    descr       = "Giờ vàng nhanh tay, tặng ngay salad",
    expiredDate = new DateTime().plusDays(100),
    effects     = Seq(Effect.Online, Effect.Store),
    values      = Seq(ItemDummy.pizza1, ItemDummy.drink1)
  )
  val coupon3 = MoneyCoupon(
    id          = Coupon.Id("CH12"),
    descr       = "Giảm ngay 20% khi mua bánh lẻ hoặc mỳ ý tại cửa hàng",
    expiredDate = new DateTime().plusDays(100),
    effects     = Seq(Effect.Store),
    value       = 20,
    unit        = Unit.Percent
  )
  val coupon4 = MoneyCoupon(
    id          = Coupon.Id("CH20"),
    descr       = "Giảm ngay 20% khi mua bánh lẻ hoặc mỳ ý tại cửa hàng",
    expiredDate = new DateTime().plusDays(100),
    effects     = Seq(Effect.Online),
    value       = 20,
    unit        = Unit.Percent
  )

  val coupons = Seq(coupon1, coupon2, coupon3, coupon4)
}
