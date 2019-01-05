package com.upgrade.volcano.campsite;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrade.volcano.campsite.dtos.BookingDTO;
import org.json.JSONArray;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(ConcurrentTestRunner.class)
@SpringBootTest(classes = BookingCampsiteApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ConcurrentBookingTest {
    private static final Logger log = LoggerFactory.getLogger(ConcurrentBookingTest.class);

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

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
    private int bookingsCount;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @PostConstruct
    public void init() {
        JSONArray json = getAllBookingsJSon();
        bookingsCount = json.length();
    }

    @Before
    public void setUp() {
        StringBuilder sb = new StringBuilder()
                .append("http://localhost:")
                .append(getPort());
        baseUrl = sb.toString();
    }

    @Test
    public void postNewBookingsMultithreading() throws Exception {
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

        MvcResult mvcResult1 = mvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDTO)))
                .andReturn();

        log.info("response status: " + mvcResult1.getResponse().getStatus());
        log.info("response content: " + mvcResult1.getResponse().getContentAsString());

    }

    @After
    public void tearDown() {
        JSONArray allBookingsJSon = getAllBookingsJSon();
        int finalBookingsCount = allBookingsJSon.length();

        //only one new booking should be added
        Assert.assertEquals("only one new booking should be added",1, finalBookingsCount - bookingsCount);

    }

    private JSONArray getAllBookingsJSon() {
        StringBuilder sb = new StringBuilder()
                .append(baseUrl)
                .append(BOOKING_RESOURCE);

        try {
            // get all bookings
            MvcResult mvcResult = mvc.perform(get(sb.toString())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();

            String strResponse = mvcResult.getResponse().getContentAsString();
            assertNotNull(strResponse);

            return new JSONArray(strResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
