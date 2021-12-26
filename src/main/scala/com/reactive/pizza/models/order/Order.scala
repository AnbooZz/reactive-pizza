package com.reactive.pizza.models.order

import com.reactive.pizza.models.coupon.Coupon
import com.reactive.pizza.models.item.PickedItem
import com.reactive.pizza.models.order.Order.{ CustomerInfo, Status }
import com.reactive.pizza.models.user.User
import org.joda.time.DateTime

import scala.math.Ordered.orderingToOrdered

case class Order(
  id:           Order.Id,
  pikItems:     Seq[PickedItem],
  coupon:       Option[Coupon],
  customerInfo: CustomerInfo,
  totalPrice:   Int,
  status:       Status,
  userId:       User.Id,
  createdAt:    DateTime,
  updatedAt:    DateTime
) {
  //----------------[ Validations ]-------------------
  require(totalPrice > 0,         "Total price must be greater than 0")
  require(createdAt <= updatedAt, "UpdatedAt must be after than CreatedAt")
}

object Order {
  case class Id(v: String)
  //---------------//-----------------
  case class CustomerInfo(fullname: String, phone: String, address: String, memo: Option[String]) {
    //----------------[ Validations ]-------------------
    require(1 <= fullname.length && fullname.length <= 255, "Fullname must be from 1 to 255 characters")
    require(1 <= phone.length && phone.length <= 11,        "Phone must be from 1 to 11 numbers")
    require(1 <= address.length && address.length <= 255,   "Address must be from 1 to 255 characters")
    require(
      memo.isEmpty || (1 <= memo.get.length && memo.get.length <= 255),
      "Memo must be from 1 to 255 characters"
    )
  }
  //---------------//-----------------
  sealed abstract class Status(val v: String)
  object Status {
    final case object Processing extends Status("Processing")
    final case object Delivery   extends Status("Delivery")
    final case object Cancel     extends Status("Cancel")
    final case object Complete   extends Status("Complete")

    def apply(v: String): Status = v match {
      case "Processing" => Processing
      case "Delivery"   => Delivery
      case "Cancel"     => Cancel
      case "Complete"   => Complete

      case _            => throw new IllegalArgumentException(s"Illegal order status: $v")
    }
  }
}
