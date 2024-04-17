INSERT INTO location (id, name, street, street_nr, postal_code, town, canton)
VALUES (1, 'Location Name 1', 'Street Name 1', 'Street Number 1', 1234, 'Town Name 1',
        'Canton Name 1'),
       (2, 'Location Name 2', 'Street Name 2', 'Street Number 2', 2345, 'Town Name 2',
        'Canton Name 2');

INSERT INTO event (id, title, description, location_id, date, customer_code, employee_code,
                   customer_only_time, is_started, started_at, ended_at)
VALUES (1, 'Event Title 1', 'Event Description 1', 1, '2022-01-01 00:00:00', 'Customer Code 1',
        'Employee Code 1', '2022-01-01 00:00:00', true, '2022-01-01 00:00:00',
        '2022-01-01 00:00:00'),
       (2, 'Event Title 2', 'Event Description 2', 2, '2022-02-02 00:00:00', 'Customer Code 2',
        'Employee Code 2', '2022-02-02 00:00:00', false, null, null);
