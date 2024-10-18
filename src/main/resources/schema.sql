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
 ('Lardo', FALSE, FALSE, FALSE, TRUE), --1
 ('Panaderia Rosetta', FALSE, TRUE, FALSE, TRUE), --2
 ('Tetetlan', FALSE, FALSE, FALSE, TRUE), -- 3
 ('Falling Piano Brewing Co', FALSE, FALSE, FALSE, FALSE), -- 4
 ('u.to.pi.a', FALSE, TRUE, TRUE, FALSE); --5

INSERT INTO Tables (RestaurantId, Capacity) VALUES
  -- for lardo
 (1, 2),
 (1, 2),
 (1, 2),
 (1, 2),
 (1, 4),
 (1, 4),
 (1, 6),
  -- for panaderia
 (2, 2),
 (2, 2),
 (2, 2),
 (2, 4),
 (2, 4),
  -- for tete
 (3, 2),
 (3, 2),
 (3, 2),
 (3, 2),
 (3, 4),
 (3, 4),
 (3, 6),
  -- for falling
 (4, 2),
 (4, 2),
 (4, 2),
 (4, 2),
 (4, 2),
 (4, 4),
 (4, 4),
 (4, 4),
 (4, 4),
 (4, 4),
 (4, 6),
 (4, 6),
 (4, 6),
 (4, 6),
 (4, 6),
  -- for utopia
 (5, 2),
 (5, 2);
