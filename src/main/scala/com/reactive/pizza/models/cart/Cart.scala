package com.reactive.pizza.models.cart

import com.reactive.pizza.models.coupon.{ Coupon, MoneyCoupon }
import com.reactive.pizza.models.coupon.Coupon.Effect
import com.reactive.pizza.models.item.PickedItem
import com.reactive.pizza.models.user.User
import com.reactive.pizza.utils.{ InvalidCouponException, Encrypter }
import org.joda.time.DateTime

import scala.math.Ordered.orderingToOrdered

case class Cart(
  id:        Cart.Id,
  pikItems:  Seq[PickedItem],
  coupon:    Option[Coupon],
  userId:    User.Id,
  createdAt: DateTime,
  updatedAt: DateTime
) {
  //-----------------[ Properties ]-------------------------
  private val pikItemMap = pikItems.map(pi => pi.item.id -> pi).toMap
  //-----------------[ Validations ]--------------------
  require(createdAt <= updatedAt, "UpdatedAt must be after than CreatedAt")
  //---------------[ Methods ]----------------
  def getTotalPrice: Int = {
    val beforeUsingCoupon = pikItems.map(_.getPrice).sum

    coupon match {
      case Some(cp: MoneyCoupon) => cp.reduce(beforeUsingCoupon)
      case _                     => beforeUsingCoupon
    }
  }

  def updateCoupon(coupon: Option[Coupon]): Cart =
    coupon match {
      case Some(cp) if cp.isExpired                        =>
        throw new InvalidCouponException("Coupon is out of time")
      case Some(cp) if !cp.effects.contains(Effect.Online) =>
        throw new InvalidCouponException("Coupon is only use at store")

      case _ => this.copy(coupon = coupon)
    }

  def addItem(pikItem: PickedItem): Cart = {
    val updatedPikItemMap = pikItemMap.get(pikItem.item.id) match {
      case Some(v) =>
        val updatedPikItem = v.copy(quantity = v.quantity + pikItem.quantity)
        pikItemMap ++ Map(pikItem.item.id -> updatedPikItem)

      case None    =>
        pikItemMap ++ Map(pikItem.item.id -> pikItem)
    }

    this.copy(pikItems = updatedPikItemMap.values.toSeq)
  }

  def addItems(pikItems: Seq[PickedItem]): Cart = {
    val updatedPikItemMap = pikItems.map { pikItem =>
      pikItemMap.get(pikItem.item.id) match {
        case Some(epi) => epi.item.id     -> epi.copy(quantity = epi.quantity + pikItem.quantity)
        case None      => pikItem.item.id -> pikItem
      }
    }.toMap ++ pikItemMap

    this.copy(pikItems = updatedPikItemMap.values.toSeq)
  }

  def reduceItem(pikItem: PickedItem): Cart = {
    val updatedPikItemMap = pikItemMap.get(pikItem.item.id) match {
      case Some(v) =>
        if (v.quantity > pikItem.quantity) {
          val updatedPikItem = v.copy(quantity = v.quantity - pikItem.quantity)
          pikItemMap ++ Map(pikItem.item.id -> updatedPikItem)
        } else if(v.quantity == pikItem.quantity) {
          pikItemMap.removed(pikItem.item.id)
        } else {
          throw errorByQuantity
        }

      case None    =>
        throw notFoundItemInCart
    }

    this.copy(pikItems = updatedPikItemMap.values.toSeq)
  }

  def reduceItems(pikItems: Seq[PickedItem]): Cart = {
    val updatedPikItemMap = pikItems.map { pikItem =>
      pikItemMap.get(pikItem.item.id) match {
        case Some(epi) =>
          if (epi.quantity >= pikItem.quantity)
            epi.copy(quantity = epi.quantity - pikItem.quantity)
          else
            throw errorByQuantity

        case None      =>
          throw notFoundItemInCart
      }
    }.map(epi => epi.item.id -> epi).toMap ++ pikItemMap

    this.copy(pikItems = updatedPikItemMap.values.toSeq)
  }

  def reset = this.copy(pikItems = Nil, coupon = None)

  //---------------------[ Exceptions ]-------------------------------------
  private def errorByQuantity    = new IllegalArgumentException("Reduce item from cart is invalid by ordered quantity")
  private def notFoundItemInCart = new IllegalArgumentException(s"Item is not in cart: ${id.v}")
}

object Cart {
  //----------[ Class definitions ]-------------------
  case class Id(v: String)
  //----------[ Methods ]---------------------
  def apply(userId: User.Id, pikItem: PickedItem): Cart = new Cart(
    id        = Id(Encrypter.generateId),
    pikItems  = Seq(pikItem),
    coupon    = None,
    userId    = userId,
    createdAt = new DateTime(),
    updatedAt = new DateTime()
  )
}
