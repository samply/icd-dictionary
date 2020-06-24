CREATE TABLE IcdCode (
    id SERIAL PRIMARY KEY,
    code VARCHAR(20) NOT NULL,
    kind VARCHAR(20) NOT NULL,
    display VARCHAR NOT NULL,
    definition VARCHAR NOT NULL,
    parentCode VARCHAR(20),
    childCodes VARCHAR(500)
);
