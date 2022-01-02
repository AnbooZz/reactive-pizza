package com.reactive.pizza.dummy

import com.reactive.pizza.models.item.{ ComboItem, Description, Item, NoSizeableItem, SizableItem, SizeDescription }
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

  //---------------------------[ BBQs ]----------------------------
  lazy val bbq1 = NoSizeableItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "SƯỜN NƯỚNG BBQ – SIZE M – 300GR",
    descr   = new Description(Seq("Khối lượng: Thịt heo 300gr", "Sốt BBQ truyền thống")),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2019/10/sườnbbq.png"),
    group   = Group.BBQ,
    price   = 119
  )
  lazy val bbq2 = NoSizeableItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "SƯỜN NƯỚNG BBQ – SIZE L – 500GR",
    descr   = new Description(Seq("Khối lượng: Thịt heo 500gr", "Sốt BBQ truyền thống")),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2019/10/sườnbbq.png"),
    group   = Group.BBQ,
    price   = 179
  )

  //-----------------------[ Noodles ]------------------------------
  lazy val noodle1 = NoSizeableItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "M1. SPAGHETTI BOLOGNESE",
    descr   = new Description(Seq("Sốt bò băm, pho mai Parmesan")),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2018/06/M1-260x204.jpg"),
    group   = Group.Noodle,
    price   = 65
  )
  lazy val noodle2 = NoSizeableItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "M2. SPAGHETTI CARBONARA",
    descr   = new Description(Seq("Jam bông, nấm, sốt kem trứng, pho mai Parmesan")),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2018/06/M2-260x204.jpg"),
    group   = Group.Noodle,
    price   = 65
  )
  lazy val noodle3 = NoSizeableItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "M3. SPAGHETTI SMOKE CHICKEN",
    descr   = new Description(Seq("Thịt gà hun khói, sốt kem trứng, pho mai Parmesan")),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2018/06/M3-1.jpg"),
    group   = Group.Noodle,
    price   = 65
  )
  lazy val noodle4 = NoSizeableItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "M4. SPAGHETTI RATATOUILLE",
    descr   = new Description(Seq("Xúc xích Chorizo, sốt cà chua rau hầm, pho mai Parmesan")),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2018/06/M4-260x204.png"),
    group   = Group.Noodle,
    price   = 65
  )
  lazy val noodle5 = NoSizeableItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "M5. SPAGHETTI SEAFOOD",
    descr   = new Description(Seq("Mực, tôm, ớt xanh, cà chua, hành tây, sốt cà chua, pho mai Parmesan")),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2018/06/41411111.jpg"),
    group   = Group.Noodle,
    price   = 75
  )
  lazy val noodle6 = NoSizeableItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "M6. SPAGHETTI SHRIMP",
    descr   = new Description(Seq("Tôm, nấm, sốt kem, pho mai Parmesan")),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2018/06/M5-260x204.jpg"),
    group   = Group.Noodle,
    price   = 75
  )

  //--------------------[ Salads ]-------------------------
  lazy val salad1 = NoSizeableItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "S1. GARDEN SALAD",
    descr   = new Description(Seq("Xà lách, cà chua, dưa chuột, sốt dầu dấm")),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2018/06/S1.-Garden-Salad.jpg"),
    group   = Group.Salad,
    price   = 40
  )
  lazy val salad2 = NoSizeableItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "S2. SEASONAL SALAD",
    descr   = new Description(Seq("Rau theo mùa, thịt gà hun khói, xà lách, sốt Ceasar")),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2018/06/S2-260x204.jpg"),
    group   = Group.Salad,
    price   = 60
  )
  lazy val salad3 = NoSizeableItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "S3. NICOISE SALAD",
    descr   = new Description(Seq("Cá ngừ, trứng, cà chua tươi, hành tây, dưa chuột, pho mai Parmesan")),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2018/06/S3-260x204.jpg"),
    group   = Group.Salad,
    price   = 60
  )

  //----------------[ Drinks ]-----------------------------
  lazy val drink1 = NoSizeableItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "U1. COCA COLA 390ML",
    descr   = new Description(Seq("Coca cola chai nhựa 390 ml")),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2018/06/coca-390ml-370x330-260x204.jpg"),
    group   = Group.Drink,
    price   = 15
  )
  lazy val drink2 = NoSizeableItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "U2. COCA COLA 1.5L",
    descr   = new Description(Seq("Coca cola chai nhựa 1.5 lít")),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2018/06/Coca-1.5L-260x204.jpg"),
    group   = Group.Drink,
    price   = 30
  )

  //------------------[ Others ]--------------------------------
  lazy val other1 = NoSizeableItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "K3. KHOAI TÂY CHIÊN",
    descr   = new Description(Seq("Khoai tây chiên")),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2018/06/KV3-370x330-260x204.jpg"),
    group   = Group.Other,
    price   = 35
  )
  lazy val other2 = SizableItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "K4. ĐẾ BÁNH PIZZA",
    descr   = SizeDescription(
                Seq("Đế làm bánh Pizza"),
                Seq(
                  SizeInfo(Size.S, 20, 15),
                  SizeInfo(Size.M, 24, 20),
                  SizeInfo(Size.L, 30, 25),
                )
              ),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2018/12/pizza-bases.jpg"),
    group   = Group.Other,
  )
  lazy val other3 = NoSizeableItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "K5. PHO MAI MOZZARELLA PHÁP (1KG) – BÀO SỢI, CHIA GÓI NHỎ",
    descr   = new Description(Seq("Pho mai Mozzarella Pháp (1kg) – Bào sợi, chia gói nhỏ")),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2018/06/Phomaisoi.png"),
    group   = Group.Other,
    price   = 180
  )

  //------------------[ Combos ]--------------------------------
  lazy val combo1 = ComboItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "COMBO 01",
    descr   = new Description(Seq("Combo dành cho 2 người ăn", "1 Pizza size S", "1 Coca 390 ml", "1 Salad")),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2018/06/combo-11-1.jpg"),
    items   = Seq(pizza1, drink1, salad1),
    price   = 139
  )
  lazy val combo2 = ComboItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "COMBO 02",
    descr   = new Description(Seq("Combo 02 dành cho 3 người ăn", "1 Pizza size L", "1 Mỳ Ý", "1 Coca 390ml")),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2018/06/combo-22-1.jpg"),
    items   = Seq(pizza1, noodle1, drink1),
    price   = 179
  )
  lazy val combo3 = ComboItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "COMBO 03",
    descr   = new Description(Seq("Combo 03 dành cho 4 người ăn", "1 Pizza size L", "1 Pizza size M", "1 Mỳ Ý", "1 Coca 390ml")),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2018/06/combo-33-1.jpg"),
    items   = Seq(pizza1, pizza1, noodle1, drink1),
    price   = 269
  )
  lazy val combo4 = ComboItem(
    id      = Item.Id(Encrypter.generateId),
    name    = "COMBO 04",
    descr   = new Description(Seq("Dành cho 6 người ăn", "3 Pizza size L", "1 Mỳ Ý", "1 Coca 1.5L", "1 Khoai tây chiên")),
    imgLink = new URL("https://www.pizzaexpress.vn/wp-content/uploads/2018/06/combo-44-260x204.png"),
    items   = Seq(pizza1, pizza2, pizza3, noodle1, drink1, other1),
    price   = 429
  )

  lazy val items = Seq(
    pizza1, pizza2, pizza3, pizza4, pizza5, pizza6, pizza7, pizza8, pizza9, pizza10, pizza11, pizza12, pizza13,
    bbq1, bbq2, noodle1, noodle2, noodle3, noodle4, noodle5, noodle6, salad1, salad2, salad3, drink1, drink2,
    combo1, combo2, combo3, combo4, other1, other2, other3
  )
}
