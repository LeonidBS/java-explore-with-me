package ru.practicum.ewmservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.ewmservice.category.dto.CategoryDto;
import ru.practicum.ewmservice.event.model.State;
import ru.practicum.ewmservice.rating.model.Emoji;
import ru.practicum.ewmservice.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class EventFullDto {

    private Integer id;

    private String annotation;

    private CategoryDto category;

    private int confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private State state;

    private String title;

    private int views;

    private Map<Emoji, Long> rates;

    private Integer rating;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public void setConfirmedRequests(int confirmedRequests) {
        this.confirmedRequests = confirmedRequests;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setRates(Map<Emoji, Long> rates) {
        this.rates = new HashMap<>(rates);
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    //    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
//    public EventFullDto(@JsonProperty("annotation") String annotation,
//                        @JsonProperty("category") CategoryDto category,
//                        @JsonProperty("confirmedRequests") int confirmedRequests,
//                        @JsonProperty("createdOn") LocalDateTime createdOn,
//                        @JsonProperty("description") String description,
//                        @JsonProperty("eventDate") LocalDateTime eventDate,
//                        @JsonProperty("initiator") UserShortDto initiator,
//                        @JsonProperty("location") LocationDto location,
//                        @JsonProperty("paid") Boolean paid,
//                        @JsonProperty("participantLimit") Integer participantLimit,
//                        @JsonProperty("requestModeration") Boolean requestModeration,
//                        @JsonProperty("state") State state,
//                        @JsonProperty("title") String title,
//                        @JsonProperty("views") int views,
//                        @JsonProperty("rates") Map<String, Integer> rates,
//                        @JsonProperty("rating") Integer rating) {
//        this.annotation = annotation;
//        this.category = category;
//        this.confirmedRequests = confirmedRequests;
//        this.createdOn = createdOn;
//        this.description = description;
//        this.eventDate = eventDate;
//        this.initiator = initiator;
//        this.location = location;
//        this.paid = paid;
//        this.participantLimit = participantLimit;
//        this.requestModeration = requestModeration;
//        this.state = state;
//        this.title = title;
//        this.views = views;
//        if (rates == null) {
//            this.rates = new HashMap<>();
//        } else {
//            this.rates = rates;
//        }
//        this.rating = rating;
//    }
}
