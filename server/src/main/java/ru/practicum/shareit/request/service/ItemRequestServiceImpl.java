package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemByRequestDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutput;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository repository;

    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto createRequest(ItemRequestDto itemRequestDto, long userId) {
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto,
                UserMapper.toUser(userService.getUserById(userId)));
        return ItemRequestMapper.toItemRequestDto(repository.save(itemRequest));
    }

    @Override
    public ItemRequestOutput getRequestById(long requestId, long userId) {
        userService.getUserById(userId);
        ItemRequest itemRequest = repository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с id = " + requestId + " не найден"));
        List<ItemByRequestDto> items = itemRepository.findAllByRequestId(requestId)
                .stream()
                .map(ItemMapper::toItemByRequestDto)
                .collect(toList());
        return ItemRequestMapper.toItemRequestOutput(itemRequest, items);
    }


    @Override
    public List<ItemRequestOutput> getRequestsByUserId(long userId) {
        userService.getUserById(userId);
        List<ItemRequestOutput> result = new ArrayList<>();
        List<ItemRequest> requests = repository.findAllByRequester_IdOrderByCreatedDesc(userId);
        List<Long> requestsId = new ArrayList<>();
        requests.forEach(r -> {
            requestsId.add(r.getId());
        });
        Map<Long, List<ItemByRequestDto>> items = itemRepository.findAllWhereRequestIdIn(requestsId)
                .stream()
                .map(ItemMapper::toItemByRequestDto)
                .collect(Collectors.groupingBy(ItemByRequestDto::getRequestId, toList()));
        requests.forEach(itemRequest -> {
            result.add(ItemRequestMapper.toItemRequestOutput(itemRequest,
                    items.getOrDefault(itemRequest.getId(), Collections.emptyList())));
        });
        return result;
    }

    @Override
    public List<ItemRequestOutput> getAllRequests(long userId, int from, Integer size) {
        userService.getUserById(userId);
        List<ItemRequest> requests = repository.findAllByRequester_IdNotOrderByCreatedDesc(userId);
        List<Long> requestsId = new ArrayList<>();
        requests.forEach(r -> {
            requestsId.add(r.getId());
        });
        List<ItemRequestOutput> result = new ArrayList<>();
        Map<Long, List<ItemByRequestDto>> items = itemRepository.findAllWhereRequestIdIn(requestsId)
                .stream()
                .map(ItemMapper::toItemByRequestDto)
                .collect(Collectors.groupingBy(ItemByRequestDto::getRequestId, toList()));
        requests.forEach(itemRequest -> {
            result.add(ItemRequestMapper.toItemRequestOutput(itemRequest,
                    items.getOrDefault(itemRequest.getId(), Collections.emptyList())));
        });
        return result.subList(from, requests.size())
                .stream()
                .limit(size)
                .collect(toList());
    }

}