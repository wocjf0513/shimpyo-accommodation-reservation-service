package com.fc.shimpyo_be.domain.cart.docs;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fc.shimpyo_be.config.RestDocsSupport;
import com.fc.shimpyo_be.domain.cart.dto.request.CartCreateRequest;
import com.fc.shimpyo_be.domain.cart.entity.Cart;
import com.fc.shimpyo_be.domain.cart.factory.CartFactory;
import com.fc.shimpyo_be.domain.cart.repository.CartRepository;
import com.fc.shimpyo_be.domain.member.entity.Authority;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.repository.MemberRepository;
import com.fc.shimpyo_be.domain.product.entity.Product;
import com.fc.shimpyo_be.domain.product.factory.ProductFactory;
import com.fc.shimpyo_be.domain.product.repository.ProductRepository;
import com.fc.shimpyo_be.domain.room.entity.Room;
import com.fc.shimpyo_be.domain.room.repository.RoomRepository;
import com.fc.shimpyo_be.global.util.DateTimeUtil;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


class CartRestIntegrationDocsTest extends RestDocsSupport {

    @MockBean
    private SecurityUtil securityUtil;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ProductRepository productRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Room room;

    private Product product;

    private Member member;

    @BeforeEach
    void initTest() {
        //given
        member = memberRepository.save(Member.builder()
            .email("wocjf" + ThreadLocalRandom.current().nextInt(100000) + "@naver.com")
            .photoUrl("hello,world.jpg").name("심재철").password("1234").authority(Authority.ROLE_USER)
            .build());

        given(securityUtil.getCurrentMemberId()).willReturn(1L);

        product = productRepository.save(ProductFactory.createTestProduct());
        room = roomRepository.save(ProductFactory.createTestRoom(product,0L));

        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @Test
    @WithMockUser
    void getCarts() throws Exception {
        //given
        for (int i = 0; i < 5; i++) {
            Cart cart = cartRepository.save(CartFactory.createCartTest(room, member));
        }

        given(securityUtil.getCurrentMemberId()).willReturn(member.getId());
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/carts"));

        //then
        resultActions.andExpect(status().isOk()).andDo(restDoc.document(
            responseFields(responseCommon()).and(
                fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                fieldWithPath("data[].cartId").type(JsonFieldType.NUMBER).description("장바구니 아이디"),
                fieldWithPath("data[].productId").type(JsonFieldType.NUMBER).description("숙소 아이디"),
                fieldWithPath("data[].productName").type(JsonFieldType.STRING).description("숙소 이름"),
                fieldWithPath("data[].image").type(JsonFieldType.STRING).description("숙소 대표 이미지"),
                fieldWithPath("data[].roomCode").type(JsonFieldType.NUMBER).description("방 코드"),
                fieldWithPath("data[].roomName").type(JsonFieldType.STRING).description("방 이름"),
                fieldWithPath("data[].price").type(JsonFieldType.NUMBER).description("총 가격"),
                fieldWithPath("data[].description").type(JsonFieldType.STRING).description("방 설명"),
                fieldWithPath("data[].standard").type(JsonFieldType.NUMBER).description("방 기준인원"),
                fieldWithPath("data[].capacity").type(JsonFieldType.NUMBER).description("방 최대인원"),
                fieldWithPath("data[].startDate").type(JsonFieldType.STRING).description("숙박 시작일"),
                fieldWithPath("data[].endDate").type(JsonFieldType.STRING).description("숙박 종료일"),
                fieldWithPath("data[].checkIn").type(JsonFieldType.STRING).description("방 체크인 시간"),
                fieldWithPath("data[].checkOut").type(JsonFieldType.STRING)
                    .description("방 체크아웃 시간"))));
    }

    @Test
    @WithMockUser
    void addCart() throws Exception {
        //given
        LocalDate tommorrow = LocalDate.now().plusDays(1);
        CartCreateRequest cartCreateRequest = CartCreateRequest.builder()
            .startDate(DateTimeUtil.toString(
                tommorrow))
            .endDate(DateTimeUtil.toString(tommorrow.plusDays(2))).price(100000L)
            .roomCode(room.getCode()).build();

        cartRepository.save(Cart.builder().roomCode(0L).price(10000L).member(member).startDate(tommorrow.plusDays(2)).endDate(tommorrow.plusDays(3)).build());
        //when
        ResultActions resultActions = mockMvc.perform(
            post("/api/carts").content(objectMapper.writeValueAsString(cartCreateRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk()).andDo(restDoc.document(
            requestFields(fieldWithPath("roomCode").type(JsonFieldType.NUMBER).description("방 코드"),
                fieldWithPath("startDate").type(JsonFieldType.STRING).description("숙박 시작일"),
                fieldWithPath("endDate").type(JsonFieldType.STRING).description("숙박 종료일"),
                fieldWithPath("price").type(JsonFieldType.NUMBER).description("장바구니 가격")),
            responseFields(responseCommon()).and(
                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                fieldWithPath("data.cartId").type(JsonFieldType.NUMBER).description("장바구니 아이디"),
                fieldWithPath("data.productId").type(JsonFieldType.NUMBER).description("숙소 아이디"),
                fieldWithPath("data.productName").type(JsonFieldType.STRING).description("숙소 이름"),
                fieldWithPath("data.image").type(JsonFieldType.STRING).description("숙소 대표 이미지"),
                fieldWithPath("data.roomCode").type(JsonFieldType.NUMBER).description("방 코드"),
                fieldWithPath("data.roomName").type(JsonFieldType.STRING).description("방 이름"),
                fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("총 가격"),
                fieldWithPath("data.description").type(JsonFieldType.STRING).description("방 설명"),
                fieldWithPath("data.standard").type(JsonFieldType.NUMBER).description("방 기준인원"),
                fieldWithPath("data.capacity").type(JsonFieldType.NUMBER).description("방 최대인원"),
                fieldWithPath("data.startDate").type(JsonFieldType.STRING).description("숙박 시작일"),
                fieldWithPath("data.endDate").type(JsonFieldType.STRING).description("숙박 종료일"),
                fieldWithPath("data.checkIn").type(JsonFieldType.STRING).description("방 체크인 시간"),
                fieldWithPath("data.checkOut").type(JsonFieldType.STRING).description("방 체크아웃 시간"),
                fieldWithPath(("data.checkOut")).type(JsonFieldType.STRING)
                    .description("방 체크아웃 시간"))));

    }

    @Test
    @WithMockUser
    void deleteCart() throws Exception {
        //given

        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/carts/{cartId}", 1L));
        //then
        resultActions.andExpect(status().isOk()).andDo(restDoc.document(
            pathParameters(parameterWithName("cartId").description("삭제할 장바구니 아이디")),
            responseFields(responseCommon()).and(
                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                fieldWithPath("data.cartId").type(JsonFieldType.NUMBER).description("장바구니 아이디"),
                fieldWithPath("data.roomCode").type(JsonFieldType.NUMBER).description("방 코드"),
                fieldWithPath("data.startDate").type(JsonFieldType.STRING).description("숙박 시작일"),
                fieldWithPath("data.endDate").type(JsonFieldType.STRING).description("숙박 종료일"))));
    }
}