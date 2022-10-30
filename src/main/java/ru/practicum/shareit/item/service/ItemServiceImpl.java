package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.model.CommentMapper;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.NotAllowedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto, long userId) {
        userService.getUserById(userId);
        Item item = ItemMapper.toItem(itemDto, userId);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto, long userId, long itemId) {
        Item item = getItemById(itemId);
        userService.getUserById(userId);
        if (item.getOwnerId() != userId) {
            throw new NotAllowedException("Пользователь с id = " + userId + " не может внести изменения");
        } else {
            Item itemToUpdate = ItemMapper.toItem(itemDto, userId);
            itemToUpdate.setId(itemId);
            if (itemToUpdate.getName() == null) {
                itemToUpdate.setName(item.getName());
            }
            if (itemToUpdate.getDescription() == null) {
                itemToUpdate.setDescription(item.getDescription());
            }
            if (itemToUpdate.getAvailable() == null) {
                itemToUpdate.setAvailable(item.getAvailable());
            }
            if (itemToUpdate.getRequestId() == null) {
                itemToUpdate.setRequestId(item.getRequestId());
            }
            return ItemMapper.toItemDto(itemRepository.save(itemToUpdate));
        }
    }


    @Override
    public Item getItemById(long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (!item.isPresent()) {
            throw new NotFoundException("Item с id = " + itemId + " не найден");
        }
        return item.get();
    }

    @Override
    public List<ItemDto> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchByText(String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        List<Item> items = itemRepository.findAllByNameOrDescriptionContainingIgnoreCase(text, text);
        return items.stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemInfoDto getItemInfoById(long itemId, long userId) {
        ItemInfoDto item = ItemMapper.toItemInfoDto(getItemById(itemId));
        if (item.getOwnerId() == userId) {
            item.setLastBooking(BookingMapper.toBookingDto(
                    bookingRepository.findByItem_IdAndEndBeforeAndStatusNotOrderByEndDesc(
                            item.getId(),
                            LocalDateTime.now(),
                            BookingStatus.REJECTED)));
            item.setNextBooking(BookingMapper.toBookingDto(
                    bookingRepository.findByItem_IdAndStatusAndStartAfterOrderByStartAsc(
                            item.getId(),
                            BookingStatus.APPROVED,
                            LocalDateTime.now())));
        }
        item.setComments(commentRepository.findAllByItemId(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toSet()));
        System.out.println(commentRepository.findAllByItemId(itemId));
        return item;
    }

    @Override
    public List<ItemInfoDto> getItemsInfoByUserId(long userId) {
        userService.getUserById(userId);
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        return items.stream()
                .map(ItemMapper::toItemInfoDto)
                .peek(i -> i.setLastBooking(BookingMapper.toBookingDto(
                                bookingRepository.findByItem_IdAndEndBeforeAndStatusNotOrderByEndDesc(
                                        i.getId(),
                                        LocalDateTime.now(),
                                        BookingStatus.REJECTED)
                        ))
                )
                .peek(i -> i.setNextBooking(BookingMapper.toBookingDto(
                                bookingRepository.findByItem_IdAndStatusAndStartAfterOrderByStartAsc(
                                        i.getId(),
                                        BookingStatus.APPROVED,
                                        LocalDateTime.now())
                        ))
                )
                .peek(i -> {
                    i.setComments(commentRepository.findAllByItemId(i.getId()).stream()
                            .map(CommentMapper::toCommentDto)
                            .collect(Collectors.toSet()));
                })
                .sorted((i1, i2) -> (int) (i1.getId() - i2.getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(CommentDto commentDto, long userId, long itemId) {
        Comment comment = CommentMapper.toComment(commentDto,
                UserMapper.toUser(userService.getUserById(userId)), itemId);
        Booking booking = bookingRepository.findByBookerIdAndItem_IdAndEndBefore(userId, itemId, LocalDateTime.now());
        if (booking != null) {
            return CommentMapper.toCommentDto(commentRepository.save(comment));
        } else {
            throw new ValidateException("Пользователь с id = " + userId + " не использовал предмет с id = " + itemId);
        }
    }

}