package com.fc.shimpyo_be.domain.product.factory;

import com.fc.shimpyo_be.domain.product.entity.Category;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.entity.ProductImage;
import com.fc.shimpyo_be.domain.product.model.Area;
import com.fc.shimpyo_be.domain.product.model.RandomProductInfo;
import com.fc.shimpyo_be.domain.room.entity.Room;
import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

public class ProductFactory {


    public static Product createTestProduct() {
        String area = Area.values()[ThreadLocalRandom.current()
            .nextInt(Area.values().length)].toString();
        return Product.builder().name(area + " 숙박").address("서울시" + area)
            .thumbnail(RandomProductInfo.genRandomImage()).category(
                Category.values()[ThreadLocalRandom.current().nextInt(Category.values().length)])
            .starAvg(ThreadLocalRandom.current().nextFloat(5))
            .description(RandomProductInfo.genRandomDescription()).build();
    }

    public static ProductImage createTestProductImage(Product product) {
        return ProductImage.builder().product(product).photoUrl(RandomProductInfo.genRandomImage())
            .build();
    }

    public static Room createTestRoom(Product product) {

        int stadard = ThreadLocalRandom.current().nextInt(10);

        return Room.builder().price(ThreadLocalRandom.current().nextInt(100000))
            .description(RandomProductInfo.genRandomDescription()).product(product)
            .checkIn(LocalTime.of(11, 0, 0)).checkOut(LocalTime.of(15, 0, 0))
            .name(product.getCategory().getName() + " 방").standard(stadard)
            .capacity(stadard + ThreadLocalRandom.current().nextInt(10)).build();

    }

//
//    public static ReservationProduct createTestReservationProduct(Room room,Reservation reservation) {
//
//        return ReservationProduct.builder()
//            .room(room)
//            .reservation(reservation)
//            .price(ThreadLocalRandom.current().nextInt(100000))
//            .accommodationDate(LocalDate.of(2023,11,22))
//            .build();
//    }
//
//    public static Reservation createTestReservation(Member member) {
//
//        int stadard = ThreadLocalRandom.current().nextInt(10);
//
//        return Reservation.builder()
//            .member(member)
//            .payMethod(PayMethod.CASH)
//            .build();
//    }
//
//    public static Member createTestMember() {
//        return Member.builder()
//            .name("심재철")
//            .email("wocjf0513@naver.com")
//            .password("1234")
//            .photoUrl("dsklaffjdlsajldfjs")
//            .build();
//    }


}
