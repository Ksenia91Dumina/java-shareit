package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoJsonTest {
    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testSerialize() throws Exception {
        var dto = new BookingDto(1L, LocalDateTime.of(2022, 11, 1, 1, 1),
                LocalDateTime.of(2022, 12, 20, 1, 1), 1L, 1L,
                BookingStatus.APPROVED);
        var result = json.write(dto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).hasJsonPath("$.itemId");
        assertThat(result).hasJsonPath("$.bookerId");
        assertThat(result).hasJsonPath("$.status");
        //assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId());
        //assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(dto.getItemId());
        //assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(dto.getBookerId());
    }

}
