package com.upgrade.volcano.campsite;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrade.volcano.campsite.dtos.BookingDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookingCampsiteApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BookingTest {
    private static final String CAMPSITE_RESOURCE = "/campsites";
    private static final String BOOKING_RESOURCE = "/bookings";
    private static final Long DEFAULT_CAMPSITE_ID = 1L;

    @LocalServerPort
    protected int port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;

    @Before
    public void setUp() {
        StringBuilder sb = new StringBuilder()
                .append("http://localhost:")
                .append(getPort());
        baseUrl = sb.toString();
    }

    @Test
    public void testSucessfullyCreateNewBooking() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();

        bookingDTO.setFullName("Newbooking Creator");
        bookingDTO.setEmail("creator@newdomain.com");

        LocalDateTime checkInTime = LocalDateTime.of(LocalDate.now().plusDays(10), LocalTime.of(12, 00));
        bookingDTO.setCheckInDateTime(checkInTime);

        LocalDateTime checkOutTime = LocalDateTime.of(LocalDate.now().plusDays(12), LocalTime.of(12, 00));
        bookingDTO.setCheckoutDateTime(checkOutTime);

        StringBuilder sb = new StringBuilder()
                .append(baseUrl)
                .append(CAMPSITE_RESOURCE).append("/")
                .append(DEFAULT_CAMPSITE_ID)
                .append(BOOKING_RESOURCE);
        String endpoint = sb.toString();

        mvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
        ;

    }

    @Test
    public void testBookingDateNotAvailable() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();

        bookingDTO.setFullName("Newbooking Creator");
        bookingDTO.setEmail("creator@newdomain.com");

        LocalDateTime checkInTime = LocalDateTime.of(LocalDate.now().plusDays(5), LocalTime.of(12, 00));
        bookingDTO.setCheckInDateTime(checkInTime);

        LocalDateTime checkOutTime = LocalDateTime.of(LocalDate.now().plusDays(8), LocalTime.of(12, 00));
        bookingDTO.setCheckoutDateTime(checkOutTime);

        StringBuilder sb = new StringBuilder()
                .append(baseUrl)
                .append(CAMPSITE_RESOURCE).append("/")
                .append(DEFAULT_CAMPSITE_ID)
                .append(BOOKING_RESOURCE);
        String endpoint = sb.toString();

        mvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().is5xxServerError())
                .andExpect(content().json("[\"The date you provided is not available for booking\"]"))
        ;

    }


    @Test
    public void test3DayLimitBreak() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();

        bookingDTO.setFullName("Newbooking Creator");
        bookingDTO.setEmail("creator@newdomain.com");

        LocalDateTime checkInTime = LocalDateTime.of(LocalDate.now().plusDays(5), LocalTime.of(12, 00));
        bookingDTO.setCheckInDateTime(checkInTime);

        LocalDateTime checkOutTime = LocalDateTime.of(LocalDate.now().plusDays(10), LocalTime.of(12, 00));
        bookingDTO.setCheckoutDateTime(checkOutTime);

        StringBuilder sb = new StringBuilder()
                .append(baseUrl)
                .append(CAMPSITE_RESOURCE).append("/")
                .append(DEFAULT_CAMPSITE_ID)
                .append(BOOKING_RESOURCE);
        String endpoint = sb.toString();

        mvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().is5xxServerError())
                .andExpect(content().json("[\"Bookings can't be for more than 3 days\"]"))
        ;
    }

    @Test
    public void testModifyBooking() throws Exception {
        StringBuilder sb = new StringBuilder()
                .append(baseUrl)
                .append(BOOKING_RESOURCE);

        // get all bookings
        MvcResult mvcResult = mvc.perform(get(sb.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String strResponse = mvcResult.getResponse().getContentAsString();
        assertNotNull(strResponse);

        JSONArray json = new JSONArray(strResponse);

        JSONObject jsonObject = json.getJSONObject(json.length() - 1);

        Long bookingId = jsonObject.getLong("id");
        String fullName = jsonObject.getString("fullName");
        String email = jsonObject.getString("email");
        LocalDateTime checkInDateTime = LocalDateTime.parse(jsonObject.getString("checkInDateTime"));
        LocalDateTime checkoutDateTime = LocalDateTime.parse(jsonObject.getString("checkoutDateTime"));

        BookingDTO bookingDTO = new BookingDTO();

        bookingDTO.setFullName(fullName);
        bookingDTO.setEmail("different email");
        bookingDTO.setCheckInDateTime(checkInDateTime);
        bookingDTO.setCheckoutDateTime(checkoutDateTime);

        sb = new StringBuilder()
                .append(baseUrl)
                .append(CAMPSITE_RESOURCE).append("/")
                .append(DEFAULT_CAMPSITE_ID)
                .append(BOOKING_RESOURCE).append("/")
                .append(bookingId);
        String endpoint = sb.toString();

        mvc.perform(put(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        sb = new StringBuilder()
                .append(baseUrl)
                .append(CAMPSITE_RESOURCE).append("/")
                .append(DEFAULT_CAMPSITE_ID)
                .append(BOOKING_RESOURCE).append("/")
                .append(bookingId);

        // get our modified booking
        mvcResult = mvc.perform(get(sb.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        strResponse = mvcResult.getResponse().getContentAsString();
        assertNotNull(strResponse);

        JSONObject modifiedBookingResponse = new JSONObject(strResponse);
        String modifiedEmail = modifiedBookingResponse.getString("email");

        assertEquals(modifiedEmail, "different email");

    }

    @Test
    public void postNewBookingsMultithreading() throws Exception {
        StringBuilder sb = new StringBuilder()
                .append(baseUrl)
                .append(BOOKING_RESOURCE);

        // get all bookings
        MvcResult mvcResult = mvc.perform(get(sb.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String strResponse = mvcResult.getResponse().getContentAsString();
        assertNotNull(strResponse);

        JSONArray json = new JSONArray(strResponse);
        int bookingsCount = json.length();

        ThreadPoolExecutor executor =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            final BookingDTO bookingDTO = new BookingDTO();

            bookingDTO.setFullName("Newbooking Creator");
            bookingDTO.setEmail("creator@newdomain.com");

            final LocalDateTime checkInTime = LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(12, 00));
            bookingDTO.setCheckInDateTime(checkInTime);

            final LocalDateTime checkOutTime = LocalDateTime.of(LocalDate.now().plusDays(5), LocalTime.of(12, 00));
            bookingDTO.setCheckoutDateTime(checkOutTime);

            final StringBuilder sb2 = new StringBuilder()
                    .append(baseUrl)
                    .append(CAMPSITE_RESOURCE).append("/")
                    .append(DEFAULT_CAMPSITE_ID)
                    .append(BOOKING_RESOURCE);
            String endpoint = sb2.toString();

            try {
                synchronized (this) {
                    MvcResult mvcResult1 = mvc.perform(post(endpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookingDTO)))
                            .andReturn();

                    System.out.println("\n\nthread 1 response status: " + mvcResult1.getResponse().getStatus());
                    System.out.println("\n\nthread 1 response content: " + mvcResult1.getResponse().getContentAsString());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("\n\n\n\n\nthread end!");
        });

        executor.submit(() -> {
            final BookingDTO bookingDTO = new BookingDTO();

            bookingDTO.setFullName("Newbooking Creator");
            bookingDTO.setEmail("creator@newdomain.com");

            final LocalDateTime checkInTime = LocalDateTime.of(LocalDate.now().plusDays(5), LocalTime.of(12, 00));
            bookingDTO.setCheckInDateTime(checkInTime);

            final LocalDateTime checkOutTime = LocalDateTime.of(LocalDate.now().plusDays(6), LocalTime.of(12, 00));
            bookingDTO.setCheckoutDateTime(checkOutTime);

            final StringBuilder sb2 = new StringBuilder()
                    .append(baseUrl)
                    .append(CAMPSITE_RESOURCE).append("/")
                    .append(DEFAULT_CAMPSITE_ID)
                    .append(BOOKING_RESOURCE);
            String endpoint = sb2.toString();

            try {
                synchronized (this) {
                    MvcResult mvcResult1 = mvc.perform(post(endpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookingDTO)))
                            .andReturn();

                    System.out.println("\n\nthread 2 response status: " + mvcResult1.getResponse().getStatus());
                    System.out.println("\n\nthread 2 response content: " + mvcResult1.getResponse().getContentAsString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("\n\n\n\n\nthread2 end!");
        });

        executor.awaitTermination(2, TimeUnit.SECONDS);

        mvcResult = mvc.perform(get(sb.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        strResponse = mvcResult.getResponse().getContentAsString();
        assertNotNull(strResponse);

        json = new JSONArray(strResponse);
        int bookingsCountAfterEnd = json.length();

        //only one new booking should be added
        Assert.assertEquals("only one new booking should be added",1, bookingsCountAfterEnd - bookingsCount);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
