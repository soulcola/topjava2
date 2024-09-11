package ru.javaops.topjava2.web.vote;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.service.VoteService;
import ru.javaops.topjava2.to.VoteTo;
import ru.javaops.topjava2.util.VoteUtil;
import ru.javaops.topjava2.web.AuthUser;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static ru.javaops.topjava2.util.validation.ValidationUtil.assureIdConsistent;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Vote Controller")
public class ProfileVoteController {
    static final String REST_URL = "/api/profile/votes";

    private final VoteService voteService;

    @Cacheable(value = "userCache", key="#authUser.id()")
    @GetMapping(REST_URL + "/today")
    public VoteTo getToday(@AuthenticationPrincipal @ApiIgnore AuthUser authUser) {
        int userId = authUser.id();
        log.info("Get today vote for user {}", userId);
        return voteService.getByDateAndUserId(LocalDate.now(), userId);
    }

    @PostMapping(REST_URL)
    public VoteTo create(@RequestBody @Valid VoteTo vote, @ApiIgnore @AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        log.info("Vote user {} for restaurant {}", userId, vote);
        return voteService.create(vote, userId, LocalDateTime.now());
    }

    @Operation(summary = "Update user's vote")
    @PutMapping(REST_URL)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid VoteTo vote,
                       @ApiIgnore @AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        log.info("Update vote for user {}", userId);
        voteService.update(vote, userId, LocalDateTime.now(), VoteUtil.DEADLINE);
    }
}