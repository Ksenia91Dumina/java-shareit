CREATE TABLE IF NOT EXISTS USERS
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY(id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE(email)
    );

CREATE TABLE IF NOT EXISTS REQUESTS
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description VARCHAR(4000) NOT NULL,
    requestor_id BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    constraint pk_item_requests primary key(id),
    constraint fk_item_requests_on_requestor foreign key(requestor_id) references USERS(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS ITEMS
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(4000) NOT NULL,
    is_available BOOLEAN NOT NULL,
    owner_id BIGINT NOT NULL,
    request_id BIGINT,
    constraint pk_items primary key(ID),
    constraint fk_items_owner foreign key(owner_id) references USERS(ID) ON DELETE CASCADE,
    constraint fk_items_requestId foreign key(request_id) references REQUESTS(ID) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS BOOKINGS
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id BIGINT NOT NULL,
    booker_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    constraint pk_booking primary key(id),
    constraint fk_booking_item foreign key(item_id) references ITEMS(id) ON DELETE CASCADE,
    constraint fk_booking_booker foreign key(booker_id) references USERS(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS COMMENTS
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR(1024) NOT NULL,
    author_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    created TIMESTAMP NOT NULL,
    constraint pk_comments primary key(id),
    constraint fk_comments_author foreign key(author_id) references USERS(id) ON DELETE CASCADE,
    constraint fk_comments_item foreign key(item_id) references ITEMS(id) ON DELETE CASCADE
    );