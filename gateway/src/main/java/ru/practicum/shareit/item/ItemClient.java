package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.additions.MyPageRequest;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(ItemDto itemDto, long userId) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> updateItem(ItemDto itemDto, long userId, long itemId) {
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> getItemById(long itemId) {
        return get("/" + itemId);
    }

    public ResponseEntity<Object> getItemInfoById(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getItemsInfoByUserId(long userId, MyPageRequest pageRequest) {
        Map<String, Object> parameters = Map.of(
                "pageRequest",pageRequest
        );
        return get("?pageRequest={pageRequest}", userId, parameters);
    }

    public ResponseEntity<Object> searchByText(String text, MyPageRequest pageRequest) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "pageRequest", pageRequest
        );
        return get("/search?text={text}&pageRequest={pageRequest}", null, parameters);
    }

    public ResponseEntity<Object> addComment(CommentDto commentDto, long userId, long itemId) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }

}