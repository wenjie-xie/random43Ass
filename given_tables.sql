USE HL;

CREATE TABLE Book (
    ISBN CHAR(13) NOT NULL,
    Title VARCHAR(45) NOT NULL,
    Publisher VARCHAR(45) NOT NULL,
    NumberOfPages INT NOT NULL,
    YearOfPublication INT NOT NULL,
    EditionNumber INT,
    Abstract MEDIUMTEXT,
    PRIMARY KEY (ISBN)
);

CREATE TABLE Keyword (
    ID INT NOT NULL,
    Tag VARCHAR(45) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE BookKeyword (
    ISBN CHAR(13),
    Keyword_ID INT,
    PRIMARY KEY (ISBN , Keyword_ID),
    FOREIGN KEY (ISBN)
        REFERENCES Book (ISBN)
        ON DELETE CASCADE,
    FOREIGN KEY (Keyword_ID)
        REFERENCES Keyword (ID)
        ON DELETE CASCADE
);

CREATE TABLE PeopleInvolved (
    ID INT NOT NULL,
    FirstName VARCHAR(45) NOT NULL,
    MiddleName VARCHAR(45),
    FamilyName VARCHAR(45) NOT NULL,
    Gender TINYINT,
    PRIMARY KEY (ID)
);

CREATE TABLE BookAuthor (
    ISBN CHAR(13),
    Author_ID INT,
    primary key (ISBN, Author_ID),
    FOREIGN KEY (ISBN)
        REFERENCES Book (ISBN)
        ON DELETE CASCADE,
    FOREIGN KEY (Author_ID)
        REFERENCES PeopleInvolved (ID)
        ON DELETE CASCADE
);

CREATE TABLE Music (
    AlbumName VARCHAR(45) NOT NULL,
    Year INT NOT NULL,
    MusicName VARCHAR(45) NOT NULL,
    Language VARCHAR(45),
    DiskType TINYINT,
    Producer_ID INT,
    PRIMARY KEY (AlbumName , Year , MusicName),
    FOREIGN KEY (Producer_ID)
        REFERENCES PeopleInvolved (ID)
        ON DELETE CASCADE
);

CREATE TABLE PeopleInvolvedMusic (
    AlbumName VARCHAR(45),
    Year INT,
    MusicName VARCHAR(45),
    PeopleInvolved_ID INT,
    IsSongwriter TINYINT,
    IsComposer TINYINT,
    IsArranger TINYINT,
    PRIMARY KEY (AlbumName , Year , MusicName , PeopleInvolved_ID),
    FOREIGN KEY (AlbumName , Year , MusicName)
        REFERENCES Music (AlbumName , Year , MusicName)
        ON DELETE CASCADE,
    FOREIGN KEY (PeopleInvolved_ID)
        REFERENCES PeopleInvolved (ID)
        ON DELETE CASCADE
);

CREATE TABLE MusicSinger (
    AlbumName VARCHAR(45),
    Year INT,
    MusicName VARCHAR(45),
    PeopleInvolved_ID INT,
    PRIMARY KEY (AlbumName , Year , MusicName , PeopleInvolved_ID),
    FOREIGN KEY (AlbumName , Year , MusicName)
        REFERENCES Music (AlbumName , Year , MusicName)
        ON DELETE CASCADE,
    FOREIGN KEY (PeopleInvolved_ID)
        REFERENCES PeopleInvolved (ID)
        ON DELETE CASCADE
);

CREATE TABLE Role (
    ID INT NOT NULL,
    Description VARCHAR(45),
    PRIMARY KEY (ID)
);

CREATE TABLE Movie (
    MovieName VARCHAR(45) NOT NULL,
    Year INT NOT NULL,
    PRIMARY KEY (MovieName , Year)
);

CREATE TABLE CrewMember (
    PeopleInvolved_ID INT,
    MovieName VARCHAR(45),
    ReleaseYear INT,
    Role_ID INT,
    PRIMARY KEY (PeopleInvolved_ID , MovieName , ReleaseYear , Role_ID),
    FOREIGN KEY (PeopleInvolved_ID)
        REFERENCES PeopleInvolved (ID)
        ON DELETE CASCADE,
    FOREIGN KEY (MovieName , ReleaseYear)
        REFERENCES Movie (MovieName , Year)
        ON DELETE CASCADE,
    FOREIGN KEY (Role_ID)
        REFERENCES Role (ID)
        ON DELETE CASCADE
);

CREATE TABLE Award (
    PeopleInvolved_ID INT,
    MovieName VARCHAR(45),
    Year INT,
    Award TINYINT,
    PRIMARY KEY (PeopleInvolved_ID , MovieName , Year),
    FOREIGN KEY (PeopleInvolved_ID)
        REFERENCES PeopleInvolved (ID)
        ON DELETE CASCADE,
    FOREIGN KEY (MovieName , Year)
        REFERENCES Movie (MovieName , Year)
        ON DELETE CASCADE
);