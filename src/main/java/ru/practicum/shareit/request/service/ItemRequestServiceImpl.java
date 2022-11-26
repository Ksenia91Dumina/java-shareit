package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    @Autowired
    public ItemRequestRepository repository;

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
    public List<ItemRequestOutput> getRequestsByUserId(long userId) {
        userService.getUserById(userId);
        List<ItemRequest> requests = repository.findAllByRequester_IdOrderByCreatedDesc(userId);
        List<ItemRequestOutput> result = new ArrayList<>();
        requests.forEach(itemRequest -> {
            List<ItemByRequestDto> items = itemRepository.findAllByRequestId(itemRequest.getId())
                    .stream()
                    .map(ItemMapper::toItemByRequestDto)
                    .collect(Collectors.toList());
            result.add(ItemRequestMapper.toItemRequestWithAnswerDto(itemRequest, items));
        });
        return result;
    }

    @Override
    public List<ItemRequestOutput> getAllRequests(long userId, int from, Integer size) {
        List<ItemRequest> requests = repository.findAllByRequester_IdNotOrderByCreatedDesc(userId);
        checkFromParameter(from, requests.size());
        List<ItemRequestOutput> result = new ArrayList<>();
        requests.forEach(itemRequest -> {
            List<ItemByRequestDto> items = itemRepository.findAllByRequestId(itemRequest.getId())
                    .stream()
                    .map(ItemMapper::toItemByRequestDto)
                    .collect(Collectors.toList());
            result.add(ItemRequestMapper.toItemRequestWithAnswerDto(itemRequest, items));
        });
            return result.subList(from, requests.size())
                    .stream()
                    .limit(size)
                    .collect(Collectors.toList());
    }

    @Override
    public ItemRequestOutput getRequestById(long requestId, long userId) {
        userService.getUserById(userId);
        ItemRequest itemRequest = repository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с id = " + requestId + " не найден"));
        List<ItemByRequestDto> items = itemRepository.findAllByRequestId(requestId)
                .stream()
                .map(ItemMapper::toItemByRequestDto)
                .collect(Collectors.toList());
        return ItemRequestMapper.toItemRequestWithAnswerDto(itemRequest, items);
    }

    private void checkFromParameter(int from, int listSize) {
        if (from > listSize) {
            throw new IllegalArgumentException("Параметр from должен быть меньше размера страницы");
        }
    }
}