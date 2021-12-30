package com.reactive.pizza.dummy

import com.reactive.pizza.models.item.{ Item, SizableItem, SizeDescription }
import com.reactive.pizza.models.item.Description.{ Size, SizeInfo }
import com.reactive.pizza.models.item.Item.Group
import com.reactive.pizza.repositories.ItemRepository
import com.reactive.pizza.utils.Encrypter

import java.net.URL
import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class ItemDummy @Inject()(itemRepository: ItemRepository)(implicit val ec: ExecutionContext) {
  def init: Future[Unit] = {
    for {
      items <- itemRepository.findAll
      _     <- items.isEmpty match {
        case true  => itemRepository.store(ItemDummy.items)
        case false => Future.successful(())
      }
    } yield ()
  }

  init
}

object ItemDummy {
  //-------------[ Pizza Items ]-----------------------
  lazy val pizza1 = SizableItem(
    id    = Item.Id(Encrypter.generateId),
    name  = "P1. BEEFY PIZZA",
    descr = SizeDescription(
      Seq("Thịt bò xay, ngô, sốt BBQ, pho mai."),
      Seq(
        SizeInfo(Size.S, 20, 90),
        SizeInfo(Size.M, 24, 110),
        SizeInfo(Size.L, 29, 150),
      )
    ),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2019/12/P1rs1.jpg"),
    group   = Group.Pizza
  )
  lazy val pizza2 = SizableItem(
    id    = Item.Id(Encrypter.generateId),
    name  = "P2. HAWAII PIZZA",
    descr = SizeDescription(
      Seq("Jam bông, dứa, sốt cà chua, pho mai."),
      Seq(
        SizeInfo(Size.S, 20, 90),
        SizeInfo(Size.M, 24, 110),
        SizeInfo(Size.L, 29, 150),
      )
    ),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2019/12/P2rs1.jpg"),
    group   = Group.Pizza
  )
  lazy val pizza3 = SizableItem(
    id    = Item.Id(Encrypter.generateId),
    name  = "P3. SALAMI PIZZA",
    descr = SizeDescription(
      Seq("Xúc xích Salami, hành tây, sốt cà chua, pho mai."),
      Seq(
        SizeInfo(Size.S, 20, 110),
        SizeInfo(Size.M, 24, 130),
        SizeInfo(Size.L, 29, 170),
      )
    ),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2019/12/P3rs1.jpg"),
    group   = Group.Pizza
  )
  lazy val pizza4 = SizableItem(
    id    = Item.Id(Encrypter.generateId),
    name  = "P4. CHORIZO PIZZA",
    descr = SizeDescription(
      Seq("Xúc xích Tây Ban Nha, hành tây, ô liu, sốt cà chua, pho mai."),
      Seq(
        SizeInfo(Size.S, 20, 90),
        SizeInfo(Size.M, 24, 110),
        SizeInfo(Size.L, 29, 150),
      )
    ),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2019/12/P4rs1.jpg"),
    group   = Group.Pizza
  )
  lazy val pizza5 = SizableItem(
    id    = Item.Id(Encrypter.generateId),
    name  = "P5. MEAT LOVERS PIZZA",
    descr = SizeDescription(
      Seq("Xúc xích các loại, jam bông, hành tây, sốt cà chua, pho mai."),
      Seq(
        SizeInfo(Size.S, 20, 100),
        SizeInfo(Size.M, 24, 120),
        SizeInfo(Size.L, 29, 160),
      )
    ),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2018/06/P5-1.jpg"),
    group   = Group.Pizza
  )
  lazy val pizza6 = SizableItem(
    id    = Item.Id(Encrypter.generateId),
    name  = "P6. BBQ CHICKEN PIZZA",
    descr = SizeDescription(
      Seq("Thịt gà xay, nấm, hành tây, sốt BBQ, pho mai."),
      Seq(
        SizeInfo(Size.S, 20, 90),
        SizeInfo(Size.M, 24, 110),
        SizeInfo(Size.L, 29, 150),
      )
    ),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2019/12/P6rs1.jpg"),
    group   = Group.Pizza
  )
  lazy val pizza7 = SizableItem(
    id    = Item.Id(Encrypter.generateId),
    name  = "P7. SMOKE CHICKEN PIZZA",
    descr = SizeDescription(
      Seq("Thịt gà hun khói, hành tây, nấm, sốt cà chua, pho mai."),
      Seq(
        SizeInfo(Size.S, 20, 90),
        SizeInfo(Size.M, 24, 110),
        SizeInfo(Size.L, 29, 150),
      )
    ),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2019/12/P7rs1.jpg"),
    group   = Group.Pizza
  )
  lazy val pizza8 = SizableItem(
    id    = Item.Id(Encrypter.generateId),
    name  = "P8. MEXICAN PIZZA",
    descr = SizeDescription(
      Seq("Thịt bò xay, dứa, sốt BBQ, pho mai."),
      Seq(
        SizeInfo(Size.S, 20, 90),
        SizeInfo(Size.M, 24, 110),
        SizeInfo(Size.L, 29, 150),
      )
    ),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2019/12/P8rs1.jpg"),
    group   = Group.Pizza
  )
  lazy val pizza9 = SizableItem(
    id    = Item.Id(Encrypter.generateId),
    name  = "P9. VEGGIE PIZZA",
    descr = SizeDescription(
      Seq("Nấm, ngô, dứa, ớt xanh, hành tây, cà chua, sốt cà chua, pho mai."),
      Seq(
        SizeInfo(Size.S, 20, 90),
        SizeInfo(Size.M, 24, 110),
        SizeInfo(Size.L, 29, 150),
      )
    ),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2019/12/P9rs1.jpg"),
    group   = Group.Pizza
  )
  lazy val pizza10 = SizableItem(
    id    = Item.Id(Encrypter.generateId),
    name  = "P10. MARGHERITA PIZZA",
    descr = SizeDescription(
      Seq("Cà chua, nhiều pho mai Mozzarella, sốt cà chua, pho mai."),
      Seq(
        SizeInfo(Size.S, 20, 90),
        SizeInfo(Size.M, 24, 110),
        SizeInfo(Size.L, 29, 150),
      )
    ),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2019/12/P10rs1.jpg"),
    group   = Group.Pizza
  )
  lazy val pizza11 = SizableItem(
    id    = Item.Id(Encrypter.generateId),
    name  = "P11. MICHIGAN PIZZA",
    descr = SizeDescription(
      Seq("Tôm, cá ngừ, hành tây, ớt xanh, dứa, sốt cà chua, pho mai."),
      Seq(
        SizeInfo(Size.S, 20, 110),
        SizeInfo(Size.M, 24, 130),
        SizeInfo(Size.L, 29, 170),
      )
    ),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2019/12/P11rs1.jpg"),
    group   = Group.Pizza
  )
  lazy val pizza12 = SizableItem(
    id    = Item.Id(Encrypter.generateId),
    name  = "P12. SEAFOOD PIZZA",
    descr = SizeDescription(
      Seq("Thanh cua, tôm, ớt xanh, hành tây, cà chua, sốt cà chua, pho mai."),
      Seq(
        SizeInfo(Size.S, 20, 110),
        SizeInfo(Size.M, 24, 130),
        SizeInfo(Size.L, 29, 170),
      )
    ),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2019/12/P12rs1.jpg"),
    group   = Group.Pizza
  )
  lazy val pizza13 = SizableItem(
    id    = Item.Id(Encrypter.generateId),
    name  = "C1. DOUBLE CHEESE (GẤP ĐÔI PHO MAI)",
    descr = SizeDescription(
      Seq("Thêm pho mai cho bánh pizza"),
      Seq(
        SizeInfo(Size.S, 20, 15),
        SizeInfo(Size.M, 24, 20),
        SizeInfo(Size.L, 29, 25),
      )
    ),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2020/05/cheese.png"),
    group   = Group.Pizza
  )

  lazy val items = Seq(
    pizza1, pizza2, pizza3, pizza4, pizza5, pizza6, pizza7, pizza8, pizza9, pizza10, pizza11, pizza12, pizza13
  )
}
