USE mybnb;

set @uid=UUID();
INSERT INTO users (user_id, name, address, dob, occupation, SIN) values (
  @uid, 'Joe Bob', '99 Fake Street', '1993-05-02', 'Breakdancer', '51232545' 
);

set @uid=UUID();
INSERT INTO users (user_id, name, address, dob, occupation, SIN) values (
  @uid, 'Snow Jon', '56 Fake Street', '2004-05-06', 'Commander', '41232545' 
);