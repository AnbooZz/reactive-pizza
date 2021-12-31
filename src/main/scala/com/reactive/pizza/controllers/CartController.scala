package com.reactive.pizza.controllers

import com.reactive.pizza.controllers.common.BaseController
import com.reactive.pizza.jsons._
import com.reactive.pizza.models.cart.Cart
import com.reactive.pizza.models.coupon.Coupon
import com.reactive.pizza.models.item.Item
import com.reactive.pizza.repositories.{ CartRepository, CouponRepository, ItemRepository }
import com.reactive.pizza.services.AuthService
import com.reactive.pizza.utils.EntityNotFoundException
import play.api.mvc.ControllerComponents

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class CartController @Inject()(
  cc:               ControllerComponents,
  cartRepository:   CartRepository,
  itemRepository:   ItemRepository,
  couponRepository: CouponRepository,
  authService:      AuthService
)(implicit val ec: ExecutionContext) extends BaseController(cc){

  def get(userId: String) = withAuth(authService.check) { user => _ =>
    if (userId != user.id.v) {
      badRequest(BAD_REQUEST_MSG)
    } else {
      cartRepository.findByUserId(user.id)
        .map(_.map(writes.CartDTO(_)))
        .map(success(_))
    }
  }

  def addItem(userId: String) = withAuth(authService.check) { user => request =>
    if (userId != user.id.v) throw new IllegalArgumentException(BAD_REQUEST_MSG)

    request.body.asJson.flatMap(_.asOpt[reads.PickedItemDTO]) match {
      case Some(dto) =>
        val f1 = itemRepository.findById(Item.Id(dto.itemId))
        val f2 = cartRepository.findByUserId(user.id)

        for {
          itemOpt <- f1
          item     = itemOpt.getOrElse(throw new EntityNotFoundException(s"Not found item with id: ${dto.itemId}"))

          cartOpt <- f2
          cart    <- cartOpt match {
                      case Some(cart) =>
                        val updatedCart = cart.addItem(dto.toPikItem(item))
                        cartRepository.update(updatedCart).map(_ => updatedCart)
                      case None       =>
                        val cart = Cart(user.id, dto.toPikItem(item))
                        cartRepository.store(cart).map(_ => cart)
                    }
        } yield {
          success(writes.CartDTO(cart))
        }

      case None           =>
        badRequest(BAD_REQUEST_MSG)
    }
  }

  def update(userId: String) = withAuth(authService.check) { user => request =>
    if (userId != user.id.v) throw new IllegalArgumentException(BAD_REQUEST_MSG)

    request.body.asJson.flatMap(_.asOpt[reads.CartDTO]) match {
      case Some(dto) =>
        val f1 = itemRepository.filterByIds(dto.getItemIds)
        val f2 = cartRepository.findByUserId(user.id)
        val f3 = dto.coupon match {
          case Some(cp) => couponRepository.findById(Coupon.Id(cp.code))
          case None     => Future.successful(None)
        }

        for {
          items   <- f1
          _        = if (dto.items.size != items.size) throw new IllegalArgumentException(BAD_REQUEST_MSG)
          cartOpt <- f2
          cart     = cartOpt.getOrElse(throw new EntityNotFoundException(s"Not found cart belong to userId: $userId"))
          coupon  <- f3

          itemMap         = items.map(it => it.id.v -> it).toMap
          (adds, reduces) = dto.items.partition(_.isAdd) match {
                              case (addedItems , reducedItems) =>
                                addedItems.map(pikIt => pikIt.toPikItem(itemMap(pikIt.itemId))) ->
                                  reducedItems.map(pikIt => pikIt.toPikItem(itemMap(pikIt.itemId)))
                            }
          updatedCart = cart.addItems(adds).reduceItems(reduces).updateCoupon(coupon)
          _          <- cartRepository.update(updatedCart)
        } yield {
          success(writes.CartDTO(updatedCart))
        }

      case None      =>
        badRequest(BAD_REQUEST_MSG)
    }
  }
}
