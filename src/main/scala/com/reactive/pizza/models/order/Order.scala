package com.reactive.pizza.models.order

import com.reactive.pizza.models.coupon.Coupon
import com.reactive.pizza.models.item.PickedItem
import com.reactive.pizza.models.order.Order.{ CustomerInfo, Status }
import org.joda.time.DateTime

import scala.math.Ordered.orderingToOrdered

class Order(
  id:           Order.Id,
  pikItems:     Seq[PickedItem],
  coupon:       Option[Coupon],
  customerInfo: CustomerInfo,
  totalPrice:   Int,
  status:       Status,
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
  case class CustomerInfo(fullName: String, phone: String, address: String, memo: Option[String]) {
    //----------------[ Validations ]-------------------
  }
  //---------------//-----------------
  sealed abstract class Status(val v: String)
  final case object Processing extends Status("Processing")
  final case object Delivery   extends Status("Delivery")
  final case object Cancel     extends Status("Cancel")
  final case object Complete   extends Status("Complete")
  object Status {
    def apply(v: String): Status = v match {
      case "Processing" => Processing
      case "Delivery"   => Delivery
      case "Cancel"     => Cancel
      case "Complete"   => Complete

      case _            => throw new IllegalArgumentException(s"Illegal order status: $v")
    }
  }
}
