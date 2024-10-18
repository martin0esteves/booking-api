DROP TABLE IF EXISTS Eater;
CREATE TABLE Eater (
    EaterId INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    Vegetarian BOOLEAN DEFAULT FALSE,
    Vegan BOOLEAN DEFAULT FALSE,
    Gluten BOOLEAN DEFAULT FALSE,
    Paleo BOOLEAN DEFAULT FALSE
);

DROP TABLE IF EXISTS Restaurants;
CREATE TABLE Restaurants (
    RestaurantId INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Vegetarian BOOLEAN DEFAULT FALSE,
    Vegan BOOLEAN DEFAULT FALSE,
    Gluten BOOLEAN DEFAULT FALSE,
    Paleo BOOLEAN DEFAULT FALSE
);

DROP TABLE IF EXISTS Tables;
CREATE TABLE Tables (
    TableId INT AUTO_INCREMENT PRIMARY KEY,
    RestaurantId INT NOT NULL,
    Capacity INT NOT NULL,
    FOREIGN KEY (RestaurantId) REFERENCES Restaurants(RestaurantId)
);

DROP TABLE IF EXISTS Reservation;
CREATE TABLE Reservation (
    ReservationId INT AUTO_INCREMENT PRIMARY KEY,
    TableId INT NOT NULL,
    StartingTime DATETIME NOT NULL,
    EndingTime DATETIME NOT NULL,
    FOREIGN KEY (TableId) REFERENCES Tables(TableId)
);

DROP TABLE IF EXISTS ReservationEater;
CREATE TABLE ReservationEater (
    ReservationId INT NOT NULL,
    EaterId INT NOT NULL,
    PRIMARY KEY (ReservationId, EaterId),
    FOREIGN KEY (ReservationId) REFERENCES Reservation(ReservationId),
    FOREIGN KEY (EaterId) REFERENCES Eater(EaterId)
);

INSERT INTO Eater (Name, Vegetarian, Vegan) VALUES
  ('Scott', TRUE, FALSE),
  ('George', FALSE, FALSE),
  ('Elise', FALSE, FALSE);

INSERT INTO Restaurants (Name, Vegetarian, Vegan, Gluten, Paleo) VALUES
  ('Restaurant A', TRUE, FALSE, FALSE, FALSE),  -- 1
  ('Restaurant B', FALSE, TRUE, FALSE, FALSE),  -- 2
  ('Restaurant C', FALSE, FALSE, FALSE, FALSE); -- 3
--  ('Lardo', FALSE, FALSE, FALSE, TRUE), --4
--  ('Panaderia Rosetta', FALSE, TRUE, FALSE, TRUE), --5
--  ('Tetetlan', FALSE, FALSE, FALSE, TRUE), -- 6
--  ('Falling Piano Brewing Co', FALSE, FALSE, FALSE, FALSE), -- 7
--  ('u.to.pi.a', FALSE, TRUE, TRUE, FALSE); --8

INSERT INTO Tables (RestaurantId, Capacity) VALUES
  (1, 4),
  (1, 2),
  (2, 6),
  (3, 3);
  -- for lardo
  -- (4, 2),
  -- (4, 2),
  -- (4, 2),
  -- (4, 2),
  -- (4, 4),
  -- (4, 4),
  -- (4, 6),
  -- for panaderia
  -- (5, 2),
  -- (5, 2),
  -- (5, 2),
  -- (5, 4),
  -- (5, 4),
  -- for tete
  -- (6, 2),
  -- (6, 2),
  -- (6, 2),
  -- (6, 2),
  -- (6, 4),
  -- (6, 4),
  -- (6, 6),
  -- for falling
  -- (7, 2),
  -- (7, 2),
  -- (7, 2),
  -- (7, 2),
  -- (7, 2),
  -- (7, 4),
  -- (7, 4),
  -- (7, 4),
  -- (7, 4),
  -- (7, 4),
  -- (7, 6),
  -- (7, 6),
  -- (7, 6),
  -- (7, 6),
  -- (7, 6),
  -- for utopia
  -- (8, 2)
  -- (8, 2)


INSERT INTO Reservation (TableId, StartingTime, EndingTime) VALUES
  (1, '2024-09-25 18:00:00', '2024-09-25 20:00:00'),
  (2, '2024-09-25 19:00:00', '2024-09-25 21:00:00'),
  (3, '2024-09-25 18:30:00', '2024-09-25 21:30:00');

INSERT INTO ReservationEater (ReservationId, EaterId) VALUES
  (1, 1),
  (1, 2),
  (2, 3),
  (3, 1);